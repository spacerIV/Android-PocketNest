package com.bliksem.pocketnestoria;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.MapsInitializer;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;


public class ActivityMain extends FragmentActivity implements DialogFragmentMapLayers.MapLayersDialogFragmentListener{
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mProductTitles;

	private MyMapFragment mMapFragment;
	private MyListFragment mMyListFragment;;
	
	private Fragment mVisible = null; // currently visible fragment

	private ArrayList<NestoriaListing> mEntries = new ArrayList<NestoriaListing>();
	//private ArrayList<MyMapPoint> mPoints = new ArrayList<MyMapPoint>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
	
		try {
			MapsInitializer.initialize(this);
		} catch (GooglePlayServicesNotAvailableException e) {
			e.printStackTrace();
		}

		mTitle = mDrawerTitle = getTitle();
		mProductTitles = getResources().getStringArray(R.array.products_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mProductTitles));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				Crouton.cancelAllCroutons();
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		//mPoints.add(new MyMapPoint(1, new LatLng(51.519933, -0.095310), "Flat1",
			//	"description", null));
		//mPoints.add(new MyMapPoint(2, new LatLng(51.518965, -0.095943),
			//	"Test Flat 2", "second description", null));

		setupFragments();
		
		

		if (savedInstanceState == null) 
		{
			mVisible = mMapFragment;
		} 
		else 
		{
			mVisible = (savedInstanceState.getBoolean("mVisibleFragment")) ? mMapFragment
					: mMyListFragment;
		}
		
		PocketNestoriaApplication PNSingleton = (PocketNestoriaApplication) getApplicationContext();
		if (PNSingleton.isNetworkAvailable()) {
			loadData();	
		}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.actionbar_map_menu, menu);
		if (menu != null) {
			menu.findItem(R.id.actionbar_map_menu_map).setVisible(false);
			menu.findItem(R.id.actionbar_map_menu_list).setVisible(true);
			menu.findItem(R.id.actionbar_map_list_sort).setVisible(false);
		}
		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		// menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		// Handle action buttons
		switch (item.getItemId()) {
		case R.id.actionbar_map_menu_search:
			Intent intent = new Intent(getApplicationContext(),
					FragmentSearch.class);
			// EditText editText = (EditText) findViewById(R.id.edit_message);
			// String message = editText.getText().toString();
			// intent.putExtra(EXTRA_MESSAGE, message);
			startActivity(intent);
			return false;
		case R.id.actionbar_map_menu_list:
			showFragment(mMyListFragment);

			return true;
		case R.id.actionbar_map_menu_map:
			showFragment(mMapFragment);

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	/* The click listener for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(mProductTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);

	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
		
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onResume() {
		super.onResume();

		// In case Google Play services has since become available.
		// setupFragments();

		selectItem(0);

		if (mVisible != null) {
			showFragment(mVisible);
		} else {
			showFragment(mMapFragment);
		}
	}

	private void showFragment(Fragment fragmentIn) {
		if (fragmentIn == null)
			return;

		final FragmentTransaction ft = getSupportFragmentManager()
				.beginTransaction();
		// ft.setCustomAnimations(android.R.anim.fade_in,
		// android.R.anim.fade_out);

		if (mVisible != null) {
			ft.hide(mVisible);
		}

		ft.show(fragmentIn).commit();
		mVisible = fragmentIn;
	}

	/**
	 * This method does the setting up of the Fragments. It basically checks if
	 * the fragments exist and if they do, we'll hide them. If the fragments
	 * don't exist, we create them, add them to the FragmentManager and hide
	 * them.
	 */
	private void setupFragments() {

		final FragmentTransaction ft = getSupportFragmentManager()
				.beginTransaction();

		/*
		 * If the activity is killed while in BG, it's possible that the
		 * fragment still remains in the FragmentManager, so, we don't need to
		 * add it again.
		 */
		mMapFragment = (MyMapFragment) getSupportFragmentManager()
				.findFragmentByTag(mMapFragment.TAG);
		if (mMapFragment == null) {
		  //  mMapFragment = MyMapFragment.newInstance(mPoints);
		    mMapFragment = MyMapFragment.newInstance();
		   ft.add(R.id.content_frame, mMapFragment, MyMapFragment.TAG);
		}
		ft.hide(mMapFragment);

		mMyListFragment = (MyListFragment) getSupportFragmentManager()
				.findFragmentByTag(MyListFragment.TAG);
		if (mMyListFragment == null) {
			mMyListFragment = new MyListFragment();
			ft.add(R.id.content_frame, mMyListFragment, MyListFragment.TAG);
		}
		ft.hide(mMyListFragment);

		ft.commit();
	}

	@Override
	public void onMapLayersDialogFragmentNewTypeClick(int mMapType) {
		if (mMapFragment != null) {
			mMapFragment.changeMapType(mMapType);
		}
		
	}
	
	private void loadData() {
		
		RequestQueue queue = MyVolley.getRequestQueue();
		queue.getCache().clear();
		queue.cancelAll(this);
		 
		// http://api.nestoria.co.uk/api?country=uk&pretty=1&action=search_listings&place_name=clerkenwell&encoding=json&listing_type=buy&number_of_results=20&page=1&sort=relevancy&bedroom_max=2&keywords=garden,high-ceilings,
		JsonObjectRequest myReq = new JsonObjectRequest(
				Method.GET,
				"http://api.nestoria.co.uk/api?country=uk&pretty=1&action=search_listings&place_name=kilburn-park-road-nw6&encoding=json&listing_type=rent&number_of_results=20&page=1&sort=relevancy",
				null, createMyReqSuccessListener(), createMyReqErrorListener());
		queue.add(myReq);
		
	}
	
	private Response.Listener<JSONObject> createMyReqSuccessListener() {
		return new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {

					JSONObject nestoria_response = response
							.getJSONObject("response");
					JSONArray listings = nestoria_response
							.getJSONArray("listings");
					JSONObject listing;
					for (int i = 0; i < listings.length(); i++) {
						Log.d("Nestoria Listing", String.valueOf(i) );
						listing = listings.getJSONObject(i);
						// Don't get listings with no lat or long
						if (listing.has("latitude") && listing.has("longitude")) {
							Log.d("ADDED LISTING", listing.getString("title") + listing.getString("lister_name") + listing.getString("datasource_name"));
							mEntries.add(new NestoriaListing(listing
									.getString("auction_date"), listing
									.getString("bathroom_number"), listing
									.getString("bedroom_number"), listing
									.getString("car_spaces"), listing
									.getString("commission"), listing
									.getString("construction_year"), listing
									.getString("datasource_name"), listing
									.getString("guid"), listing
									.getString("img_height"), listing
									.getString("img_url"), listing
									.getString("img_width"), listing
									.getString("keywords"), listing
									.getString("latitude"), listing
									.getString("lister_name"), listing
									.getString("lister_url"), listing
									.getString("listing_type"), listing
									.getString("location_accuracy"), listing
									.getString("longitude"), listing
									.getString("price"), listing
									.getString("price_currency"), listing
									.getString("price_formatted"), listing
									.getString("price_high"), listing
									.getString("price_low"), listing
									.getString("price_type"), listing
									.getString("property_type"), listing
									.getString("summary"), listing
									.getString("thumb_height"), listing
									.getString("thumb_url"), listing
									.getString("thumb_width"), listing
									.getString("title"), listing
									.getString("updated_in_days"), listing
									.getString("updated_in_days_formatted")

							));
						}
						
					}
				 	mMapFragment.clearMapPoints();
					mMapFragment.addMapPoints(mEntries);
					mMapFragment.setProgressBarState(0);
					
                   
					
				} catch (JSONException e) {
					Log.e("JSONERROR", e.toString());
					//This will most likely pass down to createMReqErrorListener
					showErrorDialog("Apologies, new and interesting error has occured. JSONException");
				}
			}
		};
	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				//Let the MyListFragment show the error, all we do here is turn off the progress bar.
				mMapFragment.setProgressBarState(0);
				showErrorDialog("A new and embarressing error occured.");
			}
		};
	}
	
	private void showErrorDialog(String errMsg) {
		//mInError = true;

		//AlertDialog.Builder b = new AlertDialog.Builder(( getApplicationContext()));
		//b.setMessage("Error occured");
		//b.show();
		
	//	Crouton.makeText(getActivity(), errMsg, Style.ALERT).show();
		//mMapFragment.showCrouton(errMsg,Style.ALERT);
		
		
        
		
		
	}

}
