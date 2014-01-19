package com.bliksem.pocketnestoria;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

public class DialogFragmentMapLayers extends DialogFragment {

	/*
	 * The activity that creates an instance of this dialog fragment must
	 * implement this interface in order to receive event callbacks. Each method
	 * passes the DialogFragment in case the host needs to query it.
	 */
	public interface MapLayersDialogFragmentListener {
		public void onMapLayersDialogFragmentNewTypeClick(int mMapType);
		
	}

	// Use this instance of the interface to deliver action events
	MapLayersDialogFragmentListener mListener;;

	// Override the Fragment.onAttach() method to instantiate the
	// NoticeDialogListener
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Verify that the host activity implements the callback interface
		try {
			// Instantiate the MapLayersDialogFragmentListener so we can send
			// events to the
			// host
			mListener = (MapLayersDialogFragmentListener) activity;
		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString()
					+ " must implement MapLayersDialogFragmentListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		new ArrayList();
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Set the dialog title
		builder.setTitle("Map Layers").setItems(R.array.map_types,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						    FragmentActivity activity = getActivity();
						    ((MapLayersDialogFragmentListener) activity).onMapLayersDialogFragmentNewTypeClick(which);
					}
				});

		return builder.create();
	}

}
