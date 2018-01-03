package ma.ac.fst.ahniche_amine.icc_gallery;

import java.util.ArrayList;

/**
 * Created by Amine on 1/2/2018.
 */


public class FacebookAlbum
{
    private String albumID;
    private ArrayList<Image> albumImgs;

    public FacebookAlbum()
    {
        super();
    }
    public FacebookAlbum(String albumID) {
        this.albumID = albumID;
    }

    public FacebookAlbum(ArrayList<Image> albumImgs) {
        this.albumImgs = albumImgs;
    }

    public FacebookAlbum(String albumID, ArrayList<Image> albumImgs) {
        this.albumID = albumID;
        this.albumImgs = albumImgs;
    }

    public String getAlbumID() {
        return albumID;
    }

    public void setAlbumID(String albumID) {
        this.albumID = albumID;
    }

    public ArrayList<Image> getAlbumImgs() {
        return albumImgs;
    }

    public void setAlbumImgs(ArrayList<Image> albumImgs) {
        this.albumImgs = albumImgs;
    }


}