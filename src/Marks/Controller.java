package Marks;

import com.sun.xml.internal.bind.v2.model.core.ID;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Image;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

public class Controller {
    public GridPane individual_seperate;
    public GridPane individual_overall;
    public ComboBox batch_student;
    public Label identity;
    public Label subject_attendance1;
    public Label attendance;
    public Label visonLogo;
    public Label subject_attendance2;
    protected Hashtable<String, SeriesMarks> table;
    protected Hashtable<Integer, String> IDMap;
    protected ArrayList<SeriesMarks> batchMarks;
    protected ArrayList<String> name;
    private  Stage stage;
    public void init() {
        javafx.scene.image.Image image = new javafx.scene.image.Image(getClass().getResourceAsStream("appIcon.jpg"));
        ImageView imv = new ImageView(image);
        imv.setFitWidth(90);
        imv.setFitHeight(90);
        visonLogo.setGraphic(imv);

        table = new Hashtable<>();
        IDMap = new Hashtable<>();
        batchMarks =new ArrayList<>();
        name =new ArrayList<>();
        readFile(new File("marks.xlsx"));
//        readAttendanceFile(new File("attendance.xlsx"));

        batch_student.getItems().addAll(name);


        batch_student.getSelectionModel().select(0);

        //selectFile();
        //process(0);

    }



    public synchronized void process(int id){
        final  String phy = "Physics";
        final  String chem = "Chemistry";
        final  String math = "C.Maths";
        final  String bio = "Biology";

        SeriesMarks seriesMarks = table.get(IDMap.get(id));

        float marksPhy [] = new float[seriesMarks.getSeriesPhy().size()];
        float marksChem [] = new float[seriesMarks.getSeriesChem().size()];
        float marksMath [] = new float[seriesMarks.getSeriesMath().size()];
        float marksBio [] = new float[seriesMarks.getSeriesBio().size()];

        int i=0;
     //   System.out.println("Test: "+ marksPhy.length);
        for(String obj : seriesMarks.getSeriesPhy()){
       //     System.out.println(obj);
            try {

                String marks = obj;
                System.out.println(marks);
                float temp = Float.parseFloat(marks);
                marksPhy[i] = temp;
            }
            catch (NullPointerException | NumberFormatException e){
                marksPhy[i] = 0;
            }

            i++;

        }

        i=0;
        for(Object obj : seriesMarks.getSeriesChem()){

            try {
                String marks = (String)obj;

                float temp = Float.parseFloat(marks);
                marksChem[i] = temp;
            }
            catch (NullPointerException | NumberFormatException e){
                marksChem[i] = 0;
            }
            i++;

        }

    i=0;
    if(seriesMarks.isMaths) {
        for (Object obj : seriesMarks.getSeriesMath()) {

            try {
                String marks = (String) obj;

                float temp = Float.parseFloat(marks);
                marksMath[i] = temp;
            } catch (NullPointerException | NumberFormatException e) {
                marksMath[i] = 0;
            }
            i++;

        }
    }
        else {

        for (Object obj : seriesMarks.getSeriesBio()) {

            try {
                String marks = (String) obj;

                float temp = Float.parseFloat(marks);
                    marksBio[i] = temp;
            } catch (NullPointerException | NumberFormatException e) {
                marksBio[i] = 0;
            }
            i++;

        }
    }

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis(0,100,1);
        final BarChart<String,Number> bc =
                new BarChart<String,Number>(xAxis,yAxis);

        yAxis.setLowerBound(0);
        yAxis.setUpperBound(100);

        String  subject = seriesMarks.isMaths()?"C.Maths":"Biology";
        float [] subject_M_B = seriesMarks.isMaths? marksMath:marksBio;

        bc.setTitle("Marks in "+ subject);
        xAxis.setLabel(subject);
        yAxis.setLabel("Marks");

        xAxis.setTickLabelFont(new Font("System", 16));

        for(int j=0; j<subject_M_B.length; j++){
            XYChart.Series series = new XYChart.Series();
            String name ="Test " +(j+1);
            series.setName(name);
            XYChart.Data<String, Number> data = new XYChart.Data(subject, subject_M_B[j]);
          //  XYChart.Data<String, Number> dataChem = new XYChart.Data(chem, marksChem[j]);


            data.nodeProperty().addListener(new ChangeListener<Node>() {
                    @Override public void changed(ObservableValue<? extends Node> ov, Node oldNode, final Node node) {
                        if (node != null) {

                            displayLabelForData(data);
                        }
                    }
                });



            series.getData().add(data);

            bc.getData().add(series);

            setMaxBarWidth(bc, xAxis, yAxis, 40, 10);
            bc.widthProperty().addListener((obs,b,b1)->{
                Platform.runLater(()->setMaxBarWidth(bc, xAxis, yAxis, 40, 10));
            });
        }


        try {
            individual_overall.getChildren().remove(0);

        }
       catch (IndexOutOfBoundsException | NullPointerException e){

       }
        individual_overall.addRow(0,bc);



        final CategoryAxis xAxisPhy = new CategoryAxis();
        final NumberAxis yAxisPhy = new NumberAxis(0,100,1);
        final BarChart<String,Number> bcPhy =
                new BarChart<String,Number>(xAxisPhy,yAxisPhy);

        yAxisPhy.setLowerBound(0);
        yAxisPhy.setUpperBound(100);

         subject = phy;
        subject_M_B = marksPhy;

        bcPhy.setTitle("Marks in "+ subject);
        xAxisPhy.setLabel(subject);
        yAxisPhy.setLabel("Marks");

        xAxisPhy.setTickLabelFont(new Font("System", 16));

        for(int j=0; j<subject_M_B.length; j++){
            XYChart.Series series = new XYChart.Series();
            String name ="Test " +(j+1);
            series.setName(name);
            XYChart.Data<String, Number> data = new XYChart.Data(subject, subject_M_B[j]);
            data.nodeProperty().addListener(new ChangeListener<Node>() {
                @Override public void changed(ObservableValue<? extends Node> ov, Node oldNode, final Node node) {
                    if (node != null) {

                        displayLabelForData(data);
                    }
                }
            });

            series.getData().add(data);

            bcPhy.getData().add(series);

            setMaxBarWidth(bcPhy, xAxisPhy, yAxisPhy, 40, 10);
            bcPhy.widthProperty().addListener((obs,b,b1)->{
                Platform.runLater(()->setMaxBarWidth(bcPhy, xAxisPhy, yAxisPhy, 40, 10));
            });
        }







        final CategoryAxis xAxisChem = new CategoryAxis();
        final NumberAxis yAxisChem = new NumberAxis(0,100,1);
        final BarChart<String,Number> bcChem =
                new BarChart<String,Number>(xAxisChem,yAxisChem);

        yAxisChem.setLowerBound(0);
        yAxisChem.setUpperBound(100);

        subject = chem;
        subject_M_B = marksChem;

        bcChem.setTitle("Marks in "+ subject);
        xAxisChem.setLabel(subject);
        yAxisChem.setLabel("Marks");

        xAxisChem.setTickLabelFont(new Font("System", 16));

        for(int j=0; j<subject_M_B.length; j++){
            XYChart.Series series = new XYChart.Series();
            String name ="Test " +(j+1);
            series.setName(name);
            XYChart.Data<String, Number> data = new XYChart.Data(subject, subject_M_B[j]);
            data.nodeProperty().addListener(new ChangeListener<Node>() {
                @Override public void changed(ObservableValue<? extends Node> ov, Node oldNode, final Node node) {
                    if (node != null) {

                        displayLabelForData(data);
                    }
                }
            });

            series.getData().add(data);

            bcChem.getData().add(series);
            bcChem.widthProperty().addListener((obs,b,b1)->{
                Platform.runLater(()->setMaxBarWidth(bcChem, xAxisChem, yAxisChem, 40, 10));
            });



        }












        individual_seperate.getChildren().clear();
        individual_seperate.addRow(0, bcPhy);
        individual_seperate.addRow(1, bcChem);

        System.out.println(seriesMarks.getName());
        identity.setText("  "+seriesMarks.getIndexNo() + " :- " + seriesMarks.getName() +"  ");
        //identity.setText(seriesMarks.getIndexNo());
        identity.setAlignment(Pos.CENTER);
        String  subjectB = "Biology: " +  seriesMarks.bioAttendance[0] +"/" +seriesMarks.bioAttendance[1];
        String  subjectM = "C.Maths: " +  seriesMarks.mathsAttendance[0] +"/" +seriesMarks.mathsAttendance[1];

        String prefix = seriesMarks.isMaths()?subjectM:subjectB;
        subject_attendance1.setText(prefix);
        subject_attendance2.setText("Physics : "+  seriesMarks.physicsAttendance[0] +"/" +seriesMarks.physicsAttendance[1]   +"\t" + "Chemistry: " +

                seriesMarks.chemistryAttendance[0] +"/" +seriesMarks.chemistryAttendance[1]);
//        individual_seperate.getChildren().add(1, bc);

    }
    private void displayLabelForData(XYChart.Data<String, Number> data) {
        final Node node = data.getNode();
        String text = data.getYValue().intValue() +"";
        if(data.getYValue().doubleValue() == 0.0){
            text = "absent";
        }
        final Text dataText = new Text(text + "");

        node.parentProperty().addListener(new ChangeListener<Parent>() {
            @Override public void changed(ObservableValue<? extends Parent> ov, Parent oldParent, Parent parent) {
                Group parentGroup = (Group) parent;
                parentGroup.getChildren().add(dataText);
            }
        });

        node.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {
            @Override public void changed(ObservableValue<? extends Bounds> ov, Bounds oldBounds, Bounds bounds) {
                dataText.setLayoutX(
                        Math.round(
                                bounds.getMinX() + bounds.getWidth() / 2 - dataText.prefWidth(-1) / 2
                        )
                );
                dataText.setLayoutY(
                        Math.round(
                                bounds.getMinY() - dataText.prefHeight(-1) * 0.5
                        )
                );
            }
        });
    }

    public void selectFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Marks File");

        fileChooser.setInitialDirectory(new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(

                new FileChooser.ExtensionFilter("Excel", "*.xlsx")
        );
        File file = fileChooser.showOpenDialog(new Stage());

        System.out.println(file);
        readFile(file);

        try {
            batch_student.getItems().clear();
        }
        catch (Exception e){

        }

        batch_student.getItems().addAll(name);


        batch_student.getSelectionModel().select(0);

        //selectFile();
        process(0);

    }

    public void readFile(File file)  {
        FileInputStream fis =null;
        try {
            fis = new FileInputStream(file); //new File("marks.xlsx")
            XSSFWorkbook workbook = new XSSFWorkbook(fis);


for(int sheet=0; sheet<4; sheet++) {


    XSSFSheet spreadsheet = workbook.getSheetAt(sheet);
        Iterator<Row> rowIterator = spreadsheet.iterator();
    XSSFRow row;
    int id = 0;
    while (rowIterator.hasNext()) {


        row = (XSSFRow) rowIterator.next();
        Iterator<Cell> cellIterator = row.cellIterator();

        if (id < 1) {
            id++;
            continue;
        }

        int numberOfCells = row.getPhysicalNumberOfCells();
        int iterate = (numberOfCells - 4);

        // System.out.println("**** "+numberOfCells);
        //
        SeriesMarks seriesMarks = table.get(getValue(row.getCell(0)));
        if(seriesMarks ==null){
            seriesMarks = new SeriesMarks();
            seriesMarks.setIndexNo((String) getValue(row.getCell(0)));
            seriesMarks.setName((String) getValue(row.getCell(1)));
            name.add(seriesMarks.getName());

            int ID = name.size()-1;
            IDMap.put(ID, seriesMarks.getIndexNo());
            table.put(seriesMarks.getIndexNo(), seriesMarks);


        }


        for (int i = 0; i < iterate; i++) {
            switch (sheet){
                case 0:
                    seriesMarks.addSeriesMath(getValue(row.getCell( 4+  i)));
                    seriesMarks.mathsAttendance[0] =(int) Float.parseFloat(getValue(row.getCell(2)));
                    seriesMarks.mathsAttendance[1] = (int) Float.parseFloat(getValue(row.getCell(3)));
                    seriesMarks.setMaths(true);
                    break;
                case 1:
                    seriesMarks.addSeriesBio(getValue(row.getCell(4+  i)));
                    seriesMarks.bioAttendance[0] =(int) Float.parseFloat(getValue(row.getCell(2)));
                    seriesMarks.bioAttendance[1] = (int) Float.parseFloat(getValue(row.getCell(3)));
                    seriesMarks.setMaths(false);
                    break;
                case 2:

                    seriesMarks.addSeriesChem(getValue(row.getCell(4+  i)));
                    seriesMarks.chemistryAttendance[0] = (int) Float.parseFloat(getValue(row.getCell(2)));
                    seriesMarks.chemistryAttendance[1] = (int) Float.parseFloat(getValue(row.getCell(3)));
                    break;
                case 3:
                    seriesMarks.addSeriesPhy(getValue(row.getCell(4+  i)));
                    seriesMarks.physicsAttendance[0] = (int) Float.parseFloat(getValue(row.getCell(2)));
                    seriesMarks.physicsAttendance[1] = (int) Float.parseFloat(getValue(row.getCell(3)));
                    break;
            }



        }
//               seriesMarks.addSeriesChem(getValue(row.getCell(2)));
//                seriesMarks.addSeriesChem(getValue(row.getCell(4)));
//
//                seriesMarks.addSeriesPhy(getValue(row.getCell(3)));
//                seriesMarks.addSeriesPhy(getValue(row.getCell(5)));


        //batchMarks.add(seriesMarks);






//

        while (cellIterator.hasNext()) {


            Cell cell = cellIterator.next();


            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC:
                    System.out.print(cell.getNumericCellValue() + " \t\t ");
                    break;
                case Cell.CELL_TYPE_STRING:
                    System.out.print(cell.getStringCellValue() + " \t\t ");
                    break;
            }
        }
        System.out.println();
    }

}

            //fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public void readAttendanceFile(File file)  {

        try {
            FileInputStream fis = new FileInputStream(file); //new File("marks.xlsx")
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet spreadsheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = spreadsheet.iterator();
            XSSFRow row;
            int id =0;
            while (rowIterator.hasNext())
            {

                row = (XSSFRow) rowIterator.next();
                Iterator <Cell> cellIterator = row.cellIterator();

                if(id<1){
                    id++;
                    continue;
                }

                try {
                    SeriesMarks series = table.get(getValue(row.getCell(0)));

                   /* series.chemistryAttendance[0] = subString(getValue(row.getCell(2)));
                    series.chemistryAttendance[1] = subString(getValue(row.getCell(3)));

                    series.physicsAttendance[0] = subString(getValue(row.getCell(4)));
                    series.physicsAttendance[1] = subString(getValue(row.getCell(5)));*/
                }
                catch (Exception e){
                  //  e.printStackTrace();
                }



/*
                seriesMarks.addSeriesChem(getValue(row.getCell(2)));
                seriesMarks.addSeriesChem(getValue(row.getCell(4)));

                seriesMarks.addSeriesPhy(getValue(row.getCell(3)));
                seriesMarks.addSeriesPhy(getValue(row.getCell(5)));
*/



                while ( cellIterator.hasNext())
                {


                    Cell cell = cellIterator.next();


                    switch (cell.getCellType())
                    {
                        case Cell.CELL_TYPE_NUMERIC:
                            System.out.print(  cell.getNumericCellValue() + " \t\t " );
                            break;
                        case Cell.CELL_TYPE_STRING:
                            System.out.print( cell.getStringCellValue() + " \t\t " );
                            break;
                    }
                }
                System.out.println();
            }
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public  String subString(String temp){

        return  temp.substring(0, temp.indexOf("."));
    }
    public String getValue(Cell cell){

        String object = null;
        switch (cell.getCellType())
        {
            case Cell.CELL_TYPE_NUMERIC:

                object = cell.getNumericCellValue() +"";
                break;
            case Cell.CELL_TYPE_STRING:

                object =  cell.getStringCellValue();
                break;
        }

        return object;
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public synchronized  void showMarks(ActionEvent actionEvent) {
            int index = batch_student.getSelectionModel().getSelectedIndex();
        try {
            process(index);
           // save(index);



        }
        catch (Exception e){
            e.printStackTrace();
        }


    }

    public synchronized  void  save(int index){
        process(index);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        batch_student.getSelectionModel().select(index);
        System.out.println("running  " + index);
        //XSLFSlide slide = ppt.createSlide();


        Platform.runLater(() -> {
            try {
                BufferedImage screencapture = null;
                screencapture = new Robot().createScreenCapture(new Rectangle(2, 85, 1365, 620));
                //File temp = new File(batchMarks.get(index).getIndexNo() + ".png");
                File temp = new File(index + ".png");
                ImageIO.write(screencapture, "png", temp);
            } catch (AWTException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }


        });
    }

    public void createSlide(ActionEvent actionEvent) {

/*
X: 2.0 Y: 68.0
X: 1365.0 Y: 657.0
 */
   try {
       //creating an FileOutputStream object
    //   XMLSlideShow ppt = new XMLSlideShow();

       //creating a slide in it
      // XSLFSlide slide = ppt.createSlide();
    //   File file=new File("addingimage.pptx");
     //  FileOutputStream out = new FileOutputStream(file);

       //saving the changes to a file

       for ( int id=0; id< batchMarks.size(); id++) {

           try {
               Thread.sleep(100);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
           final int index = id;
           Task task = new Task<Object>() {

               @Override
               protected Object call() throws Exception {
                   process(index);
                   batch_student.getSelectionModel().select(index);
                   return null;
               }
           };
            Platform.runLater(task);
/*           Platform.runLater(() -> {
                process(index);
                   batch_student.getSelectionModel().select(index);
               System.out.println("running");
               //XSLFSlide slide = ppt.createSlide();
               BufferedImage screencapture = null;
               try {
                   screencapture = new Robot().createScreenCapture(new Rectangle(2, 92, 1365, 620));
                   File temp = new File(batchMarks.get(index).getIndexNo() + ".png");
                   ImageIO.write(screencapture, "png", temp);
               } catch (AWTException e) {
                   e.printStackTrace();
               } catch (IOException e) {
                   e.printStackTrace();
               }
               //byte[] picture = ((DataBufferByte) screencapture.getData().getDataBuffer()).getData();

           });*/
       }

           //PictureData idx = ppt.addPicture(picture, XSLFPictureData.PictureType.PNG);


           //XSLFPictureShape pic = slide.createPicture(idx);
          // ppt.write(out);
      // }


       //reading an image
/*       File file1 = new File("caputre.jpg");
       ImageIO.write(screencapture, "jpg", file1);
       File image=new File("appIcon.jpg");*/


       //converting it into a byte array


       //adding the image to the presentation


       //creating a slide with given picture on it


       //creating a file object

       System.out.println("image added successfully");
     //  out.close();
   }
   catch (Exception e){

   }

        //saving the changes to a file

    }


    private void setMaxBarWidth(BarChart<String,Number> bc,CategoryAxis xAxis ,NumberAxis yAxis, double maxBarWidth, double minCategoryGap){
        double barWidth=0;
        do{
            double catSpace = xAxis.getCategorySpacing();
            double avilableBarSpace = catSpace - (bc.getCategoryGap() + bc.getBarGap());
            barWidth = (avilableBarSpace / bc.getData().size()) - bc.getBarGap();
            if (barWidth >maxBarWidth){
                avilableBarSpace=(maxBarWidth + bc.getBarGap())* bc.getData().size();
                bc.setCategoryGap(catSpace-avilableBarSpace-bc.getBarGap());
            }
        } while(barWidth>maxBarWidth);

        do{
            double catSpace = xAxis.getCategorySpacing();
            double avilableBarSpace = catSpace - (minCategoryGap + bc.getBarGap());
            barWidth = Math.min(maxBarWidth, (avilableBarSpace / bc.getData().size()) - bc.getBarGap());
            avilableBarSpace=(barWidth + bc.getBarGap())* bc.getData().size();
            bc.setCategoryGap(catSpace-avilableBarSpace-bc.getBarGap());
        } while(barWidth < maxBarWidth && bc.getCategoryGap()>minCategoryGap);
    }
}
