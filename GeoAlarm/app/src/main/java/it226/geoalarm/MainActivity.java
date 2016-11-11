package it226.geoalarm;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {
    LocationManager lm;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final Spinner timer = (Spinner) findViewById(R.id.spinner1);


        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 4, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                    Spinner t = (Spinner) findViewById(R.id.spinner1);
                    alarm(t.getSelectedItemPosition());

            }
            @Override
            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
                // TODO Auto-generated method stub
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            return;
        }
        timer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {
                Spinner t = (Spinner) findViewById(R.id.spinner1);
                Toast.makeText(parent.getContext(), "Inactivity timer set to " +parent.getItemAtPosition(pos) ,Toast.LENGTH_SHORT).show();
                alarm(parent.getSelectedItemPosition());
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.inactivity_Timer, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timer.setAdapter(adapter);

        int defaultTime = adapter.getPosition("2 Minutes");
        timer.setSelection(defaultTime);
    }

    public void alarm(int timer){
        AlarmManager alm;
        Intent in;
        PendingIntent alarmIntent;

        alm = (AlarmManager)getSystemService(Activity.ALARM_SERVICE);
        in = new Intent(MainActivity.this, AlarmReceiever.class);

        alarmIntent = PendingIntent.getActivity(this, 000001, in, PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND,posToTime(timer));
        alm.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), alarmIntent);
    }

    public int posToTime(int i){
        switch (i){
            case 0:return 30;

            case 1:return 60;

            case 2:return 60*2;

            case 3:return 60*3;

            case 4:return 60*4;

            case 5:return 60*5;

            case 6:return 60*10;

            case 7:return 60*15;

            case 8:return 60*30;

            case 9:return 60*60;
        }
        return 60*2;
    }

}
