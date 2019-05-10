package com.example.acmeexplorer;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.acmeexplorer.Adapters.TripsAdapter;
import com.example.acmeexplorer.Adapters.TripsFavAdapter;
import com.example.acmeexplorer.Entities.Trip;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class TripFavActivity extends AppCompatActivity {

    private List<Trip> allTrips;
    private List<Trip> favTrips;
    private RecyclerView rvTripsFav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_fav);

        // Lookup the recyclerview in activity layout
        rvTripsFav = (RecyclerView) findViewById(R.id.rvTripsFav);

        allTrips = TripListActivity.trips;
        favTrips = new LinkedList<>();

        for(int i=0; i<allTrips.size();i++){
            if(allTrips.get(i).isFav()){
                favTrips.add(allTrips.get(i));
            }
        }

        if(favTrips.size() ==0){
            Toast.makeText(this, "No tienes viajes guardados", Toast.LENGTH_LONG).show();
        }

        // Create adapter passing in the sample user data
        TripsFavAdapter adapter = new TripsFavAdapter(getBaseContext(), favTrips);
        // Attach the adapter to the recyclerview to populate items
        rvTripsFav.setAdapter(adapter);

        //DECORATIONS
        /*RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvTrips.addItemDecoration(itemDecoration);*/

        //ItemSlide
        rvTripsFav.setItemAnimator(new SlideInUpAnimator());

        // Set layout manager to position the items
        rvTripsFav.setLayoutManager(new LinearLayoutManager(this));
    }
}
