package com.theqyl.karyamurni;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import static com.theqyl.karyamurni.R.drawable.cloudy;
import static com.theqyl.karyamurni.R.drawable.drizzle;
import static com.theqyl.karyamurni.R.drawable.fog;
import static com.theqyl.karyamurni.R.drawable.rainy;

public class MainActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    TextView tvLocation,tvTemp,tvMainWeather;
    Calendar c ;
    ImageView ivWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this);
        c.getInstance();
        Spinner spinner = findViewById(R.id.listItem);
        tvLocation = findViewById(R.id.tvLocation);
        tvTemp = findViewById(R.id.tvTemp);
        tvMainWeather = findViewById(R.id.tvMainWeather);
        ivWeather = findViewById(R.id.ivWeather);

        LinearLayout relativeLayout = findViewById(R.id.RLid);

        relativeLayout.getBackground().setAlpha(60);
        // Create an ArrayAdapter using the string array and a default spinner custom_list_item
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.daftar_makanan, R.layout.custom_item);
        // Specify the custom_list_item to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPrompt("title");
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = adapterView.getItemAtPosition(i).toString();
                parseData(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        parseData("jakarta");

    }

    private void parseData(String selectedItem) {
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+selectedItem+"&units=metric&APPID=0c14d9aec878a7c9928fb50320da55b2";
        Log.e("URL : ", url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject data = response.getJSONObject("coord");
                            String location = response.getString("name");
                            tvLocation.setText(location);
                            tvTemp.setText(Math.round(response.getJSONObject("main").getInt("temp"))+"°");
                            JSONArray arrayWeather = response.getJSONArray("weather");
                            JSONObject objectWeather = arrayWeather.getJSONObject(0);
                            tvMainWeather.setText(objectWeather.getString("main"));
                            if (objectWeather.getString("main")=="Cloudy"){
                                ivWeather.setImageResource(cloudy);
                            }else if(objectWeather.getString("main")=="Cloudy"){
                                ivWeather.setImageResource(fog);
                            }else if(objectWeather.getString("main")=="Drizzle"){
                                ivWeather.setImageResource(drizzle);
                            }else if(objectWeather.getString("main")=="Rain"){
                                ivWeather.setImageResource(rainy);
                            } else {
                                ivWeather.setImageResource(R.drawable.sun);

                            }

//                            Log.e("Datas", Temp + "");
//                            Toast.makeText(MainActivity.this, Temp, Toast.LENGTH_SHORT).show();

//                            JsonArrayRequest jsonArrayRequest = response.getJSONArray('')
//                            String version = response.getString("version");
//                            Toast.makeText(MainActivity.this, message + '\n' + version , Toast.LENGTH_SHORT).show();
//                            Toast.makeText(Home.this, version, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }


}