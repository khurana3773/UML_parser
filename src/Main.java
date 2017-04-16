import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.text.FieldView;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import h.field_t;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

public class Main
{

	private static PrintWriter pw;
	private static Map<String, CompilationUnit> classesAndCu = new HashMap<String, CompilationUnit>();
	private static StringBuilder plantUmlSource;
	private static StringBuilder writeDependency;

	public static void main(String[] args)
	{
		// deal with args for command line interface
		// String srcFolder = args[0];
		// String outputFolder = args[1];

	
		// temp, hard code file location:
		String srcFolder = "C:/Software/202/cmpe202-master/umlparser/uml-parser-test-1";

		
		System.out.println(srcFolder);
		File mainFolder = new File(srcFolder);
		if (!mainFolder.exists())
		{
			// error
		}
		if (!mainFolder.isDirectory())
		{
			mainFolder = mainFolder.getParentFile();
		}

		try
		{
			//testUML();
			plantUmlSource = new StringBuilder();			
			plantUmlSource.append("@startuml \n");
			plantUmlSource.append("!pragma graphviz_dot jdot \n");
			plantUmlSource.append("skinparam classAttributeIconSize 0 \n");
			getFiles(mainFolder);
			doSomething();
			
			plantUmlSource.append("@enduml");
			
			System.out.println(plantUmlSource);
			
			SourceStringReader reader = new SourceStringReader(plantUmlSource.toString());
			FileOutputStream output = new FileOutputStream(new File("C:\\Software\\202\\test\\test.png"));
			reader.generateImage(output, new FileFormatOption(FileFormat.PNG, false));
			
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e2)
		{

		}

		// ClassOrInterfaceDeclaration classA =
		// compilationUnit.getClassByName("A");

	}

	private static void doSomething()
	{
			Set<Entry<String,CompilationUnit>> entrySet = classesAndCu.entrySet();
			
			
			for (Entry<String, CompilationUnit> entry : entrySet)
			{
				writeDependency = new StringBuilder();
				
				
				
				plantUmlSource.append("class ");
				
				
				plantUmlSource.append(entry.getKey());
				plantUmlSource.append("{ ");
				CompilationUnit cu = entry.getValue();
				
				
				createHierarchy(entry.getKey() , cu);
				
				plantUmlSource.append("\n ");
				plantUmlSource.append("} ");
				plantUmlSource.append("\n ");
				
				plantUmlSource.append(writeDependency);
				
			}
	}

	// private static class MethodChangerVisitor extends
	// VoidVisitorAdapter<Void>
	// {
	// @Override
	// public void visit(MethodDeclaration n, Void arg)
	// {
	// // change the name of the method to upper case
	// n.getDeclarationAsString();
	// // n.setName(n.getNameAsString().toUpperCase());
	//
	// // add a new parameter to the method
	// n.addParameter("int", "value");
	// }
	// }
	
	private static void findMethod(CompilationUnit cu)
	{
		NodeList<TypeDeclaration<?>> types = cu.getTypes();
		for (TypeDeclaration<?> type : types)
		{
			// Go through all fields, methods, etc. in this type
			NodeList<BodyDeclaration<?>> members = type.getMembers();
			for (BodyDeclaration<?> member : members)
			{

				if (member instanceof MethodDeclaration)
				{
					MethodDeclaration method = (MethodDeclaration) member;
					System.out.println(method);

				}
			}
		}
		
	}

	private static void createHierarchy(String className,CompilationUnit cu)
	{
		// Go through all the types in the file
		NodeList<TypeDeclaration<?>> types = cu.getTypes();
		for (TypeDeclaration<?> type : types)
		{
			// Go through all fields, methods, etc. in this type
			NodeList<BodyDeclaration<?>> members = type.getMembers();
			for (BodyDeclaration<?> member : members)
			{

				if (member instanceof MethodDeclaration)
				{
					MethodDeclaration method = (MethodDeclaration) member;
					EnumSet<Modifier> modifiers = method.getModifiers();
					AccessSpecifier accessSpecifier = Modifier.getAccessSpecifier(modifiers);
					if (accessSpecifier.equals(AccessSpecifier.PUBLIC))
					{
						plantUmlSource.append("\n");
						plantUmlSource.append("+"+ method.getName() +"(" );
						NodeList<Parameter> parameters = method.getParameters();
						ListIterator<Parameter> listIterator = parameters.listIterator(0);
						
						while(listIterator.hasNext())
						{
							Parameter parameter = listIterator.next();
							plantUmlSource.append(parameter.getName());
							
							if(listIterator.hasNext()==true)
							{
								plantUmlSource.append(",");
							}
						}
						
						plantUmlSource.append(")");
						
						
					} 
					
					
					plantUmlSource.append("\n");

				}

				if (member instanceof FieldDeclaration)
				{
					FieldDeclaration field = (FieldDeclaration) member;
					// field.ge
					EnumSet<Modifier> modifiers = field.getModifiers();
					AccessSpecifier accessSpecifier = Modifier.getAccessSpecifier(modifiers);
					// Iterator<Modifier> iterator = modifiers.iterator();
					// while(iterator.hasNext())
					// {
					// Modifier next = iterator.next();
					// next.getAccessSpecifier(modifiers)
					// }
					System.out.println(field.getClass());
					field.getNodeLists();
					field.getChildNodes();
					NodeList<VariableDeclarator> variables = field.getVariables();
					
					for (VariableDeclarator variable : variables)
					{
						String classNameInField = variable.getType().toString();
						
						if(classesAndCu.containsKey(classNameInField))
						{
							writeDependency.append(className + " ---- " +classNameInField);
						}
						else
						{						
							if (accessSpecifier.equals(AccessSpecifier.PUBLIC))
							{
								plantUmlSource.append(" \n ");
								plantUmlSource.append(" + " +field  );
								plantUmlSource.append(" \n ");
							}
							
							else if (accessSpecifier.equals(AccessSpecifier.PRIVATE))
							{
								plantUmlSource.append(" \n ");
								plantUmlSource.append(" - " +field  );
								plantUmlSource.append(" \n ");
							}
						
							
						}
					}
					
					
				}

			}
		}

	}

	public static void getFiles(File folder) throws FileNotFoundException
	{

		File[] allFiles = folder.listFiles();
		for (int i = 0; i < allFiles.length; i++)
		{
			if (allFiles[i].isFile() && allFiles[i].getName().toLowerCase().endsWith(".java"))
			{

				System.out.println("File " + allFiles[i].getName());
				CompilationUnit cu = JavaParser.parse(allFiles[i]); // remove throws
																	// and add
																	// try catch
				NodeList<TypeDeclaration<?>> types = cu.getTypes();
				

				cu.getNodesByType(FieldDeclaration.class).stream().
		        filter(f -> f.getModifiers().contains(AccessSpecifier.PUBLIC)).
		        forEach(f -> System.out.println("Check field at line " + f.getBegin().get().line));
				
				
				classesAndCu.put(allFiles[i].getName(), cu);

			}
			else if (allFiles[i].isDirectory())
			{
				getFiles(allFiles[i]);
			}
		}
	}

	public static void testUML() throws IOException
	{
		StringBuilder plantUmlSource = new StringBuilder();
		plantUmlSource.append("@startuml\n");
		plantUmlSource.append("Alice -> Bob: Authentication Request\n");
		plantUmlSource.append("Bob --> Alice: Authentication Response\n");
		plantUmlSource.append("@enduml");
		SourceStringReader reader = new SourceStringReader(plantUmlSource.toString());
		FileOutputStream output = new FileOutputStream(new File("C:\\Software\\202\\test\\test.png"));
		reader.generateImage(output, new FileFormatOption(FileFormat.PNG, false));
	}
}
