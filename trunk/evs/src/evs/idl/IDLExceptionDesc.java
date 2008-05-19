package evs.idl;

import java.util.ArrayList;
import java.util.List;

public class IDLExceptionDesc implements IIDLException {

	private String name;
	private String packageName;
	private List<IIDLParameter> variables;
	
	public IDLExceptionDesc(){
		this.variables = new ArrayList<IIDLParameter>();
	}
	
	public IDLExceptionDesc(String name){
		this();
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPackage() {
		return packageName;
	}
	public void setPackage(String packageName) {
		this.packageName = packageName;
	}

	public List<IIDLParameter> getVariables() {
		return variables;
	}
	public void setVariables(List<IIDLParameter> variables) {
		this.variables = variables;
	}
	public void addVariable(IIDLParameter variable){
		this.variables.add(variable);
	}
}
