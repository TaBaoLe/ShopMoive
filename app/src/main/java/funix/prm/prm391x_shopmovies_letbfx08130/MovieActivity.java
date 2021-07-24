package funix.prm.prm391x_shopmovies_letbfx08130;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static funix.prm.prm391x_shopmovies_letbfx08130.MainActivity.mypreference;

public class MovieActivity extends AppCompatActivity {
    BottomNavigationView mNavigationView;
    private RecyclerView rcvMovie;
    List<MovieInfo> mMoveList;
    GridLayoutManager mLayoutManager;
    private MovieAdapter.IClickItemListener miClickItemListener;

    @RequiresApi(api=Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_movie );
        init ( );
        setUi ( );
        getMovieList ( );
        bottomNavItemOnClick ( );
        hideBottomMenuWhenScroll ( );
    }

    /**
     * hide bottom menu when scroll down
     */
    private void hideBottomMenuWhenScroll() {
        rcvMovie.addOnScrollListener ( new RecyclerView.OnScrollListener ( ) {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && mNavigationView.isShown ( )) {
                    mNavigationView.setVisibility ( View.GONE );
                } else if (dy < 0) {
                    mNavigationView.setVisibility ( View.VISIBLE );

                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                super.onScrollStateChanged ( recyclerView, newState );
            }
        } );
    }

    private void init() {
        mMoveList=new ArrayList<> ( );
        mNavigationView=findViewById ( R.id.bottom_nav );
    }
// create menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ( ).inflate ( R.menu.menu, menu );
        getSupportActionBar ( ).setTitle ( "Movie" );
        return true;
    }
// set up sign out
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId ( ) == R.id.sign_out) {
            signOut ( );
            return true;
        }
        return super.onOptionsItemSelected ( item );
    }

    private void signOut() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(mypreference, MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit ( );
        editor.clear().commit();
        Intent intent=new Intent ( MovieActivity.this, MainActivity.class );
        startActivity ( intent );
    }

    /**
     * set up bottom menu onclick
     */
    private void bottomNavItemOnClick() {
        mNavigationView.setOnNavigationItemSelectedListener ( item -> {
            if (item.getItemId ( ) == R.id.action_profile) {
                Intent intent=new Intent ( MovieActivity.this, ProfileActivity.class );
                startActivity ( intent );
            }
            return true;
        } );
    }

    /**
     * set up recycleview layout
     */
    private void setUi() {
        rcvMovie=findViewById ( R.id.rcv_layout );
        rcvMovie.setHasFixedSize ( false );
        rcvMovie.setNestedScrollingEnabled ( false );
        mLayoutManager=new GridLayoutManager ( this, 3 );
        rcvMovie.setLayoutManager ( mLayoutManager );
        MovieAdapter movieAdapter=new MovieAdapter ( this, mMoveList,miClickItemListener );
        rcvMovie.setAdapter ( movieAdapter );
        movieAdapter.notifyDataSetChanged ( );
    }

    /**
     * get movelist from json
     */
    @RequiresApi(api=Build.VERSION_CODES.KITKAT)
    private void getMovieList() {
        String myJSONStr=loadJSONFromAsset ( "list.json" );

        try {
            //Get root JSON object node
            JSONObject rootJSONObject=new JSONObject ( myJSONStr );

            //Get movie array node
            JSONArray employeeJSONArray=rootJSONObject.getJSONArray ( "movie" );

            for (int i=0; i < employeeJSONArray.length ( ); i++) {
                //Create a temp object of the movie model class
                MovieInfo movieInfo=new MovieInfo ( );

                //Get movie JSON object node
                JSONObject jsonObject=employeeJSONArray.getJSONObject ( i );

                //Get movie details
                movieInfo.setTitle ( jsonObject.getString ( "title" ) );
                movieInfo.setPrice ( jsonObject.getString ( "price" ) );
                movieInfo.setImageView ( jsonObject.getString ( "image" ) );

                //Add movie object to the list
                mMoveList.add ( movieInfo );
            }

        } catch (JSONException e) {
            e.printStackTrace ( );
        }
    }

    @RequiresApi(api=Build.VERSION_CODES.KITKAT)
    public String loadJSONFromAsset(String fileName) {
        String json;
        try {
            InputStream is=getAssets ( ).open ( fileName );
            int size=is.available ( );
            byte[] buffer=new byte[size];
            //noinspection ResultOfMethodCallIgnored
            is.read ( buffer );
            is.close ( );
            json=new String ( buffer, StandardCharsets.UTF_8 );
        } catch (IOException ex) {
            ex.printStackTrace ( );
            return null;
        }
        return json;
    }

}