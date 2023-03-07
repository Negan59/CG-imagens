package Erro;

public class Erro {
    private String msg;
    private int linha;
    private int coluna;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Erro(String msg, int linha, int coluna) {
        this.msg = msg;
        this.linha = linha;
        this.coluna = coluna;
    }

    public Erro() {
    }

    public int getLinha() {
        return linha;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }

    public int getColuna() {
        return coluna;
    }

    public void setColuna(int coluna) {
        this.coluna = coluna;
    }

    
    
}
