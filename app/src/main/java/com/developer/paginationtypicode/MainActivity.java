package com.developer.paginationtypicode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerViewMyFollowing;
    MyPAgeAdapter userAdapter2;
    List<PageModel> ModelListMyFollowing;
    ProgressBar progressBar2;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    int page;
    String pageString="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerViewMyFollowing = findViewById(R.id.recyclee);
        linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        ModelListMyFollowing = new ArrayList<>();
        progressBar2 = findViewById(R.id.progressBar2);

        page =1;
        pageString = String.valueOf(page);
        ilkveri(pageString);

        recyclerViewMyFollowing.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                    if (loading)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            page = page +1 ;
                            pageString = String.valueOf(page);
                            progressBar2.setVisibility(View.VISIBLE);
                            loading = false;
                            Log.v("...", "Last Item Wow !");
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    loadMore(pageString);
                                    loading = true;

                                }
                            }, 1500);
                        }
                    }
                }
            }
        });
    }






    public void ilkveri(String page){
        AndroidNetworking.get("https://jsonplaceholder.typicode.com/comments?postId="+page)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                             int k = response.length();
                            Log.i("firat2" , "" + k) ;
                            Log.i("firat2" , "" + response.getJSONObject(0).toString());
                            for (int i=0; i<k;i++){
                                JSONObject jsonObject2 = response.getJSONObject(i);
                                String objename = jsonObject2.getString("name");
                                String objeemail = String.valueOf(jsonObject2.getInt("id"));

                                ModelListMyFollowing.add(new PageModel(objename,objeemail));
                            }
                            userAdapter2 =new MyPAgeAdapter(ModelListMyFollowing, MainActivity.this);
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            linearLayoutManager.scrollToPosition(0);
                            recyclerViewMyFollowing.setLayoutManager(linearLayoutManager);
                            recyclerViewMyFollowing.setHasFixedSize(true);
                            recyclerViewMyFollowing.setItemViewCacheSize(24);
                            recyclerViewMyFollowing.setAdapter(userAdapter2);
                            recyclerViewMyFollowing.setItemAnimator(new DefaultItemAnimator());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
            }


    public void loadMore(String pageNumber){
        AndroidNetworking.get("https://jsonplaceholder.typicode.com/comments?postId="+pageNumber)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            int k = response.length();
                            Log.i("firat2" , "" + k) ;
                            Log.i("firat2" , "" + response.getJSONObject(0).toString());
                            for (int i=0; i<k;i++){
                                JSONObject jsonObject2 = response.getJSONObject(i);
                                String objename = jsonObject2.getString("name");
                                String objeemail = String.valueOf(jsonObject2.getInt("id"));

                                ModelListMyFollowing.add(new PageModel(objename,objeemail));
                            }

                            userAdapter2.notifyDataSetChanged();
                            progressBar2.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }
    }





