/**
 * 
 */
package evs.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import evs.core.ACT;
import evs.core.Common;
import evs.core.ObjectReference;
import evs.core.ServerConnectionHandler;
import evs.exception.IllegalObjectException;
import evs.exception.RemotingException;
import evs.interfaces.IACT;
import evs.interfaces.ICallback;
import evs.interfaces.IClientProxy;
import evs.interfaces.IClientRequestHandler;
import evs.interfaces.IInvocationDispatcher;
import evs.interfaces.IInvoker;
import evs.interfaces.ILifecycleManager;
import evs.interfaces.IObjectReference;
import evs.interfaces.IServerConnectionHandler;
import evs.unused.Callback;

/**
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 *
 */
public class Peer implements Runnable {
	
	private String[] args;
	private int exitCode;
	private Exception exception;
	private InputStream stdin;
	private PrintStream stdout;
	private PrintStream stderr;
	private boolean running;
	private boolean verbose;
	
	private int proxyNumber;
	private Map<Integer,IClientProxy> clientProxies;
	private IClientRequestHandler clientRequestHandler;
	private IServerConnectionHandler serverConnectionHandler;
	private Thread listener;
	private InetSocketAddress remoteAddress;
	private ILifecycleManager lifeCycleManager;
	private IInvocationDispatcher invocationDispatcher;
	
	private static final int WARNING = 1;
	private static final int ERROR   = 2;
	private static final String PROMPT = "> ";
	
	public Peer(String[] args) {
		this.args = args;
		stdin = System.in;
		stdout = System.out;
		stderr = System.err;
		running = true;
	}

	/**
	 * @param args Run "&lt;class&gt; help" for help.
	 */
	public static void main(String[] args) {
		Peer instance = new Peer(args);
		instance.run();
		Exception exception = instance.getException();
		if (exception != null) {
			exception.printStackTrace(instance.getStderr());
		}
		System.exit(instance.getExitCode());
	}
	
	public void run() {
		proxyNumber = 0;
		Common.loadProperties();
		clientProxies = new HashMap<Integer,IClientProxy>();
		clientRequestHandler = Common.getClientRequesthandler();
		lifeCycleManager = Common.getObjectManager();
		invocationDispatcher = Common.getInvocationDispatcher();
		serverConnectionHandler = new ServerConnectionHandler();
		serverConnectionHandler.setInvocationDispatcher(invocationDispatcher);
		listener = new Thread(serverConnectionHandler,"serverConnectionHandler");
		
		processArguments();
		if (exitCode > WARNING)
			return;

		if (verbose) {
			processCommand("status");
		}
		stdout.println("Enter \"help\" for help.");
		InputStreamReader inputStreamReader = new InputStreamReader(stdin);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		String command;
		stdout.print(PROMPT);
		try {
			command = bufferedReader.readLine();
		} catch (IOException e) {
			exception = e;
			raiseExitCode(ERROR);
			return;
		}
		
		while (running && command != null) {
			processCommand(command);
			if (running) {
				stdout.print(PROMPT);
				try {
					command = bufferedReader.readLine();
				} catch (IOException e) {
					exception = e;
					raiseExitCode(ERROR);
					return;
				}
			}
		}
	}
	
	public int getExitCode() { return exitCode; }
	public Exception getException() { return exception; }
	public InputStream getStdin() { return stdin; }
	public PrintStream getStdout() { return stdout; }
	public PrintStream getStderr() { return stderr; }
	
	private void processArguments() {
		String port = null;
		
		for (String arg: args) {
			int x = arg.indexOf('=');
			if (x == -1) { // keyword only
				if (arg.equals("help")) {
					stdout.println(Peer.class.getName());
					stdout.println("[help]");
					stdout.println("[port=<portNumber>]");
				} else if (arg.equals("verbose")) {
					verbose = true;
				} else {
					stderr.println("The argument is not supported: " + arg);
					stderr.println("Run \"" + Peer.class.getName() + " help\" for help.");
				}
			} else { // key value pair
				String key = arg.substring(0,x);
				String value = arg.substring(x+1);
				if (key.equals("port")) {
					port = value;
				} else {
					stderr.println("The argument is not supported: " + arg);
					stderr.println("Run \"" + Peer.class.getName() + " help\" for help.");
				}
			}
		}
		
		if (port != null) {
			try {
				setPort(port);
			} catch (RemotingException e) {
				exception = e;
				raiseExitCode(ERROR);
				return;
			}
		}
	}
	
	public void processCommand(String command) {
		int x = command.indexOf('=');
		if (x == -1) { // keyword only
			if (command.equals("help")) {
				stdout.println("connect=<remotePort>");
				stdout.println("  connect the peer to this port");
				stdout.println("create-proxy=<className>");
				stdout.println("  create a new client proxy");
				stdout.println("invoke=<proxy>,<method>,<argument>,...");
				stdout.println("  invoke method of proxy");
				stdout.println("list-proxies");
				stdout.println("  list registered client proxies");
				stdout.println("listen");
				stdout.println("  start listening for incoming requests");
				stdout.println("port=<localPort>");
				stdout.println("  bind the peer to this port");
				stdout.println("quit");
				stdout.println("  terminate the peer");
				stdout.println("register-object=<className>");
				stdout.println("  register a new server object");
				stdout.println("send=<message>");
				stdout.println("  send the message to the remote peer");
				stdout.println("status");
				stdout.println("  show the status");
			} else if (command.equals("list-proxies")) {
				listProxies();
			} else if (command.equals("listen")) {
				listen();
			} else if (command.equals("quit")) {
				running = false;
			} else if (command.equals("status")) {
				stdout.println("listening: " + listener.isAlive());
				stdout.println("port: " + serverConnectionHandler.getPort());
			} else {
				stdout.println("The command was invalid.");
				stdout.println("Enter \"help\" for help.");
			}
		} else { // key value pair
			String key = command.substring(0,x);
			String value = command.substring(x+1);
			if (key.equals("connect")) {
				int port = Integer.parseInt(value);
				remoteAddress = new InetSocketAddress(port);
			} else if (key.equals("create-proxy")) {
				createProxy(value);
			} else if (key.equals("invoke")) {
				invokeMethod(value);
			} else if (key.equals("port")) {
				try {
					setPort(value);
				} catch (RemotingException e) {
					stdout.println(e.getMessage());
				}
			} else if (key.equals("register-object")) {
				registerObject(value);
			} else if (key.equals("send")) {
				stdout.println(sendTextMessage(value));
			} else {
				stdout.println("The command was invalid.");
				stdout.println("Enter \"help\" for help.");
			}
		}
	}
	
	private synchronized void addProxy(IClientProxy proxy) {
		clientProxies.put(proxyNumber,proxy);
		proxyNumber++;
	}
	
	private void createProxy(String className) {
		Class<?> clazz;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}
		
		if (!IClientProxy.class.isAssignableFrom(clazz)) {
			stderr.println("ERROR Create proxy failed.");
			stderr.println("The class " + className +
				" is not a sub type of " + IClientProxy.class.getName() + ".");
			return;
		}
		
		int modifiers = clazz.getModifiers();
		if (Modifier.isInterface(modifiers)) {
			stderr.println("ERROR Create proxy failed.");
			stderr.println("The class " + className +
				" is an interface.");
			return;
		}
		if (Modifier.isAbstract(modifiers)) {
			stderr.println("ERROR Create proxy failed.");
			stderr.println("The class " + className +
				" is an abstract class.");
			return;
		}
		
		Constructor<?> constructor;
		try {
			constructor = clazz.getConstructor(ICallback.class);
		} catch (SecurityException e) {
			e.printStackTrace();
			return;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return;
		}
		
		Object object;
		ICallback callback = new Callback();
		try {
			object = constructor.newInstance(callback);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return;
		} catch (InstantiationException e) {
			e.printStackTrace();
			return;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return;
		}
		
		IClientProxy proxy = IClientProxy.class.cast(object);
		addProxy(proxy);
	}
	
	private synchronized IClientProxy getProxy(Integer id) {
		return clientProxies.get(id);
	}
	
	private void invokeMethod(String command) {
		String[] values = command.split(",");
		if (values.length < 2) {
			stderr.println("ERROR invoke method failed.");
			stderr.println("An invocation requires at least a proxy id and a method signature.");
			return;
		}
		
		Integer proxyId = Integer.valueOf(values[0]);
		IClientProxy proxy = getProxy(proxyId);
		Class<?> clazz = proxy.getClass();
		Method[] methods = clazz.getMethods();
		Method method = null;
		Class<?>[] parameterTypes = null;
		int argc = 0;
		for (int x = 0; x < methods.length; x++) {
			String methodName = methods[x].getName();
			if (methodName.equals(values[1])) {
				method = methods[x];
				parameterTypes = method.getParameterTypes();
				argc = parameterTypes.length;
				if ((argc + 1) == values.length) {
					break;
				} else {
					method = null;
				}
			}
		}
		
		if (method == null) {
			stderr.println("ERROR invoke method failed.");
			stderr.println("The method \"" + values[1] + "\" was not found.");
			return;
		}
		
		// set arguments
		Object[] arguments = new Object[argc];
		argc--; // minus ACT
		for (int x = 0; x < argc; x++) {
			String argumentString = values[x+2];
			if (argumentString.length() > 0) {
				Class<?> parameterType = parameterTypes[x];
				if (parameterType.isAssignableFrom(String.class)) {
					arguments[x] = argumentString;
				} else if (parameterType.isAssignableFrom(Integer.class)) {
					arguments[x] = Integer.valueOf(argumentString);
				} else {
					stderr.println("ERROR invoke method failed.");
					stderr.println("The parameter type " + parameterType.getName() +
						" is not supported.");
					return;
				}
			} else {
				arguments[x] = null;
			}
		}
		
		IACT act = new ACT();
		arguments[argc] = act;
		
		try {
			method.invoke(proxy,arguments);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return;
		}
	}
	
	private void raiseExitCode(int i) {
		if (i > exitCode) {
			exitCode = i;
		}
	}
	
	private void registerObject(String className) {
		IObjectReference ref = new ObjectReference(className,className + "Invoker");
		Class<?> clazz;
		try {
			clazz = Class.forName(ref.getInvokerId());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}
		
		if (!IInvoker.class.isAssignableFrom(clazz)) {
			stderr.println("ERROR Register object failed.");
			stderr.println("The class " + className +
				" is not a sub type of " + IInvoker.class.getName() + ".");
			return;
		}
		
		int modifiers = clazz.getModifiers();
		if (Modifier.isInterface(modifiers)) {
			stderr.println("ERROR Register object failed.");
			stderr.println("The class " + className +
				" is an interface.");
			return;
		}
		if (Modifier.isAbstract(modifiers)) {
			stderr.println("ERROR Register object failed.");
			stderr.println("The class " + className +
				" is an abstract class.");
			return;
		}
		
		Object object;
		try {
			object = clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
			return;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return;
		}
		
		IInvoker invoker = IInvoker.class.cast(object);
		invocationDispatcher.registerInvoker(ref.getInvokerId(),invoker);

		try {
			lifeCycleManager.register(ref);
		} catch (IllegalObjectException e) {
			e.printStackTrace();
			return;
		}
	}
	
	private void setPort(int port) throws RemotingException {
		InetSocketAddress localAddress = new InetSocketAddress(port);
		serverConnectionHandler.bind(localAddress);
	}
	
	private void setPort(String port) throws RemotingException {
		int portNumber;
		try {
			portNumber = Integer.parseInt(port);
		} catch (NumberFormatException e) {
			throw new RemotingException(e);
		}
		setPort(portNumber);
	}
	
	private String sendTextMessage(String message) {
		byte[] response;
		try {
			response = clientRequestHandler.send(remoteAddress,message.getBytes());
		} catch (RemotingException e) {
			e.printStackTrace(stderr);
			return null;
		}
		return new String(response);
	}
	
	private synchronized void listProxies() {
		Set<Integer> keys = clientProxies.keySet();
		for (Integer key: keys) {
			IClientProxy proxy = clientProxies.get(key);
			stdout.print(key);
			stdout.print(" ");
			stdout.println(proxy.getClass().getName());
		}
	}
	
	private void listen() {
		listener.start();
	}
	
}
