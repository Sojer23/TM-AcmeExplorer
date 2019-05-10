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
import android.widget.TextView;

import com.example.acmeexplorer.Entities.Option;
import com.example.acmeexplorer.Utils.Constants;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    Option[] menuOptions = Constants.menuOptions;
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Recupero el grid
        gridView = findViewById(R.id.shortcut_gridview);

        //Adapter para el GridView
        menuAdapter menuAdap = new menuAdapter(this, menuOptions);

        gridView.setAdapter(menuAdap);
    }
}

class menuAdapter extends ArrayAdapter<Option> {

    Option[] options;
    public menuAdapter(Context context, Option[] options){
        super(context, R.layout.menu_cardview, options);
        this.options = options;
    }

    public View getView(final int position, View convertView, ViewGroup parent){
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        convertView = layoutInflater.inflate(R.layout.menu_cardview, null);

        final Class clase = this.options[position].getClase();

        CardView menuCardView = convertView.findViewById(R.id.trip_cardview);
        TextView textViewTitle = convertView.findViewById(R.id.menu_title);
        TextView textViewDescription = convertView.findViewById(R.id.menu_description);
        ImageView IVPicture = convertView.findViewById(R.id.menu_picture);

        //Set the content
        textViewTitle.setText(this.options[position].getOptionText());
        textViewDescription.setText(this.options[position].getDescription());

        if (!this.options[position].getImageUrl().isEmpty()) {
            Picasso.get()
                    .load(this.options[position].getImageUrl())
                    .placeholder(android.R.drawable.ic_menu_myplaces)
                    .error(android.R.drawable.ic_menu_myplaces)
                    .into(IVPicture);
        }

        menuCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), clase);
                getContext().startActivity(intent);
            }
        });

        return convertView;
    }
}