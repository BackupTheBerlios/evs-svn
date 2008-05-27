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
		writer.write("public class " + exception.getName() + " extends evs.exception.RemotingException {\n");
		
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
		writer.write("\tpublic " + method.getReturnType() + " " + method.getName() + "(");
		
		Iterator<IIDLParameter> iterator = method.getParameters().iterator();
		IIDLParameter param;
		while(iterator.hasNext()){
			param=iterator.next();
			writer.write(param.getType()+" "+param.getName());
			if(iterator.hasNext())
				writer.write(", ");
		}
		writer.write(")");

		Iterator<String> exceptioniterator = method.getExceptions().iterator();
		String exception;
		if(exceptioniterator.hasNext()) writer.write(" throws ");
		while(exceptioniterator.hasNext()){
			exception = exceptioniterator.next();
			writer.write(exception);
			if(exceptioniterator.hasNext())
				writer.write(", ");
		}
	}
	
	//writes method body for proxy
	private void writeProxyMethod(IIDLMethod method, FileWriter writer) throws IOException{
		//TODO: add asynchronous invocation styles
		//TODO: get AOR of object
		//Todo generate arguments
		
		writer.write("\t\tArrayList<Object> arguments = new ArrayList<Object>();\n");
		for(IIDLParameter param: method.getParameters()){
			writer.write("\t\targuments.add(" + param.getName() + ");\n");
		}
		writer.write("\t\ttry{\n");
		writer.write("\t\t\tevs.comm.InvocationObject object = new evs.comm.InvocationObject(null /*TODO Reference*/, \"");
		writer.write(method.getName() + "\", arguments, \"" + method.getReturnType() + "\");\n");
		if(!method.getReturnType().equals("void")){
			writer.write("\t\t\treturn (" + method.getReturnType() + ") ");
		} else {
			writer.write("\t\t\t");
		}
		writer.write("requestor.invoke(object, " + ((method.getReturnType().equals("void"))?"true":"false") + ");\n");
		writer.write("\t\t} catch(evs.exception.RemotingException ex){\n");
		for(String exception: method.getExceptions()){
			writer.write("\t\t\t if(ex instanceof " + exception + ") throw (" + exception + ") ex;\n");
		}
		//writer.write("\t\t\t throw new Exception(\"ERROR: \" + ex.getMessage());\n\t\t}\n");
		//TODO: what to do in case of remoting exceptions?
		
	}	
	
	//interface for operations provided to client by the remote object
	private void createOperationsInterface(IIDLClass clazz) throws IOException{
		FileWriter writer = prepareWriter(clazz.getPackage(), "I" + (clazz.getName() + "Operations"));
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
		writer.write("import " + clazz.getPackage() + ".I" + clazz.getName() + "Operations;\nimport java.util.ArrayList;\n\n");
		writer.write("public class " + clazz.getName() + "Proxy implements I" + clazz.getName()+"Operations{\n");
		
		//TODO: get requestor
		writer.write("\n\tprivate evs.comm.IRequestor requestor = new evs.comm.Requestor();\n\n");

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
		writer.write("import " + clazz.getPackage() + ".I" + clazz.getName() + "Operations;\n\n");
		writer.write("public class " + clazz.getName() + " implements I" + clazz.getName() + "Operations{\n");
		
		for(IIDLParameter parameter: clazz.getVariables()){
			writer.write("\tprivate " + parameter.getType() + " " + parameter.getName() + ";");
		}
		writer.write("\n\n");
		writer.write("\tpublic " + clazz.getName() + "(){\n\t\t/* Auto-Generated Constructor */\n\t}");
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
		writer.write("import java.util.HashMap;\nimport java.lang.Integer;\n\n");
		writer.write("public class " + clazz.getName() + "Invoker implements evs.comm.IInvoker{\n");
		
		//TODO: implement activation strategy
		writer.write("\n\tstatic private " + clazz.getName() + " localObject = new " + clazz.getName() + "();\n");
		
		//prepare HashMap with Operation-Mapping
		writer.write("\n\tstatic private final HashMap<String, Integer> operations = new HashMap<String, Integer>();\n");
		writer.write("\n\tstatic{\n");
		int i = 0;
		for(IIDLMethod method: clazz.getMethods()){
			writer.write("\t\toperations.put(\"" + method.getName() + "\", new java.lang.Integer("+i+"));\n");
			i++;
		}
		writer.write("\t}\n\n");	

		//create invoke-function
		writer.write("\tpublic byte[] invoke(byte[] message) throws evs.exception.RemotingException{\n");
		writer.write("\t\tevs.comm.IMarshaller marshaller = new evs.comm.BasicMarshaller();\n");
		writer.write("\t\tevs.comm.InvocationObject object = marshaller.deserialize(message);\n");
		writer.write("\t\tInteger index = (Integer) operations.get(object.getOperationName());\n");
		writer.write("\t\tif(index == null) throw new evs.exception.IllegalMethodException();\n\n");
		
		writer.write("\t\tswitch(index.intValue()){\n");
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
				if(iterator.hasNext())
					writer.write(", ");
			}
			writer.write(");\n");
			if(!method.getReturnType().equals("void")){
				writer.write("\t\t\t\tobject.setReturnParam(returnValue);\n");
			}
			i++;
			writer.write("\t\t\t\tbreak;\n");
		}
		writer.write("\t\t\tdefault:\n\t\t\t\tthrow new evs.exception.IllegalMethodException();\n");
		writer.write("\t\t}\n\n\t\treturn marshaller.serialize(object);\n\t}\n}");
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
	
}
