package evs.idl;

import java.util.List;

public interface IIDLException extends IIDLGeneric{
	
	public String getPackage();
	public void setPackage(String packageName);
	
	public List<IIDLParameter> getVariables();
	public void setVariables(List<IIDLParameter> variables);
	public void addVariable(IIDLParameter variable);
}
