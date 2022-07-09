package com.twistral.resources;

import javafx.scene.image.Image;

public class ImageResource {

    private ResLoader resLoader;
    private String path;
    private Image image;

    public ImageResource(ResLoader resLoader, String path){
        this.resLoader = resLoader;
        this.path = path;
        this.image = null;
    }

    public Image get(){
        if(this.image == null)
            this.image = this.resLoader.getImage(path);
        return this.image;
    }


}
