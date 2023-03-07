package photofx;

import ij.ImagePlus;
import ij.process.ImageProcessor;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

public class FXMLDocumentController implements Initializable {

    @FXML
    private ImageView imagemView;
    private Image image = null;
    private BufferedImage bImage;
    private ImagePlus imageplus;
    private boolean modified = false;
    private int x1, x2, y1, y2;
    private boolean canetaAtiva = false;
    private Graphics2D graph;
    private Color corsel = Color.WHITE;
    private boolean tipo = false;
    private boolean pinta = false;
    private boolean quadrado = false;
    File file;
    @FXML
    private MenuItem imAbrir;
    @FXML
    private MenuItem imSalvar;
    @FXML
    private MenuItem imSalvarComo;
    @FXML
    private MenuItem imFechar;
    @FXML
    private Menu tfImagens;
    @FXML
    private MenuItem opSobre;
    @FXML
    private Button btnSalvar;
    @FXML
    private Button btnSalvarComo;
    @FXML
    private Menu optCaneta;
    @FXML
    private Button btnCaneta;
    @FXML
    private Slider sldTamanhoPincel;
    @FXML
    private Label resultR;
    @FXML
    private Label resultG;
    @FXML
    private Label resultB;
    @FXML
    private Label resultC;
    @FXML
    private Label resultM;
    @FXML
    private Label resultY;
    @FXML
    private Label resultH;
    @FXML
    private Label resultS;
    @FXML
    private Label resultI;

    private Hsi[][] obj;
    @FXML
    private Menu tfBinario;
    @FXML
    private BarChart<?, ?> graficoHisto;
    @FXML
    private Menu tfHistograma;

    private int vetor[] = new int[256];
    @FXML
    private TextField txBinarizar;
    @FXML
    private Button btnBinarizar;
    @FXML
    private VBox vboxEsquerda;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        graficoHisto.setVisible(false);
    }

    @FXML
    private void evtAbrir(ActionEvent event) {
        graficoHisto.setVisible(false);
        if (!graficoHisto.getData().isEmpty()) {
            graficoHisto.getData().clear();
            //graficoHisto.getData().remove(0);
        }

        FileChooser filechooser = new FileChooser();
        filechooser.setInitialDirectory(new File("d:\\"));
        filechooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPEG Files", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG Files", "*.png"),
                new FileChooser.ExtensionFilter("JPEG Files", "*.jpeg"),
                new FileChooser.ExtensionFilter("GIF Files", "*.gif")
        );
        file = filechooser.showOpenDialog(null);
        if (file != null) {
            image = new Image(file.toURI().toString());
            imagemView.setFitHeight(image.getHeight());
            imagemView.setFitWidth(image.getWidth());
            imagemView.setImage(image);
            imSalvar.setDisable(false);
            imSalvarComo.setDisable(false);
            imFechar.setDisable(false);
            tfImagens.setVisible(true);
            tfImagens.setDisable(false);
            tfBinario.setVisible(true);
            tfBinario.setDisable(false);
            tfHistograma.setVisible(true);
            tfHistograma.setDisable(false);
            vboxEsquerda.setVisible(true);
            btnSalvarComo.setDisable(false);
            btnSalvarComo.setVisible(true);
            btnSalvar.setDisable(false);
            btnSalvar.setVisible(true);
            btnCaneta.setDisable(false);
//            graph = bImage.createGraphics();
        }
        System.out.println("altura = " + image.getHeight());
        obj = new Hsi[(int) image.getHeight()][(int) image.getWidth()];
        bImage = SwingFXUtils.fromFXImage(image, null);
        int pixel[] = {0, 0, 0, 0};
        int vetor[];
        WritableRaster raster = bImage.getRaster();
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                raster.getPixel(x, y, pixel);
                vetor = this.geraHSI(pixel[0], pixel[1], pixel[2]);
                obj[y][x] = new Hsi(vetor[0], vetor[1], vetor[2]);
            }
        }
        image = SwingFXUtils.toFXImage(bImage, null);
        imagemView.setImage(image);
    }

    @FXML
    private void evtSalvar(ActionEvent event) {
        if (modified) {
            if (file != null) {
                try {
                    ImageIO.write(bImage, "jpg", file);
                } catch (IOException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Erro:" + e.getMessage());
                    alert.showAndWait();
                }
            }
            modified = false;
        }

    }

    @FXML
    private void evtSalvarComo(ActionEvent event) {
        FileChooser filechooser = new FileChooser();
        filechooser.setInitialFileName(file.getName());
        filechooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPEG Files", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG Files", "*.png"),
                new FileChooser.ExtensionFilter("JPEG Files", "*.jpeg"),
                new FileChooser.ExtensionFilter("GIF Files", "*.gif")
        );
        file = filechooser.showSaveDialog(null);
        if (file != null) {
            try {
                ImageIO.write(bImage, "jpg", file);
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Erro:" + e.getMessage());
                alert.showAndWait();
            }
        }
        modified = false;
    }

    @FXML
    private void evtFechar(ActionEvent event) {
        if (modified) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Detectamos que a imagem não foi salva");
            alert.setContentText("Salvar a imagem?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (file != null) {
                    try {
                        ImageIO.write(bImage, "jpg", file);
                    } catch (IOException e) {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Erro:" + e.getMessage());
                        alert.showAndWait();
                    }
                }
            }
        }
        imSalvar.setDisable(true);
        imSalvarComo.setDisable(true);
        imFechar.setDisable(true);
        tfImagens.setVisible(false);
        tfImagens.setDisable(true);
        btnSalvarComo.setDisable(true);
        btnSalvarComo.setVisible(false);
        btnSalvar.setDisable(true);
        btnSalvar.setVisible(false);
        btnCaneta.setDisable(true);
        image = null;
        file = null;
        imagemView.setImage(image);

    }

    @FXML
    private void evtSair(ActionEvent event) {
        if (modified) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Detectamos uma imagem não salva");
            alert.setContentText("Salvar a imagem?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (file != null) {
                    try {
                        ImageIO.write(bImage, "jpg", file);
                    } catch (IOException e) {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Erro:" + e.getMessage());
                        alert.showAndWait();
                    }
                }
            }
        }
        Platform.exit();
    }

    @FXML
    private void evtTonsCinza(ActionEvent event) {
        int media;
        if (image != null) {
            bImage = SwingFXUtils.fromFXImage(image, null);
            int pixel[] = {0, 0, 0, 0};
            WritableRaster raster = bImage.getRaster();
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    raster.getPixel(x, y, pixel);
                    media = (int) ((pixel[0] * 0.299 + pixel[1] * 0.587 + pixel[2] * 0.114) / 3);
                    pixel[0] = media;
                    pixel[1] = media;
                    pixel[2] = media;
                    raster.setPixel(x, y, pixel);
                }
            }
            image = SwingFXUtils.toFXImage(bImage, null);
            imagemView.setImage(image);

        }
        modified = true;

    }
    //inicio das funções usando imageJ

    @FXML
    private void evtSobre(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sobre");
        alert.setHeaderText("Nome: Guilherme Lucas de Oliveira R.A:102012130\nNome: Lucas Yugo Suyama R.A:101911335");
        alert.setContentText("Aplicativo criado para manipulação de imagens\nFunções manuais:\nEscala de cinza(desenvolvida em aula)\nPreto e branco\nNegativo\nEspelho vertical\nEspelho horizontal");
        alert.showAndWait();
    }

    //teste, ainda não faz parte do produto final
    @FXML
    private void evtCaneta(ActionEvent event) {
        if (!canetaAtiva) {
            canetaAtiva = true;
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Caneta");
            alert.setHeaderText("a caneta está ativa!!!");
            alert.showAndWait();
            optCaneta.setDisable(false);
            optCaneta.setVisible(true);
        } else {
            canetaAtiva = false;
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Caneta");
            alert.setHeaderText("a caneta não esrá mais ativa!!!");
            alert.showAndWait();
            optCaneta.setDisable(true);
            optCaneta.setVisible(false);
            tipo = false;
            pinta = false;
            quadrado = false;
            sldTamanhoPincel.setVisible(false);
        }
    }

    @FXML
    private void evtMouseArrasta(MouseEvent event) {
        bImage = SwingFXUtils.fromFXImage(image, null);
        graph = bImage.createGraphics();
        graph.setColor(corsel);
        if (canetaAtiva && !tipo && !pinta && !quadrado) {
            x1 = (int) event.getX();
            y1 = (int) event.getY();
            x2 = (int) event.getX();
            y2 = (int) event.getY();
            graph.drawLine(x1, y1, x2, y2);
            graph.dispose();
            image = SwingFXUtils.toFXImage(bImage, null);
            imagemView.setImage(image);
            modified = true;
        }
        if (pinta && !quadrado) {
            x1 = (int) event.getX();
            y1 = (int) event.getY();
            x2 = (int) event.getX();
            y2 = (int) event.getY();
            graph.fillOval(x1, y1, (int) sldTamanhoPincel.getValue(), (int) sldTamanhoPincel.getValue());
            graph.dispose();
            image = SwingFXUtils.toFXImage(bImage, null);
            imagemView.setImage(image);
            modified = true;
        }

    }

    @FXML
    private void evtMousePressionado(MouseEvent event) {
        if (canetaAtiva) {
            x1 = (int) event.getX();
            y1 = (int) event.getY();
        }
    }

    @FXML
    private void evtCorAmarelo(ActionEvent event) {
        corsel = Color.YELLOW;
    }

    @FXML
    private void evtCorAzul(ActionEvent event) {
        corsel = Color.BLUE;
    }

    @FXML
    private void evtCorVerde(ActionEvent event) {
        corsel = Color.GREEN;
        //graph.setColor(Color.GREEN);
    }

    @FXML
    private void evtCorVermelho(ActionEvent event) {
        corsel = Color.RED;
    }

    @FXML
    private void optCanetaLivre(ActionEvent event) {
        tipo = false;
    }

    @FXML
    private void optCanetaReta(ActionEvent event) {
        tipo = true;
    }

    @FXML
    private void evtMouseSolta(MouseEvent event) {
        bImage = SwingFXUtils.fromFXImage(image, null);
        graph = bImage.createGraphics();
        graph.setColor(corsel);
        if (canetaAtiva && tipo && !pinta && !quadrado) {
            x2 = (int) event.getX();
            y2 = (int) event.getY();
            graph.drawLine(x1, y1, x2, y2);
            graph.dispose();
            image = SwingFXUtils.toFXImage(bImage, null);
            imagemView.setImage(image);
            modified = true;
        }
        if (quadrado) {
            x2 = (int) event.getX();
            y2 = (int) event.getY();
            graph.fillRect(x1, y1, x2 + x1, y2 + y1);
            graph.dispose();
            image = SwingFXUtils.toFXImage(bImage, null);
            imagemView.setImage(image);
            modified = true;
        }
    }

    @FXML
    private void optPinta(ActionEvent event) {
        sldTamanhoPincel.setVisible(true);
        pinta = true;
    }

    @FXML
    private void optQuadrado(ActionEvent event) {
        quadrado = true;
    }

    private int[] geraHSI(int R, int G, int B) {
        int hsi[] = {0, 0, 0};
        //primeiro passo, normaliza
        double r = (double) R / (R + G + B);
        double g = (double) G / (R + G + B);
        double b = (double) B / (R + G + B);
        double h = 0;
        double s = 0;
        double i = 0;
        if (b > g) {
            h = (2 * Math.PI) - Math.acos((0.5 * ((r - g) + (r - b))) / (Math.pow(Math.pow((r - g), 2) + ((r - b) * (g - b)), 0.5)));
        } else {
            h = Math.acos((0.5 * ((r - g) + (r - b))) / (Math.pow(Math.pow((r - g), 2) + ((r - b) * (g - b)), 0.5)));
        }

        double inter = Math.min(r, g);
        s = 1 - 3 * Math.min(b, inter);
        i = ((R + G + B) / 3);
        hsi[0] = (int) ((h * 180) / Math.PI);
        hsi[1] = (int) (s * 100);
        hsi[2] = (int) i;
        return hsi;
    }

    private int[] converteHSIparaRGB(int H, int S, int I) {
        int rgb[] = {0, 0, 0};
        double h = 0;
        double s = 0;
        double i = 0;

        h = H * (Math.PI / 180);
        s = (double) S / 100;
        i = (double) I / 255;

        double x, y, z;

        if (h < ((2 * Math.PI) / 3)) {
            x = (i * (1 - s));
            y = i * (1 + (s * Math.cos(h)) / (Math.cos(Math.PI / 3 - h)));
            z = 3 * i - (x + y);
            rgb[0] = (int) (y * 255);
            rgb[1] = (int) (z * 255);
            rgb[2] = (int) (x * 255);
        } else if (((2 * Math.PI) / 3) <= h && h < ((4 * Math.PI) / 3)) {
            h = h - ((2 * Math.PI) / 3);
            x = (i * (1 - s));
            y = i * (1 + (s * Math.cos(h)) / (Math.cos(Math.PI / 3 - h)));
            z = 3 * i - (x + y);
            rgb[0] = (int) (x * 255);
            rgb[1] = (int) (y * 255);
            rgb[2] = (int) (z * 255);
        } else {
            h = h - ((4 * Math.PI) / 3);
            x = (i * (1 - s));
            y = i * (1 + (s * Math.cos(h)) / (Math.cos(Math.PI / 3 - h)));
            z = 3 * i - (x + y);
            rgb[0] = (int) (z * 255);
            rgb[1] = (int) (x * 255);
            rgb[2] = (int) (y * 255);
        }

        return rgb;
    }

    private void geraImagemHSI() {
        int media;
        int vet[];
        Hsi aux = new Hsi(0, 0, 0);
        if (image != null) {
            bImage = SwingFXUtils.fromFXImage(image, null);
            int pixel[] = {0, 0, 0, 0};
            WritableRaster raster = bImage.getRaster();
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    raster.getPixel(x, y, pixel);
                    if (obj[y][x].getH() > 360) {
                        aux.setH(360);
                    } else if (obj[y][x].getH() < 0) {
                        aux.setH(100);
                    } else {
                        aux.setH(obj[y][x].getH());
                    }
                    if (obj[y][x].getS() > 100) {
                        aux.setS(100);
                    } else if (obj[y][x].getS() < 0) {
                        aux.setS(0);
                    } else {
                        aux.setS(obj[y][x].getS());
                    }
                    if (obj[y][x].getI() > 255) {
                        aux.setI(255);
                    } else if (obj[y][x].getI() < 0) {
                        aux.setI(0);
                    } else {
                        aux.setI(obj[y][x].getI());
                    }
                    vet = this.converteHSIparaRGB(aux.getH(), aux.getS(), aux.getI());
                    pixel[0] = vet[0];
                    pixel[1] = vet[1];
                    pixel[2] = vet[2];
                    raster.setPixel(x, y, pixel);
                }
            }
            image = SwingFXUtils.toFXImage(bImage, null);
            imagemView.setImage(image);

        }
        modified = true;
    }

    @FXML
    private void evtPegaCor(MouseEvent event) {

        int media;
        int pixel[] = {0, 0, 0, 0};
        if (image != null) {
            bImage = SwingFXUtils.fromFXImage(image, null);

            WritableRaster raster = bImage.getRaster();
            int x, y;
            x = (int) event.getX();
            y = (int) event.getY();
            raster.getPixel(x, y, pixel);

        }

        resultR.setText("" + pixel[0]);
        resultG.setText("" + pixel[1]);
        resultB.setText("" + pixel[2]);

        resultC.setText("" + (255 - pixel[0]));
        resultM.setText("" + (255 - pixel[1]));
        resultY.setText("" + (255 - pixel[2]));

        int vetor[] = this.geraHSI(pixel[0], pixel[1], pixel[2]);
        //int vetor[]= this.geraHSI(100,150,200);
        resultH.setText("" + vetor[0]);
        resultS.setText("" + vetor[1]);
        resultI.setText("" + vetor[2]);

        int rgb[] = this.converteHSIparaRGB(vetor[0], vetor[1], vetor[2]);
    }

    @FXML
    private void evtIntensidade(ActionEvent event) {
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                obj[y][x].setI(obj[y][x].getI() + 10);
            }
        }

        this.geraImagemHSI();
    }

    @FXML
    private void evtDIntensidade(ActionEvent event) {
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                obj[y][x].setI(obj[y][x].getI() - 10);
            }
        }

        this.geraImagemHSI();
    }

    @FXML
    private void evtAumentaSaturacao(ActionEvent event) {
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                obj[y][x].setS(obj[y][x].getS() + 10);
            }
        }

        this.geraImagemHSI();
    }

    @FXML
    private void evtDiminuiSaturacao(ActionEvent event) {
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                obj[y][x].setS(obj[y][x].getS() - 10);
            }
        }

        this.geraImagemHSI();
    }

    @FXML
    private void evtAumentaMatiz(ActionEvent event) {
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                obj[y][x].setH(obj[y][x].getH() + 10);
            }
        }

        this.geraImagemHSI();
    }

    @FXML
    private void evtDiminuiMatiz(ActionEvent event) {
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                obj[y][x].setH(obj[y][x].getH() - 10);
            }
        }

        this.geraImagemHSI();
    }

    @FXML
    private void evtBinarizacaoManual(ActionEvent event) {
        txBinarizar.setVisible(true);
        btnBinarizar.setVisible(true);

    }

    @FXML
    private void evtBinarizacaoOtsu(ActionEvent event) {
        double altura = image.getHeight();
        double largura = image.getWidth();
        //arruma vetor - num/M*N
        double vetorN[] = new double[256];
        for(int i = 0;i<256;i++){
            vetorN[i] = vetor[i]/(altura*largura);
        }
        
        //media global
        double mediaGlobal = 0;
        for(int i = 0;i<256;i++){
            mediaGlobal+=i*vetorN[i];
        }
        
        //variância global
        double varianciaGlobal = 0;
        for(int i = 0;i<256;i++){
            mediaGlobal+=Math.pow(i-mediaGlobal, 2) * vetorN[i];
        }
        
        //encontrar valor ótimo
        int threshold = 0;
        double maxVariance = 0;
        double sum = 0;
        double sumB = 0;
        int count = 0;
        for (int i = 0; i < 256; i++) {
            sum += i * vetorN[i];
            count += vetor[i];

            if (count == 0) {
                continue;
            }

            double p = (double) count / (altura*largura);
            double q = 1 - p;

            sumB += i * vetorN[i];

            double meanB = sumB / count;
            double meanF = (sum - sumB) / ((altura*largura) - count);

            double betweenClassVariance = p * q * Math.pow(meanB - meanF, 2);

            if (betweenClassVariance > maxVariance) {
                maxVariance = betweenClassVariance;
                threshold = i;
            }
        }
        txBinarizar.setText(""+threshold);
    }
    public void manipula(){
        if (image != null) {
            bImage = SwingFXUtils.fromFXImage(image, null);
            int pixel[] = {0, 0, 0, 0};
            WritableRaster raster = bImage.getRaster();
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    raster.getPixel(x, y, pixel);
                    vetor[pixel[0]]++;
                }
            }

        }
        XYChart.Series escala = new XYChart.Series();
        for (int i = 0; i < 256; i++) {
            escala.getData().add(new XYChart.Data("" + i, vetor[i]));
        }
        graficoHisto.getData().add(escala);
        graficoHisto.setVisible(true);
    }
    @FXML
    private void gerarHistograma(ActionEvent event) {
        for(int i = 0;i<256;i++){
            vetor[i] = 0;
        }
        this.evtTonsCinza(event);

        manipula();

    }

    @FXML
    private void equalizarHistograma(ActionEvent event) {
        double altura = image.getHeight();
        double largura = image.getWidth();
        
        //arruma vetor - num/M*N
        double vetorN[] = new double[256];
        for(int i = 0;i<256;i++){
            vetorN[i] = vetor[i]/(altura*largura);
        }
        
        //distribuição acumulado
        double cdf[] = new double[256];
        cdf[0] = vetorN[0];
        for (int i = 1; i < 256; i++) {
            cdf[i] = cdf[i - 1] + vetorN[i];
        }
        
        //equalizar imagem
        int media;
        bImage = SwingFXUtils.fromFXImage(image, null);
            int pixel[] = {0, 0, 0, 0};
            WritableRaster raster = bImage.getRaster();
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    raster.getPixel(x, y, pixel);
                    pixel[0] =  (int) (cdf[pixel[0]] * 255);
                    pixel[1] = (int) (cdf[pixel[1]] * 255);
                    pixel[2] = (int) (cdf[pixel[2]] * 255);
                    raster.setPixel(x, y, pixel);
                }
            }
            image = SwingFXUtils.toFXImage(bImage, null);
            imagemView.setImage(image);
            manipula();
        
    }

    @FXML
    private void evtBinarizarManual(ActionEvent event) {
        int media;
        int ponto = Integer.parseInt(txBinarizar.getText());
        
        if (image != null) {
            bImage = SwingFXUtils.fromFXImage(image, null);
            int pixel[] = {0, 0, 0, 0};
            WritableRaster raster = bImage.getRaster();
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    raster.getPixel(x, y, pixel);
                    media = (int) ((pixel[0] + pixel[1] + pixel[2]) / 3);
                    if (media > ponto) {
                        media = 255;
                    } else {
                        media = 0;
                    }
                    pixel[0] = media;
                    pixel[1] = media;
                    pixel[2] = media;
                    raster.setPixel(x, y, pixel);
                }
            }
            image = SwingFXUtils.toFXImage(bImage, null);
            imagemView.setImage(image);

        }
        modified = true;
    }

}
