package com.twistral.apps.tbcsc;

import com.twistral.main.*;
import com.twistral.toriafx.*;
import com.twistral.utils.*;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.effect.Glow;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.*;
import javafx.stage.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.net.*;
import java.util.*;



public class TBCSCScene extends TTScene<Bitterweed, BorderPane> {

    private VBox paneRight;
    private Button btnGetImage, btnExportChainShape, btnExportPolygon, btnRemoveLast, btnClear, btnExit;

    private HBox hboxForPoly;

    private ArrayList<Double> points = new ArrayList<>();
    private ArrayList<Circle> circles = new ArrayList<>();
    private ArrayList<Line> lines = new ArrayList<>();

    private ColorPicker colorPicker;

    private ImageView imageView;
    private Image currentImage;
    private double ratio;


    public TBCSCScene(Bitterweed app, BorderPane root) {
        super(app, root);

        root.setPrefSize(800, 600);

        initRight();
        initLeft();
    }



    //////////////
    /*  LAYOUT  */
    //////////////

    private void initLeft() {
        imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setOnMouseClicked(event -> clickedOnImageProcess(event));

        hboxForPoly = new HBox(imageView);

        root.setCenter(hboxForPoly);
    }


    private void initRight() {
        paneRight = new VBox();

        btnGetImage = new Button("LOAD/CHANGE IMAGE");
        btnExportChainShape = new Button("EXPORT FOR CHAINSHAPE (INT)");
        btnExportPolygon = new Button("EXPORT FOR \nPOLYGON TRIANGULATION (INT)");
        btnRemoveLast = new Button("REMOVE LAST");
        btnClear = new Button("CLEAR");
        btnExit = new Button("GO BACK");

        for(Button btn : new Button[]{ btnGetImage, btnExportChainShape, btnExportPolygon, btnRemoveLast, btnClear, btnExit }){
            btn.setPrefWidth(200d);
            if(btn != btnExportPolygon) btn.setPrefHeight(40d);
            btn.setTextAlignment(TextAlignment.CENTER);
        }

        btnGetImage.setOnAction(event -> openingImageProcess() );
        btnExportChainShape.setOnAction(event -> chainShapeProcess() );
        btnExportPolygon.setOnAction(event -> polygonTriangulationProcess() );
        btnRemoveLast.setOnAction(event -> removeLastProcess() );
        btnClear.setOnAction(e -> clearListsAndGraphics());
        btnExit.setOnAction(e -> {
            clearListsAndGraphics();
            app.getStage().setScene( app.getSceneMain() );
            app.getStage().centerOnScreen();
        });

        colorPicker = new ColorPicker(Color.BLACK);
        colorPicker.setPrefSize(200d, 40d);
        colorPicker.setOnAction(event -> switchColorProcess());

        Region[] regs = new Region[]{new Region(), new Region()};
        for(Region r : regs) VBox.setVgrow(r, Priority.ALWAYS);

        paneRight.setSpacing(20);
        paneRight.setPadding(ToriaFX.insetsOnlyHorizontal(15));
        paneRight.setBackground(new Background(new BackgroundFill(new Color(0.9f, 0.9f, 0.9f, 0.95f), CornerRadii.EMPTY, Insets.EMPTY)));
        paneRight.getChildren().addAll(regs[0], colorPicker, btnGetImage,
                btnRemoveLast, btnClear, new Region(), btnExportChainShape, btnExportPolygon, btnExit, regs[1]);

        root.setRight(paneRight);
    }




    /////////////////
    /*  PROCESSES  */
    /////////////////

    private void openingImageProcess() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(app.getStage());
        if(selectedFile == null) return;
        if(!selectedFile.exists()) return;
        try {
            currentImage = new Image(selectedFile.toURI().toURL().toExternalForm());
            double curWidth = currentImage.getWidth();
            double desiredWidth = app.getStage().getWidth() - paneRight.getWidth() - 30d;
            ratio = desiredWidth / curWidth;
            imageView.setFitWidth(curWidth * ratio);
            imageView.setImage( currentImage );
            clearListsAndGraphics();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void polygonTriangulationProcess() {
        if(points.isEmpty()) return;
        if(points.get(0).intValue() == points.get( points.size() - 2 ).intValue()
                && points.get(1).intValue() == points.get( points.size() - 1 ).intValue())
        {
            points.remove(points.size()-1);
            points.remove(points.size()-1);
        }

        StringBuilder sb = new StringBuilder("float[] vertices = new float[]{\n");
        for (int i = 0; i < points.size(); i+=2) {
            sb.append(String.format("\t %d, %d", points.get(i).intValue(), points.get(i+1).intValue()));
            if(i+2 < points.size()) sb.append(",");
            sb.append("\n");
        }
        sb.append("};");

        String result = sb.toString();
        copyTextToClipboard(result);
        showResult(result);
    }

    private void chainShapeProcess() {
        if(points.isEmpty()) return;
        if(points.get(0).intValue() != points.get( points.size() - 2 ).intValue()
                || points.get(1).intValue() != points.get( points.size() - 1 ).intValue())
        {
            System.out.println("added");
            points.add(points.get(0));
            points.add(points.get(1));
        }

        StringBuilder sb = new StringBuilder("Vector2[] vertices = new Vector2[]{\n");
        for (int i = 0; i < points.size(); i+=2) {
            sb.append(String.format("\t new Vector2(%d, %d)", points.get(i).intValue(), points.get(i+1).intValue()));
            if(i+2 < points.size()) sb.append(",");
            sb.append("\n");
        }
        sb.append("};");

        String result = sb.toString();
        copyTextToClipboard(result);
        showResult(result);
    }

    private void clickedOnImageProcess(MouseEvent event) {
        if(event.getButton() != MouseButton.PRIMARY) return;

        double imageX = event.getX() / ratio;
        double imageY = event.getY() / ratio;
        points.add(imageX);
        points.add(imageY);

        Circle circle = new Circle();
        makeDraggable(circle);
        circle.setManaged(false);
        circle.setCenterX(event.getX());
        circle.setCenterY(event.getY());
        circle.setRadius(5f);
        circle.setFill(Color.WHITE);
        circle.setStroke(Color.BLACK);
        circles.add(circle);

        updateCircleColors();
        addLine(circles.size() - 1, circles.size() - 2);

        hboxForPoly.getChildren().add(circle);
    }


    private void updateCircleColors() {

        for (int i = 0; i < circles.size(); i++) {
            Circle circle = circles.get(i);
            if(i == 0) {
                circle.setFill(Color.CYAN);
                circle.setEffect(new Glow());
            }
            else if(i == circles.size()-1) {
                circle.setFill(Color.CYAN);
                circle.setEffect(new Glow());
            }
            else {
                circle.setFill(Color.WHITE);
                circle.setEffect(null);
            }
        }
    }


    private void clearListsAndGraphics() {
        for(Circle c : circles) hboxForPoly.getChildren().remove(c);
        for(Line line : lines) hboxForPoly.getChildren().remove(line);

        points.clear();
        circles.clear();
        lines.clear();
    }

    private void switchColorProcess() {
        Color lineColor = colorPicker.getValue();
        for(Line line : lines)
            line.setStroke(lineColor);
    }

    private void removeLastProcess() {
        if(circles.size() <= 2) {
            clearListsAndGraphics();
            return;
        }
        Circle lastCircle = circles.get( circles.size() - 1 );
        Line lastLine = lines.get( lines.size() - 1 );

        hboxForPoly.getChildren().removeAll(lastCircle, lastLine);
        circles.remove(lastCircle);
        lines.remove(lastLine);
        points.remove( points.size() - 1 );
        points.remove( points.size() - 1 );
        updatePoints();
    }


    ///////////////
    /*  HELPERS  */
    ///////////////

    private void makeDraggable(Circle circle) {
        Draggable.Nature nature = new Draggable.Nature(circle);

        nature.addListener( (draggableNature, dragEvent) -> {
            Circle selectedCircle = (Circle) draggableNature.getEventNode();
            int index = circles.indexOf(selectedCircle);

            HashMap<Integer, Line> linesThatAreEffected = new HashMap<>();
            if(index == 0){
                linesThatAreEffected.put( 0, lines.get(0) );
            }
            else if( 0 < index && index < circles.size() - 1 ){
                linesThatAreEffected.put( index - 1, lines.get(index - 1) );
                linesThatAreEffected.put( index, lines.get(index) );
            }
            else if( index == circles.size() - 1 ){
                linesThatAreEffected.put( circles.size() - 2, lines.get(circles.size() - 2) );
            }

            for(int i : linesThatAreEffected.keySet()){
                Line line = lines.get(i);
                Circle c1 = circles.get(i);
                Circle c2 = circles.get(i+1);

                line.setStartX(c1.getCenterX() + c1.getTranslateX());
                line.setStartY(c1.getCenterY() + c1.getTranslateY());
                line.setEndX(c2.getCenterX() + c2.getTranslateX());
                line.setEndY(c2.getCenterY() + c2.getTranslateY());
            }
            updatePoints();

        });
    }



    private void addLine(int index1, int index2) {
        if( circles.size() <= 1 ) return;

        double x1 = circles.get( index1).getCenterX();
        double y1 = circles.get( index1).getCenterY();
        double x2 = circles.get( index2 ).getCenterX();
        double y2 = circles.get( index2 ).getCenterY();

        for(Line line : lines){
            if(line.getStartX() == x1 && line.getStartY() == y1 && line.getEndX() == x2 && line.getEndY() == y2)
                return;
        }

        Line line = new Line(x1,y1,x2,y2);
        line.setManaged(false);
        line.setStrokeWidth(3d);
        line.setStroke(colorPicker.getValue());
        lines.add(line);
        hboxForPoly.getChildren().add(line);
    }

    private void showResult(String result){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Your Generated Array");
        alert.setHeaderText("Copy paste this code to create shapes in box2d! \n(Code has been automaticly copied to your clipboard)");
        TextArea textArea = new TextArea(result);
        HBox hBox = new HBox(textArea);
        hBox.setPadding(ToriaFX.insetsAll(30));
        alert.getDialogPane().setContent(hBox);
        alert.show();
    }

    private void copyTextToClipboard(String text){
        StringSelection stringSelection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    private void updatePoints(){
        points.clear();
        for(Circle c : circles){
            points.add( (c.getCenterX() + c.getTranslateX()) / ratio);
            points.add( (c.getCenterY() + c.getTranslateY()) / ratio);
        }
    }


}