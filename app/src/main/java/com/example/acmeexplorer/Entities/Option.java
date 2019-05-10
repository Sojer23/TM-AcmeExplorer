package com.example.acmeexplorer.Entities;

public class Option {

    private String optionText;
    private String imageUrl;
    private String description;
    private Class clase;

    public Option(String optionText, String imageUrl, String description, Class clase) {
        this.optionText = optionText;
        this.imageUrl = imageUrl;
        this.description = description;
        this.clase = clase;
    }

    public Class getClase() {
        return clase;
    }

    public void setClase(Class clase) {
        this.clase = clase;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
