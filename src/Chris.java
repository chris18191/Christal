package src;

import src.lib.ArgumentParser;
import src.lib.ArgumentPosition;

import java.util.HashMap;
import java.util.ArrayList;


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

        ArgumentParser.testAll();

        ArgumentParser parser = new ArgumentParser("Usage: java -jar Chris.jar [switches] [options] [files...]");
        parser.addOption("-o", "--output", "FILE", "The file to write to");
        parser.searchPositionalsAt(ArgumentPosition.BACK);

        if(!parser.parse(args)) {
            System.out.println(parser.getUsage());
            System.exit(-1);
        }
        HashMap<String, Boolean> switches = parser.getSwitches();
        HashMap<String, String> options = parser.getOptions();
        ArrayList<String> files = parser.getPositionals();

        Boolean debug = switches.get("--debug");
        String outputFile = options.get("output");
    }

    /**
     * Chris shall not be made, but instead just handle the
     * commandline options
     */
    private Chris() {
    }
}
