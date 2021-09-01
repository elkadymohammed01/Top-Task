package com.great.topnote.adapter.getImage;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.HashMap;
import java.util.Map;

public class GetTrueImage implements OnSuccessListener<Uri> {
    private static Integer last =0;
    //sizeScreen is the number of item  which screen can show in One time with this  item  ..
    Integer pos,sizeScreen=5;
    ImageView image;
    Context context;

    //get position of Item which will help ous to now if it in screen or No ..
    //pass parameter  Map which will save Uri to lazy it again ..
    public GetTrueImage(Integer pos, ImageView image, Context context) {
        this.pos = pos;
        this.image = image;
        this.context = context;
    }

    //Download Image From Database (Firebase Database) ..
    @Override
    public void onSuccess(Uri uri) {
        if(Math.abs(pos-last)<sizeScreen)
            Glide.with(context).load(uri).into(image);
        map.put(pos,uri);
        if(pos>40)
            map.remove(pos-40);
        if(map.containsKey(pos+20))
            map.remove(pos+20);
    }

    //get last Item position show in Screen ..
    //it static to change this value in all item  Class which we Create
    public static void setLastOne(Integer l){
        last=l;
    }

    public static Map<Integer,Uri>map=new HashMap<>();

}