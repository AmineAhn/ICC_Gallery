package ma.ac.fst.ahniche_amine.icc_gallery;

/**
 * Created by Amine on 11/29/2017.
 */

import java.util.ArrayList;

/**
 * Updated by Amine on 1/1/2018.
 */
import android.media.FaceDetector;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FacebookAlbumParcelable implements Parcelable {

    private String mUrl;
    private String mTitle;


    public FacebookAlbumParcelable(String url, String title) {
        mUrl = url;
        mTitle = title;
    }

    protected FacebookAlbumParcelable(Parcel in) {
        mUrl = in.readString();
        mTitle = in.readString();
    }

    public static final Creator<FacebookAlbumParcelable> CREATOR = new Creator<FacebookAlbumParcelable>() {
        @Override
        public FacebookAlbumParcelable createFromParcel(Parcel in) {
            return new FacebookAlbumParcelable(in);
        }

        @Override
        public FacebookAlbumParcelable[] newArray(int size) {
            return new FacebookAlbumParcelable[size];
        }
    };

    public String getUrl()
    {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public static  FacebookAlbumParcelable[] getFacebookAlbumParcelable() {
        ArrayList<FacebookAlbum> albumList = new ArrayList<>();
        albumList = GetFacebookAlbums();
        FacebookAlbumParcelable[] fbap = new FacebookAlbumParcelable[albumList.size()];
        for(int i = 0 ; i < albumList.size(); i++)
        {
            fbap[i] = new FacebookAlbumParcelable(albumList.get(i).getAlbumImgs().get(0).getImgUrl(),
                    albumList.get(i).getAlbumID() );
        }
        return fbap;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mUrl);
        parcel.writeString(mTitle);
    }

    private static ArrayList<FacebookAlbum> GetFacebookAlbums()
    {
        ArrayList<FacebookAlbum> facebookAlbums = new ArrayList<>();
        new GraphRequest(AccessToken.getCurrentAccessToken(),  //current fb AccessToken
                "/" + AccessToken.getCurrentAccessToken().getUserId() + "/albums",//user id of login user
                null,
                HttpMethod.GET,
                new GraphRequest.Callback()
                {
                    public void onCompleted(GraphResponse response) {
                        Log.d("TAG", "Facebook Albums: " + response.toString());
                        try
                        {
                            if (response.getError() == null)
                            {
                                JSONObject joMain = response.getJSONObject(); //convert GraphResponse response to JSONObject
                                if (joMain.has("data"))
                                {
                                    JSONArray jaData = joMain.optJSONArray("data"); //find JSONArray from JSONObject
                                    for (int i = 0; i < jaData.length(); i++)
                                    {//find no. of album using jaData.length()
                                        FacebookAlbum fbAlbum = new FacebookAlbum();
                                        JSONObject joAlbum = jaData.getJSONObject(i); //convert perticular album into JSONObject
                                        String fbAlbumID = joAlbum.optString("id");
                                        fbAlbum.setAlbumID(fbAlbumID);
                                        fbAlbum.setAlbumImgs(GetFacebookImages(fbAlbumID)); //find Album ID and get All Images from album
                                    }
                                }
                            }
                            else
                            {
                                Log.d("TAG", response.getError().toString());
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
        return facebookAlbums;
    }
    private static ArrayList<Image> GetFacebookImages(final String albumId)
    {

        final ArrayList<Image> images     = new ArrayList<>();
        Bundle parameters = new Bundle()     ;
        parameters.putString("fields", "images")        ;
        /* make the API call */
        new GraphRequest(AccessToken.getCurrentAccessToken(),
                "/" + albumId + "/photos",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback()
                {
                    public void onCompleted(GraphResponse response)
                    {
                            /* handle the result */
                        Log.v("TAG", "Facebook Photos response: " + response);
                        try
                        {
                            if (response.getError() == null)
                            {
                                JSONObject joMain = response.getJSONObject();
                                if (joMain.has("data"))
                                {
                                    JSONArray jaData = joMain.optJSONArray("data");
                                    for (int i = 0; i < jaData.length(); i++)//Get no. of images
                                    {
                                        JSONObject joAlbum = jaData.getJSONObject(i);
                                        JSONArray jaImages=joAlbum.getJSONArray("images");
                                        //get images Array in JSONArray format
                                        if(jaImages.length()>0)
                                        {
                                            Image objImage = new Image();//Images is custom class with string url field
                                            objImage.setImgUrl(jaImages.getJSONObject(0).getString("source"));
                                            images.add(objImage);//lstFBImages is Images object array
                                        }
                                    }
                                    //set your adapter here
                                }
                            }
                            else
                            {
                                Log.v("TAG", response.getError().toString());
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
        return images;
    }
}
