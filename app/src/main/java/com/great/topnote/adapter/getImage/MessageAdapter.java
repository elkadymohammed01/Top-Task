package com.great.topnote.adapter.getImage;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.great.topnote.R;
import com.great.topnote.data.Comment;
import com.great.topnote.data.Message;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.NoteHolder> {

    Activity context;
    List<Message> MessageList;
    String Email;
    public MessageAdapter(Activity context, List<Message> Messages ,String email) {
        this.context = context;
        this.MessageList = Messages;
        this.Email=email;
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.mymessage, parent, false);
        if(viewType==1)
            view=LayoutInflater.from(context).inflate(R.layout.message,parent,false);
        // lets create a recyclerview row item layout file
        return new NoteHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if(Email.equals(MessageList.get(position).getId()))
        return 0;
        else
            return 1;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {

        SelectSender sender=new SelectSender(holder.view,MessageList.get(MessageList.size()-(1+position)),Email);
        sender.ItemMessage();
    }


    @Override
    public int getItemCount() {
        return MessageList.size();
    }

    public void addComment(Message message){

        MessageList.add(message);
        notifyItemInserted(MessageList.size()-1);
        notifyItemChanged(MessageList.size()-1);
        notifyItemRangeChanged(0,MessageList.size()-1);

    }

    public static final class NoteHolder extends RecyclerView.ViewHolder{

        View view;

        public NoteHolder(@NonNull View itemView) {

            super(itemView);
            view=itemView;
        }
    }

}
class SelectSender{

    View view;
    Message message;
    String Email;

    public SelectSender(View view, Message message, String email) {
        this.view = view;
        this.message = message;
        Email = email;
    }

    public void ItemMessage(){
        if(Email.equals(message.getId()))
            setMessage(new MyMessage());
        else
            setMessage(new OtherMessage());
    }
    private void setMessage(MessageType messageType){

        messageType.setDetails(message,view);

    }
}

interface MessageType {
    public void setDetails(Message message ,View view);
}

class MyMessage implements  MessageType{

    public void setDetails(Message message, View view) {

        TextView details=view.findViewById(R.id.note);
        details.setText(message.getMessage());
    }
}

class OtherMessage implements  MessageType{

    public void setDetails(Message message, View view) {

        TextView details=view.findViewById(R.id.note);
        details.setText(message.getMessage());

    }
}

