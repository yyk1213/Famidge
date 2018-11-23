package com.example.yeon1213.famidge.logIn;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yeon1213.famidge.R;
import com.example.yeon1213.famidge.database.Database;
import com.example.yeon1213.famidge.main.MainActivity;
import com.example.yeon1213.famidge.signUp.SignUpActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity implements Button.OnClickListener {

    private EditText etID;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvSingUp;
    private TextView tvFindID;

    private String mID;
    private String mPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {
            case R.id.login_btn_login:
                mID = etID.getText().toString();
                mPassword = etPassword.getText().toString();

                mAuth = FirebaseAuth.getInstance();

                mAuth.signInWithEmailAndPassword(mID, mPassword)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //확인하기
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    checkRoomPassword(user);
                                } else {
                                    Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
            case R.id.login_text_signUp:
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                break;
            case R.id.login_text_findID:
                break;
        }
    }

    private void initView() {
        etID = findViewById(R.id.login_editText_id);
        etPassword = findViewById(R.id.login_editText_password);
        btnLogin = findViewById(R.id.login_btn_login);
        tvSingUp = findViewById(R.id.login_text_signUp);
        tvFindID = findViewById(R.id.login_text_findID);

        btnLogin.setOnClickListener(this);
        tvSingUp.setOnClickListener(this);
        tvFindID.setOnClickListener(this);
    }

    //db에서 user의 방 비밀번호를 가져온다.
    private void checkRoomPassword(FirebaseUser user) {

        Database.getInstance().collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                showPasswordDialog(documentSnapshot.get("roomName").toString(), documentSnapshot.get("roomName").toString());
                            }
                        } else {

                        }
                    }
                });
    }

    private void showPasswordDialog(String roomName, final String roomPassword) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_password_layout, null);
        TextView tvRoomName = dialogView.findViewById(R.id.room_password_roomName);
        final EditText etRoomPassword = dialogView.findViewById(R.id.room_password);

        tvRoomName.setText(roomName + " 비밀번호를 입력해주세요");

        builder.setView(dialogView)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //비밀번호 일치여부 확인
                        //일치하면 메인으로
                        if (etRoomPassword.getText().equals(roomPassword)) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).create();

        builder.show();
    }
}
