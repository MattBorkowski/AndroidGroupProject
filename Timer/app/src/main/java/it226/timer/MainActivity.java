package edu.ilstu.it226.androidalarms;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import layout.LocationFragment;
import layout.TargetFragment;
import layout.DelayFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Start in TargetFragment
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        Fragment fragment = new TargetFragment();
        transaction.replace(R.id.fragment, fragment);
        transaction.commit();

        //Switch to correct fragment on tab switch
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();

                switch (tab.getPosition()) {
                    case 0:
                        Fragment fragment = new TargetFragment();
                        transaction.replace(R.id.fragment, fragment);
                        transaction.commit();
                        break;
                    case 1:
                        Fragment fragment2 = new DelayFragment();
                        transaction.replace(R.id.fragment, fragment2);
                        transaction.commit();
                        break;
                    case 2:
                        Fragment fragment3 = new LocationFragment();
                        transaction.replace(R.id.fragment, fragment3);
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
