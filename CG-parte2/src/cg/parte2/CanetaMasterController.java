package cg.parte2;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

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

    private double x1, x2, y1, y2;
    @FXML
    private ImageView imgView;

    private Image image;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        int altura = 700;
        int largura = 800;

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

    }

    private void desenhaRetaReal() {
        System.out.println("Reta real");
        BufferedImage bimage = null;
        bimage = SwingFXUtils.fromFXImage(image, null);
        int pixel[] = {0, 0, 0, 0};
        WritableRaster raster = bimage.getRaster();
        double dy = y2 - y1;
        double dx = x2 - x1;
        double m = dy / dx;
        double y = 0;
        if (x2 > x1 && y2 > y1) {
            for (double x = x1; x <= x2; x++) {
                y = y1 + m * (x - x1);
                raster.getPixel((int)x, (int)y, pixel);
                pixel[0] = 0;
                pixel[1] = 0;
                pixel[2] = 0;
                raster.setPixel((int)x,(int)y, pixel);

            }
            image = SwingFXUtils.toFXImage(bimage, null);
            imgView.setImage(image);

        }

    }

    

    

    private void dda() {
        System.out.println("dda");
    }

    private void retaMedio() {
        System.out.println("reta ponto médio");
    }

    private void cirMedio() {
        System.out.println("circunferencia ponto médio");
    }

    private void cirReal() {
        System.out.println("circunferência real");
    }

    private void cirTrig() {
        System.out.println("cricunferencia trigonométrico");
    }

    private void elipse() {
        System.out.println("elipse ponto médio");
    }

}
