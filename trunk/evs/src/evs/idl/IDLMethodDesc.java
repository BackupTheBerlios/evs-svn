package evs.idl;

import java.util.ArrayList;
import java.util.List;

public class IDLMethodDesc implements IIDLMethod {

	private String name;
	private String returnType;
	private List<IIDLParameter> parameters;
	private List<String> exceptions;
	
	public IDLMethodDesc(){
		this.parameters = new ArrayList<IIDLParameter>();
		this.exceptions = new ArrayList<String>();
		returnType = "void";
	}
	
	public IDLMethodDesc(String name){
		this();
		this.name = name;
	}
	
	public IDLMethodDesc(String name, String returnType){
		this();
		this.name = name;
		this.returnType = returnType;
	}
	
	public IDLMethodDesc(String name, String returnType, List<IIDLParameter> parameters){
		this(name, returnType);
		this.parameters.addAll(parameters);
	}
	
	public IDLMethodDesc(String name, String returnType, List<IIDLParameter> parameters,
						 List<String> exceptions){
		this(name, returnType, parameters);
		this.exceptions.addAll(exceptions);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getReturnType() {
		return returnType;
	}
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	
	public List<IIDLParameter> getParameters() {
		return parameters;
	}
	public void setParameters(List<IIDLParameter> parameters) {
		this.parameters = parameters;
	}
	public void addParameter(IIDLParameter parameter){
		this.parameters.add(parameter);
	}
	
	public List<String> getExceptions() {
		return exceptions;
	}
	public void setExceptions(List<String> exceptions) {
		this.exceptions = exceptions;
	}
	public void addException(String exception){
		this.exceptions.add(exception);
	}
}
