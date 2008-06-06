package evs.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import evs.exception.MarshallingException;
import evs.interfaces.IInvocationObject;
import evs.interfaces.IMarshaller;

/**
 * BasicMarshaller
 * provides marshalling and unmarshalling functions
 * 
 * @author Dirk Wallerstorfer
 *
 */

public class BasicMarshaller implements IMarshaller{

	/*
	 * (non-Javadoc)
	 * @see evs.interfaces.IMarshaller#serialize(evs.interfaces.IInvocationObject)
	 */
	public byte[] serialize(IInvocationObject object) throws MarshallingException{
		try {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			ObjectOutput s = new ObjectOutputStream(output);
			object.writeExternal(s);
			s.flush();
			s.close();
			output.close();
			return output.toByteArray();
		} catch(IOException ex){
			throw new MarshallingException(ex.getMessage());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see evs.interfaces.IMarshaller#deserialize(byte[])
	 */
	public IInvocationObject deserialize(byte[] bytes) throws MarshallingException{
		try {
			ByteArrayInputStream input = new ByteArrayInputStream(bytes);
			ObjectInput s = new ObjectInputStream(input);
			IInvocationObject obj = new InvocationObject();
			obj.readExternal(s);
			s.close();
			input.close();
			return obj;
		} catch(Exception ex){
			throw new MarshallingException(ex.getMessage());
		}

	
	}
	
}
