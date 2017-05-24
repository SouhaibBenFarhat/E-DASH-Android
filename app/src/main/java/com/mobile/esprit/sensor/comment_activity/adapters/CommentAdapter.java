package com.mobile.esprit.sensor.comment_activity.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobile.esprit.sensor.Entities.Comment;
import com.mobile.esprit.sensor.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Souhaib on 27/03/2017.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CustomViewHolder> {
    private List<Comment> comments;
    private Context mContext;

    public CommentAdapter(Context context, List<Comment> comments) {
        this.comments = comments;
        this.mContext = context;


    }

    @Override
    public CommentAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_item, null);
        CommentAdapter.CustomViewHolder viewHolder = new CommentAdapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CommentAdapter.CustomViewHolder customViewHolder, int i) {
        final Comment comment = comments.get(i);

        customViewHolder.userName.setText(comment.getUser().getFirstName() + " " + comment.getUser().getLastName());
        customViewHolder.commentContent.setText(comment.getContent());
        customViewHolder.commentDate.setText(comment.getDate().substring(0,10));
        Picasso.with(mContext).load(Uri.parse(comment.getUser().getProfilePicture())).into(customViewHolder.userImageView);
    }

    @Override
    public int getItemCount() {
        return (null != comments ? comments.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected CircleImageView userImageView;
        protected TextView userName;
        protected  TextView commentContent;
        protected  TextView commentDate;



        public CustomViewHolder(View view) {
            super(view);
            this.userImageView = (CircleImageView) view.findViewById(R.id.user_image_comment);
            this.userName = (TextView) view.findViewById(R.id.user_name_comment_content);
            this.commentContent = (TextView) view.findViewById(R.id.comment_content);
            this.commentDate = (TextView) view.findViewById(R.id.comment_date);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });
        }
    }


}