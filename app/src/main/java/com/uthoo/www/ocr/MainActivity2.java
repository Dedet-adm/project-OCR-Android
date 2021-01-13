package com.uthoo.www.ocr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity2 extends AppCompatActivity {
    String var ="http://34.68.192.101/api/v1/match?data=";
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
//        final TextView output=findViewById(R.id.output);

 //       save = findViewById(R.id.save1);
        final String test = readFile();
   //     save.setOnClickListener(new View.OnClickListener() {
    //        @Override
     //       public void onClick(View view) {
//                output.setText(test);
                //Toast.makeText(MainActivity2.this, "Read", Toast.LENGTH_LONG).show();
 //           }
//        });
     //  web.loadUrl(var+test);
        //  web.loadUrl("http://34.68.192.101/api/v1/match?data=Lines%3A%20%0AKAMAE3L1r%0ABr.%20Pujung%20Kelod%2C%0Aebatu%2C%20Tegallaiang%2C%2061anyar%2C%0ATelephone%20(%2B62)%20361%20901366%0AEtnail%20kumu.lilir%40gnail.%20COn%0A9ebsiteW.kuEulilir%2Cid%0ADate%0A2020-10-31%2014%3A59%3A57%0ANo.transaction120103i0012%0A166%20Htester%20(L)%0A2%20x%2025.000%20%3D%2050.000%0A173%20Hfrench%20fries%20kids%20(L)%0A2x%20%4010.000%2020.000%0A130%20Hbanana%20fritter%20(%20L%0A3X15.000%20%3D%2045.000%0APaynent%20Type%3A%20Cash%0AJunlah%20Iten%3A%207%20buah%0ATotal%20113.000%0ADiscount%3A%0ATax%26Service%2017.300%0AGrand%20Total%0A132.3%20300%0A135.000%0A700%0APayment%0Anange%0AItens%20that%20have%20been%20purchased%20cannot%20be%0Areturned%0AThank%20You");
     //  web.setWebViewClient(new WebViewClient());



    }

    private String readFile() {
        File fileEvents = new File(MainActivity2.this.getFilesDir()+"/text/sample");

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
}
