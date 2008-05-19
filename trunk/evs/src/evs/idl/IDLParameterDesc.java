package evs.idl;

public class IDLParameterDesc implements IIDLParameter {

	private String name;
	private String type;
	
	public IDLParameterDesc(){}
	
	public IDLParameterDesc(String type, String name){
		this.name = name;
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
