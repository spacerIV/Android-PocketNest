package com.bliksem.pocketnestoria;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibrary;

public class PocketNestoriaApplication extends Application {

	// READUP about extending Application
	// Reto Meier post:
	// http://stackoverflow.com/questions/456211/activity-restart-on-rotation-android/456918#456918
	// http://stackoverflow.com/questions/4891241/extending-application-for-global-variables
	
	private static PocketNestoriaApplication sInstance;
	
	public static PocketNestoriaApplication getInstance() {
		return sInstance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		sInstance = this;
		sInstance.init();
	}

	private void init() {

		// 1. Volley
		MyVolley.init(this);
	
		// 2. LittleFluffyClouds LocationAPI
		// output debug to LogCat, with tag LittleFluffyLocationLibrary
		LocationLibrary.showDebugOutput(true);

		try {
			// in most cases the following initialising code using defaults is
			// probably sufficient:
			//
			LocationLibrary.initialiseLibrary(getBaseContext(),
					"com.bliksem.pocketnestoria");
			//
			// however for the purposes of the test app, we will request
			// unrealistically frequent location broadcasts
			// every 1 minute, and force a location update if there hasn't been
			// one for 2 minutes.
			// / LocationLibrary.initialiseLibrary(getBaseContext(), 60 * 1000,
			// 2 * 60 * 1000, "mobi.littlefluffytoys.littlefluffytestclient");
		} catch (UnsupportedOperationException ex) {
			Log.d("TestApplication",
					"UnsupportedOperationException thrown - the device doesn't have any location providers");
		}
		
		LocationLibrary.forceLocationUpdate(getBaseContext());


		// TODO
		// Do we need this?
		// 3. Get last search filters from shared-preferences if available.
		SharedPreferences appSharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this.getApplicationContext());
		String jsonLastSavedFilter = appSharedPrefs.getString(
				"jsonLastSavedFilter", "");

		if (jsonLastSavedFilter != null) {
			// load savedSearch
		} else {
			// default search so something shows up at start
			

		}

	}
	
	public boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

}
