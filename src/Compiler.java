package src;

import java.util.ArrayList;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.IOException;

import src.Lexer;

public class Compiler {
    private Compiler() {
    }

    public static void compile(ArrayList<Lexer.Token> tokens,
			       String outputFile,
			       int memoryCapacity) {

	try(Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "utf-8"))) {
	    memoryCapacity++;
	    writer.write("#include <stdio.h>\n\n");
	    writer.write("int main(int argc, char** argv) {\n");
	    writer.write("\tchar memory[" + memoryCapacity + "];\n");
	    writer.write("\tmemory = \"\";\n");

	    // Write the program

	    writer.write("\treturn 0;");
	    writer.write("}");
	    writer.close();
	} catch(IOException ex) {
	    System.out.println("Error writing to file " + outputFile);
	    System.exit(-1);
	}
    }
}
