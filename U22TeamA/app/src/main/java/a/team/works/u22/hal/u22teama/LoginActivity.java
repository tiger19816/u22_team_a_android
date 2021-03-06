package a.team.works.u22.hal.u22teama;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class LoginActivity  extends AppCompatActivity {

    private static final String LOGIN_URL = GetUrl.LoginUrl;

    int userId = 0;
    String userName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        SharedPreferences pref = getSharedPreferences("prefUserId",0);
        int loginInfo = pref.getInt("id", 0);
        if(loginInfo != 0){
            Intent intent = new Intent(LoginActivity.this, ProjectSearchMapsActivity.class);
            startActivity(intent);
            finish();
        }
    }


    public void onNewRegistrationClick(View view) {
        Intent intent = new Intent(getApplication(), NewRegistrationActivity.class);
        startActivity(intent);
    }

    /**
     * ログインボタンが押された時の処理.
     *
     * @param view 画面部品。
     */
    public void onUserLoginClick(View view) {
        //ユーザーIDの取得。
        EditText etId = findViewById(R.id.etLoginId);
        String strId = etId.getText().toString();

        //パスワードの取得。
        EditText etPassword = findViewById(R.id.etPassWord);
        String strPassword = etPassword.getText().toString();
        if ("".equals(strId) || "".equals(strPassword)) {
            Toast.makeText(LoginActivity.this, R.string.msg_items, Toast.LENGTH_SHORT).show();
        } else {
            //非同期処理を開始する。
            LoginTaskReceiver receiver = new LoginTaskReceiver();
            //ここで渡した引数はLoginTaskReceiverクラスのdoInBackground(String... params)で受け取れる。
            receiver.execute(LOGIN_URL, strId, strPassword);
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
            String password = params[2];

            //POSTで送りたいデータ
            String postData = "id=" + id + "&password=" + password;

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
                isLogin = rootJSON.getBoolean("result");
                userId = rootJSON.getInt("userId");
                userName = rootJSON.getString("userName");

                SharedPreferences prefUserId = getSharedPreferences("prefUserId",0);//MODE_WORLD_WRITEABLE
                Editor e = prefUserId.edit();
                e.putInt("id",userId);
                e.putString("name",userName);
                e.commit();

            } catch (JSONException ex) {
                Log.e(DEBUG_TAG, "JSON解析失敗", ex);
            }
            if (isLogin) {
                Toast.makeText(LoginActivity.this, "成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, ProjectSearchMapsActivity.class);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(LoginActivity.this, "メールアドレス又はパスワードが間違っています。", Toast.LENGTH_SHORT).show();
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

        private class DialogButtonClickListener implements DialogInterface.OnClickListener {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }
    }
}
