package com.example.acmeexplorer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.acmeexplorer.Entities.Option;
import com.example.acmeexplorer.Security.LoginActivity;
import com.example.acmeexplorer.Utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    Option[] menuOptions = Constants.menuOptions;
    GridView gridView;
    Button sign_out_btn;
    TextView tvHello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.shortcut_gridview);
        sign_out_btn = findViewById(R.id.sign_out_btn);
        tvHello = findViewById(R.id.tV_hello);

        //Adapter para el GridView
        menuAdapter menuAdap = new menuAdapter(this, menuOptions);
        gridView.setAdapter(menuAdap);

        //Sign out
        sign_out_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            tvHello.setText("Bienvenido, "+ email);
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }
    }


    private void signOut(){
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
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