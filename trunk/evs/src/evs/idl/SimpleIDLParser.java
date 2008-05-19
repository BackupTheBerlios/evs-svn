package evs.idl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


//IDL Format:
//PACKAGE blub.blub.blub
//CLASS bla[
//type variablename
//returntype methodname(type parametername,...) THROWS exceptionname 
//]
//EXCEPTION bla[
//...
//]

public class SimpleIDLParser {

	private static int lineNumber = 1;
	private static BufferedReader reader;
	
	private static List<IIDLClass> classes = new ArrayList<IIDLClass>();
	private static List<IIDLException> exceptions = new ArrayList<IIDLException>();
	
	private static void usage(){
		System.out.println("[x] Usage: IDLParser file.idl");
		System.exit(1);
	}
	
	private static void parseFile(File file) throws FileNotFoundException, IOException, ParseException{
		reader = new BufferedReader(new FileReader(file));
		
		//first line == package name
		String line = reader.readLine();
		String[] wordArray = line.split(" ");
		
		if(!wordArray[0].equals("PACKAGE")){
			throw new ParseException("package expected", lineNumber);
		}

		String packageName = wordArray[1];
		System.out.println("[*] FOUND PACKAGE: " + packageName);
		
		//read classes and exceptions
		while((line = reader.readLine()) != null){
			lineNumber++;
			wordArray = line.split(" ");
			if(wordArray[0].equals("CLASS")){
				//parse class
				String name = wordArray[1].substring(0,wordArray[1].length()-1);
				System.out.println("[*] FOUND CLASS: " + name);
				IIDLClass newClass = new IDLClassDesc(name);
				newClass.setPackage(packageName);
				readClass(newClass);
				classes.add(newClass);
			} else if (wordArray[0].equals("EXCEPTION")){
				//parse exception
				String name = wordArray[1].substring(0,wordArray[1].length()-1);
				System.out.println("[*] FOUND EXCEPTION: " + name);
				IIDLException newException = new IDLExceptionDesc(name);
				newException.setPackage(packageName);
				readException(newException);
				exceptions.add(newException);
			} else {
				throw new ParseException("class or exception expected", lineNumber);
			}
		}
	}
	
	private static void readClass(IIDLClass newClass) throws IOException, ParseException {
		String line = "";
		while(!(line = reader.readLine()).equals("]")){
			lineNumber++;
			if(line.contains("(")){
				//method
				newClass.addMethod(readMethod(line));
			} else {
				//variable
				newClass.addVariable(readParameter(line));
			}
		}
	}
	
	private static void readException(IIDLException newException) throws IOException, ParseException {
		String line = "";
		while(!(line = reader.readLine()).equals("]")){
			lineNumber++;
			newException.addVariable(readParameter(line));
		}
	}
	
	private static IIDLParameter readParameter(String line) throws ParseException {
		String[] wordArray = line.split(" ");
		//FORMAT: type variablename
		if(wordArray.length != 2)
			throw new ParseException("Illegal Parameter Definition", lineNumber);
		return new IDLParameterDesc(wordArray[0], wordArray[1]);
	}
	
	private static IIDLMethod readMethod(String line) throws IOException, ParseException {
		//FORMAT: returntype methodname(type parametername,...) THROWS exceptionname 
		IIDLMethod newMethod;
	
		int index_1 = line.indexOf("(");
		int index_2 = 0;
		if((index_2 = line.indexOf(")", index_1)) == -1){
			throw new ParseException("Illegal Method Definition, ) missing", lineNumber);
		}
		
		try {
			//parse return type and and methodname
			String[] return_name = (line.substring(0, index_1)).split(" ");
			if(return_name.length == 1){
				newMethod = new IDLMethodDesc(return_name[0]);
			} else if(return_name.length == 2){
				newMethod = new IDLMethodDesc(return_name[1], return_name[0]);
			} else {
				throw new ParseException("Illegal Method Definition", lineNumber);
			}
			
			//read arguments
			if((index_1+1) != index_2){
				String[] arguments = (line.substring(index_1+1, index_2)).split(",");
				for(String s: arguments){
					s = s.trim();
					newMethod.addParameter(readParameter(s));
				}
			}
		
			//read exception
			if((index_1 = line.indexOf("THROWS", index_2)) != -1){
				String exceptionList = line.substring(index_1+7, line.length());
				String[] exceptions = exceptionList.split(",");
				for(String s: exceptions){
					s = s.trim();
					newMethod.addException(s);
				}
			}
		} catch(IndexOutOfBoundsException ex){
			throw new ParseException("Illegal Method Definition", lineNumber);
		}
			
		return newMethod;
	}
	
	
	
	/**
	 * Simple IDLParser
	 * @param Name of IDL-File to parse
	 */
	public static void main(String[] args) {
		if(args.length != 1) usage();
		
		//parse file
		try {
			System.out.println("[*] Parsing File " + args[0]);
			File idlFile = new File(args[0]);
			parseFile(idlFile);
			reader.close();
			System.out.println("[*] ..... done");
		} catch(FileNotFoundException ex){
			System.out.println("[x] File not found");
		} catch(IOException ex){
			System.out.println("[x] I/O Error: " + ex.getMessage());
			ex.printStackTrace();
		} catch (ParseException ex){
			System.out.println("[x] Invalid Syntax: " + ex.getMessage() + " in line " + ex.getErrorOffset());
			ex.printStackTrace();	 			
		}
		
		System.out.println("[*] Now generating Proxy and Stubs...");
		
		//call code generator
		JavaCodeGenerator generator = new JavaCodeGenerator();
		for(IIDLClass c: classes){
			try {
				generator.generateClass(c);
				System.out.println("[*] Class " + c.getName() + " successfull.");
			} catch (IOException ex){
				System.out.println("[x] Generating Files for class " + c.getName() + " failed: " + ex.getMessage());
				ex.printStackTrace();
			}
		}
		
		System.out.println("[*] Now generating Exceptions...");
		
		for(IIDLException e: exceptions){
			try {
				generator.generateException(e);
				System.out.println("[*] Exception " + e.getName() + " successfull.");
			} catch (IOException ex){
				System.out.println("[x] Generating Files for exception " + e.getName() + " failed: " + ex.getMessage());
				ex.printStackTrace();
			}
		}
		
		System.out.println("[*] ..... done");
	}

}
