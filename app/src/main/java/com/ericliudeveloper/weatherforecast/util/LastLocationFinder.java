package com.ericliudeveloper.weatherforecast.util;

import android.content.Context;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.ericliudeveloper.weatherforecast.MyApplication;

import java.util.List;

public class LastLocationFinder {

	protected static String SINGLE_LOCATION_UPDATE_ACTION = "com.huiqun.SINGLE_LOCATION_UPDATE_ACTION";

	private static LocationListener locationListener = new LocationListener() {
		// Do nothing, just to be supplied as a parameter to
		// locationManager.requestSingleUpdate
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub

		}
	};



	/**
	 * Returns the most accurate and timely previously detected location. Where
	 * the last result is beyond the specified maximum distance or latency a
	 * one-off location update is returned via the {@link LocationListener}
	 *
	 * @param minDistance
	 *            - meter Minimum distance before we require a location update.
	 * @param latestTime
	 *            - minisecond the lastest time required between location
	 *            updates.
	 * @return The most accurate and / or timely previously detected location.
	 */
	public static Location getLastBestLocation(int minDistance, long latestTime) {
		LocationManager locationManager = (LocationManager) MyApplication
				.getApplication().getSystemService(Context.LOCATION_SERVICE);
		Location bestResult = null;
		float bestAccuracy = Float.MAX_VALUE;
		long bestTime = Long.MIN_VALUE;

		// Iterate through all the providers on the system, keeping
		// note of the most accurate result within the acceptable time limit.
		// If no result is found within maxTime, return the newest Location.
		List<String> matchingProviders = locationManager.getAllProviders();
		for (String provider : matchingProviders) {
			Location location = locationManager.getLastKnownLocation(provider);

			if (location != null) {
				float accuracy = location.getAccuracy();
				long time = location.getTime();

				if ((time > latestTime && accuracy < bestAccuracy)) {
					bestResult = location;
					bestAccuracy = accuracy;
					bestTime = time;
				} else if (time < latestTime && bestAccuracy == Float.MAX_VALUE
						&& time > bestTime) {
					bestResult = location;
					bestTime = time;
				}
			}
		}

		// If the best result is beyond the allowed time limit, or the accuracy
		// of the
		// best result is wider than the acceptable maximum distance, request a
		// single update.
		// This check simply implements the same conditions we set when
		// requesting regular
		// location updates every [minTime] and [minDistance].
		if (bestTime < latestTime || bestAccuracy > minDistance) {
			IntentFilter locIntentFilter = new IntentFilter(
					SINGLE_LOCATION_UPDATE_ACTION);

			// if (matchingProviders.contains(LocationManager.GPS_PROVIDER)) {
			// locationManager.requestSingleUpdate(
			// LocationManager.GPS_PROVIDER, locationListener, null);
			// } else if (matchingProviders
			// .contains(LocationManager.NETWORK_PROVIDER)) {
			//
			// locationManager.requestSingleUpdate(
			// LocationManager.NETWORK_PROVIDER, locationListener, null);
			// }

			for (String provider : matchingProviders) {
				locationManager.requestSingleUpdate(provider, locationListener,
						null);
			}

			for (String provider : matchingProviders) {
				Location location = locationManager
						.getLastKnownLocation(provider);

				if (location != null) {
					float accuracy = location.getAccuracy();
					long time = location.getTime();

					if ((time > latestTime && accuracy < bestAccuracy)) {
						bestResult = location;
						bestAccuracy = accuracy;
						bestTime = time;
					} else if (time < latestTime
							&& bestAccuracy == Float.MAX_VALUE
							&& time > bestTime) {
						bestResult = location;
						bestTime = time;
					}
				}
			}

		}
		return bestResult;
	}
	
	
	

}
