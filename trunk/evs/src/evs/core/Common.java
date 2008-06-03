package evs.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import evs.interfaces.IClientRequestHandler;
import evs.interfaces.IInterceptorRegistry;
import evs.interfaces.IInvocationDispatcher;
import evs.interfaces.ILifecycleManager;
import evs.interfaces.ILocation;
import evs.interfaces.IMarshaller;
import evs.interfaces.IServerConnectionHandler;

public class Common {

	//GLOBAL PROPERTIES
	private static Properties properties;
	private static String defaultFile = "conf/evs.properties";
	
	//REFERENCES
	private static IClientRequestHandler clientRequestHandler = null;
	private static IServerConnectionHandler serverConnectionHandler = null;
	private static IInterceptorRegistry serverInterceptors = null;
	private static IInterceptorRegistry clientInterceptors = null;
	private static IMarshaller marshaller = null;
	private static ILifecycleManager objectManager = null;
	private static IInvocationDispatcher invocationDispatcher = null;
	
	//-------------------------------------------------------------------------------------------------------
	
	public static boolean loadProperties(){
		return loadProperties(defaultFile);
	}
	
	public static boolean loadProperties(String file){
		properties = new Properties();
		
		InputStream in = null;
		try{
			in = new FileInputStream(new File(file));
			properties.load(in);
		} catch (Exception ex){
			System.err.println("[x] Failed to initialize properties from file " + file);
			return false;
		} finally{
			try{
				if(in != null) in.close();
			} catch (IOException io){}
		}
		
		System.out.println("[*] Successfully loaded properties from file " + file);
		return true;
	}
	
	public static Long leaseTime(){
		return Long.parseLong(properties.getProperty("LEASE_TIME"));
	}
	
	public static Integer poolSize(){
		return Integer.parseInt(properties.getProperty("POOL_SIZE"));
	}
	
	public static Long passivationTimeout(){
		return Long.parseLong(properties.getProperty("PASSIVATION_TIMEOUT"));
	}
	
	public static String passivationDir(){
		return properties.getProperty("PASSIVATION_DIR");
	}
	
	public static ILocation getLocation()
	{
		return new WebLocation(properties.getProperty("HOSTNAME"), properties.getProperty("PORT"));
	}
	
	//-------------------------------------------------------------------------------------------------------
	
	public static IClientRequestHandler getClientRequesthandler(){
		if(clientRequestHandler == null)
			clientRequestHandler = new ClientRequestHandler();
		return clientRequestHandler;
	}
	
	public static IServerConnectionHandler getServerConnectionHandler(){
		if (serverConnectionHandler == null)
			serverConnectionHandler = new ServerConnectionHandler();
		return serverConnectionHandler;
	}
	
	public static IMarshaller getMarshaller(){
		if(marshaller == null)
			marshaller = new BasicMarshaller();
		return marshaller;
	}
	
	public static IInterceptorRegistry getServerInterceptors(){
		if(serverInterceptors == null)
			serverInterceptors = new InterceptorRegistry();
		return serverInterceptors;
	}
	
	public static IInterceptorRegistry getClientInterceptors(){
		if(clientInterceptors == null)
			clientInterceptors = new InterceptorRegistry();
		return clientInterceptors;
	}

	public static ILifecycleManager getObjectManager() {
		if(objectManager == null)
			objectManager = new LifecycleManager();
		return objectManager;
	}
	
	public static IInvocationDispatcher getInvocationDispatcher(){
		if(invocationDispatcher == null)
			invocationDispatcher = new InvocationDispatcher();
		return invocationDispatcher;
	}
	
}
