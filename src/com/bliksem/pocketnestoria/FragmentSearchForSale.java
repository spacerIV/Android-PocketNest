package com.bliksem.pocketnestoria;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.GridLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.bliksem.pocketnestoria.RangeSeekBar.OnRangeSeekBarChangeListener;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationInfo;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibrary;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibraryConstants;

public class FragmentSearchForSale extends Fragment implements
		DialogFragmentPropertyType.PropertyTypeDialogFragmentListener,
		DialogFragmentFeatures.KeywordsDialogFragmentListener,
		DialogFragmentDistanceRadius.DistanceRadiusDialogFragmentListener

{

	private TextView searchSalePriceRangeTextView;
	private TextView searchSaleNumBedroomsRangeTextView;
	private TextView searchSaleNumBathroomsTextView;
	private SeekBar searchSaleNumBathroomsSeekBar;
	private ImageButton searchSaleLocationButton;
	private EditText searchSaleLocationEditText;
	private CheckBox searchSaleNewBuildCheckBox;
	private CheckBox searchSalePicturesOnlyCheckBox;

	private boolean mNewBuildsOnly = false;
	private boolean mPicturesOnly = false;

	// return values from dialogfragment
	private int mRadius;
	private ArrayList<String> mFeatures;
	private ArrayList<String> mPropertyTypes;

	public FragmentSearchForSale() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mFeatures = new ArrayList<String>();
		mPropertyTypes = new ArrayList<String>();

		View rootView = inflater.inflate(R.layout.search_for_sale_fragment,
				container, false);

		// disable keyboard from showing immediately
		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// create range-seek-bar for prices
		createSearchSalePriceSeekBar();

		// create range-seek-bar for bedrooms
		createSearchSaleBedroomsSeekBar();

		searchSaleNumBathroomsTextView = (TextView) getActivity().findViewById(
				R.id.search_sale_numbathrooms_textview);

		// listener for num-bathrooms seekBar
		searchSaleNumBathroomsSeekBar = (SeekBar) getActivity().findViewById(
				R.id.search_sale_numbathrooms_seekbar);

		searchSaleNumBathroomsSeekBar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {

						if (progress == 0) {
							searchSaleNumBathroomsTextView
									.setText("Bathrooms: any");
						} else {
							searchSaleNumBathroomsTextView
									.setText("Bathrooms: from " + progress);
						}
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub
					}
				});

		// listener for distance-radius button
		ImageButton mDistanceRadiusImageButton = (ImageButton) getActivity()
				.findViewById(R.id.search_sale_distance_radius_button);
		mDistanceRadiusImageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDistanceRadiusFragmentDialog();
			}
		});

		// listener for property_type button
		ImageButton mPropertyTypeImageButton = (ImageButton) getActivity()
				.findViewById(R.id.search_sale_property_type_button);
		mPropertyTypeImageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showPropertyTypeFragmentDialog();
			}
		});

		// listener for keywords button
		ImageButton mKeywordsImageButton = (ImageButton) getActivity()
				.findViewById(R.id.search_sale_keywords_button);
		mKeywordsImageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showKeywordsFragmentDialog();
			}
		});

		searchSaleLocationButton = (ImageButton) getActivity().findViewById(
				R.id.search_sale_location_button);
		searchSaleLocationButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LocationLibrary.forceLocationUpdate(getActivity()
						.getApplicationContext());
				refreshLocationEditText();
			}

		});

		searchSaleLocationEditText = (EditText) getActivity().findViewById(
				R.id.search_sale_location_edittext);

		searchSaleNewBuildCheckBox = (CheckBox) getActivity().findViewById(
				R.id.search_sale_new_build_checkbox);
		searchSaleNewBuildCheckBox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						mNewBuildsOnly = isChecked;
					}
				});

		searchSalePicturesOnlyCheckBox = (CheckBox) getActivity().findViewById(
				R.id.search_sale_pictures_only_checkbox);
		searchSalePicturesOnlyCheckBox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						mPicturesOnly = isChecked;
					}
				});

	}

	protected void refreshLocationEditText() {
		refreshLocationEditText(new LocationInfo(getActivity()
				.getApplicationContext()));
	}

	private void refreshLocationEditText(LocationInfo locationInfo) {
		if (locationInfo.anyLocationDataReceived()) {
			if (locationInfo.hasLatestDataBeenBroadcast()) {
				Geocoder geocoder = new Geocoder(getActivity()
						.getApplicationContext(), Locale.getDefault());
				List<Address> addresses = null;
				try {
					addresses = geocoder.getFromLocation(locationInfo.lastLat,
							locationInfo.lastLong, 1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				searchSaleLocationEditText.setText(addresses.get(0)
						.getThoroughfare()
						+ ", "
						+ addresses.get(0).getPostalCode());

			} else {
				searchSaleLocationEditText.setText("Waiting for Location...");
			}
		} else {
			searchSaleLocationEditText.setText("Waiting for Location...");
		}

	}

	private final BroadcastReceiver lftBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// extract the location info in the broadcast
			final LocationInfo locationInfo = (LocationInfo) intent
					.getSerializableExtra(LocationLibraryConstants.LOCATION_BROADCAST_EXTRA_LOCATIONINFO);
			// refresh the display with it
			// refreshLocationEditText(locationInfo);
		}
	};

	@Override
	public void onResume() {
		super.onResume();

		// cancel any notification we may have received from
		// TestBroadcastReceiver
		((NotificationManager) getActivity().getSystemService(
				Context.NOTIFICATION_SERVICE)).cancel(1234);

		// refreshLocationEditText();

		// This demonstrates how to dynamically create a receiver to listen to
		// the location updates.
		// You could also register a receiver in your manifest.
		final IntentFilter lftIntentFilter = new IntentFilter(
				LocationLibraryConstants
						.getLocationChangedPeriodicBroadcastAction());
		getActivity().registerReceiver(lftBroadcastReceiver, lftIntentFilter);
	}

	@Override
	public void onPause() {
		super.onPause();
		getActivity().unregisterReceiver(lftBroadcastReceiver);
	}

	public void get_price_sale_to_show(Integer minValue, Integer maxValue) {

		searchSalePriceRangeTextView = (TextView) getActivity().findViewById(
				R.id.search_sale_price_textview);

		if ((minValue == 100) && (maxValue == 1250)) {
			searchSalePriceRangeTextView.setText("Price: any");
		} else if ((minValue == 100) && (maxValue == 100)) {
			searchSalePriceRangeTextView.setText("Price: cheap as chips");
		} else if ((minValue == 1250) && (maxValue == 1250)) {
			searchSalePriceRangeTextView
					.setText("Price: so expensive it hurts");
		} else if ((minValue > 100) && (maxValue == 1250)) {
			searchSalePriceRangeTextView.setText(getFromSearchSalePrice(
					minValue, maxValue));
		} else if ((minValue == 100) && (maxValue < 1250)) {
			searchSalePriceRangeTextView.setText(getUpToSearchSalePrice(
					minValue, maxValue));
		} else if ((minValue > 100) && (maxValue < 1250)) {
			searchSalePriceRangeTextView
					.setText(getBetweenSearchSalePriceRange(minValue, maxValue));
		}

	}

	private static CharSequence getFromSearchSalePrice(Integer minValue,
			Integer maxValue) {
		// TODO Auto-generated method stub
		return "Price: from £" + minValue.toString();
	}

	private static CharSequence getUpToSearchSalePrice(Integer minValue,
			Integer maxValue) {
		// TODO Auto-generated method stub
		return "Price: up to £" + maxValue.toString();
	}

	private static CharSequence getBetweenSearchSalePriceRange(
			Integer minValue, Integer maxValue) {
		// TODO Auto-generated method stub
		return "Price: £" + minValue.toString() + " - £" + maxValue.toString();
	}

	private void createSearchSalePriceSeekBar() {

		// the range-seek-bar doesn't support XML generation, so we need to
		// replace at runtime
		RangeSeekBar<Integer> seekBar = new RangeSeekBar<Integer>(100, 1250,
				getActivity().getApplicationContext());
		seekBar.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>() {
			@Override
			public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar,
					Integer minValue, Integer maxValue) {
				// handle changed range values

				Log.i("RangeSeekBar", "User selected new range values: MIN="
						+ minValue + ", MAX=" + maxValue);
				get_price_sale_to_show(minValue, maxValue);
			}
		});
		seekBar.setId(501);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.ALIGN_RIGHT,
				R.id.search_sale_numbedrooms_seekbar);
		lp.addRule(RelativeLayout.BELOW, R.id.search_sale_price_textview);
		seekBar.setLayoutParams(lp);

		// find the salePriceSeekBar and replace with new rangeSeekBar
		View C = getActivity().findViewById(R.id.search_sale_price_seekbar);
		ViewGroup parent = (ViewGroup) C.getParent();
		int index = parent.indexOfChild(C);
		parent.removeView(C);
		parent.addView(seekBar, index);

		return;
	}

	private void createSearchSaleBedroomsSeekBar() {

		// the range-seek-bar doesn't support XML generation, so we need to
		// replace at runtime
		RangeSeekBar<Integer> seekBar = new RangeSeekBar<Integer>(0, 6,
				getActivity().getApplicationContext());
		seekBar.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>() {
			@Override
			public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar,
					Integer minValue, Integer maxValue) {
				// handle changed range values

				Log.i("RangeSeekBar", "User selected new range values: MIN="
						+ minValue + ", MAX=" + maxValue);
				get_search_sale_numbedrooms_text_to_show(minValue, maxValue);
			}

		});
		seekBar.setId(502);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.ALIGN_LEFT,
				R.id.search_sale_numbedrooms_textview);
		lp.addRule(RelativeLayout.BELOW, R.id.search_sale_numbedrooms_textview);
		seekBar.setLayoutParams(lp);

		// find the search_sale_numbedrooms_seekbar and replace with new
		// bedrooms rangeSeekBar
		View C = getActivity().findViewById(
				R.id.search_sale_numbedrooms_seekbar);
		ViewGroup parent = (ViewGroup) C.getParent();
		int index = parent.indexOfChild(C);
		parent.removeView(C);
		parent.addView(seekBar, index);

		return;
	}

	private void get_search_sale_numbedrooms_text_to_show(Integer minValue,
			Integer maxValue) {

		searchSaleNumBedroomsRangeTextView = (TextView) getActivity()
				.findViewById(R.id.search_sale_numbedrooms_textview);

		if ((minValue == 0) && (maxValue == 6)) {
			searchSaleNumBedroomsRangeTextView.setText("Bedrooms: any");
		} else if ((minValue == 0) && (maxValue == 0)) {
			searchSaleNumBedroomsRangeTextView.setText("Bedrooms: studio");
		} else if ((minValue > 0) && (maxValue == 6)) {
			searchSaleNumBedroomsRangeTextView.setText("Bedrooms: from "
					+ minValue.toString());
		} else if ((minValue == 0) && (maxValue < 6)) {
			searchSaleNumBedroomsRangeTextView.setText("Bedrooms: up to "
					+ maxValue.toString());
		} else if ((minValue > 0) && (maxValue < 6)) {
			searchSaleNumBedroomsRangeTextView.setText("Bedrooms: "
					+ minValue.toString() + " - " + maxValue.toString());
		}

	}

	public void showDistanceRadiusFragmentDialog() {
		// Create an instance of the dialog fragment and show it
		DialogFragment dialog = new DialogFragmentDistanceRadius();
		dialog.setTargetFragment(this, 0);
		dialog.show(getChildFragmentManager(), "DistanceRadiusDialogFragment");
	}

	public void showPropertyTypeFragmentDialog() {
		// Create an instance of the dialog fragment and show it
		DialogFragment dialog = new DialogFragmentPropertyType();
		dialog.setTargetFragment(this, 0);
		dialog.show(getChildFragmentManager(), "PropertyTypeDialogFragment");
	}

	public void showKeywordsFragmentDialog() {
		// Create an instance of the dialog fragment and show it
		DialogFragment dialog = new DialogFragmentFeatures();
		dialog.setTargetFragment(this, 0);
		dialog.show(getChildFragmentManager(), "KeywordsDialogFragment");
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// Handle action buttons
		switch (item.getItemId()) {
		case R.id.actionbar_search_menu_apply:

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	public void onDistanceRadiusDialogFragmentPositiveClick(int distance) {
		mRadius = distance;
	}

	@Override
	public void onKeywordsDialogFragmentPositiveClick(
			ArrayList<Integer> mKeywordsKeys) {
		String[] all_features = getResources().getStringArray(R.array.keywords);
		for (Integer i : mKeywordsKeys) {
			mFeatures.add(all_features[i].toString());
		}
	}

	@Override
	public void onPropertyTypeDialogFragmentPositiveClick(
			ArrayList<Integer> mPropertyTypesKeys) {
		String[] all_prop_types = getResources().getStringArray(
				R.array.property_type);
		for (Integer i : mPropertyTypesKeys) {
			mPropertyTypes.add(all_prop_types[i]);
		}
	}
}
