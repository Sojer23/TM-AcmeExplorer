package com.example.acmeexplorer;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.acmeexplorer.Utils.Utils;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class TripDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details_activity);

        CollapsingToolbarLayout toolbar_layout = findViewById(R.id.toolbar_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        TextView tripTitleDetail = findViewById(R.id.trip_title_d);
        TextView tripPriceDetail = findViewById(R.id.trip_price_d);
        TextView tripPriceFakeDetail = findViewById(R.id.trip_price_d_fake);
        TextView tripCityInitDetail = findViewById(R.id.trip_cityInit_d);
        TextView tripCityEndDetail = findViewById(R.id.trip_cityEnd_d);
        TextView tripDateInitDetail = findViewById(R.id.trip_dateInit_d);
        TextView tripDateEndDetail = findViewById(R.id.trip_dateEnd_d);
        TextView tripDescriptionDetail = findViewById(R.id.trip_description_d);
        TextView tripRatingNumberDetail = findViewById(R.id.trip_rating_n_d);
        ImageView IVTripPicture = findViewById(R.id.trip_image_d);
        RatingBar ratingTrip = (RatingBar) findViewById(R.id.trip_rating_d);

        //Get Intent content
        final String trip_code = getIntent().getStringExtra("trip_code");
        final String trip_cityEnd = getIntent().getStringExtra("trip_cityEnd");
        final String trip_cityInit = getIntent().getStringExtra("trip_cityInit");
        final Long trip_dateInit = getIntent().getLongExtra("trip_dateInit",0);
        final Long trip_dateEnd = getIntent().getLongExtra("trip_dateEnd",0);
        final String trip_url = getIntent().getStringExtra("trip_url");
        final Integer trip_price = getIntent().getIntExtra("trip_price",0);
        final Float trip_rating = getIntent().getFloatExtra("trip_rating", 0);
        final String trip_description = getIntent().getStringExtra("trip_description");

        tripTitleDetail.setText(trip_cityEnd+"("+trip_code+")");
        tripPriceDetail.setText(trip_price.toString());
        tripPriceFakeDetail.setText((trip_price+100)+"€");
        tripPriceFakeDetail.setPaintFlags(tripPriceFakeDetail.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        tripCityInitDetail.setText(trip_cityInit);
        tripCityEndDetail.setText(trip_cityEnd);
        tripDateInitDetail.setText(Utils.formateaFecha(trip_dateInit));
        tripDateEndDetail.setText(Utils.formateaFecha(trip_dateEnd));
        ratingTrip.setRating(trip_rating);
        tripRatingNumberDetail.setText("("+trip_rating.toString()+")");
        tripDescriptionDetail.setText(trip_description);

        //Set title to toolbar_layout
        toolbar.setTitle("Viaje a "+ trip_cityEnd);
        toolbar_layout.setTitle("Viaje a "+ trip_cityEnd);

        if (!trip_url.isEmpty()) {
            Picasso.get()
                    .load(trip_url)
                    .placeholder(android.R.drawable.ic_menu_myplaces)
                    .error(android.R.drawable.ic_menu_myplaces)
                    .into(IVTripPicture);
        }


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Viaje a "+trip_cityEnd+" añadido  a favoritos", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/




    }
}
