package com.twistral.resources;


import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.text.Font;

import java.util.HashMap;


public class Resources {

    private final ResLoader resLoader;

    private final HashMap<String, ImageResource> images;
    private final HashMap<String, MediaResource> medias;
    private final HashMap<String, String> cssFiles;



    public Resources(){
        resLoader = new ResLoader();
        images = new HashMap<>();
        medias = new HashMap<>();
        cssFiles = new HashMap<>();

        // css
        this.cssFiles.put("mainScene", resLoader.getCSS("css/mainScene.css"));

        // images
        images.put("appImage", new ImageResource(resLoader, "images/twistralTrans600.png"));
        images.put("cookieClickerCookie", new ImageResource(resLoader, "images/cookieClickerCookie.png"));
        images.put("cookieClickerBackground", new ImageResource(resLoader, "images/cookieClickerBackground.png"));
        images.put("mineSweeperOkFace", new ImageResource(resLoader, "images/mineSweeperOkFace.png"));
        images.put("mineSweeperSadFace", new ImageResource(resLoader, "images/mineSweeperSadFace.png"));
        images.put("mineSweeperCoolFace", new ImageResource(resLoader, "images/mineSweeperCoolFace.png"));
        images.put("rttStars", new ImageResource(resLoader, "images/rttStars.png"));
        images.put("rttTarget", new ImageResource(resLoader, "images/rttTarget.png"));
        images.put("rttGerFlag", new ImageResource(resLoader, "images/rttGerFlag.png"));
        images.put("rttTrFlag", new ImageResource(resLoader, "images/rttTrFlag.png"));
        images.put("rttUkFlag", new ImageResource(resLoader, "images/rttUkFlag.png"));

        // medias
        medias.put("button", new MediaResource(resLoader, "sounds/button.wav"));
        medias.put("cookieClickerChew", new MediaResource(resLoader, "sounds/cookieClickerChew.wav"));
        medias.put("minesweeperExplosion",  new MediaResource(resLoader, "sounds/mineSweeperExplosion.wav"));
        medias.put("rttCountDown", new MediaResource(resLoader, "sounds/rttCountDown.wav"));

    }

    public Image getImage(String ID){
        if(!this.images.containsKey(ID))
            System.out.println("This image for this ID doesn't exist: " + ID);
        return this.images.get(ID).get();
    }

    public Media getMedia(String ID){
        if(!this.medias.containsKey(ID))
            System.out.println("This media for this ID doesn't exist: " + ID);
        return this.medias.get(ID).get();
    }

    public String getCSS(String ID){
        if(!this.cssFiles.containsKey(ID))
            System.out.println("This cssFile for this ID doesn't exist: " + ID);
        return this.cssFiles.get(ID);
    }


    // -------------
    // --- FONTS ---
    // -------------

    public Font getAudiowideFont(double size){
        return resLoader.getFont("fonts/audiowide.ttf", size);
    }

    public Font getAbhayaFont(double size){
        return resLoader.getFont("fonts/abhaya.ttf", size);
    }

    public Font getRockFont(double size){
        return resLoader.getFont("fonts/rockFont.ttf", size);
    }







}
