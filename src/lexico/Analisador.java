package lexico;

import Erro.Erro;
import compilador2.pkg0.Compilador20;
import exceptions.ExcecoesLexicas;
import java.util.ArrayList;

public class Analisador {

    private char[] content;
    private int estado;
    private int pos;
    private int line;
    private int column;
    private int ant;
    public static ArrayList<Erro>erros  =new ArrayList();

    public Analisador(String programa) {
        content = programa.toCharArray();
        pos = 0;
        line = 1;
        ant = line;
        column = 0;
    }

    public Token nextToken() {
        System.out.println("line - " + line);
        char currentChar = 's';
        Token token;
        String term = "";
        estado = 0;
        while (!isEOF() || estado != 0) {

            currentChar = nextChar();
            System.out.println("\natual = " + currentChar);
            ant = line;
            column++;

            switch (estado) {
                case 0:
                    if (isChar(currentChar)) {
                        term += currentChar;
                        estado = 1;
                    } else if (isDigit(currentChar)) {
                        estado = 2;
                        term += currentChar;
                    } else if (isSpace(currentChar)) {
                        estado = 0;
                    } else if (isAssign(currentChar)) {
                        estado = 3;
                    } else if (isMaior(currentChar)) {
                        estado = 4;
                    } else if (isMenor(currentChar)) {
                        estado = 5;
                    } else if (isOperator(currentChar)) {
                        term += currentChar;
                        token = new Token();
                        token.setType(Token.TK_OPERATORA);
                        token.setText(term);
                        token.setLine(ant);
                        token.setColumn(column - term.length());
                        return token;
                    } else {
                        erros.add(new Erro("Simbolo não reconhecido",line,column));
                        
                        
                    }
                    break;
                case 1:
                    if (isChar(currentChar) || isDigit(currentChar)) {
                        estado = 1;
                        term += currentChar;
                    } else if (isSpace(currentChar) || isOperator(currentChar) || isEOF(currentChar)) {
                        
                        token = new Token();
                        if(term.equals("if")){
                            token.setType(Token.TK_CONDITIONAL);
                        }else if(term.equals("then")){
                            token.setType(Token.TK_CONDITIONAL2);
                        }else if(term.equals("else")){
                            token.setType(Token.TK_CONDITIONAL3); 
                        }else if(term.equals("while")){
                            token.setType(Token.TK_LOOP); 
                        }else if(term.equals("do")){
                            token.setType(Token.TK_LOOP2);
                        }
                        else{
                            token.setType(Token.TK_IDENTIFIER); 
                        }
                        
                        token.setText(term);
                        token.setLine(ant);
                        token.setColumn(column - term.length());
                        return token;
                    } else {
                        erros.add(new Erro("Identificador no formato incorreto",line,column));
                    }
                    break;
                case 2:
                    if (isDigit(currentChar) || currentChar == '.') {
                        estado = 2;
                        term += currentChar;
                    } else if (isSpace(currentChar) || !isChar(currentChar) || isEOF(currentChar)) {

                        token = new Token();
                        token.setType(Token.TK_NUMBER);
                        token.setText(term);
                        token.setLine(ant);
                        token.setColumn(column - term.length());
                        return token;
                    } else {
                        erros.add(new Erro("Número não reconhecido",line,column));
                    }
                    break;
                case 3:
                    if (isSpace(currentChar) || isDigit(currentChar) || isChar(currentChar)) {
                        String aux = "=";
                        token = new Token();
                        token.setType(Token.TK_ASSIGN);
                        token.setText(aux);
                        token.setLine(ant);
                        token.setColumn(column - term.length());
                        return token;
                    } else if (currentChar == '=') {
                        String aux = "==";
                        token = new Token();
                        token.setType(Token.TK_OPERATORR);
                        token.setText(aux);
                        token.setLine(ant);
                        token.setColumn(column - term.length());
                        return token;
                    } else {
                        erros.add(new Erro("Operador não reconhecido",line,column));
                    }
                //break;
                case 4:
                    if (isSpace(currentChar) || isDigit(currentChar) || isChar(currentChar)) {
                        String aux = ">";
                        token = new Token();
                        token.setType(Token.TK_OPERATORR);
                        token.setText(aux);
                        token.setLine(ant);
                        token.setColumn(column - term.length());
                        return token;
                    } else if (currentChar == '=') {
                        String aux = ">=";
                        token = new Token();
                        token.setType(Token.TK_OPERATORR);
                        token.setText(aux);
                        token.setLine(ant);
                        token.setColumn(column - term.length());
                        return token;
                    } else {
                        erros.add(new Erro("Operador não reconhecido",line,column));
                    }
                case 5:
                    if (isSpace(currentChar) || isDigit(currentChar) || isChar(currentChar)) {
                        String aux = "<";
                        token = new Token();
                        token.setType(Token.TK_OPERATORR);
                        token.setText(aux);
                        token.setLine(ant);
                        token.setColumn(column - term.length());
                        return token;
                    } else if (currentChar == '=') {
                        String aux = "<=";
                        token = new Token();
                        token.setType(Token.TK_OPERATORR);
                        token.setText(aux);
                        token.setLine(ant);
                        token.setColumn(column - term.length());
                        return token;
                    }else if(isMaior(currentChar)){
                        String aux = "<>";
                        token = new Token();
                        token.setType(Token.TK_OPERATORR);
                        token.setText(aux);
                        token.setLine(ant);
                        token.setColumn(column - term.length());
                        return token;
                    } 
                    else {
                        erros.add(new Erro("Operador não reconhecido",line,column));
                    }
            }
        }
        if(erros.size()>0){
            throw new ExcecoesLexicas("Erro encontrado");
        }
        return null;
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isChar(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private boolean isMaior(char c) {
        return c == '>';
    }

    private boolean isMenor(char c) {
        return c == '<';
    }

    private boolean isAssign(char c) {
        return c == '=';
    }

    private boolean isSpace(char c) {
        if (c == '\n' || c == '\r') {
            ant = line;
            line++;
            column = 0;
        }
        return c == ' ' || c == '\t' || c == '\n' || c == '\r';
    }

    private char nextChar() {
        if (isEOF()) {
            return '\0';
        }
        return content[pos++];
    }

    public boolean isEOF() {
        return pos >= content.length;
    }

    private boolean isEOF(char c) {
        return c == '\0';
    }
}
