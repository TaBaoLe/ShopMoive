package funix.prm.prm391x_shopmovies_letbfx08130;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import static funix.prm.prm391x_shopmovies_letbfx08130.MainActivity.mypreference;

public class ProfileActivity extends AppCompatActivity {
    BottomNavigationView mNavigationView;
    private ImageView mProfileImg;
    TextView mName;
    TextView mEmail;
    TextView mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_profile );
        init ( );
        loadUserInfo ( );
        bottomNavItemOnClick ( );

    }

    private void init() {
        mProfileImg=findViewById ( R.id.user_image );
        mName=findViewById ( R.id.user_name );
        mEmail=findViewById ( R.id.user_email );
        mUserId=findViewById ( R.id.user_id );
        mNavigationView=findViewById ( R.id.bottom_nav );
    }

    /**
     * Load user infor
     */
    private void loadUserInfo() {
        SharedPreferences sharedPreferences=getSharedPreferences ( mypreference, MODE_PRIVATE );
        String name=sharedPreferences.getString ( "USERNAME", null );
        String email=sharedPreferences.getString ( "EMAIL", null );
        String userId=sharedPreferences.getString ( "USER_ID", null );
        String imageUrl=sharedPreferences.getString ( "IMAGE_URL", null );

        mName.setText ( name );
        mEmail.setText ( email );
        mUserId.setText ( userId );
        Picasso.get ( ).load ( imageUrl ).into ( mProfileImg );
    }

    /**
     *
     * create menu
     *
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ( ).inflate ( R.menu.menu, menu );
        getSupportActionBar ( ).setTitle ( "Profile" );
        return true;
    }

    /**
     *
     * set function for sign out banc to login page
     *
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText ( this, "Selected Item: " + item.getTitle ( ), Toast.LENGTH_SHORT ).show ( );
        if (item.getItemId ( ) == R.id.sign_out) {
            SharedPreferences pref = getApplicationContext().getSharedPreferences(mypreference, MODE_PRIVATE);
            SharedPreferences.Editor editor=pref.edit ( );
            editor.clear().commit();
            Intent intent=new Intent ( ProfileActivity.this, MainActivity.class );
            startActivity ( intent );
            return true;
        }
        return super.onOptionsItemSelected ( item );
    }

    /**
     * set onclick for bottom nav icon
     */
    private void bottomNavItemOnClick() {
        mNavigationView.setOnNavigationItemSelectedListener ( item -> {
            if (item.getItemId ( ) == R.id.action_movie) {
                Intent intent=new Intent ( ProfileActivity.this, MovieActivity.class );
                startActivity ( intent );
            }
            return true;
        } );
    }
}