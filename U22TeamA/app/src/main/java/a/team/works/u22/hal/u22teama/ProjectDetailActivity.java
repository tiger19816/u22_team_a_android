package a.team.works.u22.hal.u22teama;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

public class ProjectDetailActivity extends AppCompatActivity {
    private static final String ProjectDetail_URL = GetUrl.ProjectInfoUrl;
    private static String projectNo = "5";
    //プログレスバー１番の最大値設定
    private final int prFirstMax = 3000;
    private String donationMoney ="";
    private String cleaningFlag ="";
    private String prSecondMax ="0";
    private String title ="";
    private String allMoney ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail_activity);

        Intent intent = getIntent();
        projectNo = (intent.getStringExtra("projectId"));
        ProjectInfoTaskReceiver receiver = new ProjectInfoTaskReceiver();

        Button btn = findViewById(R.id.bt_FundRaising);
        btn.setOnClickListener(new ButtonClickListener());

        //ここで渡した引数はLoginTaskReceiverクラスのdoInBackground(String... params)で受け取れる。
        receiver.execute(ProjectDetail_URL, projectNo);

        //ツールバー(レイアウトを変更可)。
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("プロジェクト詳細");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 非同期通信を行うAsyncTaskクラスを継承したメンバクラス.
     */
    private class ProjectInfoTaskReceiver extends AsyncTask<String, Void, String> {

        private static final String DEBUG_TAG = "RestAccess";

        /**
         * 非同期に処理したい内容を記述するメソッド.
         * このメソッドは必ず実装する必要がある。
         *
         * @param params String型の配列。（可変長）
         * @return String型の結果JSONデータ。
         */
        @Override
        public String doInBackground(String... params) {
            String urlStr = params[0];
            String no = params[1];

            //POSTで送りたいデータ
            String postData = "no=" + no;

            HttpURLConnection con = null;
            InputStream is = null;
            String result = "";

            try {
                URL url = new URL(urlStr);
                con = (HttpURLConnection) url.openConnection();

                //GET通信かPOST通信かを指定する。
                con.setRequestMethod("POST");
                //自動リダイレクトを許可するかどうか。
                con.setInstanceFollowRedirects(false);
                //時間制限。（ミリ秒単位）
                con.setReadTimeout(10000);
                con.setConnectTimeout(20000);
                con.setDoOutput(true);
                //POSTデータ送信処理。InputStream処理よりも先に記述する。
                OutputStream os = null;
                try {
                    os = con.getOutputStream();
                    //送信する値をByteデータに変換する（UTF-8）
                    os.write(postData.getBytes("UTF-8"));
                    os.flush();
                } catch (IOException ex) {
                    Log.e(DEBUG_TAG, "POST送信エラー", ex);
                } finally {
                    if (os != null) {
                        try {
                            os.close();
                        } catch (IOException ex) {
                            Log.e(DEBUG_TAG, "OutputStream解放失敗", ex);
                        }
                    }
                }

                is = con.getInputStream();

                result = is2String(is);
            } catch (MalformedURLException ex) {
                Log.e(DEBUG_TAG, "URL変換失敗", ex);
            } catch (IOException ex) {
                Log.e(DEBUG_TAG, "通信失敗", ex);
            } finally {
                if (con != null) {
                    con.disconnect();
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException ex) {
                        Log.e(DEBUG_TAG, "InputStream解放失敗", ex);
                    }
                }
            }

            return result;
        }

        @Override
        public void onPostExecute(String result) {
            Boolean isLogin = false;
            try {
                JSONObject rootJSON = new JSONObject(result);
                Log.e("neko", result);
                //画像
                String photo = rootJSON.getString("photo");
                WebView myWebView = findViewById(R.id.wvProjectPhoto);
                myWebView.setWebViewClient(new WebViewClient());
                myWebView.getSettings().setUseWideViewPort(true);
                myWebView.getSettings().setLoadWithOverviewMode(true);
                myWebView.loadUrl(GetUrl.photoUrl + photo);

                //日付
                String postDate = rootJSON.getString("postDate");
                postDate = DataConversion.getDataConversion02(postDate);
                TextView tvPostDate = findViewById(R.id.tv_OrderDateInfo);
                tvPostDate.setText(postDate);
                //場所
                String place = rootJSON.getString("place");
                TextView tvPlace = findViewById(R.id.tv_PlaceInfo);
                tvPlace.setText(place);
                //内容
                String content = rootJSON.getString("content");
                TextView tvContent = findViewById(R.id.tv_ContentInfo);
                tvContent.setText(content);
                //タイトル
                title = rootJSON.getString("title");
                TextView tvTitle = findViewById(R.id.tv_title);
                tvTitle.setText(title);
                //現在の寄付金額
                String fundRaising = rootJSON.getString("donationMoney");
                donationMoney = fundRaising;
                TextView tvFundRaising = findViewById(R.id.tv_FundRaisingInfo);
                tvFundRaising.setText(fundRaising);
                //掃除進行状況
                cleaningFlag = rootJSON.getString("cleanFlag");
                //目標金額
                prSecondMax = rootJSON.getString("targetMoney");

                SetProgresBarr();

            } catch(JSONException ex) {
                Log.e(DEBUG_TAG, "JSON解析失敗", ex);
            }
        }

        private String is2String(InputStream is) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuffer sb = new StringBuffer();
            char[] b = new char[1024];
            int line;
            while (0 <= (line = reader.read(b))) {
                sb.append(b, 0, line);
            }
            return sb.toString();
        }
    }

    private class  ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            TextView tvTargetMoney = findViewById(R.id.tv_FundRaisingInfo);

            Intent intent = new Intent(ProjectDetailActivity.this, DonationActivity.class);
            intent.putExtra("title", title);
            intent.putExtra("cleaningFlag",cleaningFlag);
            intent.putExtra("donationMoney", donationMoney );
            intent.putExtra("projectNo",projectNo);
            intent.putExtra("TargetMoney",prSecondMax);
            startActivity(intent);
        }
    }

    private void SetProgresBarr(){
        ProgressBar prFirst = findViewById(R.id.pb_first);
        TextView tvFirst = findViewById(R.id.tvFirstPD);
        tvFirst.setText(tvFirst.getText().toString() + " : "  + donationMoney +"円 / " + prFirstMax+ "円");
        prFirst.setMax(prFirstMax);
        prFirst.setProgress(Integer.parseInt(donationMoney));
        if(Integer.parseInt(cleaningFlag) >= 2){
            ProgressBar prSecond = findViewById(R.id.pb_second);
            TextView tvSecond = findViewById(R.id.tvSecondPD);
            tvSecond.setText(tvSecond.getText().toString() + " : " + donationMoney + "円 / "  + prSecondMax + "円");
            prSecond.setMax(Integer.parseInt(prSecondMax));
            prSecond.setProgress(Integer.parseInt(donationMoney));
        }

    }
}
