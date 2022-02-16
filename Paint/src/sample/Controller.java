package sample;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Stack;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;
import javafx.stage.Window;


import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    public Rectangle1 rectangle;
    public Circle1 circle;
    @FXML
    private TextField toolSize;
    @FXML
    private TextField redText;
    @FXML
    private TextField greenText;
    @FXML
    private TextField blueText;
    @FXML
    private TextField opacityText;
    @FXML
    private Canvas drawingCanvas;
    @FXML
    private CheckBox eraser;
    @FXML
    private CheckBox pencil;
    @FXML
    private CheckBox Crectangle;
    @FXML
    private CheckBox Ccircle;
    @FXML
    private MenuItem undo1;
    @FXML
    private Slider RedSlider;
    @FXML
    private Slider GreenSlider;
    @FXML
    private Slider BlueSlider;
    @FXML
    private Slider opacity;
    @FXML
    private TextArea text;
    @FXML
    private CheckBox writeText;


    private Window primaryStage;
    private Stack<Image> undoStack;


    GraphicsContext gc;

    public void onSave() {

        //The way we implemented the onSave was bas checking the documentation provided by someone through StackOverFlow
        //https://stackoverflow.com/questions/69321301/javafx-save-canvas-to-png-file

        FileChooser fc = new FileChooser();

        //Setting the extension
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
        fc.getExtensionFilters().add(filter);

        //Show save file dialog
        File f = fc.showSaveDialog(primaryStage);

        if (f != null) {
            try {
                WritableImage wi = new WritableImage((Integer.parseInt(String.valueOf(drawingCanvas.getWidth()))), Integer.parseInt(String.valueOf(drawingCanvas.getHeight())));
                drawingCanvas.snapshot(null, wi);
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(wi, null);
                ImageIO.write(renderedImage, "png", f);
            } catch (IOException ex) {
                System.out.println("Error Saving File");
            }
        }
    }


    public void onLoad(){
        FileChooser load = new FileChooser();
        load.setTitle("Select a file");
        File f = load.showOpenDialog(primaryStage);

        if(f != null){
            try{
                InputStream is = new FileInputStream(f);
                Image image = new Image(is);
                gc.drawImage(image, 0,0);
            }catch (IOException e){
                System.out.println("Error Opening File" +e);
            }
        }
    }

    //This will allow the user to select the Exit menu item and will shutoff the application
    //We were inspired by a Youtube Video to add this functionality
    public void onExit() {
        Platform.exit();
    }

    //As the name suggests this will clear the canvas once the user selects Clear Canvas menu item
    public void clearCanvas() {
        gc.clearRect(0, 0, drawingCanvas.getWidth(), drawingCanvas.getHeight());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
    	RedSlider.setValue(0);
        redText.setText(Double.toString(0));

        GreenSlider.setValue(0);
        greenText.setText(Double.toString(0));

        BlueSlider.setValue(0);
        blueText.setText(Double.toString(0));

        opacity.setValue(0);
        opacityText.setText(Double.toString(0));
        
        redText.textProperty().bindBidirectional(RedSlider.valueProperty(), NumberFormat.getNumberInstance());
        greenText.textProperty().bindBidirectional(GreenSlider.valueProperty(), NumberFormat.getNumberInstance());
        blueText.textProperty().bindBidirectional(BlueSlider.valueProperty(), NumberFormat.getNumberInstance());
        opacityText.textProperty().bindBidirectional(opacity.valueProperty(), NumberFormat.getNumberInstance());
        undoStack = new Stack<>();
        gc = drawingCanvas.getGraphicsContext2D();
        drawingCanvas.setOnMousePressed(e ->{
            double size = Double.parseDouble(toolSize.getText());

            //Access x,y coord of the mouse
            double x = e.getX() - size/2;
            double y = e.getY() - size/2;
            if (eraser.isSelected()) {
                stackUndo();
                Ccircle.setSelected(false);
                pencil.setSelected(false);
                writeText.setSelected(false);
                gc.clearRect(x, y, size, size);

            }else if (Ccircle.isSelected()) {
                eraser.setSelected(false);
                pencil.setSelected(false);
                writeText.setSelected(false);
                Crectangle.setSelected(false);
                gc.setStroke(Color.rgb((int) RedSlider.getValue(), (int) GreenSlider.getValue(), (int) BlueSlider.getValue(), opacity.getValue()));
                gc.setFill(Color.rgb((int) RedSlider.getValue(), (int) GreenSlider.getValue(), (int) BlueSlider.getValue(), opacity.getValue()));

                circle = new Circle1();

                circle.setGraphicsContext(gc);
                circle.setColor(Color.rgb((int) RedSlider.getValue(), (int) GreenSlider.getValue(), (int) BlueSlider.getValue(), opacity.getValue()));
                circle.setFill(Color.rgb((int) RedSlider.getValue(), (int) GreenSlider.getValue(), (int) BlueSlider.getValue(), opacity.getValue()));
                circle.setCenterPoint(e.getX(), e.getY());
            }else if (pencil.isSelected() ) {
                stackUndo();
                Ccircle.setSelected(false);
                eraser.setSelected(false);
                writeText.setSelected(false);
                Crectangle.setSelected(false);
                if (!toolSize.getText().isEmpty())
                    gc.setFill(Color.rgb((int) RedSlider.getValue(), (int) GreenSlider.getValue(), (int) BlueSlider.getValue(), opacity.getValue()));
                gc.fillRoundRect(x, y, size, size, size, size);
            }else if (writeText.isSelected()){
                Ccircle.setSelected(false);
                eraser.setSelected(false);
                pencil.setSelected(false);
                Crectangle.setSelected(false);
                stackUndo();
                gc.setFill(Color.rgb((int) RedSlider.getValue(), (int) GreenSlider.getValue(), (int) BlueSlider.getValue(), opacity.getValue()));
                gc.setFont(Font.font(size+20));
                gc.fillText(text.getText(), e.getX(), e.getY());
            } else if (Crectangle.isSelected()) {
            	Ccircle.setSelected(false);
                eraser.setSelected(false);
                pencil.setSelected(false);
                writeText.setSelected(false);
                stackUndo();
            	gc.setStroke(Color.rgb((int) RedSlider.getValue(), (int) GreenSlider.getValue(), (int) BlueSlider.getValue(), opacity.getValue()));
                gc.setFill(Color.rgb((int) RedSlider.getValue(), (int) GreenSlider.getValue(), (int) BlueSlider.getValue(), opacity.getValue()));

                rectangle = new Rectangle1();

                rectangle.setGraphicsContext(gc);
                rectangle.setColor(Color.rgb((int) RedSlider.getValue(), (int) GreenSlider.getValue(), (int) BlueSlider.getValue(), opacity.getValue()));
                rectangle.setFill(Color.rgb((int) RedSlider.getValue(), (int) GreenSlider.getValue(), (int) BlueSlider.getValue(), opacity.getValue()));
                rectangle.setStartPoint(e.getX(), e.getY());
            }
        });
        drawingCanvas.setOnMouseDragged(e->{
            double size = Double.parseDouble(toolSize.getText());

            //Access x,y coord of the mouse
            double x = e.getX() - size/2;
            double y = e.getY() - size/2;


            if (eraser.isSelected()) {
                gc.clearRect(x, y, size, size);
            } else if (pencil.isSelected() ) {
                if (!toolSize.getText().isEmpty())
                    gc.setFill(Color.rgb((int) RedSlider.getValue(), (int) GreenSlider.getValue(), (int) BlueSlider.getValue(), opacity.getValue()));
                gc.fillRoundRect(x, y, size, size, size, size);
            }


        });

        drawingCanvas.setOnMouseReleased(e ->{
            if(Ccircle.isSelected()) {
                stackUndo();
                circle.setEndPoint(e.getX(), e.getY());
                circle.setRadius();
                circle.check();
                circle.draw();

            }else if(Crectangle.isSelected()) {
            	stackUndo();
            	rectangle.setEndPoint(e.getX(), e.getY());
                rectangle.setWidth();
                rectangle.setHeight();
                rectangle.check();

                rectangle.draw();
            }
        });
    }



    public void newCanvas(){
    	Stage createStage = new Stage();
            Parent root2 = null;
            try {
                root2 = FXMLLoader.load(getClass().getResource("sample.fxml"));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            Stage newStage = new Stage();
            int canvasWidthInputted = (int) drawingCanvas.getWidth();
            int canvasHeightInputted = (int) drawingCanvas.getHeight();

            //Now create a new Canvas based on the inputted values by the user

            Canvas drawingCanvas2 = new Canvas();

            drawingCanvas2.setWidth(canvasWidthInputted);
            drawingCanvas2.setHeight(canvasHeightInputted);
            Scene newsc = new Scene(root2);
            newStage.setTitle("New Canvas");
            newStage.setScene(newsc);
            newStage.show();

            createStage.close();
    }
    public void stackUndo(){
        Image snapshot = drawingCanvas.snapshot(null, null);
        undoStack.push(snapshot);
    }
    public void undo() {
        if (!undoStack.empty()) {
            Image undoImage = undoStack.pop();
            Image tempImage = undoImage;
            drawingCanvas.getGraphicsContext2D().drawImage(undoImage, 0, 0);

        }
    }
}