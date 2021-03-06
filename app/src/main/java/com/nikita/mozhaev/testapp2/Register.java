package com.nikita.mozhaev.testapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    EditText email;
    EditText pass;

    Button btn_login;
    Button btn_redict;


    FirebaseAuth auth;
    DatabaseReference reference;

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        if (firebaseUser != null) {
            Intent intent = new Intent(Register.this, Home.class);
            startActivity(intent);
            finish();
        }

        auth = FirebaseAuth.getInstance();


        email = findViewById(R.id.email_text);
        pass = findViewById(R.id.password_text);

        btn_login = findViewById(R.id.btn_login);
        btn_redict = findViewById(R.id.btn_redict);

        btn_redict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, MainActivity.class));
                finish();
            }
        });


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString();
                String txt_pass = pass.getText().toString();

                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_pass)) {
                    Toast.makeText(Register.this, "???????? ??????????!", Toast.LENGTH_LONG).show();
                } else if (txt_pass.length() < 6) {

                    Toast.makeText(Register.this, "???????????? ???????????? ??????????????????, ?????? ?????????????? 6 ????????????????", Toast.LENGTH_LONG).show();
                } else {
                    register(txt_email, txt_pass);
                }
            }
        });

    }

    private void register(final String email, String password) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = auth.getCurrentUser();
                String userid = firebaseUser.getUid();

                reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id", userid);
                hashMap.put("email", email);


                reference.setValue(hashMap).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        finish();
                        Intent intent = new Intent(Register.this, Home.class);
                        startActivity(intent);
                    }
                });

            } else {
                Toast.makeText(Register.this, "?????????????? ???? ?????? ??????????????????????????????!", Toast.LENGTH_LONG).show();
            }
        });
    }

}