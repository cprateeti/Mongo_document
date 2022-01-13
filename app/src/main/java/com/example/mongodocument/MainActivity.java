package com.example.mongodocument;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URL;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class MainActivity extends AppCompatActivity {

    String Appid = "application-1-vyade";

    private EditText dataEditText;
    private Button button;
    String njp;

    MongoDatabase mongoDatabase;
    MongoClient mongoClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataEditText = (EditText)findViewById(R.id.data);
        button = (Button)findViewById(R.id.button);
        //VideoView vv = findViewById(R.id.videoView);

        Realm.init(this);
        App app = new App(new AppConfiguration.Builder(Appid).build());

       Credentials credentials = Credentials.emailPassword("prateetichakravarty.2018.cse@ritchennai.edu.in","Somalisa@97");
        app.loginAsync(credentials, new App.Callback<User>() {
            @Override
            public void onResult(App.Result<User> result) {
                if(result.isSuccess())
                {
                    Log.v("User","Logged in successfully");

                }
                else
                {
                    Log.v("User","Failed to Login");
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = app.currentUser();
                mongoClient = user.getMongoClient("mongodb-atlas");
                mongoDatabase = mongoClient.getDatabase("CourseData");
                MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("TestData");
                njp = dataEditText.getText().toString();
                mongoCollection.insertOne(new Document("userid",user.getId()).append("data",njp)).getAsync(result -> {
                    if (result.isSuccess())
                    {
                        Log.v("Data","Data Inserted Successfully");
                    }
                    else
                    {
                        Log.v("Data","Error:"+result.getError().toString());
                    }
                });

                njp=njp.replace("https://vimeo.com/","https://player.vimeo.com/video/");

                njp+="/config";
                getUrl(njp);

            }
        });
        
    }

    private void getUrl(String njp)
    {

        StringRequest str = new StringRequest(Request.Method.GET, njp, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();

                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject req = jsonObject.getJSONObject("request");
                    JSONObject files = req.getJSONObject("files");
                    JSONArray pro = files.getJSONArray("progressive");

                    JSONObject array1 = pro.getJSONObject(1);
                    String v_url = array1.getString("url");

                    Intent intent = new Intent(MainActivity.this,VideoActivity.class);
                    intent.putExtra("url",v_url);
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }

}