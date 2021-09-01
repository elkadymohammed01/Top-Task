package com.great.topnote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.great.topnote.data.myDbAdapter;

public class logIn extends AppCompatActivity {

    EditText mail,password;
    TextView fPassword ,SignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        setId();
        animation();
    }
    private void setId(){
        mail=findViewById(R.id.email);
        password=findViewById(R.id.password);
        fPassword=findViewById(R.id.forgetPassword);
        SignIn=findViewById(R.id.textView3);

    }
    private void animation(){
        // Out of Screen
        mail.animate().translationX(-1000).alpha(0).setStartDelay(0).setDuration(0).start();
        password.animate().translationX(-1000).alpha(0).setStartDelay(0).setDuration(0).start();
        fPassword.animate().translationX(-1000).alpha(0).setStartDelay(0).setDuration(0).start();
        SignIn.animate().translationY(1000).alpha(0).setStartDelay(0).setDuration(0).start();

        // Back Again
        mail.animate().translationX(0).alpha(1).setStartDelay(50).setDuration(900).start();
        password.animate().translationX(0).alpha(1).setStartDelay(150).setDuration(900).start();
        fPassword.animate().translationX(0).alpha(1).setStartDelay(250).setDuration(900).start();
        SignIn.animate().translationY(0).alpha(1).setStartDelay(350).setDuration(900).start();
    }

    public void SignUp(View view) {
        finish();
        startActivity(new Intent(this,SignUp.class));
    }

    public void SignIn(View view) {
        // ...
        // Initialize Firebase Auth
        final FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(mail.getText().toString(),password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    myDbAdapter Db =new myDbAdapter(getApplicationContext());
                    FirebaseDatabase.getInstance().getReference().child("User")
                            .child(mail.getText().toString().substring(0,mail.getText().toString().length()-4))
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    Db.insertUserData(snapshot.child("name").getValue().toString(),
                                            snapshot.child("phone").getValue().toString(),
                                            mail.getText().toString(),
                                            password.getText().toString(),"");
                                    Db.insertGroupData(mail.getText().toString().substring(0,mail.getText().toString().length()-4),"me");

                                    finish();
                                    startActivity(new Intent(getApplicationContext(),MainPage.class));
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });}
                else
                    Toast.makeText(getApplicationContext(),"Please try Again", Toast.LENGTH_LONG).show();
            }
        });
    }
}
