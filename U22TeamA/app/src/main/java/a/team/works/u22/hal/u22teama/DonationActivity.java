package a.team.works.u22.hal.u22teama;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.EditText;
import android.widget.Spinner;
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

public class DonationActivity extends AppCompatActivity implements TextWatcher {
    //Preferences のキー名
    final private String preferencesKey = "prefUserId";
    private static final String DONATIONSET_URL = GetUrl.DonationSetUrl;
    private static String projectNo = "1";
    private static String memberNo = "3";
    private static String donationMoney;
    private static int prFirstMax = 3000;
    private int prSecondMax;
    private String cleaningFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);

        //ツールバー(レイアウトを変更可)。
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //戻るボタン
        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeButtonEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        setTitle("寄付金額入力");

        Intent intent = getIntent();
        // アプリ標準の Preferences を取得する
        SharedPreferences sp = getSharedPreferences(preferencesKey , 0);
        memberNo = String.valueOf(sp.getInt("id", 0));

        projectNo = (intent.getStringExtra("projectNo"));
        String title = (intent.getStringExtra("title"));
        cleaningFlag = (intent.getStringExtra("cleaningFlag"));
        donationMoney = (intent.getStringExtra("donationMoney"));
        prSecondMax = Integer.valueOf(intent.getStringExtra("TargetMoney"));

        TextView tvTitle = findViewById(R.id.tv_donation_title);
        tvTitle.setText(title);
        //ProgressBarrのセット
        SetProgresBarr();

        Button btCheck = findViewById(R.id.btCheckButton);
        btCheck.setOnClickListener(new donationCheckListener());


        EditText editText = findViewById(R.id.editText);
        // リスナーを登録
        editText.addTextChangedListener(this);


    }

    private class donationCheckListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if (!"0".equals(memberNo)) {
                EditText spn = (EditText) findViewById(R.id.editText);
                if(spn.getText().toString().equals("")||spn.getText().toString().equals(null)){
                    Toast.makeText(DonationActivity.this, "寄付金額を入力してください。", Toast.LENGTH_SHORT).show();
                }else {
                    String s_money = spn.getText().toString();
                    int money = Integer.parseInt(s_money);
                    if(money == 0){
                        Toast.makeText(DonationActivity.this, "寄付金額に0は入力できません。", Toast.LENGTH_SHORT).show();
                    }else{
                        DonationCheckDialog dialog = new DonationCheckDialog();
                        Bundle args = new Bundle();
                        String donationMoney = (String) spn.getText().toString();
                        args.putString("donationMoney", donationMoney);
                        dialog.setArguments(args);
                        dialog.show(getFragmentManager(), "checker");
                    }
                }
            }else{
                Toast.makeText(DonationActivity.this, "ログインIDを取得できていません。", Toast.LENGTH_SHORT).show();

            }
        }
    }

    public void donationSend(){
        EditText spn = (EditText)findViewById(R.id.editText);
        String donationMoney = (String)spn.getText().toString();
        DonationSetTaskReceiver receiver = new DonationSetTaskReceiver();
        receiver.execute(DONATIONSET_URL, projectNo, memberNo, donationMoney);

        Toast.makeText(DonationActivity.this, "寄付ありがとうございました", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(DonationActivity.this, TabLayoutCleanActivity.class);
        intent.putExtra("mood", 1);
        startActivity(intent);
    }

    /**
     * 非同期通信を行うAsyncTaskクラスを継承したメンバクラス.
     */
    private class DonationSetTaskReceiver extends AsyncTask<String, Void, String> {

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
            String projectNo = params[1];
            String memberNo = params[2];
            String donation = params[3];
            //POSTで送りたいデータ
            String postData = "projectNo=" + projectNo + "&memberNo=" + memberNo+ "&donationMoney=" + donation;

            Log.e("neko",postData);

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
            try {
                JSONObject rootJSON = new JSONObject(result);

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
            tvFirst.setText("一段階目 : " + prFirstMax + "円 / " + prFirstMax +"円" );
            prSecond.setMax(prSecondMax);
            prSecond.setProgress(Integer.parseInt(donationMoney));
        }

    }


    /**
     * アクションバーの機能
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /**
             * 戻るボタンが押された時
             */
            case android.R.id.home:
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    //-------------------
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        // テキスト変更後に変更されたテキストを取り出す
        String inputStr= s.toString();
        //金額が入力された時に変動する２段階目のプログレスバー
        if(!"".equals(inputStr)) {
            //仮入金金額表示
            if(Integer.parseInt(cleaningFlag) >= 2){
                TextView tvIfMoney = findViewById(R.id.tvSecondPD);
                tvIfMoney.setText("二段階目 : " + ((Integer.parseInt(donationMoney) + Integer.parseInt(inputStr))) + "円 / " + prFirstMax +"円" );
                TextView tvMax = findViewById(R.id.tvFirstPD);
                tvMax.setText("一段階目 : " + prFirstMax + "円 / " + prFirstMax +"円" );
                ProgressBar prSecond = findViewById(R.id.pb_second);
                prSecond.setMax(prSecondMax);
                prSecond.setSecondaryProgress(Integer.parseInt(donationMoney) +Integer.parseInt(inputStr));
            }else{
                TextView tvIfMoney = findViewById(R.id.tvFirstPD);
                tvIfMoney.setText("一段階目 : " + ((Integer.parseInt(donationMoney) + Integer.parseInt(inputStr))) + "円 / " + prFirstMax +"円" );
                ProgressBar prFirst = findViewById(R.id.pb_first);
                prFirst.setSecondaryProgress(Integer.parseInt(donationMoney) + Integer.parseInt(inputStr));
            }

        }else{
            if(Integer.parseInt(cleaningFlag) >= 2){
                TextView tvIfMoney = findViewById(R.id.tvSecondPD);
                tvIfMoney.setText("二段階目 : " + donationMoney + "円 / " + prFirstMax +"円" );
                TextView tvMax = findViewById(R.id.tvFirstPD);
                tvMax.setText("一段階目 : " + prFirstMax + "円 / " + prFirstMax +"円" );
                ProgressBar prSecond = findViewById(R.id.pb_second);
                prSecond.setSecondaryProgress(0);
            }else{

                TextView tvIfMoney = findViewById(R.id.tvFirstPD);
                tvIfMoney.setText("一段階目 : " + donationMoney + "円 / " + prFirstMax +"円" );
                ProgressBar prFirst = findViewById(R.id.pb_first);
                prFirst.setSecondaryProgress(0);
            }
        }
    }

}
