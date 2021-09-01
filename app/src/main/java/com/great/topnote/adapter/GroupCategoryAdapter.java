package com.great.topnote.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.great.topnote.R;
import com.great.topnote.data.Group;
import com.great.topnote.data.GroupCategory;

import java.util.List;

public class GroupCategoryAdapter extends RecyclerView.Adapter<GroupCategoryAdapter.ProductViewHolder> {

    Context context;
    List<Group> GroupList;
    boolean show[];
    OnClickItem item;
    public GroupCategoryAdapter(Context context, List<Group> GroupList,OnClickItem item) {
        this.context = context;
        this.GroupList = GroupList;
        this.item=item;
        show=new boolean[GroupList.size()+1];
        show[0]=true;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.category_row_item, parent, false);
        // lets create a recyclerview row item layout file
        return new ProductViewHolder(view);
    }
    View last=null;
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        holder.catagoryName.setText(GroupList.get(position).getName());
        OnClick(holder.view,position);
        if(position==0&&last==null){
            last=holder.view;}
    if(show[position])
        holder.view.setBackground(context.getResources().getDrawable(R.drawable.ic_cat_bg2));
    else
        holder.view.setBackground(context.getResources().getDrawable(R.drawable.ic_cat_bg));
    }
    public void  OnClick(View item_view,Integer pos){
        item_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.ClickItem(pos);
                show=new boolean[GroupList.size()+1];
                show[pos]=true;
                last.setBackground(context.getResources().getDrawable(R.drawable.ic_cat_bg));
                item_view.setBackground(context.getResources().getDrawable(R.drawable.ic_cat_bg2));
                last=item_view;
            }
        });
    }
    @Override
    public int getItemCount() {
        return GroupList.size();
    }


    public static final class ProductViewHolder extends RecyclerView.ViewHolder{


        TextView catagoryName;
        View view;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            catagoryName = itemView.findViewById(R.id.cat_name);
            view =itemView;
        }

    }

}
