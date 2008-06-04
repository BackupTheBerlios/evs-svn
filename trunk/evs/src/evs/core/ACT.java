package evs.core;

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
	
	
}
