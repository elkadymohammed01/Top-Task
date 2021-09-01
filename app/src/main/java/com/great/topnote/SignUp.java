package com.great.topnote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.great.topnote.data.TextCheck;
import com.great.topnote.data.User;
import com.great.topnote.data.myDbAdapter;

public class SignUp extends AppCompatActivity {

    EditText name,mail,phone,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setId();
    }
    private void setId(){
        name=findViewById(R.id.name);
        mail=findViewById(R.id.email);
        phone=findViewById(R.id.phone);
        password=findViewById(R.id.password);
    }

    public void SignUp(View view) {
        TextCheck textCheck=new TextCheck();
        if(!(textCheck.isEmpty(name,6,this,"Name")||textCheck.isEmpty(mail,12,this,"Email")
                ||textCheck.isEmpty(phone,11,this,"Phone") ||textCheck.isEmpty(password,6,this,"Password"))){
            User user=new User(name.getText().toString()+"",phone.getText().toString()
                    ,mail.getText().toString(),password.getText().toString(),mail.getText()
                    .toString().substring(0,mail.getText().toString().length()-4));
            addUser(user);

        }
    }

    private FirebaseAuth mAuth;

    private void addUser(User user){
        // ...
        // Initialize Firebase Auth
        FirebaseDatabase.getInstance().getReference().child("User")
                .child(user.getId()).setValue(user);
        final FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(),user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Check Your Email",Toast.LENGTH_LONG).show();

                            //Your Account
                            new myDbAdapter(getApplicationContext()).insertUserData(name.getText().toString(),
                                    name.getText().toString(),
                                    mail.getText().toString(),
                                    password.getText().toString(),"");

                            //Your group
                            new myDbAdapter(getApplicationContext())
                                    .insertGroupData(mail.getText().toString().substring(0,mail.getText().toString().length()-4),
                                            "me");

                            finish();
                            startActivity(new Intent(getApplicationContext(),MainPage.class));}
                        else
                            Toast.makeText(getApplicationContext(),"Please try Again", Toast.LENGTH_LONG).show();
                    }
                });
                else
                    Toast.makeText(getApplicationContext(),"Please try Again", Toast.LENGTH_LONG).show();

            }
        });

    }


    public void SignIn(View view) {
        finish();
        startActivity(new Intent(this,logIn.class));
    }

}
