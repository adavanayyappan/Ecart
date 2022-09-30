package com.am.induster.ViewHolder;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.am.induster.Interface.ItemClickListner;
import com.am.induster.R;


public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName, txtProductPrice, txtProductQuantity,txtProductOffer, txtProductOfferPrice, txtProductTotalPrice, txtBrand;
    private ItemClickListner itemClickListner;
    public ImageView imageView;

    public CartViewHolder(View itemView)
    {
        super(itemView);

        txtProductName = itemView.findViewById(R.id.cart_product_name);
        txtProductPrice = itemView.findViewById(R.id.cart_product_price);
        txtProductQuantity = itemView.findViewById(R.id.cart_product_quantity);
        imageView = (ImageView) itemView.findViewById(R.id.product_image);
        txtProductOffer = (TextView) itemView.findViewById(R.id.product_offer);
        txtProductOfferPrice = (TextView) itemView.findViewById(R.id.cart_product_offer_price);
        txtProductTotalPrice = (TextView) itemView.findViewById(R.id.cart_product_total_price);
        txtBrand = itemView.findViewById(R.id.cart_product_brand);
    }

    @Override
    public void onClick(View view)
    {
        itemClickListner.onClick(view, getAdapterPosition(), false);
    }

    public void setItemClickListner(ItemClickListner itemClickListner)
    {
        this.itemClickListner = itemClickListner;
    }
}
