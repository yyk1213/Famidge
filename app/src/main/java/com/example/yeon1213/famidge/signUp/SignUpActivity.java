package com.example.yeon1213.famidge.signUp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yeon1213.famidge.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements Button.OnClickListener {

    public static final String TAG = "SignUpActivity";

    private EditText etNewID;
    private EditText etNewPassword;
    private EditText etFamilyRoom;
    private Button btnSignUp;

    private String mNewID;
    private String mNewPassword;
    private String mRoom;
    private Map<String, Object> mUserData;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initView();
    }

    private void initView() {
        etNewID = findViewById(R.id.input_id);
        etNewPassword = findViewById(R.id.input_password);
        etFamilyRoom = findViewById(R.id.search_room);
        btnSignUp = findViewById(R.id.sign_up);

        btnSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (etNewID.getText().toString().equals("") || etNewPassword.getText().toString().equals("") || etFamilyRoom.getText().toString().equals("")) {

            Toast.makeText(this, "입력을 완료해주세요.", Toast.LENGTH_SHORT).show();
        } else {
            mNewID = etNewID.getText().toString();
            mNewPassword = etNewPassword.getText().toString();
            mRoom = etFamilyRoom.getText().toString();

            signUp();
            saveData();
        }
    }

    private void signUp() {

        mAuth = FirebaseAuth.getInstance();
        mUserData = new HashMap<>();

        mAuth.createUserWithEmailAndPassword(mNewID, mNewPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mUserData.put("ID", mNewID);
                            mUserData.put("Password", mNewPassword);
                            mUserData.put("Room", mRoom);

                            mUser = mAuth.getCurrentUser();
                        } else {
                            Log.e(TAG, "회원가입 실패");
                        }
                    }
                });
    }

    private void saveData() {

        FirebaseFirestore DB = FirebaseFirestore.getInstance();

        DB.collection("users")
                 .add(mUserData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //인텐트 값 넣어서 보내주기 mUser
                        //그 다음 화면으로 넘어가기
                        startActivity(new Intent(SignUpActivity.this, CompleteSignUpActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "데이터 저장 실패");
                    }
                });
    }
}
