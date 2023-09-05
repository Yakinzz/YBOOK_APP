package com.example.ybook;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;

public class calendarFecha extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_fecha);

        CalendarView calendar = (CalendarView) findViewById(R.id.calendarView);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int i, int i1, int i2){
                String date = i + "/" + i1 + "/" + i2;
                Log.d(TAG, "onSelectedDayChange: date" + date);

                Intent intent = new Intent(calendarFecha.this,Informacion_libro.class);
                intent.putExtra("date",date);
                startActivity(intent);
            }
        });
    }
}