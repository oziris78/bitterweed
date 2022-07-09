package com.twistral.resources;

import javafx.scene.media.Media;

public class MediaResource {

    private ResLoader resLoader;
    private String path;
    private Media media;

    public MediaResource(ResLoader resLoader, String path){
        this.resLoader = resLoader;
        this.path = path;
        this.media = null;
    }

    public Media get(){
        if(this.media == null)
            this.media = this.resLoader.getMedia(path);
        return this.media;
    }


}
