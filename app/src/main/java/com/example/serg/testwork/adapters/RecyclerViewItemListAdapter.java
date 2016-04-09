package com.example.serg.testwork.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.serg.testwork.R;
import com.example.serg.testwork.models.MyData;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by serg on 09.04.16.
 */
public class RecyclerViewItemListAdapter extends RecyclerView.Adapter<RecyclerViewItemListAdapter.Holder> {

    private List<MyData> dateList = new ArrayList<>();

    public RecyclerViewItemListAdapter(List<MyData> dateList) {
        this.dateList.addAll(dateList);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_list_name, viewGroup, false);
        Holder Holder = new Holder(view);
        return Holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        MyData data = new MyData(dateList.get(position));
//        holder.imageImageView.setImageDrawable.getUrlImage());
        holder.nameTextView.setText(data.getName());
        holder.typeTextView.setText(data.getTypeMusic());
        holder.infoTextView.setText(data.getInfo());
    }

    @Override
    public int getItemCount() {
        return dateList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_list_image)
        ImageView imageImageView;
        @Bind(R.id.item_list_name)
        TextView nameTextView;
        @Bind(R.id.item_list_type)
        TextView typeTextView;
        @Bind(R.id.item_list_info)
        TextView infoTextView;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
