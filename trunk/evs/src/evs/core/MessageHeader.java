/**
 * 
 */
package evs.core;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import evs.interfaces.IMessageHeader;

/**
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 *
 */
public class MessageHeader implements IMessageHeader {
	
	private int messageLength;
	private InvocationStyle invocationStyle;
	
	private static final int HEADER_LENGTH = 8;
	
	public MessageHeader() {
		messageLength = 0;
		invocationStyle = InvocationStyle.SYNC;
	}

	public int getHeaderLength() {
		return HEADER_LENGTH;
	}

	public int getMessageLength() {
		return messageLength;
	}

	public InvocationStyle getInvocationStyle() {
		return invocationStyle;
	}

	public void setInvocationStyle(InvocationStyle invocationStyle) {
		this.invocationStyle = invocationStyle;
	}

	public void setMessageLength(int messageLength) {
		this.messageLength = messageLength;
	}

	public void readExternal(ObjectInput in)
		throws IOException, ClassNotFoundException {
		messageLength = in.readInt();
		int i = in.readInt();
		invocationStyle = InvocationStyle.fromInt(i);
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(messageLength);
		int i = invocationStyle.getValue();
		out.writeInt(i);
	}

}
