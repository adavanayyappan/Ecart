package com.am.induster.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.am.induster.Model.Users;
import com.am.induster.R;
import com.am.induster.SupportingFiles.UserList;
import com.am.induster.databinding.ListItemUsernameBinding;


import java.util.ArrayList;
import java.util.List;

public class ListAdminUserAdapter extends RecyclerView.Adapter<ListAdminUserAdapter.ViewHolder> {


    private Context mContext;
    private List<Users> list;
    private SparseBooleanArray selectedItems;
    private int selectedIndex = -1;
    private OnItemClick itemClick;

    public void setItemClick(OnItemClick itemClick) {
        this.itemClick = itemClick;
    }


    public ListAdminUserAdapter(Context mContext, List<Users> list) {
        this.mContext = mContext;
        this.list = list;
        selectedItems = new SparseBooleanArray();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ListItemUsernameBinding bi = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.list_item_username, parent, false);

        return new ViewHolder(bi);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.bi.from.setText(list.get(position).getName());
        holder.bi.email.setText(list.get(position).getEmail());
        holder.bi.phone.setText(list.get(position).getPhone());
        holder.bi.imageLetter.setText(list.get(position).getName().substring(0, 1));


        //Changes the activated state of this view.
        holder.bi.lytParent.setActivated(selectedItems.get(position, false));


        holder.bi.lytParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClick == null) return;
                itemClick.onItemClick(view, list.get(position), position);
            }
        });
        holder.bi.lytParent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (itemClick == null) {
                    return false;
                } else {
                    itemClick.onLongPress(view, list.get(position), position);
                    return true;
                }
            }
        });
        toggleIcon(holder.bi, position);

    }

    /*
       This method will trigger when we we long press the item and it will change the icon of the item to check icon.
     */
    private void toggleIcon(ListItemUsernameBinding bi, int position) {
        if (selectedItems.get(position, false)) {
            bi.lytImage.setVisibility(View.GONE);
            bi.lytChecked.setVisibility(View.VISIBLE);
            if (selectedIndex == position) selectedIndex = -1;
        } else {
            bi.lytImage.setVisibility(View.VISIBLE);
            bi.lytChecked.setVisibility(View.GONE);
            if (selectedIndex == position) selectedIndex = -1;
        }
    }

    /*
       This method helps you to get all selected items from the list
     */

    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    /*
       this will be used when we want to delete items from our list
     */
    public void removeItems(int position) {
        list.remove(position);
        selectedIndex = -1;

    }

    /*
       for clearing our selection
     */

    public void clearSelection() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    /*
             this function will toggle the selection of items
     */

    public void toggleSelection(int position) {
        selectedIndex = position;
        if (selectedItems.get(position, false)) {
            selectedItems.delete(position);
        } else {
            selectedItems.put(position, true);
        }
        notifyItemChanged(position);
    }

    /*
      How many items have been selected? this method exactly the same . this will return a total number of selected items.
     */

    public int selectedItemCount() {
        return selectedItems.size();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ListItemUsernameBinding bi;

        public ViewHolder(@NonNull ListItemUsernameBinding itemView) {
            super(itemView.getRoot());

            bi = itemView;
        }
    }


    public interface OnItemClick {

        void onItemClick(View view, Users inbox, int position);

        void onLongPress(View view, Users inbox, int position);
    }
}
