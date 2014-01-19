package com.bliksem.pocketnestoria;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.NumberPicker;

public class DialogFragmentDistanceRadius extends DialogFragment {

	private String mDistanceRadius;

	/*
	 * The activity that creates an instance of this dialog fragment must
	 * implement this interface in order to receive event callbacks. Each method
	 * passes the DialogFragment in case the host needs to query it.
	 */
	public interface DistanceRadiusDialogFragmentListener {
		public void onDistanceRadiusDialogFragmentPositiveClick(int mDistance);

	}

	// Use this instance of the interface to deliver action events
	DistanceRadiusDialogFragmentListener mListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			mListener = (DistanceRadiusDialogFragmentListener) getTargetFragment();
		} catch (ClassCastException e) {
			throw new ClassCastException(
					"Calling fragment must implement DistanceRadiusDialogFragmentListener interface");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mDistanceRadius = new String();
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.distance_radius_dialogfragment, null);
		final NumberPicker np = (NumberPicker) view
				.findViewById(R.id.distance_radius_numberpicker);
		np.setMinValue(1);
		np.setMaxValue(10);
		np.setValue(2);
		np.setFocusable(true);
		np.setFocusableInTouchMode(true);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Distance from (mi)")
				.setView(view)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						mListener.onDistanceRadiusDialogFragmentPositiveClick(np.getValue());
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

							}
						});

		return builder.create();
	}

}
