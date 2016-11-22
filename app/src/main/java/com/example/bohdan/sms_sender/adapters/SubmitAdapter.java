package com.example.bohdan.sms_sender.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bohdan.sms_sender.R;
import com.example.bohdan.sms_sender.data.SMS;

import java.util.List;

/**
 * Created by Bohdan on 01.07.2016.
 */
public class SubmitAdapter extends RecyclerView.Adapter<SubmitAdapter.SubmitViewHolder> {
    private List<SMS> list;
    private Context context;

    public SubmitAdapter(List<SMS> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @Override
    public SubmitViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_send, parent, false);
        // set the view's size, margins, paddings and layout parameters

        SubmitViewHolder vh = new SubmitViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(SubmitViewHolder holder, int position) {
        holder.text.setText(list.get(position).getName());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class SubmitViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        CardView cardView;

        public SubmitViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.textView_SubmitActivity);
            cardView = (CardView) itemView.findViewById(R.id.cardSubmit);

        }
    }
}