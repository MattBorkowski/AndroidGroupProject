package layout;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import it226.timer.R;

public class LocationFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //Declare variables
    private LocationManager locationManager;
    private LocationListener locationListener;
    CountDownTimer timer;
    private TextView output;
    private TextView output2;
    private EditText input;
    long locationTime;

    public LocationFragment() {
    }

    public static LocationFragment newInstance(String param1, String param2) {
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Get view for findViewByID
        View temp = inflater.inflate(R.layout.fragment_location, container, false);

        //Get views from fragment
        output = (TextView) temp.findViewById(R.id.output);
        output2 = (TextView) temp.findViewById(R.id.output2);
        input = (EditText) temp.findViewById(R.id.input);

        //Set location time, manager and listener
        locationTime = 120000;
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        timer = null;
        locationListener = null;

        //Set input listener
        input.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {

                //---------When input is entered---------
                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    locationTime = Long.parseLong(input.getText().toString()) * 60000;
                    return true;
                }
                return false;
            }
        });

        //Verify app has location permission, start location listener and timer if it does
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 10);
        } else {

        }

        return temp;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
    }


    //After result update permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startListener();
                    startTimer();
                }
                return;
        }
    }


    //Start location timer
    protected void startTimer() {
        if (locationListener == null) return;
        if (timer != null) timer.cancel();
        timer = new CountDownTimer(locationTime, 1) {

            public void onTick(long millisUntilFinished) {
                long minutes = millisUntilFinished / 60000;
                String minutesText = (minutes > 1)? "Min" : "Min ";
                long seconds = (millisUntilFinished % 60000) / 1000;
                String secondsText = (seconds > 1)? "Sec" : "Sec ";
                String temp = String.format("%2d %7s %2d %7s left", minutes, minutesText, seconds, secondsText);

                output2.setText(temp);
            }

            public void onFinish() {
                output2.setText("Get Up & Move");
            }
        }.start();
    }

    //Start location listener
    protected void startListener() {
        if (locationListener == null) {
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    //----------When location is changed----------
                    startTimer();
                    output.setText("Long: " + location.getLongitude() + "\nLat: " + location.getLatitude());
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {
                }

                @Override
                public void onProviderEnabled(String s) {
                }

                @Override
                public void onProviderDisabled(String s) {
                }
            };
        }
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
            Location location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
            String temp = String.format("%9s: %f\n%9s: %f", "Long:", location.getLongitude(), "Lat:", location.getLatitude());
            output.setText(temp);
        }
    }
}
