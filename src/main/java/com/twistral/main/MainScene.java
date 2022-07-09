package com.twistral.main;

import com.twistral.toriafx.ToriaFX;
import com.twistral.utils.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import java.util.*;
import java.util.function.*;


public class MainScene extends TTScene<Bitterweed, VBox> {

    private HBox paneTop, paneBottom;

    private HashMap<String, Function<Void, TTScene>> appNames;

    private ListView<Label> listView;
    private Region regBottom1;

    public MainScene(Bitterweed app, VBox root) {
        super(app, root);
        root.setPrefSize(700, 400);

        this.getStylesheets().add(app.res.getCSS("mainScene"));

        this.appNames = new HashMap<>();
        addApps();

        // init layout stuff
        initPanes();

        initPaneBottom();
        initPaneTop();

        // after-ui
        initEventHandlers();
    }


    private void initPaneBottom() {
//        paneBottom.setPadding(toriafx.insetsSpecificTop(40, 20));

        listView = new ListView<>();
        refleshTheListView();

        HBox.setHgrow(listView, Priority.ALWAYS);
        paneBottom.getChildren().addAll(listView);
    }


    private void initPaneTop() {
        paneTop.setPrefHeight(80);

        regBottom1 = new Region();
        HBox.setHgrow(regBottom1, Priority.SOMETIMES);

        paneTop.getChildren().addAll(regBottom1);

    }


    private void refleshTheListView() {
        listView.getItems().clear();
        List list = new ArrayList(appNames.keySet());
        Collections.sort(list);
        Set<String> resultSet = new LinkedHashSet(list);
        for(String appName : resultSet) {
            TTScene tScene = appNames.get(appName).apply(null);
            addItemToList(listView, appName, 24);
        }
    }


    private void initPanes() {
        paneBottom = new HBox();
        paneTop = new HBox();

        VBox.setVgrow(paneBottom, Priority.ALWAYS);
        VBox.setVgrow(paneTop, Priority.SOMETIMES);

        root.getChildren().addAll(paneTop, paneBottom);

    }


    private void initEventHandlers(){
        listView.setOnMouseClicked(event -> {
            // get the label
            Label label = listView.getSelectionModel().getSelectedItem();
            if(label == null) return;
            String selectedItemText = label.getText();
            if(selectedItemText == null || selectedItemText.strip().contentEquals("") ) return;


            BoundingBox clickedRect = new BoundingBox(
                    0,
                    paneTop.getHeight() + listView.getItems().indexOf(label) * label.getHeight(),
                    label.getWidth(),
                    label.getHeight()
            );
            boolean isValid = clickedRect.contains(new Point2D(event.getSceneX(), event.getSceneY()));
            if(!isValid)
                return;
            if (event.getClickCount() != 2)
                return; // ONLY RESPOND TO DOUBLE CLICK EVENTS

            // open the app if you can
            if(!appNames.containsKey(selectedItemText))
                return;

            app.getStage().setScene(appNames.get(selectedItemText).apply(null));
            app.getStage().centerOnScreen();
        });
    }

    private void addApps() {
        this.appNames.put("TMAPE (Twistral Multiple Audacity Project Exporter)", unused -> app.getSceneTMAPE()  );
        this.appNames.put("TBCSC (Twistral Box2D Complex Shape Creator)", unused -> app.getSceneTBCSC()  );
    }


    private void addItemToList(ListView<Label> listView, String textForListLabel, int fontSize){
        Label label = new Label(textForListLabel);
        label.setFont(Font.font("arial", fontSize));
        label.setPadding(ToriaFX.insetsAll(14));
        listView.getItems().add(label);
    }

}
