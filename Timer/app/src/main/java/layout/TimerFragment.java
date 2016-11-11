package layout;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import edu.ilstu.it226.androidalarms.MainActivity;
import edu.ilstu.it226.androidalarms.R;

public class TimerFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //Declare variables
    private LocationManager locationManager;
    private LocationListener locationListener;
    CountDownTimer timer;
    Button button;
    private TextView output_days;
    private TextView output_minutes;
    private TextView output_message;
    private TextView output_timer;
    private TextView output_time_remaining;
    private EditText input_days;
    private EditText input_minutes;
    private EditText input_message;
    private int days, minutes;
    private String message;
    Location currentLocation;
    long millis_left;

    // Default constructor
    public TimerFragment() {
    }

    public static TimerFragment newInstance(String param1, String param2) {
        TimerFragment fragment = new TimerFragment();
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
        View temp = inflater.inflate(R.layout.fragment_timer, container, false);

        //Get views from fragment
        output_days = (TextView) temp.findViewById(R.id.text_days);
        input_days = (EditText) temp.findViewById(R.id.edit_days);
        output_minutes = (TextView) temp.findViewById(R.id.text_minutes);
        input_minutes = (EditText) temp.findViewById(R.id.edit_minutes);
        output_message = (TextView) temp.findViewById(R.id.text_message);
        input_message = (EditText) temp.findViewById(R.id.edit_message);
        output_timer = (TextView) temp.findViewById(R.id.text_timer);
        button = (Button) temp.findViewById(R.id.timer_button);

        //Set location manager and listener
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        timer = null;
        locationListener = null;

        //Set input listener
        input_days.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {

                //---------When input is entered---------
                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    days = Integer.parseInt(input_days.getText().toString());
                    return true;
                }
                return false;
            }
        });
        input_minutes.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {

                //---------When input is entered---------
                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    minutes = Integer.parseInt(input_minutes.getText().toString());
                    button.setText(minutes + " ");
                    return true;
                }
                return false;
            }
        });
        input_message.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {

                //---------When input is entered---------
                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    message = input_message.getText().toString();
                    return true;
                }
                return false;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start timer
                startTimer();
            }
        });


        //Verify app has location permission, start location listener and timer if it does
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 10);
        }
        return temp;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    protected void timer_notification() {
        // notification
        // user message & location
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity());
        mBuilder.setSmallIcon(R.drawable.text_a);
        mBuilder.setContentTitle("Android Alarms");
        mBuilder.setContentText(input_message.getText().toString());
        mBuilder.setContentInfo("Location: " + currentLocation);

        Intent resultIntent = new Intent(getActivity(), MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());
        stackBuilder.addParentStack(MainActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        // notificationID allows you to update the notification later on.
        mNotificationManager.notify(12345, mBuilder.build());
    }


//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    //After result update permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startListener();
                }
                return;
        }
    }


    //Start timer
//    protected void startTimer() {
//
//        days = Integer.parseInt(input_days.getText().toString());
//        minutes = Integer.parseInt(input_minutes.getText().toString());
//
//        millis_left = (days * 86400000) + (minutes * 60000);
//
//        button.setText(millis_left + " ");
//
//        if (locationListener == null) return;
//        if (timer != null) timer.cancel();
//
//        timer = new CountDownTimer(millis_left, 1){
//
//            public void onTick ( long millisUntilFinished){
//
//                button.setText(millisUntilFinished + " ");
//                long days = millisUntilFinished / 86400000;
//                long minutes = millisUntilFinished / 60000;
//                String temp = String.format("%2d %2d", days, minutes);
//
//            }
//
//            public void onFinish() {timer_notification();}
//        }.start();
//    }

    protected void startTimer() {

        days = Integer.parseInt(input_days.getText().toString());
        minutes = Integer.parseInt(input_minutes.getText().toString());

        millis_left = (days * 86400000) + (minutes * 60000);

        //if (locationListener == null) return;
        if (timer != null) timer.cancel();
        timer = new CountDownTimer(millis_left, 1) {

            public void onTick(long millisUntilFinished) {
                long days = millisUntilFinished / 86400000;
                long minutes = millisUntilFinished / 60000;
                String temp = String.format("%2d %2d", days, minutes);
            }

            public void onFinish() {
                timer_notification();
            }
        }.start();
    }



    //Start location listener
    protected void startListener() {
        if (locationListener == null) {
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    currentLocation = location;
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

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
        Location location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);

    }
}
