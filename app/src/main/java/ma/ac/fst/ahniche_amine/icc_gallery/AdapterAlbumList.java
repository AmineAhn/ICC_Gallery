package ma.ac.fst.ahniche_amine.icc_gallery;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;

public class AdapterAlbumList extends AppCompatActivity {

    private class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.MyViewHolder>  {

        @Override
        public ImageGalleryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View photoView = inflater.inflate(R.layout.album_item, parent, false);
            ImageGalleryAdapter.MyViewHolder viewHolder = new ImageGalleryAdapter.MyViewHolder(photoView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ImageGalleryAdapter.MyViewHolder holder, int position) {

            FacebookAlbumParcelable facebookAlbumParcelable = mFacebookAlbumParcelables[position];
            ImageView imageView = holder.mPhotoImageView;

            Glide.with(mContext)
                    .load(facebookAlbumParcelable.getUrl())
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(imageView);
        }

        @Override
        public int getItemCount() {
            return (mFacebookAlbumParcelables.length);
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public ImageView mPhotoImageView;

            public MyViewHolder(View itemView)
            {
                super(itemView);
                mPhotoImageView =itemView.findViewById(R.id.albumItem);
                itemView.setOnClickListener(this)                                  ;
            }

            @Override
            public void onClick(View view) {

                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION)
                {
                    FacebookAlbumParcelable facebookAlbumParcelable = mFacebookAlbumParcelables[position];
                    Intent intent = new Intent(mContext, AdapterAlbumList.class);
                    intent.putExtra("ALBUM_SELECTED", facebookAlbumParcelable);
                    startActivity(intent);
                }
            }
        }

        private FacebookAlbumParcelable[] mFacebookAlbumParcelables;
        private Context mContext;

        public ImageGalleryAdapter(Context context, FacebookAlbumParcelable[] facebookAlbumParcelables) {
            mContext = context;
            mFacebookAlbumParcelables = facebookAlbumParcelables;
        }
    }


    RecyclerView gridView;
    CallbackManager          callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState)          ;
        setContentView(R.layout.adapter_album_list);
        initializeComponents();
        setupListeners();
        Intent intent = new Intent(this, ActivityAlbumList.class);
        startActivity(intent);
       }

    private void setupListeners()
    {
        gridView.setOnLongClickListener(new AdapterView.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                CharSequence[] items = {"View Photos", "Export album to Firebase"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(AdapterAlbumList.this);

                dialog.setTitle("Choose an action");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) { // VIEW PHOTOS CLICKED
                            // VIEW PHOTOS ACTION
                            Toast.makeText(AdapterAlbumList.this, "YOU HAVE SELECTED TO VIEw THE PHOTOS", Toast.LENGTH_SHORT).show();



                        } else if (item == 1) { // EXPORT TO FIREBASE CLICKED

                            // EXPORT TO FIREBASE ACTION
                            Toast.makeText(AdapterAlbumList.this, "YOU HAVE SELECTED TO EXPORT TO FIREBASE", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });

        gridView.setOnClickListener(new AdapterView.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                Toast.makeText(AdapterAlbumList.this, "TO BE ADDED LATER", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initializeComponents()
    {
        gridView = findViewById(R.id.gridView)                                 ;
        callbackManager  = CallbackManager.Factory.create();
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        gridView.setHasFixedSize(true);
        gridView.setLayoutManager(layoutManager);

        ImageGalleryAdapter adapter = new ImageGalleryAdapter(this, FacebookAlbumParcelable.getFacebookAlbumParcelable());
        gridView.setAdapter(adapter);

    }




    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data)        ;
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }
}
