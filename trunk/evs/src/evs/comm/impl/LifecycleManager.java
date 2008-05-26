package evs.comm.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import evs.comm.exception.IllegalObjectException;
import evs.comm.exception.NotSupportedException;
import evs.comm.interfaces.ILifecycleManager;
import evs.comm.interfaces.IObjectPool;
import evs.comm.interfaces.IObjectReference;
import evs.comm.interfaces.IObjectState;
import evs.comm.interfaces.IRemoteObject;

/**
 * Lifecycle Manager
 * manages the configured lifecycle of remote objects and triggers lifecycle operations
 * 
 * @author evs069
 *
 */
public class LifecycleManager implements ILifecycleManager {

	/** Mapping of registered types to Lifecycle Strategy */
	private Map<String, LifecycleStrategy> registeredObjects;
	
	/** Static Instances */
	private Map<String, IObjectState> staticInstances;
	
	/** Client-Specific Instances */
	private Map<String, IObjectState> clientInstances;
	
	/** Pooled Per-Request Instances */
	private Map<String, IObjectPool> pooledInstances;

	private Integer idCounter;
	
	private Long expirationTime;
	private Long passivationTime;
	
	private CleanUpThread cleaner;
	private PassivatorThread passivator;
	
	/**
	 * Initializes the Lifecycle Manager
	 */
	public LifecycleManager(){
		this.registeredObjects = new HashMap<String, LifecycleStrategy>();
		
		this.staticInstances = new HashMap<String, IObjectState>();
		this.pooledInstances = new HashMap<String, IObjectPool>();
		this.clientInstances = new HashMap<String, IObjectState>();
		
		this.idCounter = 0;
		
		this.expirationTime = Common.leaseTime();
		this.passivationTime = Common.passivationTimeout();
		
		this.cleaner = new CleanUpThread(this, expirationTime);
		this.passivator = new PassivatorThread(this, passivationTime);
	}
	
	/**
	 * triggered by invoker on arrival of request
	 * 
	 * @param ref      Object Reference
	 * @return         Instance of Remote Object or null in case of an error
	 */
	public IRemoteObject invocationArrived(IObjectReference ref) {
		String key = ref.getClientIndependent();
		LifecycleStrategy strategy = this.registeredObjects.get(key);
		
		IRemoteObject instance = null;
		
		switch(strategy){
			case STATIC_PASSIVATION:
				if(this.staticInstances.get(key).isPersisted()){
					instance = this.activateObject(ref.getClientIndependent());
					this.staticInstances.get(key).setObject(instance);
					this.staticInstances.get(key).setLocked(true);
					return instance;
				}
				this.staticInstances.get(key).setLocked(true);
			case STATIC:
				 //one instance for all requests, already activated at registering
				return this.staticInstances.get(key).getObject();		
			case PER_REQUEST:
				//return new instance
				try {
					instance = (IRemoteObject) (Class.forName(ref.getObjectId())).newInstance();
					return instance;
				} catch (Exception exception){
					return null;
				}
			case CLIENT_PASSIVATION:
				if(this.clientInstances.get(ref.getClientDependent()).isPersisted()){
					instance = activateObject(ref.getClientDependent());
					this.clientInstances.get(ref.getClientDependent()).setObject(instance);
					this.clientInstances.get(ref.getClientDependent()).setLocked(true);
					return instance;
				}
			case CLIENT:
				this.clientInstances.get(ref.getClientDependent()).setLocked(true);
				return this.clientInstances.get(ref.getClientDependent()).getObject();	
			case LAZY:
				//instantiate object if not already available
				instance = this.staticInstances.get(key).getObject();
				if(instance != null) return instance;
				try {
					instance = (IRemoteObject) (Class.forName(ref.getObjectId())).newInstance();
					this.staticInstances.put(key, new ObjectState(instance));
					return instance;
				} catch (Exception exception){
					return null;
				}
			case POOLING:
				try {
					IRemoteObject pooledInstance = this.pooledInstances.get(key).checkOut();
					return pooledInstance;
				} catch (Exception exception){
					return null;
				}
			default:
				return null; //no object found
		}
	}
	
	/**
	 * triggered by invoker when the request is finished
	 * @param ref     Object Reference
	 * @param object  Remote Object
	 */
	public void invocationDone(IObjectReference ref, IRemoteObject object){
		String key = ref.getClientIndependent();
		LifecycleStrategy strategy = this.registeredObjects.get(key);
		
		switch(strategy){
			case STATIC:
			case LAZY:
				break; //nothing to do here
			case PER_REQUEST:
				object = null;
				break;
			case CLIENT_PASSIVATION:
				this.clientInstances.get(ref.getClientDependent()).setLocked(false);
			case CLIENT:
				this.clientInstances.get(ref.getClientDependent()).getObject().setExpiration(System.currentTimeMillis());
				break;
			case POOLING:
				this.pooledInstances.get(key).checkIn(object);
				break;
			case STATIC_PASSIVATION:
				this.staticInstances.get(ref.getClientIndependent()).getObject().setExpiration(System.currentTimeMillis());
				this.staticInstances.get(ref.getClientIndependent()).setLocked(false);
				break;
		}
	}

	/**
	 * Registers type of Remote Objects with default strategy STATIC INSTANCE
	 * @param  ref	Object Reference
	 * @throws IllegalObjectException
	 */
	public void register(IObjectReference ref) throws IllegalObjectException{
		this.register(ref, LifecycleStrategy.STATIC);
	}
	
	/**
	 * Registers type of Remote Objects with specified Lifecycle Strategy
	 * @param ref 		Object Reference
	 * @param strategy	Lifecycle Strategy to be used
	 * @throws IllegalObjectException
	 */
	public void register(IObjectReference ref, LifecycleStrategy strategy) throws IllegalObjectException {		
		if((strategy == LifecycleStrategy.STATIC) ||
		   (strategy == LifecycleStrategy.STATIC_PASSIVATION)){
			try {
				IRemoteObject instance = (IRemoteObject) (Class.forName(ref.getObjectId())).newInstance();
				if(strategy == LifecycleStrategy.STATIC_PASSIVATION)
					instance.setExpiration(System.currentTimeMillis());
				this.staticInstances.put(instance.getId(), new ObjectState(instance));
			} catch (Exception exception){
				throw new IllegalObjectException();
			}
		} else if(strategy == LifecycleStrategy.POOLING){
			ObjectPool pool = new ObjectPool(ref.getObjectId(), Common.leaseTime());
			pool.createPool(Common.poolSize());
			this.pooledInstances.put(ref.getClientIndependent(), pool);
		}
		System.out.println("[*] Remote Object for Class " + ref.getClientIndependent() + " registered with strategy " + strategy.name());
		this.registeredObjects.put(ref.getClientIndependent(), strategy);
	}
	
	//--------------------------------------------------------------------- CLIENT DEPENDENT OPERATIONS
	
	public Integer newInstance(IObjectReference ref) throws NotSupportedException, IllegalObjectException{
		LifecycleStrategy strategy = this.registeredObjects.get(ref.getClientIndependent());
		Integer instanceId = null;
		
		if((strategy == LifecycleStrategy.CLIENT) ||
		   (strategy == LifecycleStrategy.CLIENT_PASSIVATION)){
			try {
				IRemoteObject instance = (IRemoteObject) (Class.forName(ref.getObjectId())).newInstance();
				instance.setExpiration(System.currentTimeMillis());
				instanceId = (++idCounter);
				ref.setInstanceId(instanceId.toString());
				this.clientInstances.put(ref.getClientDependent(), new ObjectState(instance));
			} catch (Exception exception){
				throw new IllegalObjectException();
			}
		} else {
			throw new NotSupportedException("Operation not supported, strategy=" + strategy.name());
		}
		
		return instanceId;
	}
	
	public void keepAlive(IObjectReference ref) throws NotSupportedException, IllegalObjectException{
		LifecycleStrategy strategy = this.registeredObjects.get(ref.getClientIndependent());
		
		if((strategy == LifecycleStrategy.CLIENT) ||
		   (strategy == LifecycleStrategy.CLIENT_PASSIVATION)){
			try {
				IObjectState state = this.clientInstances.get(ref.getClientDependent());
				if(state.isPersisted()){
					state.setObject(activateObject(ref.getClientDependent()));
					state.setPersisted(false);
				}
				state.getObject().setExpiration(System.currentTimeMillis());
				this.clientInstances.put(ref.getClientDependent(), state);
			} catch (Exception exception){
				throw new IllegalObjectException();
			}
		} else {
			throw new NotSupportedException("Operation not supported, strategy=" + strategy.name());
		}
	}
	
	public void destroy(IObjectReference ref) throws NotSupportedException, IllegalObjectException{
		LifecycleStrategy strategy = this.registeredObjects.get(ref.getClientIndependent());
		
		if((strategy == LifecycleStrategy.CLIENT) ||
		   (strategy == LifecycleStrategy.CLIENT_PASSIVATION)){
			try {
				IObjectState state = this.clientInstances.get(ref.getClientDependent());
				if(state.isPersisted()){
					clearObject(ref.getClientDependent());
				}
				this.clientInstances.remove(ref.getClientDependent());
			} catch (Exception exception){
				throw new IllegalObjectException();
			}
		} else {
			throw new NotSupportedException("Operation not supported, strategy=" + strategy.name());
		}
	}
	
	
	/**
	 * Unregisters type of Remote Objects
	 * @param ref	ObjectReference
	 */
	public void unregister(IObjectReference ref){
		this.registeredObjects.remove(ref.getClientIndependent());
		//TODO: remove active instances? handle shutdown of pool etc.
	}
	
	
	//--------------------------------------- PRIVATE METHODS FOR PASSIVATION
	
	private boolean passivateObject(String name, IRemoteObject object){
		try {
			File file = new File(Common.passivationDir() + name);
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
			out.writeObject(object);
			out.flush();
			out.close();
			return true;
		} catch (IOException ex){
			return false;
		}
		
	}
	
	private IRemoteObject activateObject(String name){
		try {
			File file = new File(Common.passivationDir() + name);
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
			IRemoteObject obj = (IRemoteObject) in.readObject();
			in.close();
			//file.delete();
			return obj;
		} catch (Exception ex){
			return null;
		}
	}
	
	private void clearObject(String name){
		try {
			File file = new File(Common.passivationDir() + name);
			//file.delete();
		} catch (Exception ex){}
	}
	
	private void passivate(){
		long now = System.currentTimeMillis();
		Collection<String> keyset = this.registeredObjects.keySet();
		for(String s: keyset){
			switch(this.registeredObjects.get(s)){
				case STATIC_PASSIVATION:
					if(!this.staticInstances.get(s).isLocked() && !this.staticInstances.get(s).isPersisted()){
						if((now - ((Long) this.staticInstances.get(s).getObject().getExpiration()).longValue()) > passivationTime){
							passivateObject(s, this.staticInstances.get(s).getObject());
							this.staticInstances.get(s).setObject(null);
							this.staticInstances.get(s).setPersisted(true);
						}
					}
					
			}
		}
		
		now = System.currentTimeMillis();
		keyset = this.clientInstances.keySet();
		for(String s: keyset){
			String[] stripped = s.split("@");
			switch(this.registeredObjects.get(stripped[0]+"@"+stripped[1])){
				case CLIENT_PASSIVATION:
					if(!this.clientInstances.get(s).isLocked() && !this.clientInstances.get(s).isPersisted()){
						if((now - ((Long) this.clientInstances.get(s).getObject().getExpiration()).longValue()) > passivationTime){
							passivateObject(s, this.clientInstances.get(s).getObject());
							this.clientInstances.get(s).setObject(null);
							this.clientInstances.get(s).setPersisted(true);
						}
					}
			}
		}
	    System.gc();
	}
	
	//Thread for periodically passivating expired objects
	class PassivatorThread extends Thread {
		private LifecycleManager manager;
		private long sleepTime;
		
		public PassivatorThread(LifecycleManager manager, long sleepTime){
			this.manager = manager;
			this.sleepTime = sleepTime;
		}
		
		public void run(){
			while(true){
				try{
					sleep(sleepTime);
				} catch(InterruptedException ex){
					//ignore
				}
				manager.passivate();
			}
		}
		
	}
	
	
	//------------------------------------------- LEASING
	
	private void cleanUp(){
		long now = System.currentTimeMillis();
		Collection<String> keyset = this.clientInstances.keySet();
		for(String s: keyset){
			String[] stripped = s.split("@");
			switch(this.registeredObjects.get(stripped[0]+"@"+stripped[1])){
				case CLIENT:
					if(!this.clientInstances.get(s).isLocked())
						if((now - ((Long) this.clientInstances.get(s).getObject().getExpiration()).longValue()) > expirationTime)
							this.clientInstances.remove(s);
			}
		}
	    System.gc();
	}
	
	//Thread for periodically cleaning up expired objects
	class CleanUpThread extends Thread {
		private LifecycleManager manager;
		private long sleepTime;
		
		public CleanUpThread(LifecycleManager manager, long sleepTime){
			this.manager = manager;
			this.sleepTime = sleepTime;
		}
		
		public void run(){
			while(true){
				try{
					sleep(sleepTime);
				} catch(InterruptedException ex){
					//ignore
				}
				manager.cleanUp();
			}
		}
		
	}

}
