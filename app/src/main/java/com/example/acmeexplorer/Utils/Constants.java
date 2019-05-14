package com.example.acmeexplorer.Utils;

import com.example.acmeexplorer.Entities.Option;
import com.example.acmeexplorer.MapActivity;
import com.example.acmeexplorer.TripFavActivity;
import com.example.acmeexplorer.TripListActivity;
import com.example.acmeexplorer.TripDetailsActivity;

public final class Constants {

    //MENU ACTIVITY
    public final static Option[] menuOptions = {
            new Option("Viajes disponibles", "https://s3-eu-west-1.amazonaws.com/acme-explorer/world.png", "Explorar alguno de nuestros destinos", TripListActivity.class),
            new Option("Viajes guardados", "https://s3-eu-west-1.amazonaws.com/acme-explorer/ecologism.png", "Revisa tus viajes favoritos", TripFavActivity.class),
            new Option("Ubicaciones", "https://s3-eu-west-1.amazonaws.com/acme-explorer/map.png", "Revisa tu ubicación", MapActivity.class)};


    //TRIP ACTIVITY
    public final static String[] cities = {"Tirana", "Berlín", "Andorra La Vieja", "Ereván", "Viena", "Bakú", "Bruselas", "Minsk", "Sarajevo", "Sofía", "Praga", "Zagreb", "Copenhague", "Bratislava", "Lublijana", "Madrid", "Tallín", "Helsinki", "París", "Tiflis", "Atenas", "Budapest", "Dublín", "Reikiavik", "Roma", "Riga", "Vaduz", "Vilna", "Luxemburgo", "Skopje", "La Valeta", "Chisinau", "Mónaco", "Podgorica", "Oslo", "Amsterdam", "Varsovia", "Lisboa", "Londres", "Bucarest", "Moscú", "San Marino", "Belgrado", "Estocolmo", "Berna", "Kiev", "Ciudad Del Vaticano"};
    public final static String[] cityInit = {"Sevilla", "Málaga", "Faro", "Barcelona", "Madrid", "Valencia"};
    public final static String[] urlImagenes = {"https://png.pngtree.com/element_pic/17/09/23/891e71ffa7e5efe9f5440513fa069add.jpg", "https://png.pngtree.com/element_pic/17/04/20/e29789b631107bd82df67d3f46112f0e.jpg", "https://png.pngtree.com/element_pic/20/16/01/3156adb71123719.jpg", "https://png.pngtree.com/element_pic/30/03/20/1656fbd4b4641fc.jpg", "https://png.pngtree.com/element_pic/00/00/00/0056a3602a2cf41.jpg", "https://png.pngtree.com/element_our/sm/20180416/sm_5ad452dbaaf09.png"};
    public final static String[] authors = {"Andy", "Alma", "Jan", "Carlie", "Oma", "Carlotta", "Lauralee", "Faith", "Viola", "Sheldon", "Coretta", "Kory", "Olin", "Myrtle", "Gil", "Cherish", "Saul", "Neida", "Lavonne", "Florine", "Aleshia", "Georgene", "Jarod", "Mariko", "Sandy", "Gina", "Myrl", "Lasandra", "Pasty", "Idella"};
    public final static String[] comments = {"That's strong and killer.","Just sleek =)", "Mission accomplished. It's magnificent :-)", "Please stop!", "I think I'm crying. It's that good.", "Cool shot, friend.", "I like your shot :)", "Very beastly mate", "Graceful work you have here.", "Truly clean idea.", "This experience blew my mind.", "Lo pasé muy bien", "Disfruté de lo lindo con mi mujer", "Me gustó mucho la ciudad", "Tuvimos problemas pero repetiremos", "No volveremos a viajar con esta empresa", "Buen trato en todo momento", "Uno de los mejores viajes de mi vida"};
    public final static String[] commentsTittles = {"Bien", "Buen viaje", "Repetiré","Para la familia", "Para despejarte", "Buen viaje para invierno"};
    public final static String filterPreferences = "Filtro", dateInit = "FechaInicio", dateEnd = "FechaFin";
    public static final String IntentViaje = "Viaje";
}
