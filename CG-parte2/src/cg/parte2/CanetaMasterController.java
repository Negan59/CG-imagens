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

        System.out.println("x2 - "+x2+" y2 - "+y2);

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
        System.out.println("x1 - "+x1+" y1 - "+y1);

    }

    private void desenhaRetaReal() {
        System.out.println("Reta real");
        BufferedImage bimage = null;
        bimage = SwingFXUtils.fromFXImage(image, null);
        int pixel[] = {0, 0, 0, 0};
        WritableRaster raster = bimage.getRaster();
        double dy = y2 - y1;
        double dx = x2 - x1;
        System.out.println("dx - "+dx);
        System.out.println("dy - "+dy);

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

        if(dx > dy) {
            if(x2>x1){
                //primeiro e oitavo octantes
                for (x = x1; x <= x2; x++) {
                    y = y1 + m * (x - x1);
                    raster.getPixel((int) x, round(y), pixel);
                    pixel[0] = 0;
                    pixel[1] = 0;
                    pixel[2] = 0;
                    raster.setPixel((int) x, round(y), pixel);
                }
            }
            else{
                System.out.println("quarto e quinto octantes");
                for(x=x2;x>=x1;x--){
                    y = y1 + m * (x - x1);
                    raster.getPixel((int) x, round(y), pixel);
                    pixel[0] = 0;
                    pixel[1] = 0;
                    pixel[2] = 0;
                    raster.setPixel((int) x, round(y), pixel);
                }
            }
        }
        else{
            if(y2>y1){

                for(y = y1;y<=y2;y++){
                    x = x1 + (y - y1)/m;
                    raster.getPixel((int)x, round(y), pixel);
                    pixel[0] = 0;
                    pixel[1] = 0;
                    pixel[2] = 0;
                    raster.setPixel((int)x,round(y), pixel);
                }
            }else{
                System.out.println("este é o 6 octante?");
                for (y = y2; y <= y1; y++) {
                    x = x1 + (y - y1) / m;
                    raster.getPixel((int) x, round(y), pixel);
                    pixel[0] = 0;
                    pixel[1] = 0;
                    pixel[2] = 0;
                    raster.setPixel((int) x, round(y), pixel);
                }
            }

        }

        image = SwingFXUtils.toFXImage(bimage, null);
        imgView.setImage(image);

    }
    private int round(double num){
        double parteNaoInteira = num % 1.0;
        int numero = (int)num;
        if(parteNaoInteira > 0.5){
            numero++;
        }
        return numero;
    }
    

    

    private void dda() {
        BufferedImage bimage = null;
        bimage = SwingFXUtils.fromFXImage(image, null);
        int pixel[] = {0, 0, 0, 0};
        WritableRaster raster = bimage.getRaster();
        System.out.println("dda");
        double dy = y2 - y1;
        double dx = x2 - x1;

        double m = dy/dx;
        double x;
        double y;
        double xInc;
        double yInc;

        double tamanho;

        tamanho = Math.abs(x2-x1);
        //aqui é pra saber se o tamanho vai ser definido pelo eixo x, ou pelo eixo y
        if(Math.abs(y2-y1) > tamanho){
            tamanho = Math.abs(y2-y1);
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

        xInc = (x2-x1)/tamanho;
        yInc = (y2-y1)/tamanho;
        y = y1;
        for(x = x1; x <= x2; x += xInc){
            raster.getPixel((int)x, round(y), pixel);
            pixel[0] = 0;
            pixel[1] = 0;
            pixel[2] = 0;
            raster.setPixel((int)x, round(y), pixel);
            y += yInc;
        }

        image = SwingFXUtils.toFXImage(bimage, null);
        imgView.setImage(image);
    }

    private void retaMedio() {
        System.out.println("reta ponto médio");
        BufferedImage bimage = null;
        bimage = SwingFXUtils.fromFXImage(image, null);
        int pixel[] = {0, 0, 0, 0};
        WritableRaster raster = bimage.getRaster();



        if(y1>y2){
            y1 = -y1;
            y2 = -y2;
        }
        double dy = y2 - y1;
        double dx = x2 - x1;
        double m = dy/dx;
        double x;
        double y;
        double incE;
        double incNE;
        double d;
        double xf;
        int declive;

        //constante de bresenhan
        incE = 2 * dy;
        incNE = 2 * dy - 2 * dx;

        d = 2 * dy - dx;
        y = y1;

        if (dy >= 0) {
            declive = 1;
        } else {
            declive = -1;
        }
        if (x1 > x2) {
            x = x2;
            y = y2;
            xf = x1;
        }
        else{
            x = x1;
            y = y1;
            xf = x2;
        }

        raster.getPixel((int)x, (int)y, pixel);
        pixel[0] = 0;
        pixel[1] = 0;
        pixel[2] = 0;
        raster.setPixel((int)x, (int)y, pixel);
        while(x<xf){
            x++;
            if(d<0){
                d +=incE;
            }
            else{
                y++;
                d+=incNE;
            }
            raster.getPixel((int)x, (int)y, pixel);
            pixel[0] = 0;
            pixel[1] = 0;
            pixel[2] = 0;
            raster.setPixel((int)x, (int)y, pixel);
        }


        /*if (Math.abs(dy) > Math.abs(dx)) {
            if (y1 > y2) {
                double temp = x1;
                x1 = x2;
                x2 = temp;
                temp = y1;
                y1 = y2;
                y2 = temp;
                this.retaMedio();
            }

        } else {
            for (x = x1; x <= x2; x++) {

                if (d <= 0) {
                    d = d + incE;
                } else {
                    d = d + incNE;
                    y = y + declive;
                }
            }
        }*/

        image = SwingFXUtils.toFXImage(bimage, null);
        imgView.setImage(image);
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
