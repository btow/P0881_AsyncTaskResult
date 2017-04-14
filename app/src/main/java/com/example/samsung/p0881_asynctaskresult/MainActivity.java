package com.example.samsung.p0881_asynctaskresult;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity {

    final String LOG_TAG = "myLogs";

    private class MyTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvInfo.setText(R.string.begin);
            Log.d(LOG_TAG, getString(R.string.begin));
        }

        @Override
        protected Integer doInBackground(Void... params) {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 100500;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            String message = "End. Result = " + result;
            tvInfo.setText(message);
            Log.d(LOG_TAG, message);
        }
    }

    private MyTask myTask;
    private TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvInfo = (TextView) findViewById(R.id.tvInfo);
    }

    public void onClickBtn(View view) {

        switch (view.getId()) {

            case R.id.btnStart :
                myTask = new MyTask();
                myTask.execute();
                break;
            case R.id.btnGet :
                showResult();
                break;
            default:
                break;
        }

    }

    private void showResult() {

        if (myTask == null) {
            return;
        }
        int result = -1;
        try {
            String message = "Try to get result";
            Log.d(LOG_TAG, message);
            //Попытка получения результата без тайм-аута (подвешивает основной поток)
            //result = myTask.get();
            //Попытка получения результата с тайм-аутом (подвешивает основной поток)
            result = myTask.get(1, TimeUnit.SECONDS);
            message = "Get returns " + result;
            Log.d(LOG_TAG, message);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            //Если результат не получен за отведённое время, то:
            Log.d(LOG_TAG, "Get with timeout, result = " + result);
            e.printStackTrace();
        }
    }
}
