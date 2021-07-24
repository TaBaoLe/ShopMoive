package funix.prm.prm391x_shopmovies_letbfx08130;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;

public class ShareImageFragment extends Fragment {
    private static String TAG=ShareImageFragment.class.getName ( );

    private CallbackManager mCallbackManager;
    private Bitmap mBitmap;
    ShareDialog mShareDialog;
    private View mView;
    private String mGetImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        // Initialize facebook SDK.
        FacebookSdk.sdkInitialize ( getActivity ( ).getApplicationContext ( ) );

        // Create a callbackManager to handle the login responses.
        mCallbackManager=CallbackManager.Factory.create ( );

        mShareDialog=new ShareDialog ( this );
        mShareDialog.registerCallback ( mCallbackManager, callback );


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView=inflater.inflate ( R.layout.share_image_fragment, container, false );
        setImageShare ( mView );
        return mView;
    }

    /**
     *
     * get Image from moveItem for setup fb share image
     */
    private void setImageShare(View view) {
        ImageView mUploadImg=view.findViewById ( R.id.img_share );
        if (getArguments ( ) != null) {
            mGetImage=getArguments ( ).getString ( "img" );
        }
        Glide.with ( getActivity () ).asBitmap ( ).load ( mGetImage ).into ( new CustomTarget<Bitmap> ( ) {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                mUploadImg.setImageBitmap ( resource );
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {
            }
        } );
        sharePhotoSetUp ( view, mUploadImg );
    }

    private void sharePhotoSetUp(View view, ImageView mUploadImg) {
        mBitmap=((BitmapDrawable) mUploadImg.getDrawable ( )).getBitmap ( );
        SharePhoto photo=new SharePhoto.Builder ( )
                .setBitmap ( mBitmap )
                .build ( );
        SharePhotoContent content=new SharePhotoContent.Builder ( )
                .addPhoto ( photo )
                .build ( );
        ShareButton shareButton=(ShareButton) view.findViewById ( R.id.fb_share_button );
        shareButton.setShareContent ( content );
        mShareDialog=new ShareDialog ( this );
        mShareDialog.registerCallback ( mCallbackManager, callback );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult ( requestCode, resultCode, data );
        // Call callbackManager.onActivityResult to pass login result to the LoginManager via callbackManager.
        mCallbackManager.onActivityResult ( requestCode, resultCode, data );
    }

    private FacebookCallback<Sharer.Result> callback=new FacebookCallback<Sharer.Result> ( ) {
        @Override
        public void onSuccess(Sharer.Result result) {
            Log.v ( TAG, "Successfully posted" );
        }

        @Override
        public void onCancel() {
            Log.v ( TAG, "Sharing cancelled" );
        }

        @Override
        public void onError(FacebookException error) {
            Log.v ( TAG, error.getMessage ( ) );
        }
    };
}

