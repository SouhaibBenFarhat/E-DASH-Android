package com.mobile.esprit.sensor;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mobile.esprit.sensor.Entities.User;
import com.mobile.esprit.sensor.background_tasks.AccountManager;

public class RegisterActivity extends AppCompatActivity {

    private EditText username, password, confirmPassword, email;
    private Button registerButton;
    private AccountManager accountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        username = (EditText) findViewById(R.id.reg_name);
        password = (EditText) findViewById(R.id.reg_password);
        confirmPassword = (EditText) findViewById(R.id.reg_con_password);
        email = (EditText) findViewById(R.id.reg_email);
        registerButton = (Button) findViewById(R.id.reg_button);
        accountManager = new AccountManager(getApplicationContext());


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (username.getText().toString().isEmpty() || password.getText().toString().isEmpty() || confirmPassword.getText().toString().isEmpty() || email.getText().toString().isEmpty()) {
                    final Snackbar snackBar = Snackbar.make(findViewById(R.id.activity_register), "Oups! please complete all fields... ", Snackbar.LENGTH_LONG);
                    snackBar.setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackBar.dismiss();
                        }
                    });
                    snackBar.show();


                } else {
                    if (!(password.getText().toString().equals(confirmPassword.getText().toString()))) {



                        final Snackbar snackBar = Snackbar.make(findViewById(R.id.activity_register), "Oups! password is not the same... ", Snackbar.LENGTH_LONG);
                        snackBar.setAction("Dismiss", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                snackBar.dismiss();
                            }
                        });
                        snackBar.show();


                    } else {

                        if (password.getText().toString().length()<8){
                            final Snackbar snackBar = Snackbar.make(findViewById(R.id.activity_register), "Oups! password should be more thant 8 characters... ", Snackbar.LENGTH_LONG);
                            snackBar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    snackBar.dismiss();
                                }
                            });
                            snackBar.show();
                            return;
                        }



                        if (!(Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches())) {
                            final Snackbar snackBar = Snackbar.make(findViewById(R.id.activity_register), "Oups! please enter a valid EMAIL... ", Snackbar.LENGTH_LONG);
                            snackBar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    snackBar.dismiss();
                                }
                            });
                            snackBar.show();


                        } else {
                            User user = new User();
                            user.setProvider("anonymous");
                            user.setProfilePicture("--");
                            user.setProfileId("--");
                            user.setPassword(password.getText().toString());
                            user.setEmail(email.getText().toString());
                            user.setFirstName(username.getText().toString());
                            user.setLastName("--");
                            user.setLinkUri("--");
                            user.setLogin(username.getText().toString());
                            accountManager.registerUserAnonymosly(user,RegisterActivity.this);
                        }
                    }
                }


            }
        });
    }
}
