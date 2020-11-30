package kr.hs.emirim.sharinggoal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private Button loginBtn;
    private TextView signupBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        init();
        setUp();
    }

    private void init() {
        loginBtn = findViewById(R.id.login_btn);
        signupBtn = findViewById(R.id.login_signup);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    private void setUp() {
        loginBtn.setOnClickListener(goMainPage);
        signupBtn.setOnClickListener(goSignupPage);
    }

    View.OnClickListener goMainPage = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String email = ((TextInputEditText) findViewById(R.id.login_email)).getText().toString();
            String password = ((TextInputEditText) findViewById(R.id.login_password)).getText().toString();


            if (email.length() > 0 && password.length() > 0) {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    startMainActivity();
                                } else {
                                    if (task.getException() != null) {
                                        showToast(task.getException().toString());

                                    } else {
                                        showToast("로그인에 실패하셨습니다.");
                                    }
                                }
                            }
                        });
            } else {
                showToast("모두 입력해주세요");
            }
//
        }
    };

    View.OnClickListener goSignupPage = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        }
    };


    private void startMainActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    private void showToast(String str) {
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
    }
}