package ma.ac.fst.ahniche_amine.icc_gallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Amine on 1/2/2018.
 */

public class FbPhotosAdapter {
    private Context context;
    private int layout;
    private ArrayList<Image> facebookPhotos;

    public FbPhotosAdapter(Context context, int layout, ArrayList<Image> facebookPhotos) {
        this.context = context;
        this.layout = layout;
        this.facebookPhotos = facebookPhotos;
    }

    public int getNbrOfPhotos() {
        return facebookPhotos.size();
    }


    public Object getPhoto(int position) {
        return facebookPhotos.get(position);
    }


    public long getPhotoId(int position) {
        return position;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView photoName;
    }


    public View getView(int position, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder holder = new ViewHolder();

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.photoName = (TextView) row.findViewById(R.id.tvPhotoName);
            holder.imageView = (ImageView) row.findViewById(R.id.ivPhotoImg);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Image fbImage = facebookPhotos.get(position);

        holder.photoName.setText(fbImage.getImgUrl());

        String photoURL = fbImage.getImgUrl();

        Picasso.with(this.context).load(photoURL).into(holder.imageView);

        return row;
    }
}

