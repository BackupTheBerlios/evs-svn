/**
 * 
 */
package evs.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;

import evs.comm.BasicRequestHandler;
import evs.comm.RemotingException;

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
	private BasicRequestHandler requestHandler;
	private Thread listener;
	
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
		requestHandler = new BasicRequestHandler();
		listener = new Thread(requestHandler);
		
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
	
	private void processCommand(String command) {
		int x = command.indexOf('=');
		if (x == -1) { // keyword only
			if (command.equals("help")) {
				stdout.println("connect=<remotePort>");
				stdout.println("  connect the peer to this port");
				stdout.println("listen");
				stdout.println("  start listening for incoming requests");
				stdout.println("port=<localPort>");
				stdout.println("  bind the peer to this port");
				stdout.println("quit");
				stdout.println("  terminate the peer");
				stdout.println("send=<message>");
				stdout.println("  send the message to the remote peer");
				stdout.println("status");
				stdout.println("  show the status");
			} else if (command.equals("listen")) {
				listen();
			} else if (command.equals("quit")) {
				running = false;
			} else if (command.equals("status")) {
				stdout.println("listening: " + listener.isAlive());
				stdout.println("port: " + requestHandler.getPort());
			} else {
				stdout.println("The command was invalid.");
				stdout.println("Enter \"help\" for help.");
			}
		} else { // key value pair
			String key = command.substring(0,x);
			String value = command.substring(x+1);
			if (key.equals("connect")) {
				int port = Integer.parseInt(value);
				InetSocketAddress remoteAddress = new InetSocketAddress(port);
				try {
					requestHandler.connect(remoteAddress);
				} catch (RemotingException e) {
					stdout.println(e.getMessage());
				}
			} else if (key.equals("port")) {
				try {
					setPort(value);
				} catch (RemotingException e) {
					stdout.println(e.getMessage());
				}
			} else if (key.equals("send")) {
				sendTextMessage(value);
			} else {
				stdout.println("The command was invalid.");
				stdout.println("Enter \"help\" for help.");
			}
		}
	}
	
	private void raiseExitCode(int i) {
		if (i > exitCode) {
			exitCode = i;
		}
	}
	
	private void setPort(int port) throws RemotingException {
		InetSocketAddress localAddress = new InetSocketAddress(port);
		requestHandler.bind(localAddress);
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
	
	private void sendTextMessage(String message) {
		try {
			requestHandler.send(null,message.getBytes());
		} catch (RemotingException e) {
			e.printStackTrace(stderr);
		}
	}
	
	private void listen() {
		listener.start();
	}
	
}
