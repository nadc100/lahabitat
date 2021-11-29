package org.fiap.lhabitat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    MaterialEditText et_username, et_password, et_email;
    Button registerBtn, goBack;
    String userName, email, password;

    FirebaseAuth mAuth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        et_username = findViewById(R.id.reg_username);
        et_email = findViewById(R.id.reg_email);
        et_password = findViewById(R.id.reg_password);
        registerBtn = findViewById(R.id.register_Account_btn);
        goBack = findViewById(R.id.goBack);

        mAuth = FirebaseAuth.getInstance();

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, StartActivity.class));
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = et_username.getText().toString();
                email = et_email.getText().toString();
                password = et_password.getText().toString();

                if(TextUtils.isEmpty(email)) {
                    et_email.setError("Required");
                } else if (TextUtils.isEmpty(userName)) {
                    et_username.setError("Required");
                } else if (TextUtils.isEmpty(password)) {
                    et_password.setError("Required");
                } else if (password.length() < 6 ) {
                    et_password.setError("El password debe contener al menos 6 caracteres");
                } else {
                    registerUser(userName, password, email);
                }
            }

        });

    }

    public void goBack() {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }

    public void onBackPressed() {
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if(id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void registerUser(String userName, String password, String email) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());

                    if (user != null) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("username", userName);
                        hashMap.put("email", email);
                        hashMap.put("id", user.getUid());

                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Se ha registrado satisfactoriamente", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegisterActivity.this, StartActivity.class)
                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                                }
                            }
                        });

                    }

                }

            }
        });

    }


}