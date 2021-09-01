package com.great.topnote.adapter;

import android.content.Context;
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
import com.great.topnote.R;
import com.great.topnote.adapter.getImage.GetImage;
import com.great.topnote.data.myDbAdapter;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {

    Context context;
    List<String>email;
    String []Name;
    boolean[]Done;
    public UserAdapter(Context context,List<String>email) {
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
        if(viewType==1)
            view = LayoutInflater.from(context).inflate(R.layout.usercard, parent, false);
        else
            view = LayoutInflater.from(context).inflate(R.layout.group_id, parent, false);
        // lets create a recyclerview row item layout file
        return new UserHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0)
            return 0;

        return 1;
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        if(position>=1)
            OnUserType(holder.view ,position);

    }

    public void OnUserType(View view,Integer pos){
        TextView name=view.findViewById(R.id.name)
                ,mail=view.findViewById(R.id.email)
                ,done=view.findViewById(R.id.done);
        ImageView userImage=view.findViewById(R.id.userImage);
        ImageView correct =view.findViewById(R.id.correct),wrong=view.findViewById(R.id.wrong);
        mail.setText(email.get(pos));
        name.setText(Name[pos]);
        if(Done[pos]){
            done.setVisibility(View.VISIBLE);
            correct.setVisibility(View.INVISIBLE);
            wrong.setVisibility(View.INVISIBLE);
        }else {
            done.setVisibility(View.INVISIBLE);
            correct.setVisibility(View.VISIBLE);
            wrong.setVisibility(View.VISIBLE);
        }

        GetImage.FireBase(pos,userImage,context,email.get(pos));
        getUserDetails(pos);
        CorrectOrWrong(correct,wrong,pos);
    }

    private void CorrectOrWrong(ImageView correct,ImageView wrong,Integer pos){
        correct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFriend(pos);
                removeRequest(pos);
                removeItem(pos);

            }
        });
        wrong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeRequest(pos);
                removeItem(pos);

            }
        });

    }
    public void removeRequest(Integer pos){

        String UserMail = (new myDbAdapter(context).getUserData_inf()[1]);
        String substring = UserMail.substring(0, UserMail.length() - 4);
        FirebaseDatabase.getInstance().getReference()
                .child("FriendRequest").child(substring)
                .child(email.get(pos).substring(0,email.get(pos).length()-4))
                .removeValue();
    }
    public void AddFriend(Integer pos){

        String UserMail = (new myDbAdapter(context).getUserData_inf()[1]);
        String substring = UserMail.substring(0, UserMail.length() - 4);

        FirebaseDatabase.getInstance().getReference()
                .child("FriendList").child(substring)
                .child(email.get(pos).substring(0,email.get(pos).length()-4))
                .setValue(email.get(pos));

        FirebaseDatabase.getInstance().getReference()
                .child("FriendList")
                .child(email.get(pos).substring(0,email.get(pos).length()-4))
                .child(substring)
                .setValue(UserMail);

    }
    public void removeItem(Integer position){
            Done[position]=true;
            notifyItemChanged(position);

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
