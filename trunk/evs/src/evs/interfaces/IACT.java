package evs.interfaces;

import java.io.Externalizable;
import java.sql.Timestamp;

public interface IACT extends Externalizable {
	
	public Timestamp getTimestamp();
	public void setTimestamp(Timestamp timestamp);

}
