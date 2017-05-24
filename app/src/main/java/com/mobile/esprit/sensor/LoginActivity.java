package com.mobile.esprit.sensor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.mobile.esprit.sensor.Entities.User;
import com.mobile.esprit.sensor.background_tasks.AccountManager;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "LOGIN_ACTIVITY";
    public GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    private Button facebookButton;
    private Button login_button;
    private Button googleButton;
    private EditText emailEditText, passwordEditText;


    private ImageButton ib_login_register;

    private SignInButton mGoogleButton;
    private LoginButton facebookLoginButton;
    private CallbackManager mCallbackManager;
    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            final AccountManager accountManager = new AccountManager(getApplicationContext());
            AccessToken accessToken = loginResult.getAccessToken();
            final Profile profile = Profile.getCurrentProfile();
            final User user = new User();

            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            Log.v("LoginActivity", response.toString());

                            try {

                                user.setEmail(response.getJSONObject().getString("email"));
                                user.setFirstName(Profile.getCurrentProfile().getFirstName());
                                user.setLastName(Profile.getCurrentProfile().getLastName());
                                user.setLinkUri(Profile.getCurrentProfile().getLinkUri().toString());
                                user.setLogin("--");
                                user.setPassword("--");
                                user.setProfileId(Profile.getCurrentProfile().getId());
                                user.setProvider("facebook");
                                user.setProfilePicture(Profile.getCurrentProfile().getProfilePictureUri(150, 150).toString());
                                accountManager.authentificateUserWithFacebook(user, LoginActivity.this);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender,birthday");
            request.setParameters(parameters);
            request.executeAsync();


        }

        @Override
        public void onCancel() {

            final Snackbar snackBar = Snackbar.make(findViewById(R.id.activity_login), "Oups! connection refused", Snackbar.LENGTH_LONG);
            snackBar.setAction("Dismiss", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackBar.dismiss();
                }
            });
            snackBar.show();
        }

        @Override
        public void onError(FacebookException error) {
            final Snackbar snackBar = Snackbar.make(findViewById(R.id.activity_login), "Oups! something went wrong with facebook", Snackbar.LENGTH_LONG);
            snackBar.setAction("Dismiss", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackBar.dismiss();
                }
            });
            snackBar.show();

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

            } else {

            }
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(getApplicationContext());
        emailEditText = (EditText) findViewById(R.id.et_email);
        passwordEditText = (EditText) findViewById(R.id.et_password);
        LoginManager.getInstance().logOut();





        // SETUP FACEBOOK LOGIN
        mCallbackManager = CallbackManager.Factory.create();
        facebookLoginButton = (LoginButton) findViewById(R.id.facebook_login_button);
        facebookLoginButton.setVisibility(View.INVISIBLE);
        facebookLoginButton.setReadPermissions("email", "public_profile");
        facebookLoginButton.registerCallback(mCallbackManager, mCallback);


        //SETUP GOOGLE PLUS BUTTON

        googleButton = (Button) findViewById(R.id.google_button);
        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {


            }
        };
        mGoogleButton = (SignInButton) findViewById(R.id.google_signin_button);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(LoginActivity.this, "Something went wrong with GOOGLE ACCOUNT", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mGoogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });


        login_button = (Button) findViewById(R.id.login_button);
        ib_login_register = (ImageButton) findViewById(R.id.ib_login_register);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (emailEditText.getText().toString().equals("") || passwordEditText.getText().toString().equals("")) {
                    final Snackbar snackBar = Snackbar.make(findViewById(R.id.activity_login), "Oups! Please fill in all fields...", Snackbar.LENGTH_LONG);
                    snackBar.setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackBar.dismiss();
                        }
                    });
                    snackBar.show();
                } else {
                    if (Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText()).matches()) {
                        if (passwordEditText.getText().toString().length() >= 8) {
                            AccountManager accountManager = new AccountManager(getApplicationContext());
                            accountManager.authentificateUserAnonymosly(emailEditText.getText().toString(), passwordEditText.getText().toString(), LoginActivity.this);
                        } else {
                            final Snackbar snackBar = Snackbar.make(findViewById(R.id.activity_login), "Oups! Password must be more than 8 characters...", Snackbar.LENGTH_LONG);
                            snackBar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    snackBar.dismiss();
                                }
                            });
                            snackBar.show();
                        }
                    } else {
                        final Snackbar snackBar = Snackbar.make(findViewById(R.id.activity_login), "Oups! Please enter a valide EMAIL...", Snackbar.LENGTH_LONG);
                        snackBar.setAction("Dismiss", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                snackBar.dismiss();
                            }
                        });
                        snackBar.show();
                    }
                }


            }
        });

        ib_login_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


        facebookButton = (Button) findViewById(R.id.facebook_button);
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                facebookLoginButton.performClick();
            }
        });
    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            AccountManager accountManager = new AccountManager(getApplicationContext());
                            User user = new User();
                            user.setLinkUri("--");
                            user.setLogin("--");
                            user.setLastName("");
                            user.setEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                            user.setFirstName(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                            user.setPassword("--");
                            user.setProfileId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            user.setProfilePicture(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString());
                            user.setProvider("google");
                            accountManager.authentificateUserWithGoogle(user, LoginActivity.this);
                        }


                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }

}
