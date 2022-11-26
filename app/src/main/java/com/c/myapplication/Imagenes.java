package com.c.myapplication;

public class Imagenes {
    String Description;
    String Image;
    String Fecha;
    String Key;

    public Imagenes (String description, String image, String fecha, String key) {
        Description = description;
        Image = image;
        Fecha = fecha;
        Key=key;


    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getFecha() {
        return Fecha;}

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }
}






