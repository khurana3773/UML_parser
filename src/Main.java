import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

public class Main
{

	private static PrintWriter pw;
	private static Map<String, CompilationUnit> classesAndCu = new HashMap<String, CompilationUnit>();
	private static StringBuilder plantUmlSource;

	public static void main(String[] args)
	{
		// deal with args for command line interface
		// String srcFolder = args[0];
		// String outputFolder = args[1];

		// temp, hard code file location:
		String srcFolder = "C:/Software/202/cmpe202-master/umlparser";

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
			testUML();
			plantUmlSource = new StringBuilder();			
			plantUmlSource.append("@startuml\n");
			getFiles(mainFolder);
			doSomething();
			
			plantUmlSource.append("@enduml");
			
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
				CompilationUnit cu = entry.getValue();
				createHierarchy(cu);				
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

	private static void createHierarchy(CompilationUnit cu)
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
					System.out.println(method);

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

					if (accessSpecifier.equals(AccessSpecifier.PUBLIC))
					{

					}
					else if (accessSpecifier.equals(AccessSpecifier.PROTECTED))
					{

					}
					else if (accessSpecifier.equals(AccessSpecifier.PRIVATE))
					{
						
					}
					else
					{

					}

					// pw.write(s);

					System.out.println(field);

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
