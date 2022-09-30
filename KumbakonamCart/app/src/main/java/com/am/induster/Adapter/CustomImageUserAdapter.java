package com.am.induster.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.am.induster.Activity.Admin.AdminBannerActivity;
import com.am.induster.Activity.User.ProductDetailsActivity;
import com.am.induster.Model.Products;
import com.am.induster.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomImageUserAdapter extends PagerAdapter {

    private Context mContext;
    ArrayList<Products> mProducts;

    public CustomImageUserAdapter(Context mContext, ArrayList<Products> products) {
        this.mContext = mContext;
        this.mProducts = products;
    }

    //view inflating..
    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.layout_home_banner_view,
                collection, false);
        ImageView imgV = (ImageView) layout.findViewById(R.id.banner_img);
        Picasso.with(mContext).load(mProducts.get(position).getImage()).fit().centerInside().into(imgV);
        imgV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProductDetailsActivity.class);
                intent.putExtra("pid", mProducts.get(position).getPid());
                mContext.startActivity(intent);
            }
        });

        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return mProducts.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
