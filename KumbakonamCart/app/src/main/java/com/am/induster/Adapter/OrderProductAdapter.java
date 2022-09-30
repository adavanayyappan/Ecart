package com.am.induster.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.am.induster.Activity.Admin.AdminUserProductsActivity;
import com.am.induster.Model.Products;
import com.am.induster.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderProductAdapter extends RecyclerView.Adapter<OrderProductAdapter.ViewHolder>{
    private ArrayList<Products> listdata;
    private ArrayList<String> listQuantity;
    private Context mContext;

    // RecyclerView recyclerView;
    public OrderProductAdapter(Context mContext, ArrayList<Products> listdata, ArrayList<String> listQuantity) {
        this.listdata = listdata;
        this.mContext = mContext;
        this.listQuantity = listQuantity;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.cart_items_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Products model = listdata.get(position);
        final String quantity = listQuantity.get(position);
        holder.txtProductQuantity.setText("Quantity = " + quantity);
        holder.txtProductPrice.setText("Price = \u20B9" + model.getPrice());
        holder.txtProductName.setText("Product Name :" +model.getPname());
        holder.txtProductOffer.setText(model.getPoffer() + "%");
        holder.txtProductBrand.setText("Brand Name :" +model.getPofferbrand());
        holder.txtProductOfferPrice.setText("Offer Price = \u20B9" + model.getPofferprize());
        Picasso.with(mContext).load(model.getImage()).placeholder(R.drawable.app_icon).fit().centerCrop().into(holder.imageView);
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView txtProductQuantity,txtProductPrice, txtProductName, txtProductOffer, txtProductOfferPrice, txtProductBrand;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.product_image);
            this.txtProductName = (TextView) itemView.findViewById(R.id.cart_product_name);
            this.txtProductPrice = (TextView) itemView.findViewById(R.id.cart_product_price);
            this.txtProductQuantity = (TextView) itemView.findViewById(R.id.cart_product_quantity);
            this.txtProductOffer = (TextView) itemView.findViewById(R.id.product_offer);
            this.txtProductOfferPrice = (TextView) itemView.findViewById(R.id.cart_product_offer_price);
            this.txtProductBrand = (TextView) itemView.findViewById(R.id.cart_product_brand);
        }
    }
}
