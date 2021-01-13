package com.uthoo.www.ocr;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener  {
    private static final String LOG_TAG = "Text API";
    private static final int PHOTO_REQUEST = 10;
    private TextView scanResults;
    private Uri imageUri;
    private TextRecognizer detector;
    private static final int REQUEST_WRITE_PERMISSION = 20;
    private static final String SAVED_INSTANCE_URI = "uri";
    private static final String SAVED_INSTANCE_RESULT = "result";
    private Retrofit twohRetro;
    private Button buttonAdd;
    private Button Button1;
    private Button Button2;
    TextView txtJson1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button);
        scanResults = (TextView) findViewById(R.id.textView);
        txtJson1 = (TextView) findViewById(R.id.tvJsonItem2);
        buttonAdd= (Button) findViewById(R.id.button1);
        Button1 = (Button) findViewById(R.id.button14);

     //  final String test = readFile();

Button1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      //  Intent i = new Intent(MainActivity.this,MainActivity3.class);
        //startActivity (i);
        new RetrieveFeedTask1().execute();
    }
});
       // buttonAdd.setOnClickListener(this);



        buttonAdd.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if (!scanResults.getText().toString().isEmpty()) {
                  File file = new File(MainActivity.this.getFilesDir(), "text");
                  if (!file.exists()) {
                      file.mkdir();
                  }
                  try {
                      File gpxfile = new File(file, "sample");
                      FileWriter writer = new FileWriter(gpxfile);
                      writer.append(scanResults.getText().toString());
                      writer.flush();
                      writer.close();

                      Toast.makeText(MainActivity.this, "Saved your text", Toast.LENGTH_LONG).show();
                  } catch (Exception e) { }
              }
              final String test = readFile();
              initializeRetrofit();
              HashMap<String, String> params = new HashMap<>();
              params.put("data", test.toString());
              // params.put("lastname", etLastName.getText().toString());
              queryJSON(params);


            }
       });



        scanResults = (TextView) findViewById(R.id.results);
        if (savedInstanceState != null) {
            imageUri = Uri.parse(savedInstanceState.getString(SAVED_INSTANCE_URI));
            scanResults.setText(savedInstanceState.getString(SAVED_INSTANCE_RESULT));
        }
        detector = new TextRecognizer.Builder(getApplicationContext()).build();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(MainActivity.this, new
                        String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
            }
        });

    }

    private String readFile() {
        File fileEvents = new File(MainActivity.this.getFilesDir()+"/text/sample");

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





  //  public void sendPost(String gk ) throws IOException {
     //   Call<ResponseBody> call = ApiUtils.getAPIService().add_user(scanResults.getText().toString());
       // call.enqueue(new Callback<ResponseBody>() {
           // @Override
         //   public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
             //   try {
               //     String s = response.body().string();
                 //   Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();

                //} catch (IOException e) {
                 //   e.printStackTrace();
                //}
        //    }

          //  @Override
           // public void onFailure(Call<ResponseBody> call, Throwable t) {

            //    Toast.makeText(MainActivity.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            //}
       // });
  //  }






    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePicture();
                } else {
                    Toast.makeText(MainActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST && resultCode == RESULT_OK) {
            launchMediaScanIntent();
            try {
                Bitmap bitmap = decodeBitmapUri(this, imageUri);
                if (detector.isOperational() && bitmap != null) {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> textBlocks = detector.detect(frame);
                    String blocks = "";
                    String lines = "";
                    String words = "";
                    for (int index = 0; index < textBlocks.size(); index++) {
                        //extract scanned text blocks here
                        TextBlock tBlock = textBlocks.valueAt(index);
                        blocks = blocks + tBlock.getValue() + "\n" + "\n";
                        for (Text line : tBlock.getComponents()) {
                            //extract scanned text lines here
                            lines = lines + line.getValue() + "\n";
                            for (Text element : line.getComponents()) {
                                //extract scanned text words here
                                words = words + element.getValue() + ", ";
                            }
                        }
                    }
                    if (textBlocks.size() == 0) {
                        scanResults.setText("Scan Failed: Found nothing to scan");
                    } else {
                        scanResults.setText(scanResults.getText() + "Blocks: " + "\n");
                        scanResults.setText(scanResults.getText() + blocks + "\n");
             //           scanResults.setText(scanResults.getText() + "---------" + "\n");
                        scanResults.setText(scanResults.getText() + "Lines: " + "\n");
                        scanResults.setText(scanResults.getText() + lines + "\n");
//                      scanResults.setText(scanResults.getText() + "---------" + "\n");
                        scanResults.setText(scanResults.getText() + "Words: " + "\n");
                        scanResults.setText(scanResults.getText() + words + "\n");
                       // scanResults.setText(scanResults.getText() + "---------" + "\n");
                    }
                } else {
                    scanResults.setText("Could not set up the detector!");
                }
            } catch (Exception e) {
                Toast.makeText(this, "Failed to load Image", Toast.LENGTH_SHORT)
                        .show();
                Log.e(LOG_TAG, e.toString());
            }
        }
    }

    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStorageDirectory(), "picture.jpg");
        imageUri = FileProvider.getUriForFile(MainActivity.this,
                BuildConfig.APPLICATION_ID + ".provider", photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, PHOTO_REQUEST);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (imageUri != null) {
            outState.putString(SAVED_INSTANCE_URI, imageUri.toString());
            outState.putString(SAVED_INSTANCE_RESULT, scanResults.getText().toString());
        }
        super.onSaveInstanceState(outState);
    }

    private void launchMediaScanIntent() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(imageUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private Bitmap decodeBitmapUri(Context ctx, Uri uri) throws FileNotFoundException {
        int targetW = 600;
        int targetH = 600;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(uri), null, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeStream(ctx.getContentResolver()
                .openInputStream(uri), null, bmOptions);
    }
    private void addNama(){

        final String name = scanResults.getText().toString().trim();


        class AddNama extends AsyncTask<Void,Void,String>{

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this,"Menambahkan...","Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(MainActivity.this,s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put(konfigurasi.KEY_EMP_NAMA,name);
                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(konfigurasi.URL_ADD,params);
                return res;
            }
        }

        AddNama ae = new AddNama ();
        ae.execute();
    }


    @Override
    public void onClick(View v) {
        if(v == buttonAdd){
            addNama();
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

                        txtJson1.setText(response.body().string());
                        txtJson1.setVisibility(View.GONE);
                           Toast.makeText(MainActivity.this," response message "+response.body().string(),Toast.LENGTH_LONG).show();
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

                        Toast.makeText(MainActivity.this," response message "+response.errorBody().string(),Toast.LENGTH_LONG).show();

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

    public class RetrieveFeedTask1 extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... voids) {
            String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";

            //Add Telegram token (given Token is fake)
            String apiToken = "1371362773:AAEshne4Pe6XBS8jGEsgug77Sm6-VooMet8";

            //Add chatId (given chatId is fake)
            String chatId = "-466619672";
            //   String text = "Hello world!";
            String text = txtJson1.getText().toString();
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