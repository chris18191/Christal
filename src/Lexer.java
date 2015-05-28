package src;

import java.util.ArrayList;
import java.util.regex.Pattern;


/**
 * Lexer reads in a String, creates tokens out of it and gives them some context
 * 
 */

public class Lexer {
    public static enum TokenType{
        //defines which Token types we have
        //example:
        //NUMBER("-?[0-9]+"), BINARYOP("[*|/|+|-]"), WHITESPACE("[\t|\f|\r|\n]+");
        //TODO: implement our Token types

	LEFT("(L|l)"),
	RIGHT("(R|r)"),
	INPUT("(I|i)"),
	OUTPUT("(O|o)"),
	INCREASE("(\\+|a|A)"),
	DECREASE("(\\-|d|D)"),
	LOOPSTART("(\\{|\\()"),
	LOOPSTOP("(\\}|\\))");
	
	
        public final String pattern;

        private TokenType(String pattern){
            this.pattern = pattern;
        }
    }

    public static class Token{
        public TokenType type;

        public Token(TokenType type){
            this.type = type;
        }
        
        @Override
        public String toString(){
            return type.name();
        }

	@Override
	public boolean equals(Object other) {
	    if(other == null) {
		return false;
	    }
	    if(other == this) {
		return true;
	    }
	    Token otherToken = (Token)other;
	    return otherToken.type == this.type;
	}
    }

    public static void testAll() {
	System.out.println("Starting tests in Lexer.java");
	testCreateTokenFrom();

	System.out.println("------\nDone.");

    }

    private static void testCreateTokenFrom() {
	System.out.println("Starting to test createTokenFrom");
	assert createTokenFrom("+").type == TokenType.INCREASE;
	assert createTokenFrom("a").type == TokenType.INCREASE;
	assert createTokenFrom("A").type == TokenType.INCREASE;
	assert createTokenFrom("-").type == TokenType.DECREASE;
	assert createTokenFrom("d").type == TokenType.DECREASE;
	assert createTokenFrom("D").type == TokenType.DECREASE;
	assert createTokenFrom("l").type == TokenType.LEFT;
	assert createTokenFrom("L").type == TokenType.LEFT;
	assert createTokenFrom("r").type == TokenType.RIGHT;
	assert createTokenFrom("R").type == TokenType.RIGHT;
	assert createTokenFrom("(").type == TokenType.LOOPSTART;
	assert createTokenFrom("{").type == TokenType.LOOPSTART;
	assert createTokenFrom(")").type == TokenType.LOOPSTOP;
	assert createTokenFrom("}").type == TokenType.LOOPSTOP;
	assert createTokenFrom("i").type == TokenType.INPUT;
	assert createTokenFrom("I").type == TokenType.INPUT;
	assert createTokenFrom("o").type == TokenType.OUTPUT;
	assert createTokenFrom("O").type == TokenType.OUTPUT;

	assert createTokenFrom(null).type == null;
	assert createTokenFrom("asdf").type == null;
	assert createTokenFrom("").type == null;
	
	System.out.println("Done.");
    }
    
    public static Token createTokenFrom(String command) {
	if(command == null) {
	    return null;
	}
        for (TokenType tokenType : TokenType.values()) {
	    String pattern = tokenType.pattern;
	    if(Pattern.matches(pattern, command)) {
		return new Token(tokenType);
	    }
	}
	return null;
    }

    public static ArrayList<Token> lex(ArrayList<String> input){
	ArrayList<Token> tokens = new ArrayList<Token>();
	for(String command : input) {
	    Token token = Lexer.createTokenFrom(command);
	    if(token == null) {
		System.out.println("Unkown command " + input);
		System.exit(-1);
	    }
	    tokens.add(token);
	}
	return tokens;
    }
}
