package cg.parte2;

public class ET {

    private AET[] et;
    private int TF, qtde;

    public ET(int tf) {
        TF = tf;
        qtde = 0;
        et = new AET[TF];
        for (int i = 0; i < TF; i++) {
            et[i] = new AET();
        }
    }
    public boolean pega(int pos){
        if(!et[pos].getList().isEmpty()){
            return false;
        }
        else{
            return true;
        }
    }
    public AET getAET(int pos) {
        if (pos >= TF) {
            boolean v = 1 == 1;
        } else {
            return et[pos];
        }
        return null;
    }

    public int getTF() {
        return TF;
    }

    public int getQtde() {
        return qtde;
    }

    public void init() {
        qtde++;
    }

}
