package funix.prm.prm391x_shopmovies_letbfx08130;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private Context mContext;
    private List<MovieInfo> mMoveList;
    private View mView;
    final public MovieAdapter.IClickItemListener miClickItemListener;

    public MovieAdapter(Context mContext, List<MovieInfo> mMoveList, IClickItemListener miClickItemListener) {
        this.mContext=mContext;
        this.mMoveList=mMoveList;
        this.miClickItemListener=miClickItemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from ( mContext );
        mView=inflater.inflate ( R.layout.movie_list, parent, false );
        return new ViewHolder ( mView );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String img=mMoveList.get ( position ).getImageView ( );
        final MovieInfo mMovieInfo=new MovieInfo ( );
        holder.mMoivePrice.setText ( mMoveList.get ( position ).getPrice ( ) );
        holder.mMoiveTitle.setText ( mMoveList.get ( position ).getTitle ( ) );
        Glide.with ( mContext )
                .load ( mMoveList.get ( position ).getImageView ( ) ).into ( holder.mMovieImage );
        // set itemonClick
        holder.mMovieImage.setOnClickListener ( v -> {
            Intent intent=new Intent ( mContext, MovieItem.class );
            intent.putExtra ( "title", mMoveList.get ( position ).getTitle ( ) );
            intent.putExtra ( "price", mMoveList.get ( position ).getPrice ( ) );
            intent.putExtra ( "img", mMoveList.get ( position ).getImageView ( ) );
            String title=mMoveList.get ( position ).getTitle ( );
            mContext.startActivity ( intent );
        } );

    }

    @Override
    public int getItemCount() {
        return mMoveList.size ( );
    }

    public interface IClickItemListener {
        void onClickItemHotel(MovieInfo mMovieInfo);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mMovieImage;
        private TextView mMoiveTitle;
        private TextView mMoivePrice;


        public ViewHolder(@NonNull View itemView) {
            super ( itemView );
            mMovieImage=itemView.findViewById ( R.id.movie_img );
            mMoiveTitle=itemView.findViewById ( R.id.movie_title );
            mMoivePrice=itemView.findViewById ( R.id.movie_price );
        }
    }
}
