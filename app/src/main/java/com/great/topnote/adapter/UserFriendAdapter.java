package com.great.topnote.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.great.topnote.Chatting;
import com.great.topnote.R;
import com.great.topnote.adapter.getImage.GetImage;
import com.great.topnote.data.Save;
import com.great.topnote.data.myDbAdapter;

import java.util.List;

public class UserFriendAdapter extends RecyclerView.Adapter<UserFriendAdapter.UserHolder> {

    Context context;
    List<String>email;
    String []Name;
    boolean[]Done;
    public UserFriendAdapter(Context context, List<String>email) {
        this.context = context;
        this.Name=new String[email.size()];
        this.Done=new boolean[email.size()];
        this.email=email;
        for(int i=0;i<Name.length;i++)
            Name[i]="";
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
            view = LayoutInflater.from(context).inflate(R.layout.userfriend, parent, false);

        return new UserHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {

            OnUserType(holder.view ,position);

    }

    public void OnUserType(View view,Integer pos){
        TextView name=view.findViewById(R.id.name)
                ,mail=view.findViewById(R.id.email);
        ImageView userImage=view.findViewById(R.id.userImage);

        mail.setText(email.get(pos));
        name.setText(Name[pos]);

        GetImage.FireBase(pos,userImage,context,email.get(pos));
        getUserDetails(pos);

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save.Id=email.get(pos).substring(0,email.get(pos).length()-4);
                context.startActivity(new Intent(context, Chatting.class));
            }
        });

    }



    public void getUserDetails(Integer pos ){

        if(Name[pos].length()==0)
      FirebaseDatabase.getInstance().getReference().child("User")
              .child(email.get(pos).substring(0,email.get(pos).length()-4))
              .child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                  @Override
                  public void onDataChange(@NonNull DataSnapshot snapshot) {
                      Name[pos]=snapshot.getValue().toString();
                      notifyItemChanged(pos);
                  }

                  @Override
                  public void onCancelled(@NonNull DatabaseError error) {

                  }
              });

    }
    @Override
    public int getItemCount() {

        return email.size();
    }
    public static final class UserHolder extends RecyclerView.ViewHolder{
        View view ;
        public UserHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
        }

    }

}
