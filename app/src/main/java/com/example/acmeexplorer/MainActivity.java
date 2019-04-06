package com.example.acmeexplorer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Trip[] trips = new ArrayList<>(Trip.generateTrips(20, 4, 7)).toArray(new Trip[0]);
    GridView gridView;
    CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Recupero el grid
        gridView = findViewById(R.id.shortcut_gridview);
        //Recupero el cardView
        cardView = findViewById(R.id.shortcut_cardview);

        //Adapter para el GridView
        TripAdapter tripAdapter = new TripAdapter(this, trips);

        gridView.setAdapter(tripAdapter);
    }
}

class TripAdapter extends ArrayAdapter<Trip> {

    Trip[] trips;
    public TripAdapter(Context context, Trip[] trips){
        super(context, R.layout.trip_cardview, trips);
        this.trips = trips;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        convertView = layoutInflater.inflate(R.layout.trip_cardview, null);

        //final Class clase = this.trips[position].getClase();

        TextView textViewTittle = convertView.findViewById(R.id.trip_tittle);
        TextView textViewCityInit = convertView.findViewById(R.id.trip_cityInit);
        TextView textViewCityEnd = convertView.findViewById(R.id.trip_cityEnd);
        TextView textViewDescription = convertView.findViewById(R.id.trip_description);
        ImageView IVPicture = convertView.findViewById(R.id.trip_picture);
        RatingBar ratingTrip = convertView.findViewById(R.id.trip_ratingBar);

        //Set the content
        textViewTittle.setText(this.trips[position].getCityEnd());
        textViewCityInit.setText(this.trips[position].getCityInit());
        textViewCityEnd.setText(this.trips[position].getCityEnd());
        textViewDescription.setText(this.trips[position].getDescription());
        ratingTrip.setRating(this.trips[position].getTotalStars());
        if (!this.trips[position].getImageUrl().isEmpty()) {
            Picasso.get()
                    .load(this.trips[position].getImageUrl())
                    .placeholder(android.R.drawable.ic_menu_myplaces)
                    .error(android.R.drawable.ic_menu_myplaces)
                    .into(IVPicture);
        }

        IVPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                getContext().startActivity(intent);
            }
        });


        return convertView;
    }
}