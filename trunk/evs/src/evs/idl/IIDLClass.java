package evs.idl;

import java.util.List;

public interface IIDLClass extends IIDLGeneric{
	
	public String getPackage();
	public void setPackage(String packageName);
	
	public List<IIDLMethod> getMethods();
	public void setMethods(List<IIDLMethod> methods);
	public void addMethod(IIDLMethod method);
	
	public List<IIDLParameter> getVariables();
	public void setVariables(List<IIDLParameter> variables);
	public void addVariable(IIDLParameter variable);

}