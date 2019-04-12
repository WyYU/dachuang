package Listener;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by dell on 2019/4/12 0012.
 */

public class MyLocationListener implements LocationListener {
	double latitude ;
	//得到经度
	double longitude ;
	@Override
	public void onLocationChanged(Location location) {
		latitude = location.getLatitude();
		longitude = location.getLongitude();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}
}
