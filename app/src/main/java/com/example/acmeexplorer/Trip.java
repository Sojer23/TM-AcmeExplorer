package com.example.acmeexplorer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Trip implements Serializable {

    private String cityInit;
    private String cityEnd;
    private String description;
    private List<Stage> stages;
    private List<Comment> comments;
    private String code;
    private int price;
    private Calendar dateInit;
    private Calendar dateEnd;
    private String imageUrl;
    private float totalStars;


    public Trip(String cityInit, String cityEnd, String description, List<Stage> stages, List<Comment> comments, String code, int price, Calendar dateInit, Calendar dateEnd, String imageUrl, float totalStars) {
        this.cityInit = cityInit;
        this.cityEnd = cityEnd;
        this.description = description;
        this.stages = stages;
        this.comments = comments;
        this.code = code;
        this.price = price;
        this.dateInit = dateInit;
        this.dateEnd = dateEnd;
        this.imageUrl = imageUrl;
        this.totalStars = totalStars;
    }

    public float getTotalStars() {
        return totalStars;
    }

    public void setTotalStars(float totalStars) {
        this.totalStars = totalStars;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getCityInit() {
        return cityInit;
    }

    public void setCityInit(String cityInit) {
        this.cityInit = cityInit;
    }

    public String getCityEnd() {
        return cityEnd;
    }

    public void setCityEnd(String cityEnd) {
        this.cityEnd = cityEnd;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Stage> getStages() {
        return stages;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void setStages(List<Stage> stages) {
        this.stages = stages;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Calendar getDateInit() {
        return dateInit;
    }

    public void setDateInit(Calendar dateInit) {
        this.dateInit = dateInit;
    }

    public Calendar getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Calendar dateEnd) {
        this.dateEnd = dateEnd;
    }



    public static List<Trip> generateTrips(int numTrips, int numStages, int numComments) {
        List<Trip> trips = new ArrayList<>();

        int min = 75, max = 2050, randomNumber;
        String cityInit, cityEnd, descripcion, url, code;
        Calendar dateInit = Calendar.getInstance(), dateEnd;
        //Stage data
        List<Stage> stages = new ArrayList<>();
        String stageTittle, stageDescription;
        int stagePrice = 0;
        int totalPrice =0;
        float commentStars;
        float avgStars = 0;
        Calendar stageDateInit;
        //Comment data
        List<Comment> comments = new ArrayList<>();
        String commentTittle, commentAuthor, comment;
        for (int i = 0; i < numTrips; i++) {
            randomNumber = ThreadLocalRandom.current().nextInt(min, max);
            code = "XX"+i+"-"+randomNumber;
            cityInit = Constants.cityInit[randomNumber % Constants.cityInit.length];
            cityEnd = Constants.cities[randomNumber % Constants.cities.length];
            url = Constants.urlImagenes[randomNumber % Constants.urlImagenes.length];
            descripcion = "Una travesía ideal que termina en " + cityEnd;
            dateInit.add(Calendar.DAY_OF_MONTH, randomNumber % 60);
            dateEnd = (Calendar) dateInit.clone();
            dateEnd.add(Calendar.DAY_OF_MONTH, 3 + randomNumber % 8);

            //Create Stages arrayList
            // // Stage(String tittle, String description, Double price, Calendar dateInit)
            for (int k = 0; k < numStages; k++) {
                stageTittle = Constants.cities[randomNumber % Constants.cities.length ];
                stageDescription = "Parada en "+ stageTittle;
                stagePrice = randomNumber;
                stageDateInit = (Calendar) dateInit.clone();
                stageDateInit.add(Calendar.DAY_OF_MONTH, randomNumber%60);

                stages.add(new Stage(stageTittle, stageDescription, stagePrice, stageDateInit));
            }

            //Create Comments arrayList
            // Comment(String tittle, String author, String comment)
            for (int l = 0; l < numComments; l++) {
                comment = Constants.comments[randomNumber % Constants.comments.length];
                commentAuthor = Constants.authors[randomNumber % Constants.authors.length];
                commentTittle = Constants.commentsTittles[randomNumber % Constants.commentsTittles.length];
                commentStars = ThreadLocalRandom.current().nextInt(0, 5);

                comments.add(new Comment(commentTittle, commentAuthor, comment, commentStars));
            }

            for(int j=0; j<stages.size(); j++){
                double sP = stages.get(j).price;
                totalPrice += sP;
            }

            float totalStars = (float) 0.0;
            for(int m=0; m<comments.size(); m++){
                float cS = comments.get(m).stars;
                totalStars += cS;
                avgStars = totalStars/comments.size();
            }

            System.out.println("Average: "+avgStars);
            //Trip(String cityInit, String cityEnd, String description, Stage[] stages,Comment[] comments, String code, Double price, Calendar dateInit, Calendar dateEnd, String imageUrl)
            trips.add(new Trip(cityInit, cityEnd, descripcion, stages, comments, code, totalPrice, dateInit, dateEnd, url, avgStars));
        }
        return trips;
    }
}

class Stage {
    String tittle;
    String description;
    int price;
    Calendar dateInit;

    public Stage(String tittle, String description, int price, Calendar dateInit) {
        this.tittle = tittle;
        this.description = description;
        this.price = price;
        this.dateInit = dateInit;
    }
}

class Comment {
    String tittle;
    String author;
    String comment;
    float stars;

    public Comment(String tittle, String author, String comment, float stars) {
        this.tittle = tittle;
        this.author = author;
        this.comment = comment;
        this.stars = stars;
    }
}

