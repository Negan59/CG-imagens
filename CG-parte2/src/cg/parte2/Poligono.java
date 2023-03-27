package cg.parte2;

import java.util.ArrayList;
import java.util.List;

public class Poligono {

    private ArrayList<Pontos> original;
    private ArrayList<Pontos> atual;
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

    public ET gerarET(int height) {
        ET et = new ET(height);
        List<Pontos> pontos = atual;

        double xmax, ymax, xmin, ymin, incx, dx, dy;
        int y;
        for (int i = 0; i + 1 < pontos.size(); ++i) { // do primeiro ponto até o ultimo
            if (pontos.get(i).getX() >= pontos.get(i + 1).getY()) {
                xmax = pontos.get(i).getX();
                ymax = pontos.get(i).getY();
                xmin = pontos.get(i + 1).getX();
                ymin = pontos.get(i + 1).getY();
            } else {
                xmin = pontos.get(i).getX();
                ymin = pontos.get(i).getY();
                xmax = pontos.get(i + 1).getX();
                ymax = pontos.get(i + 1).getY();
            }
            dx = xmax - xmin;
            dy = ymax - ymin;
            incx = dx / dy;
            y = (int) ymin;
            if (y < 0) {
                y = 0;
            } else if (y >= height) {
                y = height - 1;
            }
            if (et.getAET(y).getList().isEmpty()) {
                et.init();
            }
            et.getAET(y).add(new No(ymax, xmin, incx));
        } // fim for

        // ultimo com o primeiro
        if (pontos.get(0).getY() >= pontos.get(pontos.size() - 1).getY()) {
            xmax = pontos.get(0).getX();
            ymax = pontos.get(0).getY();
            xmin = pontos.get(pontos.size() - 1).getX();
            ymin = pontos.get(pontos.size() - 1).getY();
        } else {
            xmin = pontos.get(0).getX();
            ymin = pontos.get(0).getY();
            xmax = pontos.get(pontos.size() - 1).getX();
            ymax = pontos.get(pontos.size() - 1).getY();
        }
        dx = xmax - xmin;
        dy = ymax - ymin;
        incx = dx / dy;
        y = (int) ymin;
        if (y < 0) {
            y = 0;
        } else if (y >= height) {
            y = height - 1;
        }
        if (et.getAET(y).getList().isEmpty()) {
            et.init();
        }
        et.getAET(y).add(new No(ymax, xmin, incx));
        return et;
    }
}
