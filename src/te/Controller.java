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
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class Controller implements Initializable {


    ObservableList list= FXCollections.observableArrayList();
    Image image;
    //BufferedImage image_dx, image_sx;
    int dxstart_x, dxstart_y, sxstart_x, sxstart_y;
    //int section_size = 200;
    int section_size;

    //Cross Color
    int cross_red = 0;
    int cross_green = 255;
    int cross_blue = 0;
    double cross_alpha = 0.5;

    @FXML
    ChoiceBox stage_dx, stage_sx, race, sex;
    @FXML
    TextField subject, age;
    @FXML
    Text flm, section_size_text;
    @FXML
    ImageView imageview_dx, imageview_sx, image_preview;
    @FXML
    Slider dx_widthbar, dx_heightbar, sx_widthbar, sx_heightbar;

    private int get_section_size(String path){
        try{

            BufferedReader brTest = new BufferedReader(new FileReader(path));
            String text = brTest.readLine();
            // Stop. text is the first line.
            //System.out.println(text);
            String[] tokens = text.split(" ");
            //System.out.println(Arrays.toString(strArray));

            if(tokens[0].equals("section_size") && tokens[1].equals("=")){

                String number = tokens[2];
                number = number.replaceAll("[^0-9.]", "");

                return Integer.parseInt(number);

            }else{
                return -1;
            }

        }catch (IOException ex){
            return -1;
        }

    }

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

        this.section_size = get_section_size("settings.cfg");

        this.section_size_text.setText("Section size: " + section_size);

        if(this.section_size == -1){
            this.section_size = 200;
        }


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

            image_preview.setImage(image);

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

    public void handleDragOver(DragEvent dragEvent) {

        if(dragEvent.getDragboard().hasFiles()){
            dragEvent.acceptTransferModes(TransferMode.ANY);
        }

    }

    public void handleDrop(DragEvent dragEvent) {

        List<File> files = dragEvent.getDragboard().getFiles();

        if(files != null){
            File file = files.get(0);

            flm.setText("File caricato: " + file.getPath());

            image = new Image(file.toURI().toString());

            image_preview.setImage(image);

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


                    //check
                    File theDir = new File("./saved_files");
                    if (!theDir.exists()){
                        theDir.mkdirs();
                    }


                    List<String[]> dataLines = new ArrayList<>();
                    dataLines.add(new String[] { subject, stage_dx, stage_sx, race, sex, age });

                    csvm.save_to_csv(("./saved_files/soggetto_" + subject + ".csv"), dataLines);

                    //Immagine dx

                    BufferedImage bdxi = img_to_buffered(image).getSubimage(dxstart_x,dxstart_y, section_size, section_size);
                    File imagedxfile = new File("./saved_files/soggetto_" + subject + "_dx.bmp");
                    ImageIO.write(bdxi, "bmp", imagedxfile);

                    //Immagine sx

                    BufferedImage bsxi = img_to_buffered(image).getSubimage(sxstart_x,sxstart_y, section_size, section_size);
                    File imagesxfile = new File("./saved_files/soggetto_" + subject + "_sx.bmp");
                    ImageIO.write(bsxi, "bmp", imagesxfile);


                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Info");
                    alert.setHeaderText(null);
                    alert.setContentText("I file "+ "soggetto_" + subject + "_dx.bmp, " + "soggetto_" + subject + "_sx.bmp e "
                            + "soggetto_" + subject + ".csv" + " sono stati salvati con successo in saved_files.");
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
        bi = add_cross_to_img(bi);

        Image img = SwingFXUtils.toFXImage(bi, null);
        imageview_dx.setImage(img);

    }

    private void set_imagesx(){

        BufferedImage bi = img_to_buffered(image);
        bi = bi.getSubimage(sxstart_x,sxstart_y, section_size, section_size);
        bi = add_cross_to_img(bi);

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

    private BufferedImage add_cross_to_img(BufferedImage image){

        BufferedImage crossed_image = image;

        //height line

        for(int i=(section_size/2)-1; i <= (section_size/2); i++){

            for(int j = 0; j < section_size; j++){

                int r = image.getRGB(i,j) & 0xff;
                int g = (image.getRGB(i,j) >> 8) & 0xff;
                int b = (image.getRGB(i,j) >> 16) & 0xff;

//                System.out.println(r);
//                System.out.println(g);
//                System.out.println(b);



                int rgb = ( (int) ( ( (1.0 - this.cross_alpha) * r ) + (this.cross_alpha * this.cross_red) ) << 16)  +
                        ( (int) ( ( (1.0 - this.cross_alpha) * g ) + (this.cross_alpha * this.cross_green) ) << 8 ) +
                        (int) ( ( (1.0 - this.cross_alpha) * b ) + (this.cross_alpha * this.cross_blue) );
                crossed_image.setRGB( i, j, rgb);

            }

        }

        for(int j=(section_size/2)-1; j <= (section_size/2); j++){

            for(int i = 0; i < section_size; i++){

                if(i != (section_size/2)-1 && i != (section_size/2) ){

                    int r = image.getRGB(i,j) & 0xff;
                    int g = (image.getRGB(i,j) >> 8) & 0xff;
                    int b = (image.getRGB(i,j) >> 16) & 0xff;

                    int rgb = ( (int) ( ( (1.0 - this.cross_alpha) * r ) + (this.cross_alpha * this.cross_red) ) << 16)  +
                            ( (int) ( ( (1.0 - this.cross_alpha) * g ) + (this.cross_alpha * this.cross_green) ) << 8 ) +
                            (int) ( ( (1.0 - this.cross_alpha) * b ) + (this.cross_alpha * this.cross_blue) );
                    crossed_image.setRGB( i, j, rgb);

//                    int rgb = (this.cross_red << 16) + (this.cross_green << 8) + this.cross_blue;
//                    crossed_image.setRGB( i, j, rgb);
                }

            }

        }

        return crossed_image;

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
