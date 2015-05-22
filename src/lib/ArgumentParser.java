package src.lib;

import java.util.HashMap;
import java.util.ArrayList;

import src.lib.ArgumentPosition;

public class ArgumentParser{

    private boolean isParsed;

    private HashMap<String, String> optionMap;
    private HashMap<String, Boolean> switchMap;
    private HashMap<String, String> formTranslator;

    private ArrayList<String> usageList;
    private ArrayList<String> positionals;

    private ArgumentPosition positionalsPosition;

    /**
     * A simple Argument Parser to analyze commandline arguments.
     * Example:
     * In myfile.java:
     *     ArgumentParser parser = new ArgumentParser("Usage: SampleProgram [switches] [options] [files...]);
     *     parser.addSwitch("-d", "--debug", "Enables debug mode");
     *     parser.addOption("-o", "--output", "FILE", "The name of the file that shall be written");
     *     parser.searchPositionalsAt(ArgumentPosition.BACK);
     *     boolean parserSucceeded = parser.parse(args);
     *     if(!parserSucceeded) {
     *         System.out.println(parser.getUsage());
     *         System.exit(-1);
     *     }
     *
     *     HashMap<String, String> options = parser.getOptions();
     *     HashMap<String, boolean> switches = parser.getSwitches();
     *     boolean isDebug = switches.get("--debug");   // same as: boolean isDebug = switches.get("-d");
     *     String outputFile = options.get("--output"); // same as: String outputFile = options.get("-o");
     *     ArrayList<String> positionals = parser.getPositionals();
     *
     *
     * @param usage A short String that describes how to invoke the Program
     */
    public ArgumentParser(String usage) {
        isParsed = false;

        switchMap = new HashMap<String, Boolean>();
        optionMap = new HashMap<String, String>();
        formTranslator = new HashMap<String, String>();

        positionals = new ArrayList<String>();
        usageList = new ArrayList<String>();
        usageList.add(usage);

        positionalsPosition = ArgumentPosition.NONE;
    }

    /**
     * Adds a switch to the Programs parameters.
     * Example:
     * In myfile.java:
     *     ArgumentParser parser = new ArgumentParser("Usage: SampleProgram [switches]);
     *     parser.addSwitch("-d", "--debug", "Debug switch");
     *     assert parser.parse(args);
     *
     * In commandline:
     *     java -jar myprogram.jar -d
     *                             ^~ The Switch
     *
     *
     * @param shortForm The name of the Switch that shall be set.
     * @param longForm The abbreviation of the Switch to be set.
     * @param description The description of the Switch. It will be used in the usage String.
     *
     */
    public void addSwitch(String shortForm, String longForm, String description) {
        switchMap.put(shortForm, false);
        switchMap.put(longForm, false);

        addToFormTranslator(shortForm, longForm);

        addToUsage("\t" + shortForm + ", " + longForm + "\t\t\t" + description);
    }

    /**
     * Returns the Switch Hashmap
     * Example:
     * In myfile.java:
     *    ArgumentParser parser = new ArgumentParser("Usage: SampleProgram [switches]);
     *    parser.addSwitch("-d", "--debug", "Debug mode");
     *    assert parser.parse(args);
     *    HashMap<String, Boolean> switches = parser.getSwitches();
     *    Boolean debugMode = switches.get("--debug");
     *
     *
     * @return The Switch Hashmap, in which all the switches are stored. If the value of a switch-key is true, that means it is activated.
     */
    public HashMap<String, Boolean> getSwitches() {
        assert this.isParsed : "Argumentparser has not parsed it's arguments yet, so you cannot check for a Switch yet.";
        return switchMap;
    }

    /**
     * Adds an option to the Programs parameters.
     * Example:
     * In myfile.java:
     *     ArgumentParser parser = new ArgumentParser("Usage: SampleProgram [options]");
     *     parser.addOption("-o", "--output", "FILE", "The file name to write to");
     *     assert parser.parse(args);
     *
     * In commandline:
     *     java -jar myprogram.jar -o newfile.c
     *                             ^~~~~~~~~~~~ The Option(s)
     *
     *
     * @param shortForm The short form of the option
     * @param longForm The long form of the option
     * @param exampleValue The value that will be shown in the usage string
     * @param description Description of the option
     *
     */
    public void addOption(String shortForm, String longForm, String exampleValue, String description) {
        optionMap.put(shortForm, null);
        optionMap.put(longForm, null);

        addToFormTranslator(shortForm, longForm);

        addToUsage("\t" + shortForm + ", " + longForm + " " + exampleValue + "\t\t" + description);
    }

    /**
     * Returns the Option HashMap, in which the Options are stored.
     * Example:
     * In myfile.java:
     *     ArgumentParser parser = new ArgumentParser("Usage: SampleProgram [options]");
     *     parser.addOption("-o", "--output");
     *     assert parser.parse(args);
     *     HashMap<String, String> options = parser.getOptions();
     *     String outputFile = options.get("--output");
     *     if(outputFile == null) {
     *         outputFile = "a.c";
     *     }
     *
     *
     * @return The HashMap with all the options in it. If the value
     *         was not specified by the user, it is null
     */
    public HashMap<String, String> getOptions() {
        assert this.isParsed : "Argumentparser has not parsed it's arguments yet, so you cannot retrieve an Option yet";
        return this.optionMap;
    }

    /**
     * Specifies the position to where the Positional arguments are located.
     * Example:
     * In myfile.java:
     *     ArgumentParser parser = new ArgumentParser("Usage: Sample Program [switches] [files...]");
     *     parser.searchPositionalsAt(ArgumentPosition.BACK);
     *     parser.addSwitch("-d", "--debug", "Debug mode");
     *     assert parser.parse(args);
     *     ArrayList<String> positionals = parser.getPositionals();
     *
     * In commandline:
     *    java -jar myfile.jar -d file1 file2 file3
     *                            ^~~~~~~~~~~~~~~~~ The Positional arguments (stored in positionals)
     *
     *
     * @param where The Position, where the positionals shall be searched for
     */
    public void searchPositionalsAt(ArgumentPosition where) {
        this.positionalsPosition = where;
    }

    /**
     * Returns the ArrayList of positional arguments.
     * Example:
     * In myfile.java:
     *     ArgumentParser parser = new ArgumentParser("Usage: SampleProgram [files...]");
     *     parser.searchPositionalsAt(ArgumentPosition.BACK);
     *     assert parser.parse(args);
     *     ArrayList<String> positionals = parser.getPositionals();
     *
     *
     * @return The ArrayList with the positional arguments in it
     */
    public ArrayList<String> getPositionals() {
        assert this.isParsed : "Argumentparser has not parsed it's arguments yet, so you cannot retrieve the positionals yet";
        return this.positionals;
    }


    /**
     * Parses the given Arguments and stores them in all the HashMaps and Arraylists.
     * Example:
     * In myfile.java:
     * public static void main(String[] args) {
     *     ArgumentParser parser = new ArgumentParser("Usage: java -jar myfile.jar [switches] [options] [files...]");
     *     parser.searchPositionalsAt(ArgumentPosition.BACK);
     *     parser.addSwitch("-d", "--debug", "Enable debug mode");
     *     parser.addSwitch("-v", "--verbose", "Enable verbose logging mode");
     *
     *     parser.addOption("-o", "--output", "FILE", "The file to print the output to");
     *     parser.addOption("-l", "--load", "FILE", "A file to load source code from");
     *
     *     if(!parser.parse(args)) {
     *         System.out.println(parser.getUsage());
     *         System.exit(-1);
     *     }
     * }
     *
     *
     *
     * @param args The arguments to parse
     * @return Returns true if parsing was successful, false if it it wasn't
     */
    public boolean parse(String[] args) {
        // There are not arguments. Therefore this is an error.
        if(args.length == 0) {
            System.out.println("No Arguments provided.");
            return false;
        }

        // Look if there shall be any positional Arguments at the back.
        // If there are, we just take every argument as positional, until we hit the first
        // argument that starts with a '--' or a '-', which are signs for switches or options.
        if(positionalsPosition == ArgumentPosition.BACK) {
            for(int currentArgCounter = args.length - 1; currentArgCounter > 0; currentArgCounter--) {
                String currentArg = args[currentArgCounter];
                if(currentArg.startsWith("--") || currentArg.startsWith("-")) {
                    break;
                }
                positionals.add(currentArg);
            }
        }

        boolean foundArgument = false;
        for(int currentArgIndex = 0; currentArgIndex < args.length; currentArgIndex++) {
            String currentArg = args[currentArgIndex];
            if(currentArg.startsWith("--") || currentArg.startsWith("-")) {
                // We found our first real argument. This means that following words (without '--' or '-')
                // are *no* optionals. (As they are supposed to be at the front.)
                foundArgument = true;
                String otherForm = formTranslator.get(currentArg);
                // Found a switch
                if(switchMap.containsKey(currentArg) || switchMap.containsKey(otherForm)) {
                    switchMap.put(currentArg, true);
                    switchMap.put(otherForm, true);
                } else if (optionMap.containsKey(currentArg) || optionMap.containsKey(otherForm)){
                    // Found an option
                    String optionValue = currentArgIndex < args.length - 1 ? args[currentArgIndex + 1] : null;
                    optionMap.put(currentArg, optionValue);
                    optionMap.put(otherForm, optionValue);
                    // Skip the option Value for next loop
                    currentArgIndex++;
                } else {
                    // Argument is neither a Switch nor an Option
                    if(this.positionalsPosition != ArgumentPosition.BACK) {
                        System.out.println("Argument is neither a switch nor an Option.");
                        return false;
                    }
                }
            } else if (this.positionalsPosition == ArgumentPosition.FRONT){
                if(!foundArgument) {
                    positionals.add(currentArg);
                }
            } else {
                // Argument is neither a Positional nor a Switch nor an Option
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the Usage String for the Program.
     * The Usage String is a composition of all the arguments and it's options
     *
     *
     * @return The usage String
     */
    public String getUsage() {
        StringBuilder builder = new StringBuilder();
        for(int index = 0; index < this.usageList.size(); index++) {
            builder.append(this.usageList.get(index));
            builder.append("\n");
        }
        return builder.toString();
    }

    // ------------------------ Static Methods --------------------------------

    /**
     * Runs all the Test cases for this file
     */
    public static void testAll() {
        System.out.println("Running Tests for Argumentparser");

        testParser();

        System.out.println("----------------------");
        System.out.println("All tests done.");
    }

    // ------------------------ Private Methods ------------------------------

    private void addToFormTranslator(String shortForm, String longForm) {
        this.formTranslator.put(shortForm, longForm);
        this.formTranslator.put(longForm, shortForm);
    }

    private void addToUsage(String part) {
        this.usageList.add(part);
    }

    private static void testParser() {
        // chris -o customOutfile.c --logfile mylog.txt --debug -v file1.chris file2.chris
        String[] args = {"-o", "customOutfile.c", "--logfile", "mylog.txt", "--debug", "-v", "file1.chris", "file2.chris"};

        ArgumentParser parser = new ArgumentParser("Tester for almost all features. Including positionals at back");

        parser.searchPositionalsAt(ArgumentPosition.BACK);

        parser.addOption("-i", "--infile", "FILE", "The file to parse");
        parser.addOption("-l", "--logfile", "LOGFILE", "The log file");
        parser.addOption("-o", "--output", "FILE", "The output file");

        parser.addSwitch("-d", "--debug", "Enable Debug mode.");
        parser.addSwitch("-v", "--verbose", "Enable verbose output");
        parser.addSwitch("-u", "--useless", "Useless switch that shall not be switched on");

        parser.parse(args);


        ArrayList<String> positionals = parser.getPositionals();
        assert positionals.size() == 2;
        assert positionals.get(0).equals("file1.chris");
        assert positionals.get(1).equals("file2.chris");

        HashMap<String, String> options = parser.getOptions();
        assert options.size() == 3; // infile, logfile and output
        assert options.get("-o").equals(options.get("--output"));
        assert options.get("-l").equals(options.get("--logfile"));
        assert options.get("-i").equals(options.get("--infile"));

        assert options.get("--output").equals("customOutfile.c");
        assert options.get("--logfile").equals("mylog.txt");
        assert options.get("--infile") == null;

        assert options.get("--outfile") == null;
        assert options.get("asf") == null;
        assert options.get(null) == null;


        HashMap<String, Boolean> switches = parser.getSwitches();
        assert switches.size() == 2; // debug and verbose
        assert switches.get("-d").equals(switches.get("--debug"));
        assert switches.get("-v").equals(switches.get("--verbose"));
        assert switches.get("-u").equals(switches.get("--useless"));

        assert switches.get("--debug") == true;
        assert switches.get("--verbose") == true;
        assert switches.get("--useless") == false;

        assert switches.get("--usebose") == false;
        assert switches.get("asdf") == false;
        assert switches.get(null) == false;

        System.out.println("Result of parser.getUsage():");
        System.out.println(parser.getUsage());

        String[] argsFront = {"file1.chris", "file2.chris"};
        parser = new ArgumentParser("Tester for positionals at front");
        parser.searchPositionalsAt(ArgumentPosition.FRONT);

        assert parser.parse(argsFront);

        positionals = parser.getPositionals();
        assert positionals.size() == 2;
        assert positionals.get(0).equals("file1.chris");
        assert positionals.get(1).equals("file2.chris");

        parser = new ArgumentParser("Parser that is expected to fail");
        assert !parser.parse(argsFront);
    }

}
