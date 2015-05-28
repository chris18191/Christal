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

	try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "utf-8"))) {
	    memoryCapacity++;
	    writer.write("#include <stdio.h>\n");
	    writer.write("#include <stdlib.h>\n\n");
	    writer.write("int main(int argc, char** argv) {\n");
	    writer.write("\tchar memory[" + memoryCapacity + "] = \"\";\n");
	    writer.write("\tint pointer = 0;\n");

	    // Write the program
	    for(Lexer.Token token: tokens) {
		if(token.type == Lexer.TokenType.RIGHT) {
		    writer.write("\t++pointer;\n");
		} else if(token.type == Lexer.TokenType.OUTPUT) {
		    writer.write("\tprintf(\"%c\", memory[pointer]);\n");
		} else if(token.type == Lexer.TokenType.INCREASE) {
		    writer.write("\t++memory[pointer];\n");
		} else if(token.type == Lexer.TokenType.DECREASE) {
		    writer.write("\t--memory[pointer];\n");
		} else if(token.type == Lexer.TokenType.LOOPSTART) {
                    writer.write("\twhile(memory[pointer]){\n");
                } else if(token.type == Lexer.TokenType.LOOPSTOP) {
                    writer.write("\t}\n");
                }

                writer.write("\treturn 0;\n");
                writer.write("}");
                writer.close();
            } catch(IOException ex) {
                System.out.println("Error writing to file " + outputFile);
                System.exit(-1);
            }
        }
    }
