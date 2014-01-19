package com.bliksem.pocketnestoria;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DialogFragmentFeatures extends DialogFragment {

	private ArrayList<Integer> mKeywords;

	/*
	 * The activity that creates an instance of this dialog fragment must
	 * implement this interface in order to receive event callbacks. Each method
	 * passes the DialogFragment in case the host needs to query it.
	 */
	public interface KeywordsDialogFragmentListener {
		public void onKeywordsDialogFragmentPositiveClick(ArrayList<Integer> mKeywords);
		
	}

	// Use this instance of the interface to deliver action events
	KeywordsDialogFragmentListener mListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			mListener = (KeywordsDialogFragmentListener) getTargetFragment();
		} catch (ClassCastException e) {
			throw new ClassCastException(
					"Calling fragment must implement KeywordsDialogFragmentListener interface");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mKeywords = new ArrayList(); // Where we track the selected items
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Set the dialog title
		builder.setTitle("Features")
				// Specify the list array, the items to be selected by default
				// (null for none),
				// and the listener through which to receive callbacks when
				// items are selected
				.setMultiChoiceItems(R.array.keywords, null,
						new DialogInterface.OnMultiChoiceClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which, boolean isChecked) {
								if (isChecked) {
									// If the user checked the item, add it to
									// the selected items
									mKeywords.add(which);
								} else if (mKeywords.contains(which)) {
									// Else, if the item is already in the
									// array, remove it
									mKeywords.remove(Integer
											.valueOf(which));
								}
							}
						})
				// Set the action buttons
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						// User clicked OK, so save the mSelectedItems results
						// somewhere
						// or return them to the component that opened the
						// dialog
						// ...
						// Send the positive button event back to the target fragment
	                       mListener.onKeywordsDialogFragmentPositiveClick(mKeywords);
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								 // Send the negative button event back to the host activity
			                      
							}
						});

		return builder.create();
	}

}
