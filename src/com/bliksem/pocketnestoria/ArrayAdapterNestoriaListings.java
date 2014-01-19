package com.bliksem.pocketnestoria;

import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;


public class ArrayAdapterNestoriaListings extends ArrayAdapter<NestoriaListing> {
    private ImageLoader mImageLoader;
    
    public ArrayAdapterNestoriaListings(Context context, 
                              int textViewResourceId, 
                              List<NestoriaListing> objects,
                              ImageLoader imageLoader
                              ) {
        super(context, textViewResourceId, objects);
        mImageLoader = imageLoader;
    }

    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.listview_listing_row, null);
        }
        
        ViewHolder holder = (ViewHolder) v.getTag(R.id.id_holder);       
        
        if (holder == null) {
            holder = new ViewHolder(v);
            v.setTag(R.id.id_holder, holder);
        }        
        
        NestoriaListing entry = getItem(position);
        if (entry.get_img_url() != null) {
            holder.image.setImageUrl(entry.get_img_url(), mImageLoader);
        } else {
            holder.image.setImageResource(R.drawable.device_camera);
        }
        
        holder.title.setText(entry.get_title());
        holder.keywords.setText(entry.get_keywords());
        holder.price.setText("£" + entry.get_price_formatted().replace(" GBP",""));
        holder.listerName.setText(entry.get_lister_name());
        
        // random colour for rightColour View
        Random rnd = new Random(); 
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));   
        holder.rightColour.setBackgroundColor(color);
        
        return v;
    }
    
    
    private class ViewHolder {
        NetworkImageView image;
        TextView title; 
        TextView keywords; 
        TextView price; 
        TextView listerName;
        View rightColour;
        
        public ViewHolder(View v) {
            image = (NetworkImageView) v.findViewById(R.id.iv_thumb);
            title = (TextView) v.findViewById(R.id.tv_title);
            keywords = (TextView) v.findViewById(R.id.tv_keywords);
            price = (TextView) v.findViewById(R.id.tv_price);
            listerName = (TextView) v.findViewById(R.id.listview_lister_name);
            rightColour = (View) v.findViewById(R.id.listview_right_colour);
            
            v.setTag(this);
        }
    }
}

