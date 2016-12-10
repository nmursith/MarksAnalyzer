package Marks;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
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
    protected Hashtable<String, SeriesMarks> table;
    protected ArrayList<SeriesMarks> batchMarks;
    protected ArrayList<String> name;
    private  Stage stage;
    public void init() {
        table = new Hashtable<>();
        batchMarks =new ArrayList<>();
        name =new ArrayList<>();
        readFile(new File("marks.xlsx"));

        batch_student.getItems().addAll(name);


        batch_student.getSelectionModel().select(0);

        //selectFile();
        process(0);

    }



    public void process(int id){
        final  String phy = "Physics";
        final  String chem = "Chemistry";
        SeriesMarks seriesMarks = batchMarks.get(id);//table.get(key);
        float marksPhy [] = new float[seriesMarks.getSeriesPhy().size()];
        float marksChem [] = new float[seriesMarks.getSeriesChem().size()];
        int i=0;
     //   System.out.println("Test: "+ marksPhy.length);
        for(String obj : seriesMarks.getSeriesPhy()){
       //     System.out.println(obj);
            try {

                String marks = obj;

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

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis(0,100,1);
        final BarChart<String,Number> bc =
                new BarChart<String,Number>(xAxis,yAxis);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(100);

        bc.setTitle("Overall Summary");
        xAxis.setLabel("Subject");
        yAxis.setLabel("Marks");


        for(int j=0; j<marksChem.length; j++){
            XYChart.Series series = new XYChart.Series();
            series.setName("Test "+ (j+1));
            series.getData().add(new XYChart.Data(phy, marksPhy[j]));
            series.getData().add(new XYChart.Data(chem, marksChem[j]));
            bc.getData().add(series);
        }

        try {
            individual_overall.getChildren().remove(0);

        }
       catch (IndexOutOfBoundsException | NullPointerException e){

       }
        individual_overall.addRow(0,bc);

        for (final XYChart.Series<String, Number> series : bc.getData()) {
            for (final XYChart.Data<String, Number> data : series.getData()) {

                displayLabelForData(data);
                Tooltip tooltip = new Tooltip();
                tooltip.setText(data.getYValue().toString());
                Tooltip.install(data.getNode(), tooltip);

            }
        }
/*************************/
        final CategoryAxis xAxisPhy = new CategoryAxis(); //1, 12, 1
        final NumberAxis yAxisPhy = new NumberAxis(0,100,1);

        final StackedAreaChart<String,Number> stackedAreaChartPhysic = new StackedAreaChart<>(xAxisPhy,yAxisPhy);
        XYChart.Series series3 = new XYChart.Series();
        series3.setName("Physics");

        for(int j=0; j<marksPhy.length; j++) {
            series3.getData().add(new XYChart.Data("Test " +(j+1), marksPhy[j]));

        }
        stackedAreaChartPhysic.setTitle("Stacked Area Chart Physics");
        stackedAreaChartPhysic.getData().addAll(series3);



        final CategoryAxis xAxisChem = new CategoryAxis();
        final NumberAxis yAxisChem = new NumberAxis(0,100,1);

        final StackedAreaChart<String,Number>  stackedAreaChartChem = new StackedAreaChart<>(xAxisChem,yAxisChem);
        XYChart.Series series4 = new XYChart.Series();
        series4.setName("Chemistry");

        for(int j=0; j<marksPhy.length; j++) {
            series4.getData().add(new XYChart.Data("Test " +(j+1), marksChem[j]));

        }
       // series4.getNode().lookup(".chart-series-stacked-area").setStyle("-fx-stroke-width: 1px; -fx-stroke: blue;");
        stackedAreaChartChem.setTitle("Stacked Area Chart Chemistry");
        stackedAreaChartChem.getData().addAll(series4);


        individual_seperate.getChildren().clear();
        individual_seperate.addRow(0, stackedAreaChartPhysic);
        individual_seperate.addRow(1, stackedAreaChartChem);

//        individual_seperate.getChildren().add(1, bc);

    }
    private void displayLabelForData(XYChart.Data<String, Number> data) {
        final Node node = data.getNode();
        final Text dataText = new Text(data.getYValue() + "");
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

                if(id<2){
                    id++;
                    continue;
                }

                int numberOfCells = row.getPhysicalNumberOfCells();
                int iterate = (numberOfCells - 2)/2;

               // System.out.println("**** "+numberOfCells);
                SeriesMarks seriesMarks = new SeriesMarks();
                seriesMarks.setIndexNo((String) getValue(row.getCell(0)));
                seriesMarks.setName((String) getValue(row.getCell(1)));

                for(int i=0; i<iterate; i++){
                    seriesMarks.addSeriesChem(getValue(row.getCell(2 + 2*i)));
                    seriesMarks.addSeriesPhy(getValue(row.getCell(3 + 2*i)));
                }

/*
                seriesMarks.addSeriesChem(getValue(row.getCell(2)));
                seriesMarks.addSeriesChem(getValue(row.getCell(4)));

                seriesMarks.addSeriesPhy(getValue(row.getCell(3)));
                seriesMarks.addSeriesPhy(getValue(row.getCell(5)));
*/
                name.add(getValue(row.getCell(1)));
                batchMarks.add(seriesMarks);
                table.put((String) getValue(row.getCell(0)), seriesMarks);


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



        }
        catch (Exception e){

        }


    }

    public void save(int index){
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
}
