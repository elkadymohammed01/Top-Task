package com.great.topnote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.great.topnote.data.myDbAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkMe();
    }
    private void checkMe(){
        final FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        myDbAdapter myDb=new myDbAdapter(this);
        firebaseAuth.signInWithEmailAndPassword(myDb.getUserData_inf()[1],myDb.getUserData_inf()[3])
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            finish();
                            startActivity(new Intent(getApplicationContext(),MainPage.class));}
                        else{
                            finish();
                            startActivity(new Intent(getApplicationContext(),logIn.class));
                        }
                    }
                });
    }
}
