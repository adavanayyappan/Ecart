package com.am.induster.Activity.Admin;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.am.induster.R;
import com.squareup.picasso.Picasso;

public class CustomCategoryAdapter extends RecyclerView.Adapter<CustomCategoryAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    String[] arraylist;
    private final View.OnClickListener mOnClickListener = new MyOnClickListener();
    private ItemClickListener mClickListener;
    private String[] textureArrayWin = {
            "https://firebasestorage.googleapis.com/v0/b/induster-743aa.appspot.com/o/homeicons%2Freimgcon.png?alt=media&token=1ff70c34-fc55-4a01-ac85-3ea435a7ff12",
            "https://firebasestorage.googleapis.com/v0/b/induster-743aa.appspot.com/o/homeicons%2Fbajaj_auto.png?alt=media&token=84563bd2-d3a1-4b5c-b1f8-3c53831deed8",
            "https://firebasestorage.googleapis.com/v0/b/induster-743aa.appspot.com/o/homeicons%2Fmahindra.png?alt=media&token=9b3ff396-2ab5-4596-9191-14f8f8870192",
            "https://firebasestorage.googleapis.com/v0/b/induster-743aa.appspot.com/o/homeicons%2FTVS_Motor_Company-Logo.wine.png?alt=media&token=8351f568-6811-438e-a1d2-7cf5c086170f",
            "https://firebasestorage.googleapis.com/v0/b/induster-743aa.appspot.com/o/homeicons%2FLogo-Jawa.jpg?alt=media&token=766723c5-31cb-47a1-89bd-844e7bbab14a",
            "https://firebasestorage.googleapis.com/v0/b/induster-743aa.appspot.com/o/homeicons%2Fhonda.png?alt=media&token=ad9551e1-21b9-4756-bdbd-45657fddc0ac",
            "https://firebasestorage.googleapis.com/v0/b/induster-743aa.appspot.com/o/homeicons%2Fsuzuki.png?alt=media&token=48e758e7-844d-4718-a0a4-fad01421ba15",
            "https://firebasestorage.googleapis.com/v0/b/induster-743aa.appspot.com/o/homeicons%2Fdownload.jpg?alt=media&token=8ccf11ea-f340-429f-b7ff-e55a693569c2"
    };


    public CustomCategoryAdapter(Context context, String[] arraylist) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.arraylist = arraylist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_service, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.categoryName.setText(arraylist[position]);
        Picasso.with(context).load(textureArrayWin[position]).placeholder(R.drawable.app_icon).fit().into(holder.categoryIcon);
    }

    @Override
    public int getItemCount() {
        return arraylist.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView categoryName;
        ImageView categoryIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            categoryName = (TextView) itemView.findViewById(R.id.name);
            categoryIcon = (ImageView) itemView.findViewById(R.id.icon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getBindingAdapterPosition());
        }

    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

        // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
