package com.bliksem.pocketnestoria;

import android.os.Parcel;
import android.os.Parcelable;


//http://stackoverflow.com/questions/2139134/how-to-send-an-object-from-one-android-activity-to-another-using-intents

public class NestoriaListing implements Parcelable {
	private String mAuction_date;
	private String mBathroom_number;
	private String mBedroom_number;
	private String mCar_spaces;
	private String mCommission;
	private String mConstruction_year;
	private String mDatasource_name;
	private String mGuid;
	private String mImg_height;
	private String mImg_url;
	private String mImg_width;
	private String mKeywords;
	private String mLatitude;
	private String mLister_name;
	private String mLister_url;
	private String mListing_type;
	private String mLocation_accuracy;
	private String mLongitude;
	private String mPrice;
	private String mPrice_currency;
	private String mPrice_formatted;
	private String mPrice_high;
	private String mPrice_low;
	private String mPrice_type;
	private String mProperty_type;
	private String mSummary;
	private String mThumb_height;
	private String mThumb_url;
	private String mthumb_width;
	private String mTitle;
	private String mUpdated_in_days;
	private String mUpdated_in_days_formatted;

	public NestoriaListing(String auction_date, String bathroom_number,
			String bedroom_number, String car_spaces, String commission,
			String construction_year, String datasource_name, String guid,
			String img_height, String img_url, String img_width,
			String keywords, String latitude, String lister_name,
			String lister_url, String listing_type, String location_accuracy,
			String longitude, String price,
			String price_currency, String price_formatted, String price_high,
			String price_low, String price_type, String property_type,
			String summary, String thumb_height, String thumb_url,
			String Thumb_width, String title, String updated_in_days,
			String updated_in_days_formatted) {
		super();
		mAuction_date = auction_date;
		mBathroom_number = bathroom_number;
		mBedroom_number = bedroom_number;
		mCar_spaces = car_spaces;
		mCommission = commission;
		mConstruction_year = construction_year;
		mDatasource_name = datasource_name;
		mGuid = guid;
		mImg_height = img_height;
		mImg_url = img_url;
		mImg_width = img_width;
		mKeywords = keywords;
		mLatitude = latitude;
		mLister_name = lister_name;
		mLister_url = lister_url;
		mListing_type = listing_type;
		mLocation_accuracy = location_accuracy;
		mLongitude = longitude;
		mPrice = price;
		mPrice_currency = price_currency;
		mPrice_formatted = price_formatted;
		mPrice_high = price_high;
		mPrice_low = price_low;
		mPrice_type = price_type;
		mProperty_type = property_type;
		mSummary = summary;
		mThumb_height = thumb_height;
		mThumb_url = thumb_url;
		mthumb_width = Thumb_width;
		mTitle = title;
		mUpdated_in_days = updated_in_days;
		mUpdated_in_days_formatted = updated_in_days_formatted;

	}

	public String get_auction_date() {
		return mAuction_date;
	}

	public String get_bathroom_number() {
		return mBathroom_number;
	}

	public String get_bedroom_number() {
		return mBedroom_number;
	}

	public String get_car_spaces() {
		return mCar_spaces;
	}

	public String get_commission() {
		return mCommission;
	}

	public String get_construction_year() {
		return mConstruction_year;
	}

	public String get_datasource_name() {
		return mDatasource_name;
	}

	public String get_guid() {
		return mGuid;
	}

	public String get_img_height() {
		return mImg_height;
	}

	public String get_img_url() {
		return mImg_url;
	}

	public String get_img_width() {
		return mImg_width;
	}

	public String get_keywords() {
		return mKeywords;
	}

	public String get_latitude() {
		return mLatitude;
	}

	public String get_lister_name() {
		return mLister_name;
	}

	public String get_lister_url() {
		return mLister_url;
	}

	public String get_listing_type() {
		return mListing_type;
	}

	public String get_location_accuracy() {
		return mLocation_accuracy;
	}

	public String get_longitude() {
		return mLongitude;
	}

	public String get_price() {
		return mPrice;
	}

	public String get_price_currency() {
		return mPrice_currency;
	}

	public String get_price_formatted() {
		return mPrice_formatted;
	}

	public String get_price_high() {
		return mPrice_high;
	}

	public String get_price_low() {
		return mPrice_low;
	}

	public String get_price_type() {
		return mPrice_type;
	}

	public String get_property_type() {
		return mProperty_type;
	}

	public String get_summary() {
		return mSummary;
	}

	public String get_thumb_height() {
		return mThumb_height;
	}

	public String get_thumb_url() {
		return mThumb_url;
	}

	public String get_thumb_width() {
		return mthumb_width;
	}

	public String get_title() {
		return mTitle;
	}

	public String get_updated_in_days() {
		return mUpdated_in_days;
	}

	public String get_updated_in_days_formatted() {
		return mUpdated_in_days_formatted;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

}
