package layout;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;
import java.util.Calendar;
import java.util.TimeZone;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import it226.timer.*;


public class AlarmFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private AlarmManager alarmManager;
    private int month,  day,  year, hours, minutes;
    String message , timeZone, location;
    String alarmType = "OT";
    PendingIntent operation;
    Location currentLocation;

    private OnFragmentInteractionListener mListener;

    public TargetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TargetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TargetFragment newInstance(String param1, String param2) {
        TargetFragment fragment = new TargetFragment();
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

        final View v = inflater.inflate(R.layout.fragment_alarm,container,false);
        final ToggleButton type = (ToggleButton) v.findViewById(R.id.alarmTypeButton);
        Button set = (Button) v.findViewById(R.id.setButton);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = null;

        type.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (type.isChecked()) alarmType = "R";

                else  alarmType = "OT";
            } });


        set.setOnClickListener(new View.OnClickListener() {
            EditText date = (EditText) v.findViewById(R.id.day);
            EditText time1 = (EditText) v.findViewById(R.id.time);
            EditText timeZone1 = (EditText) v.findViewById(R.id.timeZone);
            EditText message1 = (EditText) v.findViewById(R.id.message);
            public void onClick(View v) {
                String time = time1.getText().toString();
                hours = Integer.parseInt(time.substring(0,2));
                minutes = Integer.parseInt(time.substring(3,5));
                message = message1.getText().toString();
                timeZone = timeZone1.getText().toString();
                getLocation();


                if (alarmType.equalsIgnoreCase("OT")){
                    String date1 = date.getText().toString();
                    day =  Integer.parseInt(date1.substring(0,2));
                    month =  Integer.parseInt(date1.substring(3,5));
                    year =  Integer.parseInt(date1.substring(6,10));

                    setOneTime(day, month, year, hours, minutes, message, timeZone);
                }

                else  setRepeating(hours, minutes, message, timeZone);



            } });
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alarm, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    protected void setRepeating(int hours, int minutes, String message, String timeZone) {



        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.clear();

        if(timeZone != null){
            TimeZone t = TimeZone.getTimeZone(timeZone);
            calendar.setTimeZone(t);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity());
        mBuilder.setSmallIcon(R.drawable.text_a);
        mBuilder.setContentTitle(message);
        mBuilder.setContentText(message + "     Location set: " + location);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());

        // Adds the Intent that starts the Activity to the top of the stack
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        // notificationID allows you to update the notification later on.
        mNotificationManager.notify(12345, mBuilder.build());

        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, resultPendingIntent);


    }

    protected void setOneTime(int year, int month, int day, int hours, int minutes, String message, String timeZone) {



        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.clear();

        if(timeZone != null){
            TimeZone t = TimeZone.getTimeZone(timeZone);
            calendar.setTimeZone(t);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity());
        mBuilder.setSmallIcon(R.drawable.text_a);
        mBuilder.setContentTitle(message);
        mBuilder.setContentText(message + "     Location set: " + location);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());

        // Adds the Intent that starts the Activity to the top of the stack
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        // notificationID allows you to update the notification later on.
        mNotificationManager.notify(12345, mBuilder.build());

        calendar.set(year, month, day);
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                resultPendingIntent );


    }

    //Start location listener
    protected void getLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
            Location location1 = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
            String temp = String.format("%9s: %f\n%9s: %f", "Longitude", location1.getLongitude(), "Latitude", location1.getLatitude());
            location = temp;
        }

    }
}
