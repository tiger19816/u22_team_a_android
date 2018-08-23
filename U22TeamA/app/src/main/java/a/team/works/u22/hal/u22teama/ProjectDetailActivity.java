package a.team.works.u22.hal.u22teama;


import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class ProjectDetailActivity extends AppCompatActivity {
    private static final String LOGIN_URL = "http://192.168.42.27:8080/u22_team_a_web/TestServlet";
    private static String projectId = "1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail_activity);

        //Intent intent = getIntent();
        //projectId = (intent.getStringExtra("projectId"));
        LoginTaskReceiver receiver = new LoginTaskReceiver();

        Button btn = findViewById(R.id.bt_FundRaising);
        btn.setOnClickListener(new ButtonClickListener());

        //ここで渡した引数はLoginTaskReceiverクラスのdoInBackground(String... params)で受け取れる。
        receiver.execute(LOGIN_URL, projectId);

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
    private class LoginTaskReceiver extends AsyncTask<String, Void, String> {

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
            String id = params[1];

            //POSTで送りたいデータ
            String postData = "id=" + id;

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
                String postDate = rootJSON.getString("postDate");
                TextView etPostDate = findViewById(R.id.tv_OrderDateInfo);
                etPostDate.setText(postDate);
                String place = rootJSON.getString("place");
                TextView etPlace = findViewById(R.id.tv_PlaceInfo);
                etPlace.setText(place);
                String content = rootJSON.getString("content");
                TextView etContent = findViewById(R.id.tv_ContentInfo);
                etContent.setText(content);
            } catch (JSONException ex) {
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
            Spinner spn = (Spinner)findViewById(R.id.spinner);
            String item = (String)spn.getSelectedItem();

            Intent intent = new Intent(ProjectDetailActivity.this, DonationCheckActivity.class);
            intent.putExtra("projectNo",projectId);
            intent.putExtra("donationMoney",item);
            startActivity(intent);
        }
    }
}
