package com.example.acmeexplorer.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.acmeexplorer.Entities.Trip;
import com.example.acmeexplorer.R;
import com.example.acmeexplorer.TripDetailsActivity;
import com.example.acmeexplorer.Utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.ViewHolder> {


    private Context context;
    private FirebaseAuth mAuth;

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView textViewTittle, textViewCityInit,textViewCityEnd,textViewDateInit,textViewDateEnd,textViewPrice, textViewPriceFake;
        public ImageView IVPicture;
        public RatingBar ratingTrip;
        public CheckBox checkBoxFav;

        //CONSTRUCTOR
        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            textViewTittle = (TextView) itemView.findViewById(R.id.trip_title_d);
            textViewCityInit = (TextView) itemView.findViewById(R.id.trip_cityInit_d);
            textViewCityEnd = (TextView) itemView.findViewById(R.id.trip_cityEnd_d);
            textViewDateInit = (TextView) itemView.findViewById(R.id.trip_dateInit);
            textViewDateEnd = (TextView) itemView.findViewById(R.id.trip_dateEnd);
            IVPicture = (ImageView) itemView.findViewById(R.id.trip_picture);
            ratingTrip = (RatingBar) itemView.findViewById(R.id.trip_ratingBar);
            checkBoxFav = (CheckBox) itemView.findViewById(R.id.trip_fav);
            textViewPrice = (TextView) itemView.findViewById(R.id.trip_price);
            textViewPriceFake = (TextView) itemView.findViewById(R.id.trip_price2);

        }
    }

    // Store a member variable for the trips
    private List<Trip> mTrips;

    // Pass in the trips array into the constructor
    public TripsAdapter(Context context, List<Trip> trips) {
        this.context =context;
        mTrips = trips;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View tripView = inflater.inflate(R.layout.trip_cardview, null);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(tripView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        mAuth = FirebaseAuth.getInstance();


        // Get the data model based on position
        final Trip trip = mTrips.get(position);

        // Set item views based on your views and data model
        //Set the content
        TextView textViewTittle = viewHolder.textViewTittle;
        TextView textViewCityInit = viewHolder.textViewCityInit;
        TextView textViewCityEnd = viewHolder.textViewCityEnd;
        TextView textViewDateInit = viewHolder.textViewDateInit;
        TextView textViewDateEnd = viewHolder.textViewDateEnd;
        TextView textViewPrice = viewHolder.textViewPrice;
        TextView textViewPriceFake = viewHolder.textViewPriceFake;
        final CheckBox checkBoxFav = viewHolder.checkBoxFav;

        ImageView IVPicture = viewHolder.IVPicture;
        RatingBar ratingTrip = viewHolder.ratingTrip;


        textViewTittle.setText(trip.getCityEnd());
        textViewCityInit.setText(trip.getCityInit());
        textViewCityEnd.setText(trip.getCityEnd());
        textViewDateInit.setText(Utils.formateaFecha(trip.getDateInit()));
        textViewDateEnd.setText(Utils.formateaFecha(trip.getDateEnd()));
        textViewPrice.setText(trip.getPrice()+"€");
        textViewPriceFake.setText((trip.getPrice()+100)+"€");
        textViewPriceFake.setPaintFlags(textViewPriceFake.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        ratingTrip.setRating(trip.getTotalStars());
        checkBoxFav.setChecked(trip.isFav());

        if (!trip.getImageUrl().isEmpty()) {
            Picasso.get()
                    .load(trip.getImageUrl())
                    .placeholder(android.R.drawable.ic_menu_myplaces)
                    .error(android.R.drawable.ic_menu_myplaces)
                    .into(IVPicture);
        }

        StorageReference tripImageReference = FirebaseStorage.getInstance().getReference("images/trips/"+textViewTittle.getText().toString()+".png");

        Log.e("ERROR", "Downloading image from: "+tripImageReference.toString());

        tripImageReference.getDownloadUrl().addOnCompleteListener(listener -> {
            try{
                if (listener.isSuccessful() && listener.getResult() != null && listener.getResult().getPath() != null) {
                    Picasso.get()
                            .load(listener.getResult())
                            .placeholder(android.R.drawable.ic_menu_myplaces)
                            .error(android.R.drawable.ic_menu_myplaces)
                            .into(IVPicture);
                }else{
                    //Toast.makeText(context, "No existe imagen de portada para este viaje", Toast.LENGTH_SHORT).show();
                }
            }catch(Exception e){
                Log.e("ERROR", "Downloading image exception: "+e.toString());
            }

        });

        checkBoxFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trip.setFav(checkBoxFav.isChecked());
                if(checkBoxFav.isChecked()){
                    Snackbar.make(v, "Viaje "+trip.getCityEnd()+" añadido  a favoritos", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else{
                    Snackbar.make(v, "Viaje "+trip.getCityEnd()+" eliminado de favoritos", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });

        IVPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TripDetailsActivity.class);
                intent.putExtra("trip_code", trip.getCode());
                intent.putExtra("trip_cityEnd", trip.getCityEnd());
                intent.putExtra("trip_cityInit", trip.getCityInit());
                intent.putExtra("trip_dateInit", trip.getDateInit());
                intent.putExtra("trip_dateEnd", trip.getDateEnd());
                intent.putExtra("trip_price", trip.getPrice());
                intent.putExtra("trip_url", trip.getImageUrl());
                intent.putExtra("trip_rating", trip.getTotalStars());
                intent.putExtra("trip_description", trip.getDescription());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });



    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mTrips.size();
    }
}
