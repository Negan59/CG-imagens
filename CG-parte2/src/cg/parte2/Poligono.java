
package cg.parte2;

import java.util.ArrayList;

public class Poligono {
    private ArrayList <Pontos> original;
    private ArrayList <Pontos> atual;
    private double matrizTransformacao[][];

    public ArrayList<Pontos> getOriginal() {
        return original;
    }

    public void setOriginal(ArrayList<Pontos> original) {
        this.original = original;
    }

    public ArrayList<Pontos> getAtual() {
        return atual;
    }

    public void setAtual(ArrayList<Pontos> atual) {
        this.atual = atual;
    }

    public double[][] getMatrizTransformacao() {
        return matrizTransformacao;
    }

    public void setMatrizTransformacao(double[][] matrizTransformacao) {
        this.matrizTransformacao = matrizTransformacao;
    }

    public Poligono() {
        this.original = new ArrayList();
        this.atual = new ArrayList();
        this.matrizTransformacao = new double[3][3];
        this.matrizTransformacao[0][0] = 1;
        this.matrizTransformacao[0][1] = 0;
        this.matrizTransformacao[0][2] = 0;
        this.matrizTransformacao[1][0] = 0;
        this.matrizTransformacao[1][1] = 1;
        this.matrizTransformacao[1][2] = 0;
        this.matrizTransformacao[2][0] = 0;
        this.matrizTransformacao[2][1] = 0;
        this.matrizTransformacao[2][2] = 1;
    }
}
