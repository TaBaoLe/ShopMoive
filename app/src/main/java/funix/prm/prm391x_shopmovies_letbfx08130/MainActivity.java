package funix.prm.prm391x_shopmovies_letbfx08130;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import org.json.JSONException;



public class MainActivity extends AppCompatActivity {
    private SignInButton mBtnLoginGG;
    private static final int RC_SIGN_IN=0;
    public static final String TAG="TAG";
    private LoginButton mLoginButton;
    CallbackManager mCallbackManager;
    public static final String mypreference="mypref";
    private static GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );
        init ( );
        LoginManager.getInstance ( ).logOut ( );
        loginWithFb ( );
        googleSignIn ( );
    }

    /**
     * googleSignIn Method
     */
    private void googleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mBtnLoginGG.setOnClickListener ( v -> {
            switch (v.getId ()){
                case R.id.sign_in_button:
                    signIn();
                    break;
            }
        } );
    }

    private void init() {
        mLoginButton=findViewById ( R.id.login_button );
        mCallbackManager=CallbackManager.Factory.create ( );
        mBtnLoginGG = findViewById ( R.id.sign_in_button );
    }

    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent ();
        startActivityForResult ( signInIntent, RC_SIGN_IN);
}


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            setUpData ( acct );
        } catch (ApiException e) {
            Log.w("Error", "code=" + e.getStatusCode());
        }
    }

    /**
     *
     * set up data for handleSignInResult() method
     */
    private void setUpData(GoogleSignInAccount acct) {
        if (acct != null) {
            String name = acct.getDisplayName();
            String email = acct.getEmail();
            String userId = acct.getId();
            String imageUrl = String.valueOf(acct.getPhotoUrl());
            // Update data into SharedPreference file
            storeUserData(name, email, userId, imageUrl, "google");
            // Switch to MovieActivity
            Intent intent=new Intent ( MainActivity.this, MovieActivity.class );
            startActivity ( intent );
        }
    }

    public void storeUserData(String name, String email, String userId, String imageUrl, String account) {
        // Create and call SharedPreference file for editing
        SharedPreferences sharedPreferences=getSharedPreferences ( mypreference, MODE_PRIVATE );
        SharedPreferences.Editor editor=sharedPreferences.edit ( );
        // Save user info if logged in
        editor.putString ( "USERNAME", name );
        editor.putString ( "EMAIL", email );
        editor.putString ( "USER_ID", userId );
        editor.putString ( "IMAGE_URL", imageUrl );
        editor.apply ( );
    }

    private void loginWithFb() {
        mLoginButton.registerCallback ( mCallbackManager, new FacebookCallback<LoginResult> ( ) {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest graphRequest=GraphRequest.newMeRequest ( AccessToken.getCurrentAccessToken ( ), (object, response) -> {
                    try {
                        String email=object.getString ( "email" );
                        String name=object.getString ( "name" );
                        String userId=object.getString ( "id" );
                        String imageUrl=object.getJSONObject ( "picture" ).getJSONObject ( "data" ).getString ( "url" );
                        // Update user info into SharedPreference file
                        storeUserData ( name, email, userId, imageUrl, "facebook" );
                        // Switch to MovieActivity
                        Intent intent=new Intent ( MainActivity.this, MovieActivity.class );
                        startActivity ( intent );
                    } catch (JSONException e) {
                        e.printStackTrace ( );
                    }
                } );
                Bundle parameters=new Bundle ( );
                parameters.putString ( "fields", "name,email,id,picture.width(250).height(250)" );
                graphRequest.setParameters ( parameters );
                graphRequest.executeAsync ( );
            }

            @Override
            public void onCancel() {
                Toast.makeText ( MainActivity.this, "Login Cancelled", Toast.LENGTH_SHORT ).show ( );
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText ( MainActivity.this, "Login Error" + error.getMessage ( ), Toast.LENGTH_SHORT ).show ( );
            }
        } );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult ( requestCode, resultCode, data );
        mCallbackManager.onActivityResult ( requestCode, resultCode, data );

        // Result returned from launching the Intent from Google
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ( ).inflate ( R.menu.menu, menu );
        getSupportActionBar ( ).hide ( );
        return true;
    }
}