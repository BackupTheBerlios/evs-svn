/**
 * 
 */
package evs.interfaces;

import java.io.Externalizable;

import evs.core.InvocationStyle;

/**
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 *
 */
public interface IMessageHeader extends Externalizable {
	
	int getHeaderLength();

	int getMessageLength();
	void setMessageLength(int messageLength);
	
	InvocationStyle getInvocationStyle();
	void setInvocationStyle(InvocationStyle invocationStyle);

}
