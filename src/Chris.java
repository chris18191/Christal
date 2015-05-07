package src;

import java.util.HashMap;

/**
 * Chris reads command-line Arguments and Options. He then
 * passes the file / program-string to the lexer.
 */
public class Chris {
    private static final String CHRISTAL_VERSION = "Christal Compiler Alpha 0.0";
    private static final String HELP_TEXT =
        CHRISTAL_VERSION
        + "\nUsage:\n"
        + "chris file.chris [Options]\n"
        + "\n"
        + "Options:\n"
        + "\t-o DIR\tOutput dir to place the .c file\n";
    /**
     * Main entry point for the compiler.
     * Reads all the commandline switches etc. and then passes them token
     * the lexer etc.
     */
    public static void main(String[] args) {
        // No arguments given. Print help
        if(args.length == 0) {
            System.out.println(HELP_TEXT);
            System.exit(-1);
        }
        HashMap<String, String> inputMap = parseInput(args);
        String inputFile = inputMap.get("input");
        String outputFile = intputMap.get("output");
    }

    public static void testAll() {
        testParseInput();
    }

    private static void testParseInput() {
        // Simulates: java -jar chris.jar -o my_output.c file.chris
        String[] fullArgs = {"-o", "my_output.c", "file.chris"};
        HashMap<String, String> map = parseInput(fullArgs);
        assert map.get("input").equals("file.chris");
        assert map.get("output").equals("my_output.c");

        String[] onlyInputArgs = {};
        map = parseInput(onlyInputArgs);
        assert map.get("input").equals("");
        assert !map.containsKey("output");
    }

    /**
     * Parses the command-line-argument-string-array into a HashMap with them
     * desired Values.
     */
    private HashMap<String, String> parseInput(String[] args) {
        //TODO: discuss if this shall be integrated into it's own class.
        HashMap<String, String> parsedMap = new HashMap<String, String>();

        // Don't parse all args, as the last one is the input file.
        for(int argCounter = 0; argCounter < args.length - 1; argCounter++){
            if(args[argCounter].equals("-o")) {
                // Option switch used, but no output specified
                if(args.length <= argCounter) {
                    continue;
                }
                // Update the output-file-name
                parsedMap.put("output", args[argCounter + 1]);
                argCounter++;
            }
        }

        // There was no output file specified, so use a predefined name
        if(!parsedMap.containsKey("output")){
            parsedMap.puts("output", "out.c");
        }
        parsedMap.put("input", args[args.length - 1]);

        return parsedMap;
    }
    /**
     * Chris shall not be made, but instead just handle the
     * commandline options
     */
    private Chris() {
    }
}
