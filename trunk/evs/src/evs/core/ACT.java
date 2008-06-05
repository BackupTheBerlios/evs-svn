package evs.core;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.Timestamp;

import evs.interfaces.IACT;

public class ACT implements IACT{

	private Timestamp timestamp;

	public ACT() {
		this.timestamp = new Timestamp(System.currentTimeMillis());
	}
	
	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public void readExternal(ObjectInput in) throws IOException,
            ClassNotFoundException
    {
		this.timestamp = (Timestamp)in.readObject();
    }

	public void writeExternal(ObjectOutput out) throws IOException
    {
	    out.writeObject(this.timestamp);	    
    }
	
	
}
