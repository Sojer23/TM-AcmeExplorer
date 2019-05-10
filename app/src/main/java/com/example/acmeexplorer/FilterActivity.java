package com.example.acmeexplorer;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.example.acmeexplorer.Entities.Trip;
import com.example.acmeexplorer.Utils.Utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class FilterActivity extends AppCompatActivity {

    private TextView tvDateInit,tvDateEnd, tvPriceMin, tvPriceMax;
    private Button filter_apply;
    private final Calendar calendar = Calendar.getInstance();
    private CrystalRangeSeekbar rangeSeekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);


        rangeSeekbar = findViewById(R.id.range_price);

        //Get init and end date textview
        tvDateInit = findViewById(R.id.tv_dateInit);
        tvDateEnd = findViewById(R.id.tv_dateEnd);

        // get min and max text view
        tvPriceMin = findViewById(R.id.price_min);
        tvPriceMax = findViewById(R.id.price_max);

        //Get filter button
        filter_apply = findViewById(R.id.filter_apply);

        filter_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterTrips(v);
            }
        });

        // set listener
        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
               tvPriceMin.setText(String.valueOf(minValue));
               tvPriceMax.setText(String.valueOf(maxValue));
            }
        });

        // set final value listener
        rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
            }
        });
    }

    public void filterTrips(View v){
        int setPriceMin = 0;
        int setPriceMax = 3000;
        Calendar setDateInit = new GregorianCalendar(2000,1,1);
        Calendar setDateEnd = new GregorianCalendar(2200,1,1);
        long setDateInit_l = Utils.Calendar2long(setDateInit);
        long setDateEnd_l = Utils.Calendar2long(setDateEnd);


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        String tvDateInit_s = (String) tvDateInit.getText().toString();
        if(!tvDateInit_s.isEmpty()){

            Date newDateInit = simpleDateFormat.parse(tvDateInit_s, new ParsePosition(0));

            setDateInit.setTime(newDateInit);

            setDateInit_l = Utils.Calendar2long(setDateInit);
        }


        String tvDateEnd_s = (String) tvDateEnd.getText().toString();
        if(!tvDateEnd_s.isEmpty()){

            Date newDateEnd = simpleDateFormat.parse(tvDateEnd_s, new ParsePosition(0));

            setDateEnd.setTime(newDateEnd);

            setDateEnd_l = Utils.Calendar2long(setDateEnd);
        }


        String tvPriceMin_s = (String) tvPriceMin.getText().toString();
        if(!tvPriceMin_s.isEmpty()){

            setPriceMin = Integer.parseInt(tvPriceMin_s);

        }

        String tvPriceMax_s = (String) tvPriceMax.getText().toString();
        if(!tvPriceMax_s.isEmpty()){

            setPriceMax = Integer.parseInt(tvPriceMax_s);
        }

        Intent intent = new Intent();
        intent.putExtra("filter_priceMin", setPriceMin);
        intent.putExtra("filter_priceMax", setPriceMax);
        intent.putExtra("filter_dateInit", setDateInit_l);
        intent.putExtra("filter_dateEnd", setDateEnd_l);
        setResult(RESULT_OK, intent);

        finish();
    }

    public void deleteFilter(View v){


        rangeSeekbar = findViewById(R.id.range_price);
        rangeSeekbar.setMinValue(0);
        rangeSeekbar.setMaxValue(2000);
        rangeSeekbar.invalidate();
        /*// set listener
        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                tvPriceMin.setText(String.valueOf(minValue));
                tvPriceMax.setText(String.valueOf(maxValue));
            }
        });

        // set final value listener
        rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
            }
        });*/

        //this.startActivity(this.getIntent());
        //this.overridePendingTransition(0, 0);


    }

    public void getDateInit(View view) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tvDateInit.setText(dayOfMonth+"/"+(month+1)+"/"+year);
            }
        },year,month,day);

        datePickerDialog.show();

    }

    public void getDateEnd(View view) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tvDateEnd.setText(dayOfMonth+"/"+(month+1)+"/"+year);
            }
        },year,month,day);

        datePickerDialog.show();

    }


}
