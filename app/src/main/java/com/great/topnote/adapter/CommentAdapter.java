package com.great.topnote.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.great.topnote.R;
import com.great.topnote.adapter.getImage.GetImage;
import com.great.topnote.data.Comment;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.NoteHolder> {

    Activity context;
    List<Comment> commentList;
    public CommentAdapter(Activity context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.comment, parent, false);
        // lets create a recyclerview row item layout file
        return new NoteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {

        holder.Name.setText(
                commentList.get(commentList.size()-1-position)
                        .getUserName());

        holder.Details.setText(
                commentList.get(commentList.size()-1-position)
                        .getNote());
        GetImage.FireBase(position,holder.UserImage,context,commentList.get(commentList.size()-1-position).getUserId());

    }


    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public void addComment(Comment comment){
        commentList.add(comment);
        notifyItemInserted(commentList.size()-1);
        notifyItemChanged(commentList.size()-1);
        notifyItemRangeChanged(0,commentList.size()-1);

    }

    public static final class NoteHolder extends RecyclerView.ViewHolder{


        TextView Name,Details;
        ImageView  UserImage;
        View view;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            Name=itemView.findViewById(R.id.userName);
            UserImage =itemView.findViewById(R.id.userImage);
            Details=itemView.findViewById(R.id.note);
            view=itemView;
        }
    }

}
