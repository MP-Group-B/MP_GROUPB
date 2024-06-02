package com.example.testtp;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.annotation.Nonnull;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailInput, pwdInput, nameInput, telInput, pwdReInput;
    private Button registerBtn, backBtn, pwdCheckBtn;
    private String email, password, passwordCheck;
    FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        emailInput = (EditText) findViewById(R.id.emailInput);
        pwdInput = (EditText) findViewById(R.id.pwdInput);
        pwdReInput = (EditText) findViewById(R.id.pwdReInput);
        nameInput = (EditText) findViewById(R.id.telInput);
        telInput = (EditText) findViewById(R.id.telInput);
        registerBtn = (Button) findViewById(R.id.registerBtn);
        backBtn = (Button) findViewById(R.id.backBtn);
        pwdCheckBtn = (Button) findViewById(R.id.pwdCheckBtn);

        email = emailInput.getText().toString().trim();
        password = pwdInput.getText().toString().trim();
        passwordCheck = pwdReInput.getText().toString().trim();

        pwdCheckBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(password.equals(passwordCheck)){
                    Toast.makeText(getApplicationContext(), "비밀번호 확인 완료", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(getApplicationContext(), "비밀번호 확인 안됨", Toast.LENGTH_SHORT).show();
                }
            }
        });


        if(email != null || !email.isEmpty() || password != null || !password.isEmpty()){
            Log.i("test", "test : success");
        }else{
            Log.i("test", "test : fail");
        }

        registerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final String email = emailInput.getText().toString().trim();
                final String pwd = pwdInput.getText().toString().trim();
                final String name = nameInput.getText().toString().trim();
                final String tel = telInput.getText().toString().trim();

                firebaseAuth.createUserWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>(){
                            @Override
                            public void onComplete(@Nonnull Task<AuthResult> task){
                                if(task.isSuccessful()){
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else{
                                    Toast.makeText(RegisterActivity.this, "등록 에러", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        });
            }
        });
    }

}
