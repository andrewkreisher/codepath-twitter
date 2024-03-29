package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {
    EditText etTweetInput;
    TextView tvCount;
    Button btnSend;
    RestClient client;



    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            tvCount.setText(String.valueOf(280 - s.length()));
        }

        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#1da1f2")));

        etTweetInput = findViewById(R.id.etTweetInput);
        tvCount = findViewById(R.id.tvCount);
        btnSend = findViewById(R.id.btnSend);
        etTweetInput.addTextChangedListener(mTextEditorWatcher);

        btnSend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String message= etTweetInput.getText().toString();
                sendTweet();
            }

        });

        client = RestApplication.getRestClient(this);
    }

    private void sendTweet(){
        client.sendTweet(etTweetInput.getText().toString(), new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {

                    try {

                        Tweet resultTweet = Tweet.fromJSON(responseBody);

                        //return result
                        Intent resultData = new Intent();
                        //Todo change tweet to parceable
                        resultData.putExtra("resultTweet", Parcels.wrap(resultTweet));
                        setResult(RESULT_OK, resultData);
                        finish();
                        //startActivity(resultData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            //}

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject errorResponse) {
                Log.i("fail","fail");
            }
        });
    }
}
