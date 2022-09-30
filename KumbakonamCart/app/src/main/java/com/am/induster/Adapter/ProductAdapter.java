package com.am.induster.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.am.induster.Activity.Admin.CustomCategoryAdapter;
import com.am.induster.Activity.Admin.MyOnClickListener;
import com.am.induster.Activity.User.HomeProductActivity;
import com.am.induster.Model.Products;
import com.am.induster.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductAdapter  extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{
    private ArrayList<Products> listdata;
    private Context mContext;
    private final View.OnClickListener mOnClickListener = new MyOnClickListener();
    private ItemClickListener mClickListener;

    // RecyclerView recyclerView;
    public ProductAdapter(Context mContext, ArrayList<Products> listdata) {
        this.listdata = listdata;
        this.mContext = mContext;
    }
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.product_items_layout, parent, false);
        ProductAdapter.ViewHolder viewHolder = new ProductAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ProductAdapter.ViewHolder holder, int position) {
        final Products model = listdata.get(position);
        holder.txtProductName.setText(model.getPname());
        holder.txtProductDescription.setText("Description: "  + model.getDescription());
        holder.txtProductPrice.setText("Price: \u20B9"  + model.getPrice());
        holder.txtProductOffer.setText(model.getPofferprize() + "%");
        holder.txtProductOfferPrice.setText("Offer Price: \u20B9" + model.getPoffer());
        holder.txtProductBrand.setText("Brand: "  + model.getPofferbrand());
        Picasso.with(mContext).load(model.getImage()).fit().centerCrop().into(holder.imageView);
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imageView;
        public TextView txtProductDescription,txtProductPrice, txtProductName, txtProductOffer, txtProductOfferPrice, txtProductBrand;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.product_image);
            this.txtProductName = (TextView) itemView.findViewById(R.id.product_name);
            this.txtProductPrice = (TextView) itemView.findViewById(R.id.product_price);
            this.txtProductDescription = (TextView) itemView.findViewById(R.id.product_description);
            this.txtProductOffer = (TextView) itemView.findViewById(R.id.product_offer);
            this.txtProductBrand = (TextView) itemView.findViewById(R.id.product_brand_name);
            this.txtProductOfferPrice = (TextView) itemView.findViewById(R.id.product_offer_price);
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

