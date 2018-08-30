package a.team.works.u22.hal.u22teama;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.OnMapReadyCallback;

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
 * タブレイアウトサンプル画面のActivityクラス.
 * ViewPager.OnPageChangeListenerを実装(implements)する。
 * 各ページのFragmentのOnFragmentInteractionListenerを実装(implements)する。
 *
 * @author Taiga Hirai
 */
public class MypageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String LOGIN_URL = GetUrl.MypageChangeUrl;
    private Intent intent;
    String userName = "";
    String userBirthdate = "";
    String userAddress = "";
    String userSex = "";
    String userMail = "";
    String userPhone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tab_page1);

        //ツールバー(レイアウトを変更可)。
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.dlMainContent);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //レフトナビ本体。
        NavigationView navigationView = findViewById(R.id.nvSideMenuButton);
        navigationView.setNavigationItemSelectedListener(this);

        setTitle("マイページ");

//        //ユーザ名を表示する
//        SharedPreferences pref = getSharedPreferences("prefUserId",0);
//        if(Build.VERSION.SDK_INT < 23) {
//            TextView navTvUserName = navigationView.findViewById(R.id.navTvUserName);
//            navTvUserName.setText(pref.getString("name", "ユーザ名"));
//        } else {
//            View headerView = navigationView.getHeaderView(0);
//            TextView navTvUserName = headerView.findViewById(R.id.navTvUserName);
//            navTvUserName.setText(pref.getString("name", "ユーザ名"));
//        }
//        int loginInfo = pref.getInt("id", 0);
//        String loginInfoStr = String.valueOf(loginInfo);
//        //非同期処理を開始する。
//        MypageReceiver receiver = new MypageReceiver();
//        //ここで渡した引数はLoginTaskReceiverクラスのdoInBackground(String... params)で受け取れる。
//        receiver.execute(LOGIN_URL, loginInfoStr);
    }

    @Override
    public void onResume() {
        super.onResume();
        //ツールバー(レイアウトを変更可)。
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.dlMainContent);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //レフトナビ本体。
        NavigationView navigationView = findViewById(R.id.nvSideMenuButton);
        navigationView.setNavigationItemSelectedListener(this);

        setTitle("マイページ");

        //ユーザ名を表示する
        SharedPreferences pref = getSharedPreferences("prefUserId",0);
        if(Build.VERSION.SDK_INT < 23) {
            TextView navTvUserName = navigationView.findViewById(R.id.navTvUserName);
            navTvUserName.setText(pref.getString("name", "ユーザ名"));
        } else {
            View headerView = navigationView.getHeaderView(0);
            TextView navTvUserName = headerView.findViewById(R.id.navTvUserName);
            navTvUserName.setText(pref.getString("name", "ユーザ名"));
        }
        int loginInfo = pref.getInt("id", 0);
        String loginInfoStr = String.valueOf(loginInfo);
        //非同期処理を開始する。
        MypageReceiver receiver = new MypageReceiver();
        //ここで渡した引数はLoginTaskReceiverクラスvvのdoInBackground(String... params)で受け取れる。
        receiver.execute(LOGIN_URL, loginInfoStr);
    }

    /**
     * レフトナビ以外をクリックした時の動き。
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.dlMainContent);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * レフトナビをクリックした時。
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        Intent intent;
        if (id == R.id.nav_mypage) {
//            intent = new Intent(MypageActivity.this,MypageActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
        } else if (id == R.id.nav_join_project) {
            intent = new Intent(MypageActivity.this,TabLayoutCleanActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else if (id == R.id.nav_project_search) {
            intent = new Intent(MypageActivity.this,ProjectSearchMapsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_project_contribution) {
            intent = new Intent(MypageActivity.this,NewProjectPostsScreenActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_contact) {
            intent = new Intent(MypageActivity.this,ContentEditActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else if (id == R.id.nav_logout){
            //ユーザーID削除。
            SharedPreferences setting = getSharedPreferences("prefUserId" , 0);
            SharedPreferences.Editor editor = setting.edit();
            editor.remove("id");
            editor.commit();
            intent = new Intent(MypageActivity.this, LoginActivity.class);
            finish();
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.dlMainContent);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 変更ボタンを押下したときの処理を記述したメソッド
     *
     * @param view button
     */
    public void onChangeClick(View view) {
        Intent intent = new Intent(MypageActivity.this, MypageChangeActivity.class);

        String sex = "0";

        TextView tvName = findViewById(R.id.tv_mypage_name);
        TextView tvBirth = findViewById(R.id.tv_birth);
        TextView tvAddress = findViewById(R.id.tv_address);
        TextView tvSex = findViewById(R.id.tv_sex);
        if (tvSex.getText().toString().equals("女")) {
            sex = "1";
        }
        TextView tvMail = findViewById(R.id.tv_mail);
        TextView tvPhone = findViewById(R.id.tv_phone);

//        intent.putExtra("no",id);
        intent.putExtra("name", tvName.getText().toString());
        intent.putExtra("birth", tvBirth.getText().toString());
        intent.putExtra("address", tvAddress.getText().toString());
        intent.putExtra("sex", sex);
        intent.putExtra("mail", tvMail.getText().toString());
        intent.putExtra("phone", tvPhone.getText().toString());
        startActivity(intent);
    }

    /**
     * 非同期通信を行うAsyncTaskクラスを継承したメンバクラス.
     */
    private class MypageReceiver extends AsyncTask<String, Void, String> {

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
            String postData = "userId=" + id;

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
            try {
                JSONObject rootJSON = new JSONObject(result);
                String name = rootJSON.getString("name");
                String birthdate = rootJSON.getString("birthdate");
                String address = rootJSON.getString("address");
                String mail_address = rootJSON.getString("mail_address");
                String phone = rootJSON.getString("phone");

                TextView tvName = findViewById(R.id.tv_mypage_name);
                TextView tvBirth = findViewById(R.id.tv_birth);
                TextView tvAddress = findViewById(R.id.tv_address);
                TextView tvSex = findViewById(R.id.tv_sex);
                TextView tvMail = findViewById(R.id.tv_mail);
                TextView tvPhone = findViewById(R.id.tv_phone);

                SharedPreferences prefUserName = getSharedPreferences("prefUserName",0);
                SharedPreferences.Editor e = prefUserName.edit();
                e.putString("name",userName);
                e.commit();

                SharedPreferences prefUserBirthdate = getSharedPreferences("prefUserBirthdate",0);
                SharedPreferences.Editor ed = prefUserBirthdate.edit();
                ed.putString("birthdate",userBirthdate);
                ed.commit();

                SharedPreferences prefUserAddress = getSharedPreferences("prefUserAddress",0);
                SharedPreferences.Editor edi = prefUserAddress.edit();
                edi.putString("address",userAddress);
                edi.commit();

                SharedPreferences prefUserSex = getSharedPreferences("prefUserSex",0);
                SharedPreferences.Editor edit = prefUserSex.edit();
                edit.putString("sex",userSex);
                edit.commit();

                SharedPreferences prefUserMail = getSharedPreferences("prefUserMail",0);
                SharedPreferences.Editor edito = prefUserMail.edit();
                edito.putString("sex",userSex);
                edito.commit();

                SharedPreferences prefUserPhone = getSharedPreferences("prefUserPhone",0);
                SharedPreferences.Editor editor = prefUserPhone.edit();
                editor.putString("phone",userPhone);
                editor.commit();



                String sex = "男";

                tvName.setText(name);
                tvBirth.setText(birthdate);
                tvAddress.setText(address);
                if (tvSex.getText().toString().equals("1")) {
                    sex = "女";
                }
                tvSex.setText(sex);
                tvMail.setText(mail_address);
                tvPhone.setText(phone);

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
}
