package gpsmock.piratas.me.controledgpsmock;

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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class StartActivity extends Activity implements LocationListener {

    Switch enable;

    public static final String ACTION = "gpsmock.piratas.me.Location";

    LocationManager locationManager;

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = new Location(LocationManager.GPS_PROVIDER);
            location.setLatitude(intent.getFloatExtra("lat", 0));
            location.setLongitude(intent.getFloatExtra("lon", 0));
            location.setAltitude(intent.getFloatExtra("alt", 0));
            location.setAccuracy(intent.getFloatExtra("acc", 0));
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

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        listView = (ListView) findViewById(R.id.listview);

        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, log));

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void onLocationChanged(Location location) {
//        Toast.makeText(this, location.toString(), Toast.LENGTH_LONG).show();
        log.add(0, location.toString());
        ((ArrayAdapter<String>) this.listView.getAdapter()).notifyDataSetChanged();
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
}
