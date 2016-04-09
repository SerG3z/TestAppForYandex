package com.example.serg.testwork.models;

/**
 * Created by serg on 09.04.16.
 */
public class MyData {
    private String urlImage;
    private String name;
    private String typeMusic;
    private String info;

    public MyData(String urlImage, String name, String typeMusic, String info) {
        this.urlImage = urlImage;
        this.name = name;
        this.typeMusic = typeMusic;
        this.info = info;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public MyData(MyData data) {
        this.urlImage = data.getUrlImage();
        this.name = data.getName();
        this.typeMusic = data.getTypeMusic();
        this.info = data.getInfo();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeMusic() {
        return typeMusic;
    }

    public void setTypeMusic(String typeMusic) {
        this.typeMusic = typeMusic;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
