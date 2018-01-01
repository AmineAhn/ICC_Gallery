package ma.ac.fst.ahniche_amine.icc_gallery;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class ActivityLogin extends AppCompatActivity
{

    ImageView       ivProfilePicture;
    LoginButton     loginButton     ;
    TextView        tvStringFullname;
    TextView        tvStringEmail   ;
    TextView        tvFullname      ;
    TextView        tvEmail         ;

    CallbackManager callbackManager ;
    String[]        permissions     ;

    Bundle          bundle          ;

    public final String FIRST_NAME  = "first_name";
    public final String LAST_NAME   = "last_name" ;
    public final String PICTURE_URL = "profile_pic";
    public final String FACEBOOK_ID = "facebook_id";
    public final String EMAIL       = "email";
    public final String ACCESS_TOKEN= "access_token";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState)     ;
        setContentView(R.layout.activity_login);
        //printKeyHash()                       ;
        initializeComponents()                    ;
        //setupWidgets(bundle);

    }

    private void setupWidgets(Bundle bundle)
    {
        Picasso.with(this).load(bundle.getString(PICTURE_URL)).into(ivProfilePicture)     ;                                                               ;
        tvEmail.setText(bundle.getString(EMAIL))                                          ;
        tvFullname.setText(bundle.getString(FIRST_NAME) +" "+ bundle.getString(LAST_NAME));
    }

    private void setupPermissions(String[] permissions)
    {
        loginButton.setReadPermissions(Arrays.asList(permissions));
    }

    private void initializeComponents()
    {
        loginButton      = findViewById(R.id.loginButton)     ;
        ivProfilePicture = findViewById(R.id.ivProfilePicture);
        tvStringFullname = findViewById(R.id.tvStringFullname);
        tvStringEmail    = findViewById(R.id.tvStringEmail)   ;
        tvFullname       = findViewById(R.id.tvFullname)      ;
        tvEmail          = findViewById(R.id.tvEmail)         ;

        callbackManager  = CallbackManager.Factory.create()                      ;
        permissions      = new String[]{"public_profile", "user_photos", "email"};
        setupPermissions(permissions);
        registerCallback();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data)        ;
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    private void registerCallback()
    {
        loginButton.registerCallback(
                callbackManager,
                new FacebookCallback<LoginResult>()
                {
                    @Override
                    public void onSuccess(LoginResult loginResult)
                    {
                        Toast.makeText(getApplicationContext(), "ACTLOGIN > LOGINRESULT > ONSUCCESS >" , Toast.LENGTH_LONG).show();
                        String accessToken        = loginResult.getAccessToken().getToken();
                        GraphRequest graphRequest = GraphRequest.newMeRequest
                            (
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback()
                                {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse graphResponse)
                                    {
                                        bundle = getFacebookData(object);
                                    }
                                }
                            );
                        Bundle parameters = new Bundle()                              ;
                        parameters.putString("fields","id,first_name,last_name,email");
                        graphRequest.setParameters(parameters)                        ;
                        graphRequest.executeAsync ()                                  ;
                    }

                @Override
                public void onCancel()
                {
                    Toast.makeText(getApplicationContext(), "ACTLOGIN > LOGINRESULT > ONCANCEL >" , Toast.LENGTH_LONG).show();
                }
                @Override
                public void onError(FacebookException error)
                {
                    Toast.makeText(getApplicationContext(), "ACTLOGIN > LOGINRESULT > ONERROR >" , Toast.LENGTH_LONG).show();
                }
                });
    }

    private Bundle getFacebookData(JSONObject object)
    {
        try
        {
            Bundle bundle = new Bundle();
            String id = object.getString("id");
            URL    profile_pic                      ;
            try
            {
                profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=200");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString(PICTURE_URL, profile_pic.toString());
                bundle.putString(FACEBOOK_ID, id);
                bundle.putString(FIRST_NAME, object.getString("first_name"));
                bundle.putString(LAST_NAME , object.getString("last_name") );
                bundle.putString(EMAIL, object.getString("email"));
                this.bundle = bundle;
                setupWidgets(bundle);
            }
            catch (MalformedURLException e)
            {
                Toast.makeText(this, "GETFBDATA > GETTING PROFILE PIC ERROR!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return bundle;
    }


    public void onClickSeePhotos(View view)
    {
        Intent intent = new Intent(this, null);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void printKeyHash()
    {
        // I USED IT ONLY ONCE
        try
        {
            PackageInfo info = getPackageManager().getPackageInfo("ma.ac.fst.ahniche_amine.icc_gallery", PackageManager.GET_SIGNATURES);
            for(android.content.pm.Signature signature:info.signatures)
            {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
