package it226.androidalarmclock;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import Layout.AlarmFragment;
import Layout.LocationFragment;
import Layout.TimerFragment;

import it226.androidalarmclock.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Start in TargetFragment
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        Fragment fragment = new AlarmFragment();
        transaction.add(fragment,"TAG");
        transaction.commit();

        //Switch to correct fragment on tab switch
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("STATE", "\n" + tab.getPosition() + "\n");
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();

                switch (tab.getPosition()) {
                    case 0:
                        Fragment alarm = new AlarmFragment();
                        transaction.replace(R.id.alarm_fragment, alarm);
                        transaction.commit();
                        break;
                    case 1:
                        Fragment timer = new TimerFragment();
                        transaction.replace(R.id.timer_fragment, timer);
                        transaction.commit();
                        break;
                    case 2:
                        Fragment location = new LocationFragment();
                        transaction.replace(R.id.location_fragment, location);
                        transaction.commit();
                        break;
                    }
                }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
            });
    }
}


