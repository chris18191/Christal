## Example run (compiled language)
1. Lex
2. Parse
3. Semantic Actions/Analysis
4. Frame Layout
5. Translate
6. Code Emission

## Structure of a Compiler
1. Analysis
    1. Lexical Analysis:
        - Read the source program and group them into *lexemes*.
        - Produce a token for each of the lexemes in the form of `(token-name,
        attribute-value)`, where `token-name` is an abstract symbol that is used
        during syntax-analysis and `attribute-value` points to an entry in the
        symbol table for this token
        - Pass the tokens into the syntax analysis


## Ideas
- Brainf*ck-like
- _Compiled_ instead of interpreted
- 8 Commands (custom syntax)
- Turing like
- Code output: file.chris -> compiler -> file.c -> gcc -> file.o

## Example syntax:
- A
- D
- I
- {
