package te;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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


    ObservableList<String> list= FXCollections.observableArrayList();

    //Images variables
    Image image;
    int dxstart_x, dxstart_y, sxstart_x, sxstart_y;
    int section_size;
    String image_path;

    //bar percentage
    Double dx_x_bar_per, dx_y_bar_per;

    //Cross Color
    int cross_red = 0;
    int cross_green = 255;
    int cross_blue = 0;
    double cross_alpha = 0.5;

    //UI Eleements
    @FXML
    ChoiceBox<String> demirijian_stage_dx, demirijian_stage_sx, moorrees_stage_dx, moorrees_stage_sx, race, sex;
    @FXML
    TextField subject, subject_years, subject_months;
    @FXML
    Text flm, section_size_text;
    @FXML
    ImageView imageview_dx, imageview_sx, image_preview;
    @FXML
    Slider dx_widthbar, dx_heightbar, sx_widthbar, sx_heightbar;
    @FXML
    CheckBox dx_np, sx_np;
    @FXML
    DatePicker birth_date, opg_date;

    //===Inizialization===

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

    private Double get_dx_x_bar_per(String path){
        try{

            BufferedReader brTest = new BufferedReader(new FileReader(path));
            String text = brTest.readLine();
            text = brTest.readLine();
            // Two times. text is the second line.
            //System.out.println(text);
            String[] tokens = text.split(" ");
            //System.out.println(Arrays.toString(strArray));

            if(tokens[0].equals("initial_bar_dx_x_percentage") && tokens[1].equals("=")){

                String number = tokens[2];
                number = number.replaceAll("[^0-9.]", "");

                return Double.parseDouble(number);

            }else{
                return -1.0;
            }

        }catch (IOException ex){
            return -1.0;
        }
    }

    private Double get_dx_y_bar_per(String path){
        try{

            BufferedReader brTest = new BufferedReader(new FileReader(path));
            String text = brTest.readLine();
            text = brTest.readLine();
            text = brTest.readLine();
            // Three times. text is the third line.
            //System.out.println(text);
            String[] tokens = text.split(" ");
            //System.out.println(Arrays.toString(strArray));

            if(tokens[0].equals("initial_bar_dx_y_percentage") && tokens[1].equals("=")){

                String number = tokens[2];
                number = number.replaceAll("[^0-9.]", "");

                return Double.parseDouble(number);

            }else{
                return -1.0;
            }

        }catch (IOException ex){
            return -1.0;
        }
    }

    private void set_stages(){

        //demirijian
        list.removeAll(list);
        list.addAll("sconosciuto", "A", "B", "C", "D", "E", "F", "G", "H");
        demirijian_stage_dx.setValue("sconosciuto");
        demirijian_stage_dx.getItems().addAll(list);
        demirijian_stage_sx.setValue("sconosciuto");
        demirijian_stage_sx.getItems().addAll(list);


        //moorrees
        list.removeAll(list);
        list.addAll("sconosciuto", "Ci", "Cco", "Coc", "Cr 1/2", "Cr 3/4", "Crc", "Ri", "R 1/4", "R 1/2",
                "R 3/4", "Rc", "A 1/2", "Ac");
        moorrees_stage_dx.setValue("sconosciuto");
        moorrees_stage_dx.getItems().addAll(list);
        moorrees_stage_sx.setValue("sconosciuto");
        moorrees_stage_sx.getItems().addAll(list);


    }

    private void set_race() {
        try {
            BufferedReader brTest = new BufferedReader(new FileReader("race_list.cfg"));
            String text = brTest.readLine();
            text = text.replaceAll(" ", "");
            String[] tokens = text.split(",");

            int fal = 1;        //determines length of firstArray
            int sal = tokens.length;   //determines length of secondArray
            String[] result = new String[fal + sal];  //resultant array of size first array and second array
            String[] sa = {"sconosciuta"};
            System.arraycopy(sa, 0, result, 0, fal);
            System.arraycopy(tokens, 0, result, fal, sal);

            ObservableList<String> ol = FXCollections.observableArrayList(result);
            race.setValue("sconosciuta");
            race.getItems().addAll(ol);


        }catch (IOException ex) {
            list.removeAll(list);
            list.addAll("sconosciuta", "caucasoide", "mongoloide", "amerindioide", "negroide", "australoide");
            race.setValue("sconosciuta");
            race.getItems().addAll(list);
        }
    }

    private void set_sex(){
        try {
            BufferedReader brTest = new BufferedReader(new FileReader("sex_list.cfg"));
            String text = brTest.readLine();
            text = text.replaceAll(" ", "");
            String[] tokens = text.split(",");

            int fal = 1;        //determines length of firstArray
            int sal = tokens.length;   //determines length of secondArray
            String[] result = new String[fal + sal];  //resultant array of size first array and second array
            String[] sa = {"sconosciuto"};
            System.arraycopy(sa, 0, result, 0, fal);
            System.arraycopy(tokens, 0, result, fal, sal);

            list.removeAll(list);
            ObservableList<String> ol = FXCollections.observableArrayList(result);
            list.addAll(ol);
            sex.setValue("sconosciuto");
            sex.getItems().addAll(list);


        }catch (IOException ex){
            list.removeAll(list);
            list.addAll("sconosciuto", "maschio", "femmina");
            sex.setValue("sconosciuto");
            sex.getItems().addAll(list);
        }



    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        set_stages();
        set_sex();
        set_race();

        this.section_size = get_section_size("settings.cfg");
        if(this.section_size == -1){ this.section_size = 200; }
        this.section_size_text.setText("Section size: " + section_size);

        this.dx_x_bar_per = get_dx_x_bar_per("settings.cfg");
        if(this.dx_x_bar_per == -1.0){ this.dx_x_bar_per = 0.15; }

        this.dx_y_bar_per = get_dx_y_bar_per("settings.cfg");
        if(this.dx_y_bar_per == -1.0){ this.dx_y_bar_per = 0.66; }

    }

//----------------------------------------------------------------------



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

            flm.setText("File caricato: " + file.getPath());

            image = new Image(file.toURI().toString());

            this.image_path = file.getPath();

            image_preview.setImage(image);

            dxstart_x = (int) ((image.getWidth() - section_size) * this.dx_x_bar_per);
            dxstart_y = (int) ((image.getHeight() - section_size) * this.dx_y_bar_per);

            set_dx_widthbar();
            set_dx_heightbar();
            set_imagedx();

            sxstart_x = (int) ((image.getWidth() - section_size) * (1.0 - this.dx_x_bar_per) );
            sxstart_y = dxstart_y;

            set_sx_widthbar();
            set_sx_heightbar();
            set_imagesx();
        }

    }



    //DRAG N DROP
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

            this.image_path = file.getPath();

            image_preview.setImage(image);

            dxstart_x = (int) ((image.getWidth() - section_size) * this.dx_x_bar_per);
            dxstart_y = (int) ((image.getHeight() - section_size) * this.dx_y_bar_per);

            set_dx_widthbar();
            set_dx_heightbar();
            set_imagedx();

            sxstart_x = (int) ((image.getWidth() - section_size) * (1.0 - this.dx_x_bar_per) );
            sxstart_y = dxstart_y;

            set_sx_widthbar();
            set_sx_heightbar();
            set_imagesx();
        }

    }

    //------------------------------------------------------------------------------

    public void exit_program(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void save(ActionEvent actionEvent) throws IOException, InterruptedException {

        if(image != null){

            if( (isInteger(this.subject_years.getText()) || this.subject_years.getText().isEmpty()) &&
                    (isInteger(this.subject_months.getText()) || this.subject_months.getText().isEmpty())){

                if( ( this.subject_years.getText().isEmpty() || Integer.parseInt(this.subject_years.getText()) >= 0 ) &&
                        ( this.subject_months.getText().isEmpty() || (Integer.parseInt(this.subject_months.getText()) >= 0
                        && Integer.parseInt(this.subject_months.getText()) <= 11) ) ){

                    if(!this.subject.getText().isEmpty()){

                        //Istanziazione CSVManager
                        CSVManager csvm = new CSVManager();

                        //Variabili

                        String tpdx, tpsx, demi_stage_dx, demi_stage_sx, moorrees_stage_dx, moorrees_stage_sx, subject,
                                race, sex, subject_years, subject_months, birth_date, opg_date;

                        if(this.dx_np.isSelected()){
                            tpdx = "dente non presente";
                        }else{
                            tpdx = "dente presente";
                        }

                        if(this.sx_np.isSelected()){
                            tpsx = "dente non presente";
                        }else{
                            tpsx = "dente presente";
                        }

                        demi_stage_dx = this.demirijian_stage_dx.getValue().toString();
                        demi_stage_sx = this.demirijian_stage_sx.getValue().toString();
                        moorrees_stage_dx = this.moorrees_stage_dx.getValue().toString();
                        moorrees_stage_sx = this.moorrees_stage_sx.getValue().toString();

                        subject = this.subject.getText();
                        race = this.race.getValue().toString();
                        sex = this.sex.getValue().toString();

                        if(this.subject_years.getText().isEmpty()){
                            subject_years = "sconosciuti";
                        }else{
                            subject_years = this.subject_years.getText();
                        }

                        if(this.subject_months.getText().isEmpty()){
                            subject_months = "sconosciuti";
                        }else{
                            subject_months = this.subject_months.getText();
                        }

                        if(this.birth_date.getValue() == null){
                            birth_date = "sconosciuta";
                        }else{
                            birth_date = this.birth_date.getValue().toString();
                        }

                        if(this.opg_date.getValue() == null){
                            opg_date = "sconosciuta";
                        }else{
                            opg_date = this.opg_date.getValue().toString();
                        }








                        //check saved_files folder
                        File theDir = new File("./saved_files");
                        if (!theDir.exists()){
                            theDir.mkdirs();
                        }


                        List<String[]> dataLines = new ArrayList<>();
                        dataLines.add(new String[] { subject, tpdx, demi_stage_dx, moorrees_stage_dx,
                                tpsx, demi_stage_sx, moorrees_stage_sx, race, sex, subject_years, subject_months,
                                birth_date, opg_date,
                                //technical data
                                this.image_path,
                                Integer.toString((int) this.image.getWidth()),
                                Integer.toString((int) this.image.getHeight()),
                                Integer.toString(this.section_size),
                                Integer.toString(this.dxstart_x + (this.section_size / 2)),
                                Integer.toString(this.dxstart_y + (this.section_size / 2)),
                                Integer.toString(this.sxstart_x + (this.section_size / 2)),
                                Integer.toString(this.sxstart_y + (this.section_size / 2))});

                        csvm.save_to_csv(("./saved_files/soggetto_" + subject + ".csv"), dataLines);








                        //Immagine dx

                        BufferedImage bdxi = img_to_buffered(image).getSubimage(dxstart_x,dxstart_y, section_size, section_size);
                        File imagedxfile = new File("./saved_files/soggetto_" + subject + "_dx.bmp");
                        ImageIO.write(bdxi, "bmp", imagedxfile);

                        //Immagine sx

                        BufferedImage bsxi = img_to_buffered(image).getSubimage(sxstart_x,sxstart_y, section_size, section_size);
                        File imagesxfile = new File("./saved_files/soggetto_" + subject + "_sx.bmp");
                        ImageIO.write(bsxi, "bmp", imagesxfile);

                        //Box message
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
                    alert.setTitle("Errore: anni e mesi non corretti");
                    alert.setHeaderText(null);
                    alert.setContentText("I valori di anni e mesi non sono corretti: esempi accettabili per anni sono 0, 1, 2, 16, ecc., esempi accettabili per mesi sono da 0 a 11, o lasciare vuoto se sconosciuti.");
                    alert.showAndWait();
                }

            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Errore: anni e mesi non corretti");
                alert.setHeaderText(null);
                alert.setContentText("I valori di anni e mesi non sono corretti: esempi accettabili per anni sono 0, 1, 2, 16, ecc., esempi accettabili per mesi sono da 0 a 11, o lasciare vuoto se sconosciuti.");
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

    private boolean isInteger(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int i = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }


}
