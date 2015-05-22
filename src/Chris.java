package src;

import src.lib.ArgumentParser;
import src.lib.ArgumentPosition;

import src.Lexer;
import src.Compiler;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * Chris reads command-line Arguments and Options. He then
 * passes the file / program-string to the lexer.
 */
public class Chris {
    /**
     * Main entry point for the compiler.
     * Reads all the commandline switches etc. and then passes them token
     * the lexer etc.
     */
    public static void main(String[] args) {

	//ArgumentParser.testAll();
	//Lexer.testAll();

	ArgumentParser parser = new ArgumentParser("Usage: java -jar Chris.jar [switches] [options] [files...]");
	parser.addOption("-o", "--output", "FILE", "The file to write to");
	
	parser.addSwitch("-d", "--debug", "Run tests before compiling");
	
	parser.searchPositionalsAt(ArgumentPosition.BACK);

	if(!parser.parse(args)) {
	    System.out.println(parser.getUsage());
	    System.exit(20);
	}

	HashMap<String, Boolean> switches = parser.getSwitches();
	HashMap<String, String> options = parser.getOptions();
	ArrayList<String> files = parser.getPositionals();

	boolean debug = switches.get("--debug");
	String outputFile = options.get("output");
	if(outputFile == null) {
	    outputFile = "out.c";
	}

	// Sanitize input (replace newlines, and read each command into one String. Join the Strings and give
	//  them to the Lexer).
	if(files.size() == 0) {
	    return;
	}
	String fileName = files.get(0);
	List<String> lines;
	try{
	    lines = Files.readAllLines(Paths.get(fileName),
				       Charset.defaultCharset());
	} catch(IOException ex) {
	    System.out.println("Error reading file " + fileName);
	    System.exit(-1);
	    return;
	}
	StringBuilder sb = new StringBuilder();
	for(String line : lines) {
	    sb.append(line);
	}
	String[] commands = sb.toString().split("");
	// TODO: populate arraylist
	ArrayList<Lexer.Token> tokens = Lexer.lex(new ArrayList<String>(Arrays.asList(commands)));

	int moves = 0;
	for(Lexer.Token tk : tokens) {
	    if(tk.type == Lexer.TokenType.LEFT || tk.type == Lexer.TokenType.RIGHT) {
		moves++;
	    }
	}

	Compiler.compile(tokens, outputFile, moves);
    }

	    /**
	     * Chris shall not be made, but instead just handle the
	     * commandline options
	     */
	    private Chris() {
	}
    }
