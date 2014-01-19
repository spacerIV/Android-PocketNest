package com.bliksem.pocketnestoria;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

public class MyListFragment extends ListFragment {

	public static final String TAG = "listFragment";

	private static final int RESULTS_PAGE_SIZE = 20;
	private boolean mHasData = false;
	private boolean mInError = false;
	private ArrayList<NestoriaListing> mEntries = new ArrayList<NestoriaListing>();
	private ArrayAdapterNestoriaListings mAdapter;
	private LinearLayout mProgressLayout;
	
	@Override
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);

		setHasOptionsMenu(true);
		setRetainInstance(true);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {
		View v = inflater.inflate(R.layout.fragment_list, null);
		mProgressLayout = (LinearLayout) v.findViewById(R.id.linlaHeaderProgress);
		mProgressLayout.setVisibility(View.VISIBLE);
		return v;
	}

	@Override
	public void onViewCreated(View arg0, Bundle arg1) {
		super.onViewCreated(arg0, arg1);

		mAdapter = new ArrayAdapterNestoriaListings(getActivity()
				.getApplicationContext(), 0, mEntries,
				MyVolley.getImageLoader());
		setListAdapter(mAdapter);
		getListView().setOnScrollListener(new EndlessScrollListener());
		if (!mHasData && !mInError) {
			mProgressLayout.setVisibility(View.VISIBLE);
			loadPage();

		}
	}

	@Override
	public void onStart() {
		super.onStart();

		getListView().setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(getActivity().getApplicationContext(),
						FragmentListingDetail.class);
				// EditText editText = (EditText) findViewById(R.id.edit_message);
				// String message = editText.getText().toString();
				//intent.putExtra(EXTRA_MESSAGE, message);
				
				 LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		         View v = vi.inflate(R.layout.listview_listing_row, null);
		         
		         NetworkImageView niv = (NetworkImageView) v.findViewById(R.id.iv_thumb);
		        // String img_url = niv.setImage
				
				startActivity(intent);
			}
		});
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		if (menu != null) {
			menu.findItem(R.id.actionbar_map_menu_list).setVisible(false);
			menu.findItem(R.id.actionbar_map_menu_map).setVisible(true);
			menu.findItem(R.id.actionbar_map_list_sort).setVisible(true);
		}
	}

	/**
	 * Detects when user is close to the end of the current page and starts
	 * loading the next page so the user will not have to wait (that much) for
	 * the next entries.
	 * 
	 * @author Ognyan Bankov (ognyan.bankov@bulpros.com)
	 */
	public class EndlessScrollListener implements OnScrollListener {
		// how many entries earlier to start loading next page
		private int visibleThreshold = 5;
		private int currentPage = 0;
		private int previousTotal = 0;
		private boolean loading = true;

		public EndlessScrollListener() {
		}

		public EndlessScrollListener(int visibleThreshold) {
			this.visibleThreshold = visibleThreshold;
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			if (loading) {
				if (totalItemCount > previousTotal) {
					loading = false;
					previousTotal = totalItemCount;
					currentPage++;
				}
			}
			if (!loading
					&& (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
				// I load the next page of listings using a background task,
				// but you can call any function here.
				loadPage();
				loading = true;

			}
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {

		}

		public int getCurrentPage() {
			return currentPage;
		}
	}

	private void loadPage() {
		RequestQueue queue = MyVolley.getRequestQueue();

		int startIndex = 1 + mEntries.size();
		/*
		 * JsonObjectRequest myReq = new JsonObjectRequest(Method.GET,
		 * "https://picasaweb.google.com/data/feed/api/all?q=kitten&max-results="
		 * + RESULTS_PAGE_SIZE + "&thumbsize=160&alt=json" + "&start-index=" +
		 * startIndex, null, createMyReqSuccessListener(),
		 * createMyReqErrorListener());
		 */

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
						listing = listings.getJSONObject(i);
						// dont get listings with no lat or long
						if (listing.has("latitude") && listing.has("longitude")) {
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

					mAdapter.notifyDataSetChanged();
					mProgressLayout.setVisibility(View.GONE);
				} catch (JSONException e) {
					Log.e("JSONERROR", e.toString());
					showErrorDialog();
				}
			}
		};
	}

	private Response.ErrorListener createMyReqErrorListener() {

		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				mProgressLayout.setVisibility(View.GONE);
				Crouton.makeText(getActivity(), "Apologies, a new and embarressing error occured..",Style.ALERT ).show();
				//showErrorDialog();
			}
		};
	}

	private void showErrorDialog() {
		mInError = true;

		AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
		b.setMessage("Error occured");
		b.show();
	}
}
