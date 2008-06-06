package evs.core;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.Timestamp;

import evs.interfaces.IACT;

/**
 * ACT - asynchronous completion token
 * the client uses an ACT for determinig which response belongs
 * to which request when using asynchronous communication
 * style
 * 
 * @author Dirk Wallerstorfer
 *
 */
public class ACT implements IACT{

	private Timestamp timestamp;

	/**
	 * constructor
	 */
	public ACT() {
		this.timestamp = new Timestamp(System.currentTimeMillis());
	}
	
	/*
	 * (non-Javadoc)
	 * @see evs.interfaces.IACT#getTimestamp()
	 */
	public Timestamp getTimestamp() {
		return timestamp;
	}

	/*
	 * (non-Javadoc)
	 * @see evs.interfaces.IACT#setTimestamp(java.sql.Timestamp)
	 */
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * implementation of Externalizable interface
	 * @param in ObjectInput to be read from
	 * @throws IOException when ObjectInput is corrupt
	 * @throws ClassNotFoundException when the class cannot be found
	 */
	public void readExternal(ObjectInput in) throws IOException,
            ClassNotFoundException
    {
		this.timestamp = (Timestamp)in.readObject();
    }

	/**
	 * implementation of Externalizable interface
	 * @param out ObjectOutput to be written to
	 * @throws IOException when ObjectOutput is corrupt
	 */
	public void writeExternal(ObjectOutput out) throws IOException
    {
	    out.writeObject(this.timestamp);	    
    }
	
	
}
