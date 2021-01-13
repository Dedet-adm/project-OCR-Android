package com.uthoo.www.ocr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity3 extends AppCompatActivity {
  String var ="http://34.68.192.101/api/v1/match?data=";;
    Button btnHit;
    TextView txtJson;
    ProgressDialog pd;
    private Retrofit twohRetro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        btnHit = (Button) findViewById(R.id.btnHit);
        txtJson = (TextView) findViewById(R.id.tvJsonItem1);
     //        String shareFact = txtJson.getText().toString();
        final String test = readFile();




        initializeRetrofit();
        btnHit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
   //             new JsonTask().execute(var);
                HashMap<String, String> params = new HashMap<>();
                params.put("data", test.toString());
               // params.put("lastname", etLastName.getText().toString());
                queryJSON(params);
                new RetrieveFeedTask().execute();


            }
        });
    }

    private void initializeRetrofit(){

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();



        twohRetro = new Retrofit.Builder()
                .baseUrl(Const.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private String readFile() {
        File fileEvents = new File(MainActivity3.this.getFilesDir()+"/text/sample");

        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileEvents));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
        }
        String result = text.toString();
        return result;
    }

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MainActivity3.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();

        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()){
                pd.dismiss();
            }



            txtJson.setText(result.toString());

        }

    }
    private void queryJSON(HashMap<String,String> params){
        APIService apiService = twohRetro.create( APIService.class);
        Call<ResponseBody> result = apiService.getStoryOfMe(params);
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
       //         dialog.dismiss();
                try {
                    if(response.body()!=null)

                  txtJson.setText(response.body().string());
                    txtJson.setVisibility(View.GONE);
    //         Toast.makeText(MainActivity3.this," response message "+response.body().string(),Toast.LENGTH_LONG).show();
                    // The number on which you want to send SMS
              //      String number = txtJson.getText().toString();
              //      startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));
                 //   String url = "https://api.telegram.org/bot1371362773:AAEshne4Pe6XBS8jGEsgug77Sm6-VooMet8/sendMessage?chat_id=-466619672&text=";
                //    Intent i = new Intent(Intent.ACTION_VIEW);
               //     i.setData(Uri.parse(url));
                 //   startActivity(i);





//Now the statement below gets the text displayed
                  //  String displayedText = ((TextView)((LinearLayout)myToast.getView()).getChildAt(0)).getText().toString();

                    //   Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                 //   sharingIntent.setType("text/html");
                   // sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, result);
                //    startActivity(Intent.createChooser(sharingIntent,"Share using"));
                     // The number on which you want to send SMS

                    if(response.errorBody()!=null)

                     Toast.makeText(MainActivity3.this," response message "+response.errorBody().string(),Toast.LENGTH_LONG).show();

                }catch (Exception e){
                    e.printStackTrace();

                }
             // final String result = "rseee"+response.body().toString();
               // startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", result, null)));


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
       //         dialog.dismiss();
                t.printStackTrace();
            }
        });


//Now the statement below gets the text displayed

    }


    public class RetrieveFeedTask extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... voids) {
            String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";

            //Add Telegram token (given Token is fake)
            String apiToken = "1371362773:AAEshne4Pe6XBS8jGEsgug77Sm6-VooMet8";

            //Add chatId (given chatId is fake)
            String chatId = "-466619672";
         //   String text = "Hello world!";
            String text = txtJson.getText().toString();
            //https://api.telegram.org/bot664321744:AAGimqEuidlzO84qMoY1-_C-1OsNWRQ8FyM/sendMessage?chat_id=-1001349137188&amp&text=Hello+World
            urlString = String.format(urlString, apiToken, chatId, text);

            try {
                URL url = new URL(urlString);
                URLConnection conn = url.openConnection();
                InputStream is = new BufferedInputStream(conn.getInputStream());

                //getting text, we can set it to any TextView
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String inputLine = "";
                StringBuilder sb = new StringBuilder();
                while ((inputLine = br.readLine()) != null) {
                    sb.append(inputLine);
                }
                //You can set this String to any TextView
                String response = sb.toString();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }



}