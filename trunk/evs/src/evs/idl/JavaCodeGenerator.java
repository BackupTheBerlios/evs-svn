package evs.idl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

public class JavaCodeGenerator {

	public void generateClass(IIDLClass clazz) throws IOException{
		createOperationsInterface(clazz);
		createProxy(clazz);
		createRemoteObject(clazz);
		createInvoker(clazz);
	}
	
	public void generateException(IIDLException exception) throws IOException {
		FileWriter writer = prepareWriter((exception.getPackage() + ".exception"), exception.getName());
		writer.write("public class " + exception.getName() + " extends evs.exception.RemotingException {\n\n");
		
		writer.write(generateSerialVersionUID() + "\n");
		
		for(IIDLParameter param: exception.getVariables()){
			writer.write("\tpublic "+param.getType()+" "+param.getName() + ";\n");	
		}
		
		writer.write("\n");
		writer.write("\tpublic " + exception.getName() + "(){\n\t\tsuper();\n\t}\n\n");
		writer.write("\tpublic " + exception.getName() + "(String message){\n\t\tsuper(message);\n\t}\n");
		writer.write("}\n");
		writer.close();
	}
	
	//writes method header
	private void writeMethod(IIDLMethod method, FileWriter writer) throws IOException{
		//writer.write("\tpublic " + method.getReturnType() + " " + method.getName() + "(");
		writer.write("\tpublic Object " + method.getName() + "(");
		
		Iterator<IIDLParameter> iterator = method.getParameters().iterator();
		IIDLParameter param;
		while(iterator.hasNext()){
			param=iterator.next();
			writer.write(param.getType()+" "+param.getName());
			writer.write(", ");
		}
		writer.write("IACT act)");

		Iterator<String> exceptioniterator = method.getExceptions().iterator();
		String exception;
		writer.write(" throws ");
		while(exceptioniterator.hasNext()){
			exception = exceptioniterator.next();
			writer.write(exception);
			writer.write(", ");
		}
		writer.write("NotSupportedException");
	}
	
	//writes method body for proxy
	private void writeProxyMethod(IIDLMethod method, FileWriter writer) throws IOException{
		writer.write("\t\tArrayList<Object> arguments = new ArrayList<Object>();\n");
		for(IIDLParameter param: method.getParameters()){
			writer.write("\t\targuments.add(" + param.getName() + ");\n");
		}
		writer.write("\t\tObject returnObject = null;\n\n");
		writer.write("\t\ttry{\n");
		writer.write("\t\t\tIInvocationObject object = new InvocationObject(getAOR(), \"");
		writer.write(method.getName() + "\", arguments, \"" + method.getReturnType() + "\");\n");
		writer.write("\t\t\treturnObject = requestor.invoke(object, " + ((method.getReturnType().equals("void"))?"true":"false") + ", callback, act, requestType);\n");
		writer.write("\t\t} catch(RemotingException ex){\n");
		for(String exception: method.getExceptions()){
			writer.write("\t\t\t if(ex instanceof " + exception + ") throw (" + exception + ") ex;\n");
		}
		writer.write("\t\t\t throw new NotSupportedException(ex.getMessage());\n\t\t}\n");
		writer.write("\t\treturn returnObject;\n");
		
	}	
	
	//interface for operations provided to client by the remote object
	private void createOperationsInterface(IIDLClass clazz) throws IOException{
		FileWriter writer = prepareWriter(clazz.getPackage(), "I" + (clazz.getName() + "Operations"));
		writer.write("import evs.interfaces.IACT;\nimport evs.exception.NotSupportedException;\n\n");
		
		writer.write("public interface I" + clazz.getName() + "Operations {\n");
		
		for(IIDLMethod method: clazz.getMethods()){
			writeMethod(method, writer);
			writer.write(";\n");
		}
		
		writer.write("}\n");
		writer.close();
	}
	
	//client proxy implementation
	private void createProxy(IIDLClass clazz) throws IOException{
		FileWriter writer = prepareWriter((clazz.getPackage()+".client"), (clazz.getName() + "Proxy"));
		writer.write("import " + clazz.getPackage() + ".I" + clazz.getName() + "Operations;\nimport java.util.ArrayList;\nimport evs.core.*;\nimport evs.exception.*;\nimport evs.interfaces.*;\n\n");
		writer.write("public class " + clazz.getName() + "Proxy extends AClientProxy implements I" + clazz.getName()+"Operations{\n");
		
		writer.write("\n\t//automatically generated default values\n");
		writer.write("\tprivate static final String INVOKER_ID = " + clazz.getPackage() + ".server." + clazz.getName() + "Invoker.class.getName();\n");
		writer.write("\tprivate static final String OBJECT_ID = " + clazz.getPackage() + ".server." + clazz.getName() + ".class.getName();\n");
		writer.write("\tprivate static final String HOSTNAME = \"localhost\";\n");
		writer.write("\tprivate static final String PORT = \"31337\";\n\n");
		
		writer.write("\tpublic " + clazz.getName() + "Proxy(ICallback callback){\n");
		writer.write("\t\tsuper(callback);\n");
		writer.write("\t\tthis.aor = new AOR(new WebLocation(HOSTNAME, PORT), new ObjectReference(OBJECT_ID, INVOKER_ID));\n\t}\n\n");

		writer.write("\tpublic " + clazz.getName() + "Proxy(InvocationStyle requestType, ICallback callback){\n");
		writer.write("\t\tsuper(requestType, callback);\n");
		writer.write("\t\tthis.aor = new AOR(new WebLocation(HOSTNAME, PORT), new ObjectReference(OBJECT_ID, INVOKER_ID));\n\t}\n\n");		

		for(IIDLMethod method: clazz.getMethods()){
			writeMethod(method, writer);
			writer.write("{\n");
			writeProxyMethod(method, writer);
			writer.write("\t}\n\n");
		}

		writer.write("}\n");
		writer.close();
	}
	
	//method stubs for remote object implementation
	private void createRemoteObject(IIDLClass clazz) throws IOException{
		FileWriter writer = prepareWriter((clazz.getPackage() + ".server"), (clazz.getName()));
		writer.write("import " + clazz.getPackage() + ".I" + clazz.getName() + "Operations;\nimport evs.interfaces.IACT;\nimport evs.core.ARemoteObject;\nimport evs.exception.NotSupportedException;\n\n");
		writer.write("public class " + clazz.getName() + " extends ARemoteObject implements I" + clazz.getName() + "Operations{\n");
		
		writer.write("\n" + generateSerialVersionUID() + "\n");
		for(IIDLParameter parameter: clazz.getVariables()){
			writer.write("\tprivate " + parameter.getType() + " " + parameter.getName() + ";");
		}
		writer.write("\n\n");
		writer.write("\tpublic " + clazz.getName() + "(){\n\t\t/* Auto-Generated Constructor */\n");
		writer.write("\t\tthis.objectId = " + clazz.getName() + ".class.getName();\n\t}");
		writer.write("\n\n");
		for(IIDLMethod method: clazz.getMethods()){
			writeMethod(method, writer);
			writer.write("{\n\t\t/* Auto-Generated Method Stub */ \n\t}\n\n");
		}
		writer.write("}\n");
		writer.close();
		
	}
	
	//invoker implementation for remote object
	private void createInvoker(IIDLClass clazz) throws IOException{
		FileWriter writer = prepareWriter((clazz.getPackage() + ".server"), (clazz.getName() + "Invoker"));
		writer.write("import java.util.HashMap;\nimport java.lang.Integer;\nimport evs.core.*;\nimport evs.interfaces.*;\nimport evs.exception.*;\n\n");
		writer.write("public class " + clazz.getName() + "Invoker extends AInvoker{\n");
		
		//prepare HashMap with Operation-Mapping
		writer.write("\n\tstatic private final HashMap<String, Integer> operations = new HashMap<String, Integer>();\n");
		writer.write("\n\tstatic{\n");
		int i = 0;
		for(IIDLMethod method: clazz.getMethods()){
			writer.write("\t\toperations.put(\"" + method.getName() + "\", new java.lang.Integer("+i+"));\n");
			i++;
		}
		writer.write("\t\toperations.put(\"newInstance\", new java.lang.Integer("+i+"));\n");
		writer.write("\t\toperations.put(\"keepAlive\", new java.lang.Integer("+(i+1)+"));\n");
		writer.write("\t\toperations.put(\"destroy\", new java.lang.Integer("+(i+2)+"));\n");
		writer.write("\t}\n\n");	

		//create invoke-function
		writer.write("\tpublic IInvocationObject invoke(IInvocationObject object) throws RemotingException{\n");
		writer.write("\n\t\tfor(IInterceptor interceptor: Common.getServerInterceptors().getInterceptors()){\n");
		writer.write("\t\t\tinterceptor.beforeInvocation(object);\n\t\t}\n\n");
		
		
		writer.write("\t\t" + clazz.getName() + " localObject = null;\n\n");
		writer.write("\t\tInteger index = (Integer) operations.get(object.getOperationName());\n");
		writer.write("\t\tif(index == null) throw new IllegalMethodException(\"Method \" + object.getOperationName() + \" not supported\");\n\n");
		
		writer.write("\t\tif(index < 2){\n");
		writer.write("\t\t\tlocalObject = (" + clazz.getName() + ") Common.getObjectManager().invocationArrived(object.getObjectReference().getReference());\n");
		writer.write("\t\t\tif(localObject == null) throw new IllegalObjectException(\"Object \" + object.getObjectReference().getReference().getClientDependent() + \" not available\");\n\t\t}");
		
		
		writer.write("\n\n\t\tswitch(index.intValue()){\n");
		i=0;
		for(IIDLMethod method: clazz.getMethods()){
			writer.write("\t\t\tcase "+ i + ":\n\t\t\t\t");
			if(!method.getReturnType().equals("void")){
				writer.write("Object returnValue = ");
			}
			writer.write("localObject." + method.getName() +"(");
			Iterator<IIDLParameter> iterator = method.getParameters().iterator();
			IIDLParameter param;
			int j=0;
			while(iterator.hasNext()){
				param=iterator.next();
				writer.write("("+param.getType()+") object.getArguments().get("+j+")");
				j++;
				writer.write(", ");
			}
			writer.write("(IACT) object.getArguments().get("+ j + "));\n");
			if(!method.getReturnType().equals("void")){
				writer.write("\t\t\t\tobject.setReturnParam(returnValue);\n");
			}
			i++;
			writer.write("\t\t\t\tbreak;\n");
		}
		writer.write("\t\t\tcase "+ i + ":\n\t\t\t\t");
		writer.write("Integer id = Common.getObjectManager().newInstance(object.getObjectReference().getReference());\n\t\t\t\t");
		writer.write("object.setReturnParam(id.toString());\n\t\t\t\t");
		writer.write("return object;");
		writer.write("\n\t\t\tcase "+ (i+1) + ":\n\t\t\t\t");
		writer.write("Common.getObjectManager().keepAlive(object.getObjectReference().getReference());\n\t\t\t\t");
		writer.write("return object;");
		writer.write("\n\t\t\tcase "+ (i+2) + ":\n\t\t\t\t");
		writer.write("Common.getObjectManager().destroy(object.getObjectReference().getReference());\n\t\t\t\t");
		writer.write("return object;\n");
		
		writer.write("\t\t\tdefault:\n\t\t\t\tthrow new IllegalMethodException(\"Method \" +  object.getOperationName() + \" not supported\");\n\n");
		
		writer.write("\t\tCommon.getObjectManager().invocationDone(object.getObjectReference().getReference(), localObject);");
		writer.write("\n\n\t\tfor(IInterceptor interceptor: Common.getServerInterceptors().getInterceptors()){\n");
		writer.write("\t\t\tinterceptor.afterInvocation(object);\n\t\t}\n\n");
		writer.write("\t\treturn object;\n\t}\n}");
		
		writer.close();
	}
	
	private FileWriter prepareWriter(String packageName, String name) throws IOException {
		String path = "src/" + packageName.replaceAll("\\.", "\\/");
		(new File(path)).mkdirs();
		FileWriter writer = new FileWriter(path + "/" + name + ".java");
		writer.write("package " + packageName + ";\n");
		writer.write("\n/* Automatically generated by EVS JavaCodeGenerator */\n\n");
		return writer;
	}
	
	private String generateSerialVersionUID(){
		return "\tprivate static final long serialVersionUID = 1L;";
	}
	
}
