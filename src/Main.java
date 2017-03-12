import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

import com.github.javaparser.*;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.contexts.CompilationUnitContext;

public class Main
{

	public static void main(String[] args)
	{
		// deal with args for command line interface
		// String srcFolder = args[0];
		// String outputFolder = args[1];

		// temp, hard code file location:
		String srcFolder = "C:/Software/202/cmpe202-master/umlparser/uml-parser-test-1/A.java";

		System.out.println(srcFolder);
		File file = new File(srcFolder);
		if (!file.isDirectory())
		{
			 file = file.getParentFile();
		}
		
		
		
		if (file.exists())
		{
			
			CompilationUnit parse = JavaParser.parse(srcFolder);
			
			System.out.println(parse.getBegin());
			// System.out.println(parse.toString());

			ClassOrInterfaceDeclaration classA;
		}

	}

}
