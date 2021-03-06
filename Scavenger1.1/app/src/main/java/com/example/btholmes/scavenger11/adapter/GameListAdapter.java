package com.example.btholmes.scavenger11.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.btholmes.scavenger11.R;
import com.example.btholmes.scavenger11.model.Game;

import java.util.ArrayList;
import java.util.List;

public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.ViewHolder> {

    private List<Game> items = new ArrayList<>();

    private Context ctx;

    // for item click listener
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, Game obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    // for item long click listener
    private OnItemLongClickListener mOnItemLongClickListener;

    public interface OnItemLongClickListener {
        void onItemClick(View view, Game obj, int position);
    }

    public void setOnItemLongClickListener(final OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView image;
        public ImageView icon;

        public TextView name;
        public TextView wordsLeft;

        public ViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image);
            icon = (ImageView) v.findViewById(R.id.icon);

            name = (TextView) v.findViewById(R.id.name);
            wordsLeft = (TextView) v.findViewById(R.id.wordsLeft);

        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public GameListAdapter(Context ctx, List<Game> items) {
        this.ctx = ctx;
        this.items = items;
    }

    @Override
    public GameListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_game, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Game p = items.get(position);
        if(p!=null){

            holder.name.setText(p.getGameID());
            holder.image.setBackgroundColor(ContextCompat.getColor(ctx, R.color.ten));
            holder.wordsLeft.setText(p.getWords().toString());


//            Picasso.with(ctx).load(p.getFriend().getPhoto()).resize(80, 80).transform(new CircleTransform()).into(holder.image);
//            holder.name.setText(p.getFriend().getName());
        }

        // content photo
//        if(p.getPhoto()!=-1){
//            holder.photo_content.setVisibility(View.VISIBLE);
//            holder.photo_content.setImageResource(p.getPhoto());
//        }
        // content text
//        if(p.getText()!=null){
//            holder.text_content.setVisibility(View.VISIBLE);
//            holder.text_content.setText(p.getText());
//        }
//
//        holder.text_date.setText(p.getDate());
//        holder.bt_more.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View view) {
//                Snackbar.make(view, "More Clicked", Snackbar.LENGTH_SHORT).show();
//            }
//        });
//        holder.bt_like.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Like Clicked", Snackbar.LENGTH_SHORT).show();
//            }
//        });
//
//        holder.bt_comment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Comment Clicked", Snackbar.LENGTH_SHORT).show();
//            }
//        });
//        holder.bt_share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Share Clicked", Snackbar.LENGTH_SHORT).show();
//            }
//        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}