package com.twistral.main;

import com.twistral.apps.tbcsc.TBCSCScene;
import com.twistral.apps.tmape.*;
import com.twistral.resources.Resources;
import com.twistral.utils.*;
import javafx.application.*;
import javafx.scene.layout.*;
import javafx.stage.*;


public class Bitterweed extends Application {

    // main application (bitterweed)
    private Stage stage;
    private TTScene sceneMain;

    // non-stupid apps
    private TTScene sceneTMAPE, sceneTBCSC;

    public Resources res;

    @Override
    public void init() throws Exception {
        res = new Resources();
        // TODO: 28/09/2021 her şeyin otomatik yüklenmesini engelle bir şekilde
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;

        // non-stupid apps
        sceneTMAPE = new TMAPEScene(this, new BorderPane());
        sceneTBCSC = new TBCSCScene(this, new BorderPane());

        // main
        sceneMain = new MainScene(this, new VBox());

        stage.setScene(sceneMain);

        // stage setup
        stage.setTitle(AppConsts.APP_TITLE);
        stage.getIcons().add(res.getImage("appImage"));
        stage.show();


    }

    @Override
    public void stop() throws Exception {
        System.exit(0);
        Platform.exit();
    }


    /*  SCENE RESETTERS  */
    public void resetTMAPE() {
        sceneTMAPE = new TMAPEScene(this, new BorderPane());
    }


    /*  GETTERS AND SETTERS  */
    public Stage getStage() { return stage; }
    public TTScene getSceneMain() { return sceneMain; }

    // getter's
    public TTScene getSceneTMAPE() { return sceneTMAPE; }
    public TTScene getSceneTBCSC() { return sceneTBCSC; }


}
