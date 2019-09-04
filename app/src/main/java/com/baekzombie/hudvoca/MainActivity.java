package com.baekzombie.hudvoca;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.baekzombie.hudvoca.api.ApiHandler;
import com.baekzombie.hudvoca.common.Constants;
import com.baekzombie.hudvoca.record.VocaInfo;
import com.baekzombie.hudvoca.utils.NetworkUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    ApiHandler apiHandler = new ApiHandler();
    ApiAsyncTask apiAsyncTask;
    int apiType = 0;
    ArrayList<VocaInfo> vocaInfos;

    TextView tvVoca;
    TextView tvMean;

    Timer timer = new Timer();
    TimerTask timerTask = null;

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

//        tvVoca.setScaleX(-1); //좌우 거울 형식으로 뒤집기
        tvVoca.setScaleY(-1); //상하 거울 형식으로 뒤집기
        tvMean.setScaleY(-1);
        listener();
        getVoca();
    }

    @Override
    protected void onDestroy() {
        try {
            if (apiAsyncTask != null)
                apiAsyncTask.cancel(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    private void init() {
        tvVoca = (TextView) findViewById(R.id.tv_voca);
        tvMean = (TextView) findViewById(R.id.tv_mean);
    }

    private void listener() {

    }

    private void getVoca() {
        apiType = 1;
        apiRunner();
    }

    public void apiRunner() {
        apiAsyncTask = new ApiAsyncTask();
        if (NetworkUtil.isConnected(this)) {
            apiAsyncTask.execute();
        } else {
            new android.app.AlertDialog.Builder(this)
                    .setTitle(R.string.alert)
                    .setMessage(R.string.network_error_msg)
                    .setPositiveButton(R.string.retry,
                            new android.app.AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    apiRunner();
                                    dialog.dismiss();
                                }
                            })
                    .setNegativeButton(R.string.cancel,
                            new android.app.AlertDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                    .setCancelable(false)
                    .create()
                    .show();
        }
    }

    class ApiAsyncTask extends AsyncTask<Void, Void, Integer> {
        Gson gson = new Gson();

        @Override
        protected void onPreExecute() {
            if (!isCancelled()) {
            }
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            int result = 0;

            if (!isCancelled()) {
                try {
                    if (apiType == 1) {
                        String json = "?cate=trip";
                        result = apiHandler.postMessage(Constants.GET_VOCA, json);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(Integer resCode) {
            if (!isCancelled()) {
                if (resCode == 200) {
                    String json = apiHandler.getResponseMessage();
                    if (apiType == 1) {
                        try {
                            JSONObject dataJson = new JSONObject(json);
                            String data = dataJson.getString("data");
                            vocaInfos = gson.fromJson(data, new TypeToken<List<VocaInfo>>() {
                            }.getType());

                            timerTask = new TimerTask() {
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (i < 20) {
                                                tvVoca.setText(vocaInfos.get(i).getVocabulary());
                                                tvMean.setText(vocaInfos.get(i).getMean());
                                                i++;
                                            }

                                            if (i == 20) {
                                                i = 0;
                                            }
                                        }
                                    });
                                }
                            };

                            timer.schedule(timerTask, 0, 1000);
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
