package ma.ac.fst.ahniche_amine.icc_gallery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class ActivityAlbumList extends AppCompatActivity {

    public static final String EXTRA_ALBUM_SELECTED = "ALBUM_SELECTED";
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_list);

        mImageView = findViewById(R.id.imageItem);
        FacebookAlbumParcelable facebookAlbumParcelable = getIntent().getParcelableExtra(EXTRA_ALBUM_SELECTED);

        Glide.with(this)
                .load(facebookAlbumParcelable.getUrl())
                .asBitmap()
                .error(R.drawable.ic_launcher_background)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(mImageView);
    }
}
