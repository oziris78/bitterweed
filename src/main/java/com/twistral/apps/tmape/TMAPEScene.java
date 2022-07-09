package com.twistral.apps.tmape;

import com.twistral.toriafx.ToriaFX;
import com.twistral.utils.*;
import com.twistral.main.*;
import javafx.application.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import java.util.*;


public class TMAPEScene extends TTScene<Bitterweed, BorderPane> {


    private HBox paneMidTop, paneMidBottom, paneMidTopBelow;
    private VBox paneMiddle;

    private HBox paneBottom;

    private Text textAppName;
    private TextField tfDirectory, tfOutputDirectory, tfTimeForAudacity, tfTimeForEnter, tfFolderLimit;
    private Button btnExport, btnInfo, btnGoBack;




    public TMAPEScene(Bitterweed app, BorderPane root) {
        super(app, root);

        initPanes();

        initTop();
        initMiddle();
        initBottom();

    }


    private void initPanes() {
        paneBottom = new HBox();
        paneMidTop = new HBox();
        paneMidBottom = new HBox();
        paneMidTopBelow = new HBox();
        paneMiddle = new VBox();
    }


    private void initTop(){
        textAppName = new Text("TWISTRAL MULTIPLE AUDACITY PROJECT EXPORTER");
        textAppName.setFont(app.res.getAudiowideFont(50d));
        textAppName.setTextAlignment(TextAlignment.CENTER);
        textAppName.wrappingWidthProperty().bind(this.widthProperty().multiply(0.9d));
        BorderPane.setAlignment(textAppName, Pos.CENTER);
        BorderPane.setMargin(textAppName, new Insets(25));
        root.setTop(textAppName);
    }


    private void initMiddle(){
        tfDirectory = new TextField("C:\\Users\\oguzh\\Desktop\\test");
        tfDirectory.setPromptText("Enter your directory");
        tfDirectory.setFont(Font.font("arial", 35d));
        tfDirectory.setPrefSize(650, 80);
        tfDirectory.setAlignment(Pos.CENTER);

        HBox.setMargin(tfDirectory, new Insets(30));
        paneMidTop.setAlignment(Pos.CENTER);
        paneMidTop.getChildren().add(tfDirectory);

        tfOutputDirectory = new TextField("C:\\Users\\oguzh\\Desktop\\output");
        tfOutputDirectory.setPromptText("Enter your output directory");
        tfOutputDirectory.setFont(Font.font("arial", 35d));
        tfOutputDirectory.setPrefSize(650, 80);
        tfOutputDirectory.setAlignment(Pos.CENTER);

        HBox.setMargin(tfDirectory, new Insets(30));
        paneMidTopBelow.setAlignment(Pos.CENTER);
        paneMidTopBelow.getChildren().add(tfOutputDirectory);

        tfTimeForAudacity = new TextField("3000");
        tfTimeForAudacity.setPromptText("Audacity time in ms");
        tfTimeForAudacity.setFont(Font.font("arial", 30d));
        tfTimeForAudacity.setPrefSize(175, 50);
        tfTimeForAudacity.setAlignment(Pos.CENTER);

        tfTimeForEnter = new TextField("600");
        tfTimeForEnter.setPromptText("Enter time in ms");
        tfTimeForEnter.setFont(Font.font("arial", 30d));
        tfTimeForEnter.setPrefSize(175, 50);
        tfTimeForEnter.setAlignment(Pos.CENTER);

        tfFolderLimit = new TextField("75");
        tfFolderLimit.setPromptText("Enter file limit");
        tfFolderLimit.setFont(Font.font("arial", 30d));
        tfFolderLimit.setPrefSize(175, 50);
        tfFolderLimit.setAlignment(Pos.CENTER);

        HBox.setMargin(tfTimeForAudacity, ToriaFX.insetsAll(30));
        HBox.setMargin(tfTimeForEnter, ToriaFX.insetsAll(30));
        HBox.setMargin(tfFolderLimit, ToriaFX.insetsAll(30));
        paneMidBottom.setAlignment(Pos.CENTER);
        paneMidBottom.getChildren().addAll(tfTimeForAudacity, tfTimeForEnter, tfFolderLimit);

        paneMiddle.getChildren().addAll(paneMidTop, paneMidTopBelow, paneMidBottom);
        root.setCenter(paneMiddle);
    }



    private void initBottom(){

        btnExport = new Button("EXPORT");
        btnInfo = new Button("INFO");
        btnGoBack = new Button("EXIT");

        for(Button btn : new Button[]{btnExport, btnInfo, btnGoBack}){
            btn.setFont(Font.font("arial", 30d));
            btn.setTextAlignment(TextAlignment.CENTER);
            btn.setPrefSize(200, 50);
        }

        btnExport.setOnMouseClicked(event -> {
            startExporting();
        });

        btnInfo.setOnMouseClicked(event -> {
            TMAPEAlert.showInfo();
        });

        btnGoBack.setOnMouseClicked(event -> {
            app.resetTMAPE();
            app.getStage().setScene( app.getSceneMain() );
            app.getStage().centerOnScreen();
        });

        Region reg1 = new Region(); Region reg3 = new Region();

        HBox.setHgrow(btnExport, Priority.SOMETIMES);
        HBox.setHgrow(reg1, Priority.ALWAYS);
        HBox.setHgrow(reg3, Priority.ALWAYS);
        HBox.setHgrow(btnGoBack, Priority.SOMETIMES);
        HBox.setMargin(btnExport, new Insets(35));
        HBox.setMargin(btnInfo, new Insets(35));
        HBox.setMargin(btnGoBack,new Insets(35));
        paneBottom.getChildren().addAll(reg1, btnExport, btnInfo, btnGoBack, reg3);

        paneMiddle.getChildren().add(paneBottom);
    }



    public void startExporting(){
        try{
            btnExport.setDisable(true);
            String directory = tfDirectory.getText();
            String outputDirectory = tfOutputDirectory.getText();
            long timeForAudacity = Long.parseLong(tfTimeForAudacity.getText());
            long timeForEnter = Long.parseLong(tfTimeForEnter.getText());
            int folderFileLimit = Integer.parseInt(tfFolderLimit.getText());
            String estimatedTime = RobotUtils.getEstimatedTimeInMinutesAndSeconds(directory, timeForAudacity, timeForEnter);
            textAppName.setText("The entire process will take " + estimatedTime);
            new Timer().schedule(new TimerTask() {@Override public void run() {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            RobotUtils.getRunnable(directory, outputDirectory, timeForAudacity, timeForEnter, folderFileLimit).run();
                            textAppName.setText("TWISTRAL MULTIPLE AUDACITY PROJECT EXPORTER");
                            textAppName.setFill(Color.BLACK);
                            btnExport.setDisable(false);
                        }
                    });
            }}, 3000);
        }
        catch (Exception e){
            String errorMessage = e.getMessage().startsWith("Folder not found for this directory") ? "FOLDER NOT FOUND" : "INVALID INPUT";
            textAppName.setText(errorMessage);
            textAppName.setFill(Color.RED);
            btnExport.setDisable(true);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    textAppName.setText("TWISTRAL MULTIPLE AUDACITY PROJECT EXPORTER");
                    textAppName.setFill(Color.BLACK);
                    btnExport.setDisable(false);
                }
            }, 3000);
        }

    }



}
