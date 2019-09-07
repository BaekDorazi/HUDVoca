package com.baekzombie.hudvoca;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.baekzombie.hudvoca.api.ApiHandler;
import com.baekzombie.hudvoca.common.Constants;
import com.baekzombie.hudvoca.record.VocaInfo;
import com.baekzombie.hudvoca.utils.NetworkUtil;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ApiHandler apiHandler = new ApiHandler();
    ApiAsyncTask apiAsyncTask;
    int apiType = 0;
    ArrayList<VocaInfo> vocaInfos;

    TextView tvSettingStatus, tvUp, tvDown;

    private FloatingActionButton fab_main, fab_word, fab_time, fab_flip, fab_10_sec, fab_30_sec, fab_1_min, fab_5_min;
    private Animation fab_open, fab_close, fab_sub_open, fab_sub_close;

    AdView mAdView;

    private boolean isFabOpen = false;
    private boolean isTimeFabOpen = false;
    boolean isFlipMode = false; //플립기능 구분(false = 정방향, true = 거울)

    Timer timer = new Timer();
    TimerTask timerTask = null;

    int i = 0;
    long period = 30 * 1000;
    String strPeriod = "30초";
    String strFlipMode = "일반모드";

    AdRequest adRequest = new AdRequest.Builder().build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        listener();
        getVoca();

        mAdView.loadAd(adRequest);
    }

    @Override
    protected void onDestroy() {
        try {
            if (apiAsyncTask != null)
                apiAsyncTask.cancel(true);
            if (timerTask != null)
                timerTask.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    private void init() {
        tvSettingStatus = (TextView) findViewById(R.id.tv_setting_status);
        tvUp = (TextView) findViewById(R.id.tv_up);
        tvDown = (TextView) findViewById(R.id.tv_down);
        tvUp.setSelected(true);
        tvDown.setSelected(true);

        fab_main = (FloatingActionButton) findViewById(R.id.fab_main);
        fab_word = (FloatingActionButton) findViewById(R.id.fab_word);
        fab_time = (FloatingActionButton) findViewById(R.id.fab_time);
        fab_flip = (FloatingActionButton) findViewById(R.id.fab_flip);
        fab_10_sec = (FloatingActionButton) findViewById(R.id.fab_10_sec);
        fab_30_sec = (FloatingActionButton) findViewById(R.id.fab_30_sec);
        fab_1_min = (FloatingActionButton) findViewById(R.id.fab_1_min);
        fab_5_min = (FloatingActionButton) findViewById(R.id.fab_5_min);

        fab_open = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(this, R.anim.fab_close);
        fab_sub_open = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fab_sub_close = AnimationUtils.loadAnimation(this, R.anim.fab_close);

        mAdView = findViewById(R.id.ad_view);
    }

    private void listener() {
        fab_main.setOnClickListener(this);
        fab_word.setOnClickListener(this);
        fab_time.setOnClickListener(this);
        fab_flip.setOnClickListener(this);
        fab_10_sec.setOnClickListener(this);
        fab_30_sec.setOnClickListener(this);
        fab_1_min.setOnClickListener(this);
        fab_5_min.setOnClickListener(this);
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

                            setTimerTask();
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

    /**
     * 메뉴 on/off
     */
    private void toggleFab() {
        if (isFabOpen) {
            fab_main.setImageResource(R.drawable.plus_btn);
            fab_word.startAnimation(fab_close);
            fab_time.startAnimation(fab_close);
            fab_flip.startAnimation(fab_close);
            fab_word.setClickable(false);
            fab_time.setClickable(false);
            fab_flip.setClickable(false);
            isFabOpen = false;

            if (isTimeFabOpen) {
                isTimeFabOpen = true;
                toggleTimeFab();
            }
        } else {
            fab_main.setImageResource(R.drawable.close_btn);
            fab_word.startAnimation(fab_open);
            fab_time.startAnimation(fab_open);
            fab_flip.startAnimation(fab_open);
            fab_word.setClickable(true);
            fab_time.setClickable(true);
            fab_flip.setClickable(true);
            isFabOpen = true;
        }
    }

    /**
     * 시간 선택하는 메뉴 on/off
     */
    private void toggleTimeFab() {
        if (isTimeFabOpen) {
            fab_10_sec.startAnimation(fab_sub_close);
            fab_30_sec.startAnimation(fab_sub_close);
            fab_1_min.startAnimation(fab_sub_close);
            fab_5_min.startAnimation(fab_sub_close);
            fab_10_sec.setClickable(false);
            fab_30_sec.setClickable(false);
            fab_1_min.setClickable(false);
            fab_5_min.setClickable(false);
            isTimeFabOpen = false;
        } else {
            fab_10_sec.startAnimation(fab_sub_open);
            fab_30_sec.startAnimation(fab_sub_open);
            fab_1_min.startAnimation(fab_sub_open);
            fab_5_min.startAnimation(fab_sub_open);
            fab_10_sec.setClickable(true);
            fab_30_sec.setClickable(true);
            fab_1_min.setClickable(true);
            fab_5_min.setClickable(true);
            isTimeFabOpen = true;
        }
    }

    /**
     * 타이머 돌리는 메소드
     */
    private void setTimerTask() {
        if (timerTask != null)
            timerTask.cancel();

        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvSettingStatus.setText(strPeriod + " / " + strFlipMode);
                        if (i < 20) {
                            if (isFlipMode) {
                                tvDown.setText(vocaInfos.get(i).getVocabulary());
                                tvUp.setText(vocaInfos.get(i).getMean());
                            } else {
                                tvUp.setText(vocaInfos.get(i).getVocabulary());
                                tvDown.setText(vocaInfos.get(i).getMean());
                            }
                            i++;
                        }

                        if (i == 20) {
                            i = 0;
                        }
                    }
                });
            }
        };
        timer.schedule(timerTask, 0, period);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_main:
                toggleFab();
                break;
            case R.id.fab_word:
                toggleFab();
                Intent intent = new Intent(this, WordActivity.class);
                intent.putExtra("vocaInfos", vocaInfos);
                startActivity(intent);
                break;
            case R.id.fab_time:
                toggleTimeFab();
                break;
            case R.id.fab_flip:
                if (isFlipMode) {
                    fab_flip.setImageResource(R.drawable.car_btn);
                    strFlipMode = getString(R.string.str_normal_mode);
                    tvUp.setScaleY(1);
                    tvDown.setScaleY(1);
                    isFlipMode = false;
                } else {
                    fab_flip.setImageResource(R.drawable.phone_btn);
                    strFlipMode = getString(R.string.str_car_mode);
                    tvUp.setScaleY(-1); //상하 거울 형식으로 뒤집기
                    tvDown.setScaleY(-1);
                    isFlipMode = true;
                }
                tvSettingStatus.setText(strPeriod + " / " + strFlipMode);
                break;
            case R.id.fab_10_sec:
                period = 10 * 1000;
                strPeriod = getString(R.string.str_10_sec);
                setTimerTask();
                break;
            case R.id.fab_30_sec:
                period = 30 * 1000;
                strPeriod = getString(R.string.str_30_sec);
                setTimerTask();
                break;
            case R.id.fab_1_min:
                period = 60 * 1000;
                strPeriod = getString(R.string.str_1_min);
                setTimerTask();
                break;
            case R.id.fab_5_min:
                period = 300 * 1000;
                strPeriod = getString(R.string.str_5_min);
                setTimerTask();
                break;
        }
    }
}
