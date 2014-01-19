package com.bliksem.pocketnestoria;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.cyrilmottier.polaris2.maps.CameraUpdateFactory;
import com.cyrilmottier.polaris2.maps.GoogleMap;
import com.cyrilmottier.polaris2.maps.MapView;
import com.cyrilmottier.polaris2.maps.MapsInitializer;
import com.cyrilmottier.polaris2.maps.model.CameraPosition;
import com.cyrilmottier.polaris2.maps.model.LatLng;
import com.cyrilmottier.polaris2.maps.model.Marker;
import com.cyrilmottier.polaris2.maps.model.MarkerOptions;
import com.twotoasters.clusterkraf.Clusterkraf;
import com.twotoasters.clusterkraf.InputPoint;
import com.twotoasters.clusterkraf.Options;
import com.twotoasters.clusterkraf.Options.ClusterClickBehavior;
import com.twotoasters.clusterkraf.Options.ClusterInfoWindowClickBehavior;
import com.twotoasters.clusterkraf.Options.SinglePointClickBehavior;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class MyMapFragment extends Fragment {

	// http://stackoverflow.com/questions/16236439/restoring-map-state-position-and-markers-of-google-maps-v2-on-rotate-and-on

	public static final String TAG = "mapFragment";
	public static final String KEY_POINTS = "points";

	private MapView mMapView;
	private ImageButton mMapRefreshButton;
	private ImageButton mMapDrawButton;
	private ImageButton mMapLayerButton;
	private ImageButton mMapSearchInfoButton;
	

	private boolean mMapRefresh = true;
	private GoogleMap mMap;
	
	private HashMap<MyMapPoint, Marker> mPoints = new HashMap<MyMapPoint, Marker>();
	private ArrayList<YourMapPointModel> mYourMapPointModels = new ArrayList<YourMapPointModel>();
	ArrayList<InputPoint> inputPoints;
    Clusterkraf clusterkraf;
	com.twotoasters.clusterkraf.Options ClusterKrafOptions;

	private CameraPosition cp;
	private Crouton myCrouton;

	private LinearLayout mProgressLayout;

	public static MyMapFragment newInstance() {
		MyMapFragment fragment = new MyMapFragment();
		/**
		 * Bundle args = new Bundle(); args.putParcelableArrayList(KEY_POINTS,
		 * points); fragment.setArguments(args);
		 **/
		fragment.setRetainInstance(true);
		return fragment;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		/**
		 * mMapView.onSaveInstanceState(outState); MyMapPoint[] points =
		 * mPoints.keySet().toArray( new MyMapPoint[mPoints.size()]);
		 * outState.putParcelableArray(KEY_POINTS, points);
		 **/
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/**
		 * if (savedInstanceState == null) { Bundle extras = getArguments(); if
		 * ((extras != null) && extras.containsKey(KEY_POINTS)) { for
		 * (Parcelable pointP : extras .getParcelableArrayList(KEY_POINTS)) {
		 * mPoints.put((MyMapPoint) pointP, null); } } } else { MyMapPoint[]
		 * points = (MyMapPoint[]) savedInstanceState
		 * .getParcelableArray(KEY_POINTS); for (MyMapPoint point : points) {
		 * mPoints.put(point, null); } }
		 **/

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.fragment_map, container, false);

		mProgressLayout = (LinearLayout) layout
				.findViewById(R.id.linearLayoutMapFragmentProgressDialog);
		this.setProgressBarState(1);
		
		PocketNestoriaApplication PNSingleton = (PocketNestoriaApplication) getActivity().getApplicationContext();
		if (!PNSingleton.isNetworkAvailable()) {
			this.setProgressBarState(0);
		}

		mMapView = (MapView) layout.findViewById(R.id.mapview);
		mMapRefreshButton = (ImageButton) layout
				.findViewById(R.id.googlemaps_navigation_refresh);
		mMapLayerButton = (ImageButton) layout
				.findViewById(R.id.googlemaps_map_layer);
		mMapSearchInfoButton = (ImageButton) layout
				.findViewById(R.id.googlemaps_search_info);

		mMapSearchInfoButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//For sale: Clerkenwell, EC1,  2 beds, 1 bath,  garden, high-ceilings, gym, wood-floor
				showCrouton("For sale: Clerkenwell, EC1\nFrom: £350,000   To: £500,000\n2 beds - 1 bath - garden - high-ceilings - gym - wood-floor", Style.INFO);
			}
		});
		
		mMapRefreshButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mMapRefresh == true) {
					mMapRefresh = false;
					mMapRefreshButton.setImageResource(R.drawable.av_stop);
					showCrouton("Map Refresh OFF", Style.ALERT);

				} else {
					mMapRefresh = true;
					mMapRefreshButton
							.setImageResource(R.drawable.navigation_refresh);
					showCrouton("Map Refresh ON", Style.CONFIRM);
				}

			}
		});

		mMapLayerButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentManager fm = getChildFragmentManager();
				DialogFragmentMapLayers mapLayersDialogFragmentDialog = new DialogFragmentMapLayers();
				mapLayersDialogFragmentDialog.show(fm, "fragment_edit_name");
			}
		});
		
	

		if (cp != null) {
			mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
			cp = null;
		}

		return layout;
	}

	protected void showCrouton(String croutonText, Style croutonStyle) {
		if (myCrouton != null) {
			Crouton.hide(myCrouton);

		}
		myCrouton = Crouton.makeText(getActivity(), croutonText, croutonStyle);
		myCrouton.show();

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mMapView.onCreate(savedInstanceState);
		setUpMapIfNeeded();
		addMapPointsBasic();
	}

	@Override
	public void onPause() {
		mMapView.onPause();
		super.onPause();

		cp = mMap.getCameraPosition();
		mMap = null;
	}

	@Override
	public void onResume() {
		super.onResume();
		setUpMapIfNeeded();
		mMapView.onResume();

		if (cp != null) {
			mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
			cp = null;
		}

	}

	@Override
	public void onDestroy() {
		mMapView.onDestroy();
		super.onDestroy();
	}

	public void onLowMemory() {
		super.onLowMemory();
		mMapView.onLowMemory();
	};

	private void setUpMapIfNeeded() {
		if (mMap == null) {
			mMap = ((MapView) getView().findViewById(R.id.mapview)).getPolarisMap();
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {
		try {
			MapsInitializer.initialize(getActivity());
		} catch (GooglePlayServicesNotAvailableException e) {
		}
		mMap.setMyLocationEnabled(true);
		// mMap.setOnInfoWindowClickListener(this);
		// mMap.setOnMapLongClickListener(this);
		addMapPointsBasic();
		setUpCamera();

	}

	public void addMapPoints(ArrayList<NestoriaListing> mEntries) {

		/**
		 * //working map points if (mMap != null) { for (NestoriaListing NL :
		 * mEntries) { mMap.addMarker(new MarkerOptions() .position(new
		 * LatLng(Double.parseDouble(NL.get_latitude()),
		 * Double.parseDouble(NL.get_longitude()))) .title(NL.get_title())); }
		 * 
		 * 
		 * }
		 **/

		// ClusterKraf //
		if (mMap != null) {
			for (NestoriaListing NL : mEntries) {
				mYourMapPointModels.add(new YourMapPointModel(new LatLng(Double
						.parseDouble(NL.get_latitude()), Double.parseDouble(NL
						.get_longitude()))));
			}
		}

		this.inputPoints = new ArrayList<InputPoint>(mYourMapPointModels.size());
		for (YourMapPointModel model : this.mYourMapPointModels) {
			this.inputPoints.add(new InputPoint(model.latLng, model));
		}

		if (mMap != null && this.inputPoints != null
				&& this.inputPoints.size() > 0) {
			 ClusterKrafOptions = new com.twotoasters.clusterkraf.Options();
			 ClusterKrafOptions.setPixelDistanceToJoinCluster(getPixelDistanceToJoinCluster());
			// customize the options before you construct a Clusterkraf instance
			 clusterkraf = new Clusterkraf(mMap, ClusterKrafOptions,	this.inputPoints);
		}

	}

	private void addMapPointsBasic() {

		if (mMap != null) {
			HashMap<MyMapPoint, Marker> toAdd = new HashMap<MyMapPoint, Marker>();
			for (Entry<MyMapPoint, Marker> entry : mPoints.entrySet()) {
				Marker marker = entry.getValue();
				if (marker == null) {
					MyMapPoint point = entry.getKey();
					marker = mMap.addMarker(point.getMarkerOptions());
					toAdd.put(point, marker);
				}
			}
			mPoints.putAll(toAdd);

		}
	}

	private void setUpCamera() {

		final LatLng CLERKENWELL = new LatLng(51.519933, -0.095310);

		// Construct a CameraPosition focusing on Clerkenwell and animate
		// the camera to that position.
		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(CLERKENWELL).zoom(12) // Sets the zoom
				.bearing(0) // Sets the orientation of the camera to east
				// .tilt(45) // Sets the tilt of the camera to 30 degrees
				.build(); // Creates a CameraPosition from the builder

		mMap.animateCamera(CameraUpdateFactory
				.newCameraPosition(cameraPosition));
		mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		// Toast.makeText(getActivity().getApplicationContext(),
		// "setUpMap again", Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// inflater.inflate(R.menu.actionbar_map_menu, menu);
		if (menu != null) {
			menu.findItem(R.id.actionbar_map_menu_list).setVisible(true);
			menu.findItem(R.id.actionbar_map_menu_map).setVisible(false);
			menu.findItem(R.id.actionbar_map_list_sort).setVisible(false);
		}
	}

	/*
	 * @Override public void onInfoWindowClick(Marker marker) { Fragment
	 * fragment = DetailsFragment.newInstance();
	 * getActivity().getSupportFragmentManager
	 * ().beginTransaction().replace(R.id.contentPane,
	 * fragment).addToBackStack(null).commit(); }
	 */

	public static class MyMapPoint implements Parcelable {
		private static final int CONTENTS_DESCR = 1;

		public int objectId;
		public LatLng latLng;
		public String title;
		public String snippet;

		public MyMapPoint(int oId, LatLng point, String infoTitle,
				String infoSnippet, String infoImageUrl) {
			objectId = oId;
			latLng = point;
			title = infoTitle;
			snippet = infoSnippet;
		}

		public MyMapPoint(Parcel in) {
			objectId = in.readInt();
			latLng = in.readParcelable(LatLng.class.getClassLoader());
			title = in.readString();
			snippet = in.readString();
		}

		public MarkerOptions getMarkerOptions() {
			return new MarkerOptions().position(latLng).title(title)
					.snippet(snippet);
		}

		@Override
		public int describeContents() {
			return CONTENTS_DESCR;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeInt(objectId);
			dest.writeParcelable(latLng, 0);
			dest.writeString(title);
			dest.writeString(snippet);
		}

		public static final Parcelable.Creator<MyMapPoint> CREATOR = new Parcelable.Creator<MyMapPoint>() {
			public MyMapPoint createFromParcel(Parcel in) {
				return new MyMapPoint(in);
			}

			public MyMapPoint[] newArray(int size) {
				return new MyMapPoint[size];
			}
		};
	}

	public void changeMapType(int mapType) {
		if (mMap != null) {
			switch (mapType) {
			case 0: {
				mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				break;
			}
			case 1: {
				mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
				break;
			}
			case 2: {
				mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
				break;
			}
			case 3: {
				mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
				break;
			}
			default: {
				mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				break;
			}
			}
		}
	}

	public void setProgressBarState(int i) {
		if (i == 0) {
			mProgressLayout.setVisibility(View.GONE);
		} else {
			mProgressLayout.setVisibility(View.VISIBLE);
		}
	}

	public void clearMapPoints() {
		mMap.clear();
	}
	
	private int getPixelDistanceToJoinCluster() {
		return convertDeviceIndependentPixelsToPixels(40);
	}

	private int convertDeviceIndependentPixelsToPixels(int dip) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		return Math.round(displayMetrics.density * dip);
	}

}