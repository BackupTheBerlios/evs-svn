package evs.idl;

import java.util.ArrayList;
import java.util.List;

public class IDLClassDesc implements IIDLClass {

	private String name;
	private String packageName;
	private List<IIDLParameter> variables;
	private List<IIDLMethod> methods;
	
	public IDLClassDesc(){
		this.variables = new ArrayList<IIDLParameter>();
		this.methods = new ArrayList<IIDLMethod>();
	}
	
	public IDLClassDesc(String name){
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
	
	public List<IIDLMethod> getMethods() {
		return methods;
	}
	public void setMethods(List<IIDLMethod> methods) {
		this.methods = methods;
	}
	public void addMethod(IIDLMethod method){
		this.methods.add(method);
	}
	
	
}
