package src.lib;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Lexer reads in a String, creates tokens out of it and gives them some context
 * 
 */

public class Lexer{
    public enum TokenType{
        ;
        /*
        defines which Token types we have
        example:
        NUMBER("-?[0-9]+"), BINARYOP("[*|/|+|-]"), WHITESPACE("[\t|\f|\r|\n]+");
        TODO: implement our Token types
        */

        public final String pattern;


        TokenType(String pattern){
            this.pattern = pattern;
        }
    }

    public static class Token{
        public TokenType type;
        public String data;

        public Token(TokenType type, String data){
            this.type = type;
            this.data = data;
        }
        
        @Override
        public String toString(){
            return String.format("%s %s", type.name(), data);
        } 
    }

    

    public static ArrayList<Token> lex(String input){
        // The tokens we'll return
        ArrayList<Token> tokens = new ArrayList<>();

        StringBuilder tokenPatternBuilder = new StringBuilder();
        for (TokenType tokenType : TokenType.values()) {
            tokenPatternBuilder.append(String.format("|(?<%s>%s)", tokenType.name(), tokenType.pattern));
        }
        Pattern tokenPatterns = Pattern.compile(tokenPatternBuilder.substring(1));


        //Token matching
        Matcher matcher = tokenPatterns.matcher(input);
        while(matcher.find()){
            for(TokenType tk : TokenType.values()){
            /*
            TODO: implement logic according to the used language
            example:
                if(matcher.group(TokenType.WHITESPACE)!= null)
                continue;

            else if(matcher.group(tk.name()) != null){
            tokens.add(new Token(tk, matcher.group(tk.name())));
            break;
            }
            */


            }
        }

        return tokens;
    }

}
