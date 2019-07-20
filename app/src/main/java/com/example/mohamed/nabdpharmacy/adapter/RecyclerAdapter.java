package com.example.mohamed.nabdpharmacy.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.example.mohamed.nabdpharmacy.R;
import com.example.mohamed.nabdpharmacy.model.Product;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by mohamed on 01/10/17.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> implements FastScrollRecyclerView.SectionedAdapter {

    private List<Product> productList;
    private Context context;

    public RecyclerAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_view, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvName.setText(context.getString(R.string.product_name, productList.get(position).getName()));
        holder.tvExpirationDate.setText(context.getString(R.string.expiration_date_value, String.valueOf(productList.get(position).getExpirationDate())));
        holder.tvBoxesAmount.setText(String.valueOf(context.getString(R.string.boxes_amount_value, String.valueOf(productList.get(position).getPackagesAmount()))));
        holder.tvStripesAmount.setText(context.getString(R.string.stripes_amount_value, String.valueOf(productList.get(position).getStripesAmount())));

        // Glide.with(context).load(productList.get(position).getImagePath()).into(holder.imageView);

        Glide.with(context)
                .load(productList.get(position).getImagePath())
                .signature(new StringSignature(productList.get(position).getImagePath()))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        return Character.toString(productList.get(position).getName().charAt(0));
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private TextView tvName, tvExpirationDate, tvBoxesAmount, tvStripesAmount;
        private ImageView imageView;
        private CardView cardView;
        private LinearLayout linearLayout;

        MyViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvExpirationDate = itemView.findViewById(R.id.tv_expiration_date);
            tvBoxesAmount = itemView.findViewById(R.id.tv_boxes_amount);
            tvStripesAmount = itemView.findViewById(R.id.tv_stipe_amount);
            imageView = itemView.findViewById(R.id.img_product_item);
            cardView = itemView.findViewById(R.id.item_view_layout);
            linearLayout = itemView.findViewById(R.id.constraint_layout);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View view) {
            itemClickCallBack.onItemClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            itemClickCallBack.onItemLongClick(getAdapterPosition());
            return true;
        }
    }

    private ItemClickCallBack itemClickCallBack;

    public interface ItemClickCallBack {
        void onItemClick(int p);

        void onItemLongClick(int p);
    }

    public void setItemClickCallBack(ItemClickCallBack itemClickCallBack) {
        this.itemClickCallBack = itemClickCallBack;
    }

    //filter list when use search function
    public void setFilter(List<Product> newList) {
        productList = new ArrayList<>();
        productList.addAll(newList);
        notifyDataSetChanged();
    }

    //sort list by alphabets..
    public void alphabeticListSorting(List<Product> productList) {
        if (productList.size() > 0) {
            Collections.sort(productList, new Comparator<Product>() {
                @Override
                public int compare(final Product object1, final Product object2) {
                    return object1.getName().compareTo(object2.getName());
                }
            });
        }
        notifyDataSetChanged();
    }

    //sort list by date..
    public void dateSorting(List<Product> productList) {
        if (productList.size() > 0) {
            Collections.sort(productList, new Comparator<Product>() {
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy mm dd");

                @Override
                public int compare(Product product, Product t1) {

                    int comparingResult = 0;
                    try {
                        Date d1 = dateFormat.parse(product.getExpirationDate());
                        Date d2 = dateFormat.parse(product.getExpirationDate());
                        comparingResult = d1.compareTo(d2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        comparingResult = product.getExpirationDate().compareTo(t1.getExpirationDate());
                    }
                    return comparingResult;
                }
            });
        }

        notifyDataSetChanged();
    }

    //notify changes
    public void notifyChange(int position) {
        notifyItemRemoved(position);
    }

    public Product getItem(int position) {
        // Toast.makeText(context, ""+productList.get(position).getImagePath(), Toast.LENGTH_SHORT).show();
        return productList.get(position);
    }

}
