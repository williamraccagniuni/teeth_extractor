package te;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.w3c.dom.css.RGBColor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class Controller implements Initializable {


    ObservableList list= FXCollections.observableArrayList();
    Image image;
    BufferedImage image_dx, image_sx;
    int dxstart_x, dxstart_y, sxstart_x, sxstart_y;
    int section_size = 200;

    @FXML
    ChoiceBox stage_dx, stage_sx, race, sex;
    @FXML
    TextField subject, age;
    @FXML
    Text flm;
    @FXML
    ImageView imageview_dx, imageview_sx;
    @FXML
    Slider dx_widthbar, dx_heightbar, sx_widthbar, sx_heightbar;

    private void set_stages(){
        list.removeAll(list);
        list.addAll("dente non presente", "1", "2", "3", "4", "5", "6", "7", "8");
        stage_dx.setValue("dente non presente");
        stage_dx.getItems().addAll(list);
        stage_sx.setValue("dente non presente");
        stage_sx.getItems().addAll(list);
    }

    private void set_race() {
        list.removeAll(list);
        list.addAll("sconosciuta", "caucasoide", "mongoloide", "amerindioide", "negroide", "australoide");
        race.setValue("sconosciuta");
        race.getItems().addAll(list);
    }

    private void set_sex(){
        list.removeAll(list);
        list.addAll("sconosciuto", "maschio", "femmina");
        sex.setValue("sconosciuto");
        sex.getItems().addAll(list);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        set_stages();
        set_sex();
        set_race();
    }



    private void set_dx_widthbar(){
        dx_widthbar.setMin(0.0);
        dx_widthbar.setMax(image.getWidth() - section_size);
        dx_widthbar.setValue(dxstart_x);
    }

    private void set_dx_heightbar(){
        dx_heightbar.setMin(0.0);
        dx_heightbar.setMax(image.getHeight() - section_size);
        dx_heightbar.setValue(dxstart_y);
    }

    private void set_sx_widthbar(){
        sx_widthbar.setMin(0.0);
        sx_widthbar.setMax(image.getWidth() - section_size);
        sx_widthbar.setValue(sxstart_x);
    }

    private void set_sx_heightbar(){
        sx_heightbar.setMin(0.0);
        sx_heightbar.setMax(image.getHeight() - section_size);
        sx_heightbar.setValue(sxstart_y);
    }

    public void load_image(ActionEvent actionEvent) {

        Window stage = new Stage();
        File file;

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Carica File");
        file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            //System.out.println(file.getPath());
            flm.setText("File caricato: " + file.getPath());

            image = new Image(file.toURI().toString());

            dxstart_x = (int) ((image.getWidth() - section_size) * 0.15);
            dxstart_y = (int) ((image.getHeight() - section_size) * 0.66);

            set_dx_widthbar();
            set_dx_heightbar();
            set_imagedx();

            sxstart_x = (int) ((image.getWidth() - section_size) * 0.85);
            sxstart_y = dxstart_y;

            set_sx_widthbar();
            set_sx_heightbar();
            set_imagesx();
        }

    }

    public void exit_program(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void save(ActionEvent actionEvent) throws IOException, InterruptedException {

        if(image != null){

            if( isNumeric(this.age.getText()) || this.age.getText().isEmpty()){

                if(!this.subject.getText().isEmpty()){

                    CSVManager csvm = new CSVManager();

                    String stage_dx, stage_sx, subject, race, sex, age;

                    if(((String) this.stage_dx.getValue()).equals("dente non presente")){
                        stage_dx = "tnp";
                    }else{
                        stage_dx = (String) this.stage_dx.getValue();
                    }

                    if(((String) this.stage_sx.getValue()).equals("dente non presente")){
                        stage_sx = "tnp";
                    }else{
                        stage_sx = (String) this.stage_sx.getValue();
                    }

                    subject = this.subject.getText();



                    if(((String) this.race.getValue()).equals("sconosciuta")){
                        race = "na";
                    }else{
                        race = (String) this.race.getValue();
                    }

                    if(((String) this.sex.getValue()).equals("sconosciuto")){
                        sex = "na";
                    }else{
                        sex = (String) this.sex.getValue();
                    }

                    if(this.age.getText().isEmpty()){
                        age = "na";
                    }else{
                        age = this.age.getText();
                    }



                    List<String[]> dataLines = new ArrayList<>();
                    dataLines.add(new String[] { subject, stage_dx, stage_sx, race, sex, age });

                    csvm.save_to_csv(("soggetto_" + subject + ".csv"), dataLines);

                    //Immagine dx

                    BufferedImage bdxi = img_to_buffered(image).getSubimage(dxstart_x,dxstart_y, section_size, section_size);
                    File imagedxfile = new File("soggetto_" + subject + "_dx.bmp");
                    ImageIO.write(bdxi, "bmp", imagedxfile);

                    //Immagine sx

                    BufferedImage bsxi = img_to_buffered(image).getSubimage(sxstart_x,sxstart_y, section_size, section_size);
                    File imagesxfile = new File("soggetto_" + subject + "_sx.bmp");
                    ImageIO.write(bsxi, "bmp", imagesxfile);


                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Info");
                    alert.setHeaderText(null);
                    alert.setContentText("I file "+ "soggetto_" + subject + "_dx.bmp, " + "soggetto_" + subject + "_sx.bmp e "
                            + "soggetto_" + subject + ".csv" + " sono stati salvati con successo.");
                    alert.showAndWait();
                }else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Soggetto: campo vuoto");
                    alert.setHeaderText(null);
                    alert.setContentText("Il valore del soggetto non è presente.");
                    alert.showAndWait();
                }

            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Errore: età non corretta");
                alert.setHeaderText(null);
                alert.setContentText("Il valore del campo età non è corretto: esempi accettabili 12, 13.4, ecc., o lasciare vuoto se sconosciuta.");
                alert.showAndWait();
            }

        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore: Nessun File Caricato");
            alert.setHeaderText(null);
            alert.setContentText("Impossibile salvare, non è stata caricata alcuna immagine.");
            alert.showAndWait();
        }

    }



    public void dx_image_height_update(){
//        System.out.println(dx_heightbar.getValue());

        dxstart_y = (int) dx_heightbar.getValue();
        set_imagedx();
    }

    public void dx_image_width_update(){
//        System.out.println("dx width");
//        System.out.println(dx_widthbar.getValue());

        dxstart_x = (int) dx_widthbar.getValue();
        set_imagedx();
    }

    public void sx_image_height_update(){
//        System.out.println("sx height");
//        System.out.println(sx_heightbar.getValue());

        sxstart_y = (int) sx_heightbar.getValue();
        set_imagesx();
    }

    public void sx_image_width_update(){
//        System.out.println("sx width");
//        System.out.println(sx_widthbar.getValue());

        sxstart_x = (int) sx_widthbar.getValue();
        set_imagesx();
    }

    private void set_imagedx(){
//  image_dx = new BufferedImage(1, 1, TYPE_INT_RGB);
//  imma.setRGB(0,0,0xff0000);

        BufferedImage bi = img_to_buffered(image);
        bi = bi.getSubimage(dxstart_x,dxstart_y, section_size, section_size);
        Image img = SwingFXUtils.toFXImage(bi, null);
        imageview_dx.setImage(img);

    }

    private void set_imagesx(){

        BufferedImage bi = img_to_buffered(image);
        bi = bi.getSubimage(sxstart_x,sxstart_y, section_size, section_size);
        Image img = SwingFXUtils.toFXImage(bi, null);
        imageview_sx.setImage(img);

    }

    private BufferedImage img_to_buffered(Image img){

        int width = (int) img.getWidth();
        int height = (int) img.getHeight();

        BufferedImage buf = new BufferedImage(width, height, TYPE_INT_RGB);

        for(int x=0; x<width; x++){
            for(int y=0; y<height; y++){
                Color c = img.getPixelReader().getColor(x,y);
                String hex = String.format("0x%02X%02X%02X", (int) (c.getRed() * 255), (int) (c.getGreen() * 255), (int) (c.getBlue() * 255));
                buf.setRGB(x,y, Integer.decode(hex));
            }
        }

        return buf;
    }

    private boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}
