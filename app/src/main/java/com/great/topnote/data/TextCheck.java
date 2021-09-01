package com.great.topnote.data;

import android.content.Context;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

import com.great.topnote.R;

public class TextCheck {
    public boolean isEmpty(EditText ed, int size, Context context,String hint){

        int val=0;
        for(int i=0;i<ed.getText().toString().length();i++)
            if(ed.getText().toString().charAt(i)!=' '&&ed.getText().toString().charAt(i)!='\n')
                val++;
        if(size>val){
            ed.startAnimation(shakeError());
            ed.setHintTextColor(context.getResources().getColor(R.color.red));
            ed.setText("");
            ed.setHint(hint+" is too Small .");
            return  true;
        }
        return false;
    }
    public TranslateAnimation shakeError() {
        TranslateAnimation shake = new TranslateAnimation(0, 10, 0, 0);
        shake.setDuration(500);
        shake.setInterpolator(new CycleInterpolator(7));
        return shake;
    }
}
