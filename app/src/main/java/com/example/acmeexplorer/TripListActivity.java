package com.example.acmeexplorer;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Filter;
import android.widget.TextView;

import com.example.acmeexplorer.Adapters.TripsAdapter;
import com.example.acmeexplorer.Entities.Trip;
import com.example.acmeexplorer.Utils.Constants;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class TripListActivity extends AppCompatActivity {


    static final int PICK_FILTER_LIST = 1;
    public static List<Trip> trips = Trip.generateTrips(20,3,4);
    RecyclerView rvTrips;
    TextView tripsSize;
    Button filter_button;
    List<Trip> tripsToInflate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);
        tripsToInflate = new ArrayList<>();
        filter_button = (Button) findViewById(R.id.filter_button);
        tripsSize = (TextView) findViewById(R.id.trips_size);

        filter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickFilterListIntent = new Intent(v.getContext(),FilterActivity.class);
                startActivityForResult(pickFilterListIntent, PICK_FILTER_LIST);
            }
        });

        this.refreshRecycle(949359600,7260793200L,50,2000);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_FILTER_LIST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Integer filter_priceMin = data.getIntExtra("filter_priceMin",0);
                Integer filter_priceMax= data.getIntExtra("filter_priceMax",0);
                Long filter_dateInit = data.getLongExtra("filter_dateInit",0);
                Long filter_dateEnd = data.getLongExtra("filter_dateEnd",0);
                Log.e("ERROR", "FILTERS: "+filter_priceMin.toString()+"/"+filter_priceMax.toString()+"/"+filter_dateInit.toString()+"/"+filter_dateEnd.toString());

                this.refreshRecycle(filter_dateInit,filter_dateEnd,filter_priceMin,filter_priceMax);
            }
        }
    }

    public void refreshRecycle(long dateInit, long dateEnd, int priceMin, int priceMax){
        tripsToInflate.clear();
        for(int i=0; i<trips.size();i++){
            Trip trip = trips.get(i);
            if(trip.getDateInit() > dateInit &&
            trip.getDateEnd() < dateEnd &&
            trip.getPrice()>priceMin &&
            trip.getPrice() < priceMax){
                this.tripsToInflate.add(trip);
            }
        };

        // Lookup the recyclerview in activity layout
        rvTrips = (RecyclerView) findViewById(R.id.rvTrips);


        // Create adapter passing in the sample user data
        TripsAdapter adapter = new TripsAdapter(getBaseContext(), tripsToInflate);


        // Attach the adapter to the recyclerview to populate items
        rvTrips.setAdapter(adapter);


        tripsSize.setText("Mostrando "+tripsToInflate.size()+" viajes");
        //DECORATIONS
        /*RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvTrips.addItemDecoration(itemDecoration);*/

        //ItemSlide
        rvTrips.setItemAnimator(new SlideInUpAnimator());

        // Set layout manager to position the items
        rvTrips.setLayoutManager(new LinearLayoutManager(this));
    }
}

