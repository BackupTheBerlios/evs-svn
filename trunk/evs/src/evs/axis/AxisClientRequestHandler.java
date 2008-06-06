/**
 * 
 */
package evs.axis;

import java.net.SocketAddress;

import javax.xml.namespace.QName;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;

import evs.exception.RemotingException;
import evs.interfaces.IClientRequestHandler;

/**
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 *
 */
public class AxisClientRequestHandler implements IClientRequestHandler {

	public byte[] send(SocketAddress address, byte[] request)
		throws RemotingException {
		RPCServiceClient client;
		try {
			client = new RPCServiceClient();
		} catch (AxisFault e) {
			throw new RemotingException(e);
		}
		
		Options options = client.getOptions();
		EndpointReference endPointRef =
			new EndpointReference("http://localhost:8080/axis2/services/InvokerService");
		options.setTo(endPointRef);
		QName invoke = new QName("http://axis.evs/xsd","invoke");
		Object[] invokeArgs = new Object[] {request};
		Class<?>[] returnTypes = new Class[] {byte[].class};
		Object[] response;
		try {
			response = client.invokeBlocking(invoke,invokeArgs,returnTypes);
		} catch (AxisFault e) {
			throw new RemotingException(e);
		}
		byte[] result = (byte[]) response[0];
		return result;
	}

	public void fireAndForget(SocketAddress address, byte[] request)
		throws RemotingException {
		RPCServiceClient client;
		try {
			client = new RPCServiceClient();
		} catch (AxisFault e) {
			throw new RemotingException(e);
		}
		
		Options options = client.getOptions();
		EndpointReference endPointRef =
			new EndpointReference("http://localhost:8080/axis2/services/InvokerService");
		options.setTo(endPointRef);
		QName invoke = new QName("http://axis.evs/xsd","fireAndForget");
		Object[] invokeArgs = new Object[] {request};
		try {
			client.invokeRobust(invoke,invokeArgs);
		} catch (AxisFault e) {
			throw new RemotingException(e);
		}
	}

}
