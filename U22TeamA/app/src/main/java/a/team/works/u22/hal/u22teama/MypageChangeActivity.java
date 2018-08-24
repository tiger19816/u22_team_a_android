package a.team.works.u22.hal.u22teama;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
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

/**
 * 会員情報変更画面の処理を記述したActivity
 *
 * @author Miyazaki Kazuma
 */
public class MypageChangeActivity extends AppCompatActivity {

    private static final String MYPAGECHANGECOMPLETE_URL = GetUrl.MypageChangeCompleteUrl;
    //性別　0:男  1:女
    private String SEX_TYPE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_change);

        Intent intent = getIntent();

        EditText etName = findViewById(R.id.et_mypage_name);
        TextView tvBirth = findViewById(R.id.tv_birth);
        EditText etAddress = findViewById(R.id.et_address);
        RadioGroup radioSex = findViewById(R.id.radio_sex);
        EditText etMail = findViewById(R.id.et_mail);
        EditText etPhone = findViewById(R.id.et_phone);

        etName.setText(intent.getStringExtra("name"));
        tvBirth.setText(intent.getStringExtra("birth"));
        etAddress.setText(intent.getStringExtra("address"));
        SEX_TYPE = intent.getStringExtra("sex");
        etMail.setText(intent.getStringExtra("mail"));
        etPhone.setText(intent.getStringExtra("phone"));

        if (SEX_TYPE.equals("0")) {
            radioSex.check(R.id.radio_male);
        } else {
            radioSex.check(R.id.radio_female);
        }

        radioSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (SEX_TYPE.equals("0")) {
                    SEX_TYPE = "1";
                } else {
                    SEX_TYPE = "0";
                }
            }
        });

        //ツールバー(レイアウトを変更可)。
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("マイページ");

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
     * 変更完了ボタンが押された時の処理.
     *
     * @param view 画面部品。
     */
    public void onChangeClickComplete(View view){

        //ユーザーIDの取得。
//        EditText etId = findViewById(R.id.etId);
//        String strId = etId.getText().toString();

        //氏名の取得。
        EditText etName = findViewById(R.id.et_mypage_name);
        String name = etName.getText().toString();

        //住所の取得
        EditText etAddress = findViewById(R.id.et_address);
        String address = etAddress.getText().toString();

        //メールアドレスの取得
        EditText etMail = findViewById(R.id.et_mail);
        String mail = etMail.getText().toString();

        //電話番号の取得
        EditText etPhone = findViewById(R.id.et_phone);
        String phone = etPhone.getText().toString();

        //非同期処理を開始する。
        LoginTaskReceiver receiver = new LoginTaskReceiver();
        //ここで渡した引数はLoginTaskReceiverクラスのdoInBackground(String... params)で受け取れる。
        receiver.execute(MYPAGECHANGECOMPLETE_URL, name, address, SEX_TYPE, mail, phone);
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
            String name = params[1];
            String address = params[2];
            String sex = params[3];
            String mail = params[4];
            String phone= params[5];

            //POSTで送りたいデータ
            String postData = "name=" + name + "&address=" + address + "&sex=" + sex + "&mail=" + mail + "&phone=" + phone;

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
            Boolean isUpdate = false;
            try {
                JSONObject rootJSON = new JSONObject(result);
                isUpdate = rootJSON.getBoolean("result");
            }
            catch (JSONException ex) {
                Log.e(DEBUG_TAG, "JSON解析失敗", ex);
            }
            if (isUpdate) {
                Toast.makeText(MypageChangeActivity.this , "成功" , Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(MypageChangeActivity.this , "失敗" , Toast.LENGTH_SHORT).show();
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
