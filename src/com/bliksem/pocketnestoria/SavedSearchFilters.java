package com.bliksem.pocketnestoria;

import java.util.ArrayList;

public class SavedSearchFilters {

	private String mLocation;
	private String mSearchType; //rent or buy
	private String mPriceLow;
	private String mPriceHigh;
	private String mBedroomsLow;
	private String mBedroomsHigh;
	private String mBathroomsLow;
	private String mBathroomsHigh;
	private boolean mNewBuildsOnly;
	private boolean mPicturesOnly;
	private ArrayList<String> mFeatures = new ArrayList<String>();
	private ArrayList<String> mPropertyTypes = new ArrayList<String>();

	public final String getmLocation() {
		return mLocation;
	}

	public final void setmLocation(String mLocation) {
		this.mLocation = mLocation;
	}

	public final String getmSearchType() {
		return mSearchType;
	}

	public final void setmSearchType(String mSearchType) {
		this.mSearchType = mSearchType;
	}

	public final String getmPriceLow() {
		return mPriceLow;
	}

	public final void setmPriceLow(String mPriceLow) {
		this.mPriceLow = mPriceLow;
	}

	public final String getmPriceHigh() {
		return mPriceHigh;
	}

	public final void setmPriceHigh(String mPriceHigh) {
		this.mPriceHigh = mPriceHigh;
	}

	public final String getmBedroomsLow() {
		return mBedroomsLow;
	}

	public final void setmBedroomsLow(String mBedroomsLow) {
		this.mBedroomsLow = mBedroomsLow;
	}

	public final String getmBedroomsHigh() {
		return mBedroomsHigh;
	}

	public final void setmBedroomsHigh(String mBedroomsHigh) {
		this.mBedroomsHigh = mBedroomsHigh;
	}

	public final String getmBathroomsLow() {
		return mBathroomsLow;
	}

	public final void setmBathroomsLow(String mBathroomsLow) {
		this.mBathroomsLow = mBathroomsLow;
	}

	public final String getmBathroomsHigh() {
		return mBathroomsHigh;
	}

	public final void setmBathroomsHigh(String mBathroomsHigh) {
		this.mBathroomsHigh = mBathroomsHigh;
	}

	public final boolean ismNewBuildsOnly() {
		return mNewBuildsOnly;
	}

	public final void setmNewBuildsOnly(boolean mNewBuildsOnly) {
		this.mNewBuildsOnly = mNewBuildsOnly;
	}

	public final boolean ismPicturesOnly() {
		return mPicturesOnly;
	}

	public final void setmPicturesOnly(boolean mPicturesOnly) {
		this.mPicturesOnly = mPicturesOnly;
	}

	public final ArrayList<String> getmFeatures() {
		return mFeatures;
	}

	public final void setmFeatures(ArrayList<String> mFeatures) {
		this.mFeatures = mFeatures;
	}

	public final ArrayList<String> getmPropertyTypes() {
		return mPropertyTypes;
	}

	public final void setmPropertyTypes(ArrayList<String> mPropertyTypes) {
		this.mPropertyTypes = mPropertyTypes;
	}

}
