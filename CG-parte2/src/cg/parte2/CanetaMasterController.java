package cg.parte2;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Stack;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class CanetaMasterController implements Initializable {

    @FXML
    private ToggleGroup exp;
    @FXML
    private RadioButton retaReal;
    @FXML
    private RadioButton dda;
    @FXML
    private RadioButton retaMedio;
    @FXML
    private RadioButton cirReal;
    @FXML
    private RadioButton cirTrig;
    @FXML
    private RadioButton cirMedio;
    @FXML
    private RadioButton elipse;

    private int corR = 0;
    private int corG = 0;
    private int corB = 0;

    private int numObj;

    private double x1, x2, y1, y2;
    @FXML
    private ImageView imgView;

    private Image image;
    @FXML
    private ContextMenu ctxMenu;

    private boolean poligono = false;

    private ArrayList<Poligono> poligonos = new ArrayList();

    private Poligono p;
    @FXML
    private VBox vboxEsquerda;

    private boolean translada = false;
    @FXML
    private TextField txRoda;
    @FXML
    private Button btnEsquerda;
    @FXML
    private Button btnDireita;

    private boolean floodfill = false;
    @FXML
    private TextField txEscala;
    @FXML
    private Button btnEscala;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        int altura = 1200;
        int largura = 768;

        BufferedImage imagem = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_ARGB);
        int cor = 0xFFFFFFFF; // branco
        for (int x = 0; x < largura; x++) {
            for (int y = 0; y < altura; y++) {
                imagem.setRGB(x, y, cor);
            }
        }
        image = SwingFXUtils.toFXImage(imagem, null);
        imgView.setImage(image);

    }

    @FXML
    private void evtPegaXYFinal(MouseEvent event) {
        x2 = event.getX();
        y2 = event.getY();

        if (retaReal.isSelected()) {
            this.desenhaRetaReal();
        } else if (dda.isSelected()) {
            this.dda();
        } else if (retaMedio.isSelected()) {
            this.retaMedio();
        } else if (cirReal.isSelected()) {
            this.cirReal();
        } else if (cirTrig.isSelected()) {
            this.cirTrig();
        } else if (cirMedio.isSelected()) {
            this.cirMedio();
        } else if (elipse.isSelected()) {
            this.elipse();
        }
    }

    @FXML
    private void evtPegaXY(MouseEvent event) {
        x1 = event.getX();
        y1 = event.getY();
        if (this.floodfill) {
            floodfill = false;
            this.floodfill(x1, y1);
        } else if (translada) {
            this.translacao(x1, y1);
            this.translada = false;
        } else if (poligono) {
            if (event.getClickCount() == 1) {
                p.getOriginal().add(new Pontos(x1, y1));
            }

            int i;
            if (event.getClickCount() == 2) {
                Button b = new Button();
                TableView<Pontos> tabela = new TableView<>();
                TableColumn<Pontos, Double> colunaNome = new TableColumn<>("X");
                colunaNome.setCellValueFactory(new PropertyValueFactory<>("x"));

                TableColumn<Pontos, Integer> colunaIdade = new TableColumn<>("Y");
                colunaIdade.setCellValueFactory(new PropertyValueFactory<>("y"));

                tabela.getColumns().add(colunaNome);
                tabela.getColumns().add(colunaIdade);

                tabela.setItems(FXCollections.observableArrayList(p.getOriginal()));
                addClickListener(tabela, vboxEsquerda.getChildren().size() - 2);
                if (p.getOriginal().size() > 2) {
                    for (i = 0; i < p.getOriginal().size() - 1; i++) {
                        x1 = p.getOriginal().get(i).getX();
                        y1 = p.getOriginal().get(i).getY();
                        x2 = p.getOriginal().get(i + 1).getX();
                        y2 = p.getOriginal().get(i + 1).getY();
                        this.retaMedio();
                    }
                    x1 = p.getOriginal().get(0).getX();
                    y1 = p.getOriginal().get(0).getY();
                    x2 = p.getOriginal().get(i).getX();
                    y2 = p.getOriginal().get(i).getY();
                    this.retaMedio();
                }
                poligono = false;
                vboxEsquerda.getChildren().add(tabela);
                p = salvaAtual(p);
                poligonos.add(p);
            }
        }

    }

    private void addClickListener(TableView<Pontos> tabela, int indiceTabela) {
        tabela.setOnMouseClicked(event -> {
            numObj = indiceTabela;
            System.out.println("Tabela " + indiceTabela + " clicada");
            this.pintarNovamente(numObj);
        });
    }

    private void desenhaRetaReal() {
        BufferedImage bimage = null;
        bimage = SwingFXUtils.fromFXImage(image, null);
        int pixel[] = {0, 0, 0, 0};
        WritableRaster raster = bimage.getRaster();
        double dy = y2 - y1;
        double dx = x2 - x1;

        if (dx < 0) { // se dx é negativo, inverte a ordem dos pontos
            double tmp = x1;
            x1 = x2;
            x2 = tmp;
            tmp = y1;
            y1 = y2;
            y2 = tmp;
            dx = -dx;
            dy = -dy;
        }

        double m = dy / dx;
        double y;
        double x;

        if (dx > dy) {
            if (x2 > x1) {
                //primeiro e oitavo octantes
                for (x = x1; x <= x2; x++) {
                    y = y1 + m * (x - x1);
                    raster.getPixel((int) x, round(y), pixel);
                    pixel[0] = corR;
                    pixel[1] = corG;
                    pixel[2] = corB;
                    raster.setPixel((int) x, round(y), pixel);
                }
            } else {
                for (x = x2; x >= x1; x--) {
                    y = y1 + m * (x - x1);
                    raster.getPixel((int) x, round(y), pixel);
                    pixel[0] = corR;
                    pixel[1] = corG;
                    pixel[2] = corB;
                    raster.setPixel((int) x, round(y), pixel);
                }
            }
        } else {
            if (y2 > y1) {

                for (y = y1; y <= y2; y++) {
                    x = x1 + (y - y1) / m;
                    raster.getPixel((int) x, round(y), pixel);
                    pixel[0] = corR;
                    pixel[1] = corG;
                    pixel[2] = corB;
                    raster.setPixel((int) x, round(y), pixel);
                }
            } else {
                for (y = y2; y <= y1; y++) {
                    x = x1 + (y - y1) / m;
                    raster.getPixel((int) x, round(y), pixel);
                    pixel[0] = corR;
                    pixel[1] = corG;
                    pixel[2] = corB;
                    raster.setPixel((int) x, round(y), pixel);
                }
            }

        }

        image = SwingFXUtils.toFXImage(bimage, null);
        imgView.setImage(image);

    }

    private int round(double num) {
        double parteNaoInteira = num % 1.0;
        int numero = (int) num;
        if (parteNaoInteira > 0.5) {
            numero++;
        }
        return numero;
    }

    private void dda() {
        BufferedImage bimage = null;
        bimage = SwingFXUtils.fromFXImage(image, null);
        int pixel[] = {0, 0, 0, 0};
        WritableRaster raster = bimage.getRaster();
        double dy = y2 - y1;
        double dx = x2 - x1;

        double m = dy / dx;
        double x;
        double y;
        double xInc;
        double yInc;

        double tamanho;

        tamanho = Math.abs(x2 - x1);
        //aqui é pra saber se o tamanho vai ser definido pelo eixo x, ou pelo eixo y
        if (Math.abs(y2 - y1) > tamanho) {
            tamanho = Math.abs(y2 - y1);
        }
        //a inversão de pontos é necessária para que todos os octantes sejam atingidos, pois o x inicial não pode ser maior que o final
        if (x1 > x2) {
            double temp = x1;
            x1 = x2;
            x2 = temp;
            temp = y1;
            y1 = y2;
            y2 = temp;
        }

        xInc = (x2 - x1) / tamanho;
        yInc = (y2 - y1) / tamanho;
        y = y1;
        for (x = x1; x <= x2; x += xInc) {
            raster.getPixel((int) x, round(y), pixel);
            pixel[0] = corR;
            pixel[1] = corG;
            pixel[2] = corB;
            raster.setPixel((int) x, round(y), pixel);
            y += yInc;
        }

        image = SwingFXUtils.toFXImage(bimage, null);
        imgView.setImage(image);
    }

    private void retaMedio() {
        BufferedImage bimage = null;
        bimage = SwingFXUtils.fromFXImage(image, null);
        int pixel[] = {0, 0, 0, 0};
        WritableRaster raster = bimage.getRaster();

        double dy = y2 - y1;
        double dx = x2 - x1;
        double m = dy / dx;
        double x = x1;
        double y = y1;
        double incE;
        double incNE;
        double d;

        int ic, iy;

        raster.getPixel((int) x, (int) y, pixel);
        pixel[0] = corR;
        pixel[1] = corG;
        pixel[2] = corB;
        raster.setPixel((int) x, (int) y, pixel);

        //constante de bresenhan
        x = x1;
        y = y1;

        //seta delta x e o valor de incremento
        if (dx < 0) {
            dx = -dx;
            ic = -1;
        } else {
            ic = 1;
        }
        //seta delta y e o valor de incremento
        if (dy < 0) {
            dy = -dy;
            iy = -1;
        } else {
            iy = 1;
        }

        //depois de manipular os deltas
        if (dy < dx) {
            incE = 2 * dy;
            incNE = 2 * dy - 2 * dx;

            d = 2 * dy - dx;
            for (double i = 0; i <= dx; i++) {
                x = x + ic;
                if (d < 0) {
                    d += incE;
                } else {
                    d += incNE;
                    y += iy;
                }
                raster.getPixel((int) x, (int) y, pixel);
                pixel[0] = corR;
                pixel[1] = corG;
                pixel[2] = corB;
                raster.setPixel((int) x, (int) y, pixel);
            }

        } else {
            //aqui eu troco a constante para inverter os valores sem precisar chamar a recursão, o resultado é o mesmo
            incE = 2 * dx;
            incNE = 2 * dx - 2 * dy;

            d = 2 * dx - dy;

            for (double i = 0; i <= dy; i++) {
                y = y + iy;
                if (d < 0) {
                    d += incE;
                } else {
                    d += incNE;
                    x += ic;
                }
                raster.getPixel((int) x, (int) y, pixel);
                pixel[0] = corR;
                pixel[1] = corG;
                pixel[2] = corB;
                raster.setPixel((int) x, (int) y, pixel);
            }
        }

        image = SwingFXUtils.toFXImage(bimage, null);
        imgView.setImage(image);

    }

    private void cirMedio() {
        double raio = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        double x = 0;
        double y = raio;
        double d = 1 - raio;
        this.imprimeSimetrico((int) x, (int) y);
        while (y > x) {
            if (d < 0) {
                d += 2 * x + 3;
            } else {
                d += 2 * (x - y) + 5;
                y--;
            }
            x++;
            this.imprimeSimetrico((int) x, (int) y);
        }
    }

    private void cirReal() {
        double raio = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        double x = 0;
        double y = raio;
        double d = 3 - 2 * raio;
        while (x <= y) {
            // imprimir pontos simétricos
            imprimeSimetrico((int) x, (int) y);

            // atualizar valores de x, y e d
            if (d < 0) {
                d = d + 4 * x + 6;
            } else {
                d = d + 4 * (x - y) + 10;
                y--;
            }
            x++;
        }
    }

    private void cirTrig() {
        double deltaTheta = 0;

        double raio = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        double inc = 360.0 / (2 * Math.PI * raio);
        System.out.println("inc - " + inc);
        //é 45, porque um octante é espelhado nos outos
        for (double i = 0; i <= 45; i += inc) {
            deltaTheta = Math.PI * i / 180.0;
            double x = raio * Math.cos(deltaTheta);
            double y = raio * Math.sin(deltaTheta);
            this.imprimeSimetrico((int) x, (int) y);
        }
    }

    private void elipse() {
        double a = (x2 - x1) / 2;
        double b = (y2 - y1) / 2;
        double xc = (x1 + x2) / 2;
        double yc = (y1 + y2) / 2;

        double x = 0;
        double y = b;
        double d = b * b - a * a * b + a * a / 4;
        //1 lado
        while (b * b * (x + 1) < a * a * (y - 0.5)) {
            imprimeElipse(xc + x, yc + y);
            imprimeElipse(xc - x, yc + y);
            imprimeElipse(xc + x, yc - y);
            imprimeElipse(xc - x, yc - y);

            x++;
            if (d < 0) {
                d = d + b * b * (2 * x + 3);
            } else {
                y--;
                d = d + b * b * (2 * x + 3) + a * a * (-2 * y + 2);
            }
        }
        //2 lado
        d = b * b * (x + 0.5) * (x + 0.5) + a * a * (y - 1) * (y - 1) - a * a * b * b;
        while (y > 0) {
            imprimeElipse(xc + x, yc + y);
            imprimeElipse(xc - x, yc + y);
            imprimeElipse(xc + x, yc - y);
            imprimeElipse(xc - x, yc - y);

            y--;
            if (d < 0) {
                x++;
                d = d + b * b * (2 * x + 2) + a * a * (-2 * y + 3);
            } else {
                d = d + a * a * (-2 * y + 3);
            }
        }

    }

    private void imprimeSimetrico(int x, int y) {

        BufferedImage bimage = null;
        bimage = SwingFXUtils.fromFXImage(image, null);
        int pixel[] = {0, 0, 0, 0};
        WritableRaster raster = bimage.getRaster();
        //1
        raster.getPixel((int) (x1 + x), (int) (y1 + y), pixel);
        pixel[0] = corR;
        pixel[1] = corG;
        pixel[2] = corB;
        raster.setPixel((int) (x1 + x), (int) (y1 + y), pixel);

        //2
        raster.getPixel((int) (x1 + x), (int) (y1 - y), pixel);
        pixel[0] = corR;
        pixel[1] = corG;
        pixel[2] = corB;
        raster.setPixel((int) (x1 + x), (int) (y1 - y), pixel);

        //3
        raster.getPixel((int) (x1 - x), (int) (y1 + y), pixel);
        pixel[0] = corR;
        pixel[1] = corG;
        pixel[2] = corB;
        raster.setPixel((int) (x1 - x), (int) (y1 + y), pixel);

        //4
        raster.getPixel((int) (x1 - x), (int) (y1 - y), pixel);
        pixel[0] = corR;
        pixel[1] = corG;
        pixel[2] = corB;
        raster.setPixel((int) (x1 - x), (int) (y1 - y), pixel);

        //5
        raster.getPixel((int) (x1 + y), (int) (y1 + x), pixel);
        pixel[0] = corR;
        pixel[1] = corG;
        pixel[2] = corB;
        raster.setPixel((int) (x1 + y), (int) (y1 + x), pixel);

        //6
        raster.getPixel((int) (x1 + y), (int) (y1 - x), pixel);
        pixel[0] = corR;
        pixel[1] = corG;
        pixel[2] = corB;
        raster.setPixel((int) (x1 + y), (int) (y1 - x), pixel);

        //7
        raster.getPixel((int) (x1 - y), (int) (y1 + x), pixel);
        pixel[0] = corR;
        pixel[1] = corG;
        pixel[2] = corB;
        raster.setPixel((int) (x1 - y), (int) (y1 + x), pixel);

        //8
        raster.getPixel((int) (x1 - y), (int) (y1 - x), pixel);
        pixel[0] = corR;
        pixel[1] = corG;
        pixel[2] = corB;
        raster.setPixel((int) (x1 - y), (int) (y1 - x), pixel);

        image = SwingFXUtils.toFXImage(bimage, null);
        imgView.setImage(image);
    }

    public void imprimeElipse(double x, double y) {

        BufferedImage bimage = null;
        bimage = SwingFXUtils.fromFXImage(image, null);
        int pixel[] = {0, 0, 0, 0};
        WritableRaster raster = bimage.getRaster();

        raster.getPixel((int) (x), (int) y, pixel);
        pixel[0] = corR;
        pixel[1] = corG;
        pixel[2] = corB;
        raster.setPixel((int) x, (int) y, pixel);

        image = SwingFXUtils.toFXImage(bimage, null);
        imgView.setImage(image);
    }

    @FXML
    private void evtLimparTela(ActionEvent event) {
        int altura = 1200;
        int largura = 768;

        BufferedImage imagem = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_ARGB);
        int cor = 0xFFFFFFFF; // branco
        for (int x = 0; x < largura; x++) {
            for (int y = 0; y < altura; y++) {
                imagem.setRGB(x, y, cor);
            }
        }
        image = SwingFXUtils.toFXImage(imagem, null);
        imgView.setImage(image);
        poligonos = new ArrayList();
        vboxEsquerda.getChildren().remove(2, vboxEsquerda.getChildren().size());
    }

    @FXML
    private void evtPreto(ActionEvent event) {
        corR = 0;
        corG = 0;
        corB = 0;
    }

    @FXML
    private void evtvermelho(ActionEvent event) {
        corR = 255;
        corG = 0;
        corB = 0;
    }

    @FXML
    private void evtAzul(ActionEvent event) {
        corR = 0;
        corG = 0;
        corB = 255;
    }

    @FXML
    private void evtRosa(ActionEvent event) {
        corR = 255;
        corG = 0;
        corB = 203;
    }

    @FXML
    private void evtCriaPoligono(ActionEvent event) {
        poligono = true;
        p = new Poligono();
        corR = 0;
        corG = 0;
        corB = 0;
    }

    private Poligono salvaAtual(Poligono pol) {
        double xtemp, ytemp;
        for (int i = 0; i < pol.getOriginal().size(); i++) {
            xtemp = pol.getOriginal().get(i).getX();
            ytemp = pol.getOriginal().get(i).getY();
            System.out.println("x - " + xtemp + " y - " + ytemp);
            pol.getAtual().add(new Pontos(xtemp, ytemp));
        }
        return pol;
    }

    private void pintarNovamente(int num) {
        int altura = 1200;
        int largura = 768;
        System.out.println("num = " + num);
        BufferedImage imagem = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_ARGB);
        int cor = 0xFFFFFFFF; // branco
        for (int x = 0; x < largura; x++) {
            for (int y = 0; y < altura; y++) {
                imagem.setRGB(x, y, cor);
            }
        }
        image = SwingFXUtils.toFXImage(imagem, null);
        imgView.setImage(image);
        System.out.println(poligonos.size());
        for (int i = 0; i < poligonos.size(); i++) {
            if (i == num) {
                corR = 255;
                corG = 0;
                corB = 0;
            } else {
                corR = 0;
                corG = 0;
                corB = 0;
            }
            ArrayList<Pontos> pt = poligonos.get(i).getAtual();
            System.out.println("pt - " + pt.size());
            if (pt.size() > 2) {
                int j;
                for (j = 0; j < pt.size() - 1; j++) {
                    x1 = pt.get(j).getX();
                    y1 = pt.get(j).getY();
                    x2 = pt.get(j + 1).getX();
                    y2 = pt.get(j + 1).getY();
                    this.retaMedio();
                }
                x1 = pt.get(0).getX();
                y1 = pt.get(0).getY();
                x2 = pt.get(j).getX();
                y2 = pt.get(j).getY();
                this.retaMedio();
            }
        }
    }

    public static double[][] multiplicarMatrizes(double[][] a, double[][] b) {
        int aRows = a.length;
        int aColumns = a[0].length;
        int bRows = b.length;
        int bColumns = b[0].length;
        if (aColumns != bRows) {
            throw new IllegalArgumentException("Number of columns of first matrix must match number of rows of second matrix");
        }
        double[][] result = new double[aRows][bColumns];
        for (int i = 0; i < aRows; i++) {
            for (int j = 0; j < bColumns; j++) {
                for (int k = 0; k < aColumns; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return result;
    }

    private void translacao(double x3, double y3) {

        double dx;
        double dy;
        Poligono po = poligonos.get(numObj);

        dx = x3 - po.getAtual().get(0).getX();
        dy = y3 - po.getAtual().get(0).getY();

        System.out.println("dx - " + dx + " dy - " + dy);
        double[][] matrizTranslacao = {{1, 0, dx}, {0, 1, dy}, {0, 0, 1}};
        po.setMatrizTransformacao(multiplicarMatrizes(matrizTranslacao, po.getMatrizTransformacao()));

        //po.setMatrizTransformacao(multiplicarMatrizes(po.getMatrizTransformacao(), matrizTranslacao));
        for (int k = 0; k < po.getOriginal().size(); k++) {
            double x = po.getOriginal().get(k).getX();
            double y = po.getOriginal().get(k).getY();
            double xNovo = po.getMatrizTransformacao()[0][0] * x + po.getMatrizTransformacao()[0][1] * y + po.getMatrizTransformacao()[0][2];
            double yNovo = po.getMatrizTransformacao()[1][0] * x + po.getMatrizTransformacao()[1][1] * y + po.getMatrizTransformacao()[1][2];
            System.out.println("xNovo - " + xNovo);
            po.getAtual().get(k).setX(xNovo);
            po.getAtual().get(k).setY(yNovo);
        }

        pintarNovamente(numObj);

    }

    private void escala(double tam) {

        Poligono po = poligonos.get(numObj);

        po.getMatrizTransformacao()[0][0] = tam;
        po.getMatrizTransformacao()[1][1] = tam;

        double[][] matrizEscala = {{tam, 0, 0}, {0, tam, 0}, {0, 0, 1}};
        po.setMatrizTransformacao(multiplicarMatrizes(matrizEscala, po.getMatrizTransformacao()));
        for (int k = 0; k < po.getOriginal().size(); k++) {
            double x = po.getOriginal().get(k).getX();
            double y = po.getOriginal().get(k).getY();
            double xNovo = po.getMatrizTransformacao()[0][0] * x + po.getMatrizTransformacao()[0][1] * y + po.getMatrizTransformacao()[0][2];
            double yNovo = po.getMatrizTransformacao()[1][0] * x + po.getMatrizTransformacao()[1][1] * y + po.getMatrizTransformacao()[1][2];
            System.out.println("xNovo - " + xNovo);
            po.getAtual().get(k).setX(xNovo);
            po.getAtual().get(k).setY(yNovo);
        }

        pintarNovamente(numObj);
    }

    private void rotacao(double grau) {
        double rad = Math.toRadians(grau); // Conversão para radianos
        Poligono po = poligonos.get(numObj);

        // Média aritmética das coordenadas para calcular o centro do polígono
        double xCentro = 0, yCentro = 0;
        for (int k = 0; k < po.getOriginal().size(); k++) {
            xCentro += po.getOriginal().get(k).getX();
            yCentro += po.getOriginal().get(k).getY();
        }
        xCentro /= po.getOriginal().size();
        yCentro /= po.getOriginal().size();

        double[][] matrizTranslacao1 = {{1, 0, -xCentro}, {0, 1, -yCentro}, {0, 0, 1}};

        // Rotação
        double[][] matrizRotacao = {{Math.cos(rad), -Math.sin(rad), 0}, {Math.sin(rad), Math.cos(rad), 0}, {0, 0, 1}};

        // Deslocamento de volta para a posição original
        double[][] matrizTranslacao2 = {{1, 0, xCentro}, {0, 1, yCentro}, {0, 0, 1}};

        // Multiplicação das matrizes de transformação
        po.setMatrizTransformacao(multiplicarMatrizes(multiplicarMatrizes(multiplicarMatrizes(po.getMatrizTransformacao(), matrizTranslacao2), matrizRotacao), matrizTranslacao1));
        System.out.println("teste - " + po.getMatrizTransformacao()[0][0]);
        for (int k = 0; k < po.getOriginal().size(); k++) {
            double x = po.getOriginal().get(k).getX();
            double y = po.getOriginal().get(k).getY();
            double xNovo = po.getMatrizTransformacao()[0][0] * x + po.getMatrizTransformacao()[0][1] * y + po.getMatrizTransformacao()[0][2];
            double yNovo = po.getMatrizTransformacao()[1][0] * x + po.getMatrizTransformacao()[1][1] * y + po.getMatrizTransformacao()[1][2];
            System.out.println("xNovo - " + xNovo);
            po.getAtual().get(k).setX(xNovo);
            po.getAtual().get(k).setY(yNovo);
        }
        pintarNovamente(numObj);

    }

    @FXML
    private void evtTranslada(ActionEvent event
    ) {
        this.translada = true;
    }

    @FXML
    private void evtRotacao(ActionEvent event
    ) {
        btnEsquerda.setVisible(true);
        btnDireita.setVisible(true);
        txRoda.setVisible(true);
    }

    @FXML
    private void evtRotacionaEsquerda(ActionEvent event
    ) {
        this.rotacao(Double.parseDouble(txRoda.getText()));
    }

    @FXML
    private void evtRotacionaDireita(ActionEvent event
    ) {
        this.rotacao(Double.parseDouble(txRoda.getText()));
    }

    @FXML
    private void evtFloodFill(ActionEvent event
    ) {
        floodfill = true;
    }

    private void floodfill(double x, double y) {
        Stack<Pontos> pilha = new Stack<Pontos>();
        BufferedImage bimage = null;
        bimage = SwingFXUtils.fromFXImage(image, null);
        int pixel[] = {0, 0, 0, 0};
        WritableRaster raster = bimage.getRaster();
        pilha.push(new Pontos(x, y));
        while (!pilha.isEmpty()) {
            Pontos p = pilha.pop();
            raster.getPixel((int) p.getX(), (int) p.getY(), pixel);
            pixel[0] = corR;
            pixel[1] = corG;
            pixel[2] = corB;
            raster.setPixel((int) p.getX(), (int) p.getY(), pixel);

            // Verifica os pixels vizinhos
            if (p.getX() < image.getWidth() - 1) {
                Pontos novoPonto = new Pontos(p.getX() + 1, p.getY());
                raster.getPixel((int) novoPonto.getX(), (int) novoPonto.getY(), pixel);
                if ((pixel[0] + pixel[1] + pixel[2]) / 3 == 255) {
                    pilha.add(novoPonto);
                }
            }

            if (p.getY() < image.getHeight() - 1) {
                Pontos novoPonto = new Pontos(p.getX(), p.getY() + 1);
                raster.getPixel((int) novoPonto.getX(), (int) novoPonto.getY(), pixel);
                if ((pixel[0] + pixel[1] + pixel[2]) / 3 == 255) {
                    pilha.add(novoPonto);
                }
            }

            if (p.getX() > 0) {
                Pontos novoPonto = new Pontos(p.getX() - 1, p.getY());
                raster.getPixel((int) novoPonto.getX(), (int) novoPonto.getY(), pixel);
                if ((pixel[0] + pixel[1] + pixel[2]) / 3 == 255) {
                    pilha.add(novoPonto);
                }
            }

            if (p.getY() > 0) {
                Pontos novoPonto = new Pontos(p.getX(), p.getY() - 1);
                raster.getPixel((int) novoPonto.getX(), (int) novoPonto.getY(), pixel);
                if ((pixel[0] + pixel[1] + pixel[2]) / 3 == 255) {
                    pilha.add(novoPonto);
                }
            }
        }
        image = SwingFXUtils.toFXImage(bimage, null);
        imgView.setImage(image);

    }

    @FXML
    private void evtEscala(ActionEvent event) {
        btnEscala.setVisible(true);
        txEscala.setVisible(true);
    }

    @FXML
    private void evtGeraEscala(ActionEvent event) {
        this.escala(Double.parseDouble(txEscala.getText()));
    }

    private ArrayList<Estrutura> geraET() {
        ArrayList<Estrutura> lista = new ArrayList();
        ArrayList<Pontos> pt = poligonos.get(numObj).getAtual();
        System.out.println("pt - " + pt.size());
        if (pt.size() > 2) {
            int j;
            for (j = 0; j < pt.size() - 1; j++) {
                x1 = pt.get(j).getX();
                y1 = pt.get(j).getY();
                x2 = pt.get(j + 1).getX();
                y2 = pt.get(j + 1).getY();
                this.retaMedio();
            }
            x1 = pt.get(0).getX();
            y1 = pt.get(0).getY();
            x2 = pt.get(j).getX();
            y2 = pt.get(j).getY();
            this.retaMedio();
        }
        return lista;
    }

    @FXML
    private void evtRasteirizacao(ActionEvent event) {
    }

}
