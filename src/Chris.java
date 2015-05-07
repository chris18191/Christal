package src;
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
            System.exit(0);
        }

        for(String s : args) {
            System.out.println(s);
        }
    }
    /**
     * Chris shall not be made, but instead just handle the
     * commandline options
     */
    private Chris() {
    }
}
