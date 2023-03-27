package cg.parte2;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class AET {
    private List<No> lista;

    public AET() {
        lista = new ArrayList<No>();
    }

    public void add(No info) {
        lista.add(info);
    }

    public List<No> getList() {
        return lista;
    }

    public void add(List<No> L) {
        for (No n : L) {
            lista.add(n);
        }
    }

    public void sort() {
        if (lista.size() > 1) {
            Stack<Integer> pilha = new Stack<>();
            int inicio = 0, fim = lista.size() - 1, i, j, meio;
            No aux;
            double pivoX;
            pilha.push(inicio);
            pilha.push(fim);
            while (!pilha.isEmpty()) {
                fim = pilha.pop();
                inicio = pilha.pop();
                i = inicio;
                j = fim;
                meio = (i + j) / 2;
                pivoX = lista.get(meio).getXmin();
                while (i < j) {
                    while (lista.get(i).getXmin() < pivoX) {
                        ++i;
                    }
                    while (lista.get(j).getXmin() > pivoX) {
                        --j;
                    }
                    if (i <= j) {
                        aux = lista.get(i);
                        lista.set(i, lista.get(j));
                        lista.set(j, aux);
                        ++i;
                        --j;
                    }
                }
                if (inicio < j) {
                    pilha.push(inicio);
                    pilha.push(j);
                }
                if (i < fim) {
                    pilha.push(i);
                    pilha.push(fim);
                }
            }
        }
    } // fim sort
}