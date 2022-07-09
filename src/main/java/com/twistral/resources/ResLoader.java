package com.twistral.resources;


import com.twistral.Main;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.text.Font;
import java.util.HashMap;


final class ResLoader {

    private HashMap<String, Image> images;
    private HashMap<String, Media> medias;
    private HashMap<String, Font> fonts;


    public ResLoader(){
        this.images = new HashMap<>();
        this.medias = new HashMap<>();
        this.fonts = new HashMap<>();
    }


    public Font getFont(String resName, double size) {
        String resID = resName + size;
        if( !this.fonts.containsKey(resID) ){
            Font font = Font.loadFont(Main.class.getResourceAsStream(resName), size);
            this.fonts.put(resID, font);
            return font;
        }
        else {
            return this.fonts.get(resID);
        }
    }



    public Image getImage(String resName){
        if( !this.images.containsKey(resName) ){
            Image image = new Image(Main.class.getResource(resName).toExternalForm());
            this.images.put(resName, image);
            return image;
        }
        else {
            return this.images.get(resName);
        }
    }


    public Media getMedia(String resName){
        if( !this.medias.containsKey(resName) ){
            Media media = new Media(Main.class.getResource(resName).toString());
            this.medias.put(resName, media);
            return media;
        }
        else{
            return this.medias.get(resName);
        }
    }


    public String getCSS(String resName) {
        return Main.class.getResource(resName).toExternalForm();
    }

}
