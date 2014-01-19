package com.bliksem.pocketnestoria;

import com.android.volley.toolbox.NetworkImageView;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

public class FragmentListingDetail extends FragmentActivity {

	private ImageView mListingDetailImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listing_detail);
		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		// Show the Up button in the action bar.
		actionBar.setDisplayHomeAsUpEnabled(true);

		mListingDetailImageView = (ImageView) findViewById(R.id.listingdetail_nw_imageview);
		
	//	mListingDetailImageView.setImageUrl(entry.get_img_url(), mImageLoader);
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	//	getMenuInflater().inflate(R.menu.actionbar_search_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			finish();
			// NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
