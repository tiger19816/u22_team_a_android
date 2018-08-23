package a.team.works.u22.hal.u22teama;

import android.support.v7.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class ContentResultActivity  extends AppCompatActivity {

    /**
     * 2画面目を開けるときのjava
     * @param savedInstanceState
     */


    /**
     * ログインする先のURLを入れる定数.
     * AndroidエミュレータからPC内のサーバ(Eclipse上)にアクセスする場合は、localhost(127.0.0.1)ではなく、10.0.2.2にアクセスする。
     */
    private static final String LOGIN_URL = "http://10.0.2.2:8080/U22Verification/LoginServlet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_result);

Intent intent = getIntent();
Bundle extras = intent.getExtras();
String content= extras.getString("content");
     EditText edContent = findViewById(R.id.edContent);
edContent.setText(content);

        Button button = findViewById(R.id.btSend);
        ButtonClickListener listener = new ButtonClickListener();
        button.setOnClickListener(listener);

        //ツールバー(レイアウトを変更可)。
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("お問い合わせ");

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

    private class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            //仮引数
            String strId = "0";

            EditText edContent = findViewById(R.id.edContent);
            String content = edContent.getText().toString();

            //非同期処理を開始する。
            LoginTaskReceiver receiver = new LoginTaskReceiver();
            //ここで渡した引数はLoginTaskReceiverクラスのdoInBackground(String... params)で受け取れる。
            receiver.execute(LOGIN_URL, strId, content);

        }
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
                String contentStr = params[2];

                //POSTで送りたいデータ
                String postData = "id=" + id + "&content=" + contentStr;

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

                    con.connect();

                    //POSTデータ送信処理。InputStream処理よりも先に記述する。
                    OutputStream os = null;
                    try {
                        os = con.getOutputStream();

                        //送信する値をByteデータに変換する（UTF-8）
                        os.write(postData.getBytes("UTF-8"));
                        os.flush();
                    }
                    catch (IOException ex) {
                        Log.e(DEBUG_TAG, "POST送信エラー", ex);
                    }
                    finally {
                        if(os != null) {
                            try {
                                os.close();
                            }
                            catch (IOException ex) {
                                Log.e(DEBUG_TAG, "OutputStream解放失敗", ex);
                            }
                        }
                    }

                    is = con.getInputStream();

                    result = is2String(is);
                }
                catch (MalformedURLException ex) {
                    Log.e(DEBUG_TAG, "URL変換失敗", ex);
                }
                catch (IOException ex) {
                    Log.e(DEBUG_TAG, "通信失敗", ex);
                }
                finally {
                    if(con != null) {
                        con.disconnect();
                    }
                    if(is != null) {
                        try {
                            is.close();
                        }
                        catch (IOException ex) {
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
                    isLogin = rootJSON.getBoolean("result");
                    String name = rootJSON.getString("name");
                }
                catch (JSONException ex) {
                    Log.e(DEBUG_TAG, "JSON解析失敗", ex);
                }
                if (isLogin) {
                    Toast.makeText(ContentResultActivity.this , "成功" , Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ContentResultActivity.this , "失敗" , Toast.LENGTH_SHORT).show();
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

            private class  DialogButtonClickListener implements DialogInterface.OnClickListener {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            }

        }


}
