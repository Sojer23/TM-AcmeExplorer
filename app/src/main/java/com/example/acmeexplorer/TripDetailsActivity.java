package com.example.acmeexplorer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.example.acmeexplorer.Security.LoginActivity;
import com.example.acmeexplorer.Utils.Utils;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class TripDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    //MAP
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0x412;
    private static final int REQUEST_CHECK_SETTINGS = 0x512;
    private GoogleMap map;
    private FusedLocationProviderClient fusedLocationClient;

    private FirebaseAuth mAuth;
    private static final int PERMISSION_REQUEST_STATE = 0x5123;
    private static int permissionIndex = 0;
    private static String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public TextView tripTitleDetail, tripCityEndDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details_activity);



        CollapsingToolbarLayout toolbar_layout = findViewById(R.id.toolbar_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        //requestPermissions();

        tripTitleDetail = findViewById(R.id.trip_title_d);
        TextView tripPriceDetail = findViewById(R.id.trip_price_d);
        TextView tripPriceFakeDetail = findViewById(R.id.trip_price_d_fake);
        TextView tripCityInitDetail = findViewById(R.id.trip_cityInit_d);
        tripCityEndDetail = findViewById(R.id.trip_cityEnd_d);
        TextView tripDateInitDetail = findViewById(R.id.trip_dateInit_d);
        TextView tripDateEndDetail = findViewById(R.id.trip_dateEnd_d);
        TextView tripDescriptionDetail = findViewById(R.id.trip_description_d);
        TextView tripRatingNumberDetail = findViewById(R.id.trip_rating_n_d);
        RatingBar ratingTrip = (RatingBar) findViewById(R.id.trip_rating_d);

        //Get Intent content
        final String trip_code = getIntent().getStringExtra("trip_code");
        final String trip_cityEnd = getIntent().getStringExtra("trip_cityEnd");
        final String trip_cityInit = getIntent().getStringExtra("trip_cityInit");
        final Long trip_dateInit = getIntent().getLongExtra("trip_dateInit", 0);
        final Long trip_dateEnd = getIntent().getLongExtra("trip_dateEnd", 0);
        final String trip_url = getIntent().getStringExtra("trip_url");
        final Integer trip_price = getIntent().getIntExtra("trip_price", 0);
        final Float trip_rating = getIntent().getFloatExtra("trip_rating", 0);
        final String trip_description = getIntent().getStringExtra("trip_description");

        tripTitleDetail.setText(trip_cityEnd + "(" + trip_code + ")");
        tripPriceDetail.setText(trip_price.toString());
        tripPriceFakeDetail.setText((trip_price + 100) + "€");
        tripPriceFakeDetail.setPaintFlags(tripPriceFakeDetail.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        tripCityInitDetail.setText(trip_cityInit);
        tripCityEndDetail.setText(trip_cityEnd);
        tripDateInitDetail.setText(Utils.formateaFecha(trip_dateInit));
        tripDateEndDetail.setText(Utils.formateaFecha(trip_dateEnd));
        ratingTrip.setRating(trip_rating);
        tripRatingNumberDetail.setText("(" + trip_rating.toString() + ")");
        tripDescriptionDetail.setText(trip_description);

        //Set title to toolbar_layout
        toolbar.setTitle("Viaje a " + trip_cityEnd);
        toolbar_layout.setTitle("Viaje a " + trip_cityEnd);

        /*if (!trip_url.isEmpty()) {
            Picasso.get()
                    .load(trip_url)
                    .placeholder(android.R.drawable.ic_menu_myplaces)
                    .error(android.R.drawable.ic_menu_myplaces)
                    .into(IVTripPicture);
        }*/


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePicker();
            }
        });


        StorageReference tripImageReference = FirebaseStorage.getInstance().getReference("images/trips/"+tripCityEndDetail.getText().toString()+".png");

        Log.e("ERROR", "Downloading image from: "+tripImageReference.toString());

        tripImageReference.getDownloadUrl().addOnCompleteListener(listener -> {
            try{
                if (listener.isSuccessful() && listener.getResult() != null && listener.getResult().getPath() != null) {
                    Picasso.get()
                            .load(listener.getResult())
                            .placeholder(android.R.drawable.ic_menu_myplaces)
                            .error(android.R.drawable.ic_menu_myplaces)
                            .into((ImageView) findViewById(R.id.top_image_d));
                }else{
                    //Toast.makeText(TripDetailsActivity.this, "No existe imagen de portada para este viaje", Toast.LENGTH_SHORT).show();
                }
            }catch(Exception e){
                Log.e("ERROR", "Downloading image exception: "+e.toString());
            }

        });


        //////////////////////////////////////////////////////////////////
        /////////////////////MAP CONFIGURATION///////////////////////////
        /////////////////////////////////////////////////////////////////

        SupportMapFragment mapView = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView_d);
        assert mapView != null;
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


        //Getting smartphone location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


       /* Geocoder geoCoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geoCoder.getFromLocationName(
                    tripCityEndDetail.getText().toString(), 1);

            Log.e("ERROR", addresses.toString());
            if (addresses.size() > 0) {
                Log.e("ERROR","ADDRESS "+ addresses.get(0) +",LAT :" + addresses.get(0).getLatitude() +", LONG :" + addresses.get(0).getLongitude() );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/



        //////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////


    }

    public void showImagePicker() {
        EasyImage.openChooserWithGallery(this, getString(R.string.setting_profile_picture_title), 0);
    }

    private void requestPermissions() {
        if (permissionIndex < permissions.length) {
            String permission = permissions[permissionIndex];
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        permission)) {
                    String text = "";
                    switch (permission) {
                        case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                            //text = getString(R.string.permission_rationale_external_storage);
                            text = "Permite el acceso al almacenamiento interno";
                            break;
                        case Manifest.permission.CAMERA:
                            //text = getString(R.string.permission_rationale_camera);
                            text = "Permite el acceso al almacenamiento interno";
                            break;
                    }
                    Snackbar.make(findViewById(R.id.trip_description_d), text,
                            Snackbar.LENGTH_INDEFINITE).setAction("OK", view -> ActivityCompat.requestPermissions(this,
                            new String[]{permission},
                            PERMISSION_REQUEST_STATE)).show();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{permission},
                            PERMISSION_REQUEST_STATE);
                }
            } else {
                permissionIndex++;
                requestPermissions();
            }
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            showImagePicker();
        }
    }

    private boolean retry = false;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissionsReq[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_STATE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    retry = false;
                    permissionIndex++;
                    requestPermissions();
                } else if (!retry) {
                    retry = true;
                    requestPermissions();
                } else if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
                    if (permissions.length == 1 &&
                            permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        checkLocationSettings();
                    } else {
                        Toast.makeText(this, R.string.location_not_allowed, Toast.LENGTH_SHORT).show();
                    }
                }else {
                    retry = false;
                    permissionIndex++;
                    requestPermissions();
                }
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //////////////////////////MAPS FUNCTION//////////////////////////
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                assert data != null;
                final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
                switch (resultCode) {
                    case Activity.RESULT_OK: {
                        if (states.isGpsUsable()) {
                            Toast.makeText(this, R.string.allow_fine_location_correct, Toast.LENGTH_SHORT).show();
                            showCurrentLocation();
                            break;
                        }
                    }
                    case Activity.RESULT_CANCELED: {
                        Toast.makeText(this, R.string.allow_fine_location_error, Toast.LENGTH_SHORT).show();
                        break;
                    }
                    default: {
                        break;
                    }
                }
                break;
        }

        //////////////////////////END MAPS FUNCTION//////////////////////////

        //requestPermissions();
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(TripDetailsActivity.this);
                builder1.setCancelable(false);
                builder1.setMessage(R.string.setting_profile_picture_error_title);

                builder1.setPositiveButton(
                        R.string.setting_profile_picture_error_yes,
                        (dialog, id) -> dialog.cancel());
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }

            @Override
            public void onImagesPicked(List<File> imageFiles, EasyImage.ImageSource source, int type) {
                File imageFile = imageFiles.get(0);
                //StorageReference storageReference = FirebaseStorage.getInstance().getReference("images/" + mAuth.getCurrentUser().getUid() + "/profile.png");
                StorageReference storageReference = FirebaseStorage.getInstance().getReference("images/trips/"+tripCityEndDetail.getText().toString()+".png");
                Bitmap bmp = null;
                try {
                    bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(imageFile));

                    ExifInterface ei = new ExifInterface(imageFile.getPath());
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED);

                    Matrix matrix = new Matrix();
                    switch (orientation) {

                        case ExifInterface.ORIENTATION_ROTATE_90:
                            matrix.preRotate(90);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_180:
                            matrix.preRotate(180);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_270:
                            matrix.preRotate(270);
                            break;
                    }
                    Bitmap rotatedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);

                    int width = rotatedBitmap.getWidth();
                    int height = rotatedBitmap.getHeight();

                    float bitmapRatio = (float) width / (float) height;
                    if (bitmapRatio > 1) {
                        width = 400;
                        height = (int) (width / bitmapRatio);
                    } else {
                        height = 400;
                        width = (int) (height * bitmapRatio);
                    }

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    Bitmap.createScaledBitmap(rotatedBitmap, width, height, true).compress(Bitmap.CompressFormat.JPEG, 15, baos);

                    byte[] data = baos.toByteArray();
                    storageReference.putBytes(data).addOnFailureListener(exception -> {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(TripDetailsActivity.this);
                        builder1.setCancelable(false);
                        builder1.setMessage(R.string.setting_profile_picture_error_uploading_title);

                        builder1.setPositiveButton(
                                R.string.setting_profile_picture_error_yes,
                                (dialog, id) -> dialog.cancel());
                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }).addOnSuccessListener(taskSnapshot -> {
                        Toast.makeText(TripDetailsActivity.this, getString(R.string.setting_profile_picture_picked_title), Toast.LENGTH_SHORT).show();
                        storageReference.getDownloadUrl().addOnCompleteListener(listener -> {
                            if (listener.isSuccessful() && listener.getResult() != null && listener.getResult().getPath() != null) {
                                Toast.makeText(TripDetailsActivity.this, getString(R.string.setting_profile_picture_upload_title), Toast.LENGTH_SHORT).show();
                                Picasso.get()
                                        .load(listener.getResult())
                                        .placeholder(android.R.drawable.ic_menu_myplaces)
                                        .error(android.R.drawable.ic_menu_myplaces)
                                        .into((ImageView) findViewById(R.id.top_image_d));
                            }
                        });
                    });
                } catch (IOException exception) {
                    Crashlytics.logException(exception);
                }
            }
        });
    }


    //////////////////////////////////////////////////////////////////
    /////////////////////MAP FUNCTIONS///////////////////////////
    /////////////////////////////////////////////////////////////////


    @Override
    protected void onResume() {
        super.onResume();
        if (map != null){
            checkLocationSettings();
        }
    }

    @Override
    public void onMapReady(GoogleMap mapParam) {
        map = mapParam;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            checkLocationSettings();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.mapView_d), R.string.allow_fine_location_question, Snackbar.LENGTH_LONG)
                        .setAction(R.string.allow_fine_location_question_button, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    ActivityCompat.requestPermissions(TripDetailsActivity.this,
                                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                            MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                                }
                            }
                        });

                snackbar.show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        }
    }

    private void showCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                MarkerOptions marker = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("Posición");
                                map.addMarker(marker);
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()),8));
                            }
                        }
                    });
        }
    }


    public void checkLocationSettings() {
        final LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                requestLocationUpdates();
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull final Exception e) {
                if (e instanceof ResolvableApiException) {
                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.mapView_d), R.string.location_system_not_configured, Snackbar.LENGTH_LONG)
                            .setAction(R.string.location_system_not_configured_button, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    try {
                                        ResolvableApiException resolvable = (ResolvableApiException) e;
                                        resolvable.startResolutionForResult(TripDetailsActivity.this,
                                                REQUEST_CHECK_SETTINGS);
                                    } catch (IntentSender.SendIntentException ignored) {
                                    }
                                }
                            });

                    snackbar.show();
                }
            }
        });
    }


    private void requestLocationUpdates() {

            Address cityAddress = getCityEndLocationAddress();

            if(cityAddress == null){
                return;
            }else{
                map.clear();
                MarkerOptions marker = new MarkerOptions().position(new LatLng(cityAddress.getLatitude(), cityAddress.getLongitude())).title("Posición").snippet("Mi ubicación");
                map.addMarker(marker);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(cityAddress.getLatitude(), cityAddress.getLongitude()), 8));


                map.animateCamera(CameraUpdateFactory.zoomIn());
                map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
            }

    }

    private Address getCityEndLocationAddress(){

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        String errorMessage = "";

        List<Address> addresses = null;
        Address address = null;

        try {
            addresses = geocoder.getFromLocationName(
                    tripCityEndDetail.getText().toString(), 1);
            Log.e("ERROR", addresses.toString());
        } catch (IOException ioException) {
            errorMessage = getString(R.string.service_not_available);
        } catch (IllegalArgumentException illegalArgumentException) {
            errorMessage = getString(R.string.invalid_lat_long_used);
        }

        if (addresses == null || addresses.size()  == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.no_address_found);
            }
        } else {
            address = addresses.get(0);
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();
            return address;
        }

        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();

        return address;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
