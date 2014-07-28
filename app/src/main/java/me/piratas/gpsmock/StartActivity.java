package me.piratas.gpsmock;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;


public class StartActivity extends Activity implements LocationListener {

    Switch enable;

    public static final String ACTION = "me.piratas.gpsmock.Location";

    LocationManager locationManager;

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = new Location(LocationManager.GPS_PROVIDER);
            float lat = intent.getFloatExtra("lat", 0);
            if (lat < -90 || lat > 90)
                lat = 0;

            float lon = intent.getFloatExtra("lon", 0);
            if (lon < -180 || lon > 180)
                lon = 0;
            location.setLatitude(lat);
            location.setLongitude(lon);
            location.setAltitude(intent.getFloatExtra("alt", 0));
            location.setAccuracy(intent.getFloatExtra("acc", 0));
            location.setSpeed(intent.getFloatExtra("spd", 0));
            location.setBearing(intent.getFloatExtra("brn", 0));
            location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
            location.setTime(System.currentTimeMillis());
            locationManager.setTestProviderLocation(LocationManager.GPS_PROVIDER, location);
        }
    };

    ListView listView;

    List<String> log = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        enable = (Switch) findViewById(R.id.enable);
        enable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    enableMock();
                } else {
                    disableMock();
                }
            }
        });

        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, log));

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

    }

    private void disableMock() {
        locationManager.removeTestProvider(LocationManager.GPS_PROVIDER);
        unregisterReceiver(receiver);

    }

    private void enableMock() {
        locationManager.addTestProvider(LocationManager.GPS_PROVIDER, false, false,
                false, false, true, false, false, 0, 5);
        locationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);
        registerReceiver(receiver, new IntentFilter(ACTION));

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void onLocationChanged(Location location) {
        log.add(0, location.toString());
        ((BaseAdapter) this.listView.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        //pass
    }

    @Override
    public void onProviderEnabled(String s) {
        //pass
    }

    @Override
    public void onProviderDisabled(String s) {
        //pass
    }
}
