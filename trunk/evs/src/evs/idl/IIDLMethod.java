package evs.idl;

import java.util.List;

public interface IIDLMethod extends IIDLGeneric{
		
	public List<IIDLParameter> getParameters();
	public void setParameters(List<IIDLParameter> parameters);
	public void addParameter(IIDLParameter parameter);
	
	public String getReturnType();
	public void setReturnType(String type);
	
	public List<String> getExceptions();
	public void setExceptions(List<String> exceptions);
	public void addException(String exception);
}