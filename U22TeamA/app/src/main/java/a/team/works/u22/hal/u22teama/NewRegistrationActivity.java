package a.team.works.u22.hal.u22teama;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioButton;

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
import android.os.AsyncTask;
import android.widget.Toast;
import android.app.DatePickerDialog;
import java.util.Calendar;
import android.widget.DatePicker;
import android.widget.TextView;

public class NewRegistrationActivity extends AppCompatActivity {

    /**
     * ログインする先のURLを入れる定数.
     * AndroidエミュレータからPC内のサーバ(Eclipse上)にアクセスする場合は、localhost(127.0.0.1)ではなく、10.0.2.2にアクセスする。
     */
    private static final String LOGIN_URL = "http://10.0.2.2:8080/u22_team_a_web/NewRegistrationServlet";
    private static final String MYPAGE_URL = "http://10.0.2.2:8080/u22_team_a_web/MypageChangeCompleteServlet";

    int displayInt = 0;
    String displayChar = "";
    private int year;
    private int month;
    private int day;
    int flag;

    CharSequence input_name;
    CharSequence input_birthday;
    CharSequence input_address;
    CharSequence input_phone_number;
    CharSequence input_credit_card_number;
    CharSequence input_mail;
    CharSequence input_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_registration);

        final String TAG = "DialogTest";

        ((Button) findViewById(R.id.Registration)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final EditText text_Name = findViewById(R.id.input_name);
                final TextView text_Birthday = findViewById(R.id.input_birthday);
                final EditText text_Address = findViewById(R.id.input_address);
                final EditText text_Phone_Number = findViewById(R.id.input_phone_number);
                final RadioGroup text_sex = findViewById(R.id.RadioGroup);
                final EditText text_Credit_Card_Number = findViewById(R.id.input_credit_card_number);
                final EditText text_mail = findViewById(R.id.input_mail);
                final EditText text_Password = findViewById(R.id.input_password);

                if (ErrorCheck(text_Name)) {
                    input_name = text_Name.getText();
                }
                if (ErrorCheck(text_Birthday)) {
                    input_birthday = text_Birthday.getText();
                }
                if (ErrorCheck(text_Address)) {
                    input_address = text_Address.getText();
                }
                if (ErrorCheck(text_Phone_Number)) {
                    input_phone_number = text_Phone_Number.getText();
                }
                if (ErrorCheck(text_Credit_Card_Number)) {
                    input_credit_card_number = text_Credit_Card_Number.getText();
                }
                if (ErrorCheck(text_mail)) {
                    input_mail = text_mail.getText();
                }
                if (ErrorCheck(text_Password)) {
                    input_password = text_Password.getText();
                }

                int radioId = text_sex.getCheckedRadioButtonId();
                RadioButton rbtSex = findViewById(radioId);

                switch (radioId) {
                    case R.id.bt_men:
                        if (rbtSex.isChecked()) {
                            displayInt = 0;
                            displayChar = "男";
                        }
                        break;
                    case R.id.bt_women:
                        if (rbtSex.isChecked()) {
                            displayInt = 1;
                            displayChar = "女";
                        }
                        break;
                }



                if (flag != 0) {
                    Toast.makeText(NewRegistrationActivity.this, R.string.msg_items, Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(NewRegistrationActivity.this, R.style.AlertDialogStyle);
                    new AlertDialog.Builder(NewRegistrationActivity.this);



                    builder.setTitle("確認");
                    builder.setMessage("氏名：" + input_name + "\n"
                            + "生年月日：" + input_birthday + "\n"
                            + "住所：" + input_address + "\n"
                            + "電話番号：" + input_phone_number + "\n"
                            + "性別：" + displayChar + "\n"
                            + "クレジット番号：" + input_credit_card_number + "\n"
                            + "メールアドレス：" + input_mail + "\n"
                            + "パスワード：" + input_password + "");

                    builder.setPositiveButton("登録する", new DialogInterface.OnClickListener() {


                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String strName = text_Name.getText().toString();
                            String strBirthday = text_Birthday.getText().toString();
                            String strAddress = text_Address.getText().toString();
                            String strPhoneNumber = text_Phone_Number.getText().toString();
                            String strCreditCardNumber = text_Credit_Card_Number.getText().toString();
                            String strSex = displayChar;
                            String strMail = text_mail.getText().toString();
                            String strPassword = text_Password.getText().toString();

                            //非同期処理を開始する。
                            RegistrationTaskReceiver receiver = new RegistrationTaskReceiver();
                            //ここで渡した引数はLoginTaskReceiverクラスのdoInBackground(String... params)で受け取れる。
                            receiver.execute(LOGIN_URL, strName, strBirthday, strAddress, strPhoneNumber, strSex, strCreditCardNumber, strMail, strPassword);

                            RegistrationTaskReceiver mypage_receiver = new RegistrationTaskReceiver();
                            mypage_receiver.execute(MYPAGE_URL, strName, strBirthday, strAddress, strPhoneNumber, strSex, strCreditCardNumber, strMail, strPassword);


                        }
                    })

                            .setNegativeButton("訂正する", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    flag = 1;
                                }
                            })
                            .show();
                }
            }
        });

    }

    /**
     * 非同期通信を行うAsyncTaskクラスを継承したメンバクラス.
     */
    private class RegistrationTaskReceiver extends AsyncTask<String, Void, String> {

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
            String birthday = params[2];
            String address = params[3];
            String phoneNumber = params[4];
            String sex = params[5];
            String creditNumber = params[6];
            String mail = params[7];
            String password = params[8];

            //POSTで送りたいデータ
            String postData = "name=" + name + "&birthday=" + birthday + "&address=" + address + "&phoneNumber=" + phoneNumber + "&sex=" + sex + "&creditNumber=" + creditNumber + "&mail=" + mail + "&password=" + password;
            String postDataMypage = "name=" + name + "&birthday=" + birthday + "&address=" + address + "&phoneNumber=" + phoneNumber + "&sex=" + sex + "&creditNumber=" + creditNumber + "&mail=" + mail + "&password=" + password;

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
                    os.write(postDataMypage.getBytes("UTF-8"));
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
            Boolean isRegistration = false;
            try {
                JSONObject rootJSON = new JSONObject(result);
                isRegistration = rootJSON.getBoolean("result");
                String name = rootJSON.getString("name");
            } catch (JSONException ex) {
                Log.e(DEBUG_TAG, "JSON解析失敗", ex);
            }
            if (isRegistration) {
                Toast.makeText(NewRegistrationActivity.this, "完了", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(NewRegistrationActivity.this, "失敗", Toast.LENGTH_SHORT).show();
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

    /**
     * 日付選択ボタンが押された時のイベント処理用メソッド。
     */
    public void onUpdateButtonClick(View view) {
        int nowYear = year;
        int nowMonth = month - 1;
        int nowDayOfMonth = day;

        DatePickerDialog dialog = new DatePickerDialog(NewRegistrationActivity.this, new DatePickerDialogDateSetListener(), nowYear, nowMonth, nowDayOfMonth);
        dialog.show();
    }


    /**
     * 日付選択ダイアログ表示ボタンが押された時の処理が記述されたメンバクラス。
     */
    private class DatePickerDialogDateSetListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int s_year, int monthOfYear, int dayOfMonth) {
            String s_month = String.valueOf(monthOfYear + 1);
            String s_day = String.valueOf(dayOfMonth);
            if (s_month.length() == 1) {
                s_month = "0" + s_month;
            }
            if (s_day.length() == 1) {
                s_day = "0" + s_day;
            }
            String msg = +s_year + "年" + s_month + "月" + s_day + "日";
            //          Toast.makeText(NewRegistrationActivity.this, msg, Toast.LENGTH_SHORT).show();
            year = s_year;
            month = Integer.parseInt(s_month);
            day = Integer.parseInt(s_day);

            TextView et_birthday = findViewById(R.id.input_birthday);
            et_birthday.setText(msg);
        }
    }
    public boolean ErrorCheck(EditText str){
        flag = 0;
        if(!"".equals(str.getText().toString())){
            return true;
        }
        flag++;
        return false;
    }
    public boolean ErrorCheck(TextView str){
        flag = 0;
        if(!"".equals(str.getText().toString())){
            return true;
        }
        flag++;
        return false;
    }
}
