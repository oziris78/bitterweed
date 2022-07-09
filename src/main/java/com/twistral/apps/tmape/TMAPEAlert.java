package com.twistral.apps.tmape;

import javafx.scene.control.Alert;

public class TMAPEAlert {

    private static final String title = "HOW TO USE TMAPE";
    private static final String headerText = "CHECK THESE BEFORE YOU USE TMAPE:";
    private static final String contentText = "1- Make sure your audacity projects have only ascii characters in their names.\n" +
            "2- Make sure CTRL+SHIFT+L opens the exporting section in Audacity.\n" +
            "3- Make sure Audacity will be opened without any saving dialogs or popups.\n" +
            "4- Make sure in the exporting menu (opened with CTRL+SHIFT+L) your settings are well chosen.\n" +
            "(Directory for output etc.)";


    public static void showInfo() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }


}
