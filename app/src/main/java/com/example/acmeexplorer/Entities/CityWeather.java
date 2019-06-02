package com.example.acmeexplorer.Entities;

public class CityWeather {

    private String name;
    private String job;
    private String id;
    private String createdAt;

}
 class coord {
    private int lon;
    private int lat;

    public coord(int lon, int lat) {
        this.lon = lon;
        this.lat = lat;
    }

     public int getLon() {
         return lon;
     }

     public void setLon(int lon) {
         this.lon = lon;
     }

     public int getLat() {
         return lat;
     }

     public void setLat(int lat) {
         this.lat = lat;
     }
 }

 class weather{
    private int id;
    private String main;
     private String description;
     private String icon;
 }
/*
  "coord": {
    "lon": -3.7,
    "lat": 40.42
  },
  "weather": [
    {
      "id": 800,
      "main": "Clear",
      "description": "clear sky",
      "icon": "01d"
    }
  ],
  "base": "stations",
  "main": {
    "temp": 306.02,
    "pressure": 1017,
    "humidity": 14,
    "temp_min": 303.71,
    "temp_max": 307.15
  },
  "visibility": 10000,
  "wind": {
    "speed": 2.6
  },
  "clouds": {
    "all": 0
  },
  "dt": 1559488427,
  "sys": {
    "type": 1,
    "id": 6443,
    "message": 0.0072,
    "country": "ES",
    "sunrise": 1559450803,
    "sunset": 1559504345
  },
  "timezone": 7200,
  "id": 3117735,
  "name": "Madrid",
  "cod": 200
}*/
