package com.example.testtp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth; //Firebase 인증
    private DatabaseReference mDatabaseRef; //실시간 데이터 베이스
    private EditText emailInput, pwdInput;
    private TextView registerBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        emailInput = findViewById(R.id.emailInput);
        pwdInput = findViewById(R.id.pwdInput);
        registerBtn = findViewById(R.id.registerBtn);

        registerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // 회원가입 데이터 처리 시작
                String strEmail = emailInput.getText().toString();
                String strPwd = pwdInput.getText().toString();

                // Firebase Auth 진행
                mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                                    UserAccount account = new UserAccount();
                                    account.setIdToken(firebaseUser.getUid());
                                    account.setEmailId(firebaseUser.getEmail());
                                    account.setPassword(strPwd);

                                    mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);
                                    Toast.makeText(RegisterActivity.this, "회원가입에 성공했습니다.", Toast.LENGTH_SHORT).show();
                                } else{
                                    Toast.makeText(RegisterActivity.this, "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });

    }
}
