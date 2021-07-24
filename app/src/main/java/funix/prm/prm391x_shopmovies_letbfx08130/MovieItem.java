package funix.prm.prm391x_shopmovies_letbfx08130;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;


import java.io.ByteArrayOutputStream;

import static funix.prm.prm391x_shopmovies_letbfx08130.MainActivity.mypreference;

public class MovieItem extends AppCompatActivity {
    private ImageView mImage;
    private TextView mTitle;
    private TextView mPrice;
    private String mUploadImage;
    private Bitmap mBitmap;

    @RequiresApi(api=Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_movie_item );
        init ( );
        getData ( );
        addShareImageFragment ( );
    }

    private void addShareImageFragment() {
        //send data to fb share
        Bundle bundle=new Bundle ( );
        bundle.putString ( "img",mUploadImage );
        ShareImageFragment shareImageFragment=new ShareImageFragment ( );
        shareImageFragment.setArguments ( bundle );
        FragmentTransaction fragmentTransaction=getSupportFragmentManager ( ).beginTransaction ( );
        fragmentTransaction.replace ( R.id.fb_share_button, shareImageFragment );
        fragmentTransaction.commit ( );
    }


    private void init() {
        mImage=findViewById ( R.id.img );
        mTitle=findViewById ( R.id.title );
        mPrice=findViewById ( R.id.price );
    }


    private void getData() {
        String title=getIntent ( ).getStringExtra ( "title" );
        String price=getIntent ( ).getStringExtra ( "price" );
        mUploadImage=getIntent ( ).getStringExtra ( "img" );
        mTitle.setText ( title );
        mPrice.setText ( price );
        Glide.with ( this ).asBitmap ( ).load ( mUploadImage ).into ( new CustomTarget<Bitmap> ( ) {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                mImage.setImageBitmap ( resource );
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {
            }
        } );

    }

    /**
     *
     *  menu
     *
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ( ).inflate ( R.menu.menu, menu );
        getSupportActionBar ( ).setTitle ( "Movie" );
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
            SharedPreferences pref=getApplicationContext ( ).getSharedPreferences ( mypreference, MODE_PRIVATE );
            SharedPreferences.Editor editor=pref.edit ( );
            editor.clear ( ).commit ( );
            Intent intent=new Intent ( MovieItem.this, MainActivity.class );
            startActivity ( intent );
            return true;
        }
        return super.onOptionsItemSelected ( item );
    }

}