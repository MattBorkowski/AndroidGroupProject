package it226.alm;

import android.app.Activity;
import android.app.AlarmManager;

import android.app.PendingIntent;

import android.app.assist.AssistStructure;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.text.InputType;
import android.view.View;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;

import android.widget.Toast;

import android.widget.ToggleButton;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity

{
    cb=(CheckBox) findViewById(R.id.cb);
    cb2=(CheckBox) findViewById(R.id.checkbox);
    buton = (Button)findViewById(R.id.buton);
    buton.setOnClickListener(this);

    TimePicker alarmTimePicker;

    PendingIntent pendingIntent;

    AlarmManager alarmManager;

    @Override

    protected void onCreate(Bundle savedInstanceState)

    {



        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        alarmTimePicker = (TimePicker) findViewById(R.id.timePicker);

        alarmManager = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);


    }

    public void onClick(View arg0) {
        Toast toast;
        AssistStructure.ViewNode cb;
        AssistStructure.ViewNode cb2;
        if(cb.isChecked()&&cb2.isChecked()) toast = Toast.makeText(getApplicationContext(), "Text", Toast.LENGTH_SHORT).show();
        else if(cb.isChecked()&&!cb2.isChecked()) toast = Toast.makeText(getApplicationContext(), "Text", Toast.LENGTH_SHORT).show();
        else if(!cb.isChecked()&&cb2.isChecked()) toast = Toast.makeText(getApplicationContext(), "Text", Toast.LENGTH_SHORT).show();
        else if(!cb.isChecked()&&!cb2.isChecked()) toast = Toast.makeText(getApplicationContext(), "Text", Toast.LENGTH_SHORT).show();


    }


    public void OnToggleClicked(View view)

    {

        long time;

        if (((ToggleButton) view).isChecked())

        {
            EditText a = (EditText) findViewById(R.id.alarmMes);

            a.setInputType(InputType.TYPE_CLASS_TEXT);

            String str = a.getText().toString();

            Toast.makeText(MainActivity.this, "ALARM ON", Toast.LENGTH_SHORT).show();

            Calendar calendar = Calendar.getInstance();

            calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());

            calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());

            Intent intent = new Intent(MainActivity.this, pannen.class);

            intent.putExtra("alames",str);

            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            time = (calendar.getTimeInMillis() - (calendar.getTimeInMillis() % 60000));

            if (System.currentTimeMillis() > time)

            {

                if (calendar.AM_PM == 0)

                    time = time + (1000 * 60 * 60 * 12);

                else

                    time = time + (1000 * 60 * 60 * 24);

            }

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, 10000, pendingIntent);

        }

        else

        {

            alarmManager.cancel(pendingIntent);

            Toast.makeText(MainActivity.this, "ALARM OFF", Toast.LENGTH_SHORT).show();

        }

    }


}