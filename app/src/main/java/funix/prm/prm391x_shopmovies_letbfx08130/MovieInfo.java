package funix.prm.prm391x_shopmovies_letbfx08130;

public class MovieInfo {
 String title;
     String ImageView;
     String price;

    public MovieInfo(String title, String imageView, String price) {
        this.title=title;
        ImageView=imageView;
        this.price=price;
    }

    public MovieInfo() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title=title;
    }

    public String getImageView() {
        return ImageView;
    }

    public void setImageView(String imageView) {
        ImageView=imageView;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price=price;
    }
}
