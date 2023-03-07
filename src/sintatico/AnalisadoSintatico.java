package sintatico;

import exceptions.ExcecoesSintaticas;
import lexico.Analisador;
import lexico.Token;

public class AnalisadoSintatico {

    private Analisador scanner;
    private Token token;

    public AnalisadoSintatico(Analisador scanner) {
        this.scanner = scanner;
        token = scanner.nextToken();
    }

    public void P() {
        if (!scanner.isEOF()) {
            if (token.getType() == Token.TK_CONDITIONAL) {
                C();
            } else if (token.getType() == Token.TK_LOOP) {
                R();
            } else {
                D();
            }
        }else{
            
        }

    }

    public void D() {
        if (token.getType() == Token.TK_IDENTIFIER) {
            token = scanner.nextToken();
            A();
            P();

        } else if (token.getType() == Token.TK_NUMBER || token.getType() == Token.TK_OPERATORA || token.getType() == Token.TK_OPERATORR) {
            E();
            P();

        }
        else{
            System.out.println("token inválido");
        }
    }

    public void A() {
        if (token.getType() == Token.TK_ASSIGN) {
            token = scanner.nextToken();
            E();
        } else {
            throw new ExcecoesSintaticas(Token.TK_TEXT[Token.TK_ASSIGN] + " era esperado, foi encontrado o token " + token.toString());
        }

    }

    public void E() {
        System.out.println("token : " + token.toString());
        if (token.getType() == Token.TK_IDENTIFIER && !scanner.isEOF()) {
            token = scanner.nextToken();

        } else if (token.getType() == Token.TK_NUMBER && !scanner.isEOF()) {
            token = scanner.nextToken();
            //P();

        } else if ((token.getType() == Token.TK_OPERATORA || token.getType() == Token.TK_OPERATORR) && !scanner.isEOF()) {
            token = scanner.nextToken();
            //E();

        } /*else if(token.getType() == Token.TK_ASSIGN && !scanner.isEOF()){
            throw new ExcecoesSintaticas(Token.TK_TEXT[Token.TK_NUMBER] + " era esperado, foi encontrado o token " + token.toString());
        }*/ else {
            return;
        }

    }

    public void C() {
        token = scanner.nextToken();
        E();
        System.out.println("token atual : " + token.toString());
        if (token.getType() == Token.TK_CONDITIONAL2) {
            token = scanner.nextToken();
            P();

            if (token.getType() == Token.TK_CONDITIONAL3 && token != null) {
                token = scanner.nextToken();
                P();

            } else {
                throw new ExcecoesSintaticas(Token.TK_TEXT[Token.TK_CONDITIONAL3] + " era esperado, foi encontrado o token " + token.toString());
            }
        } else {
            throw new ExcecoesSintaticas(Token.TK_TEXT[Token.TK_CONDITIONAL2] + " era esperado, foi encontrado o token " + token.toString());
        }

    }

    public void R() {
        token = scanner.nextToken();
        E();
        if (token.getType() == Token.TK_LOOP2) {
            token = scanner.nextToken();
            P();

        } else {
            throw new ExcecoesSintaticas(Token.TK_TEXT[Token.TK_LOOP2] + " era esperado, foi encontrado o token " + token.toString());
        }
    }

}
