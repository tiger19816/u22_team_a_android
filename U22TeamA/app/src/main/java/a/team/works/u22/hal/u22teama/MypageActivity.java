package a.team.works.u22.hal.u22teama;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.OnMapReadyCallback;

/**
 * タブレイアウトサンプル画面のActivityクラス.
 * ViewPager.OnPageChangeListenerを実装(implements)する。
 * 各ページのFragmentのOnFragmentInteractionListenerを実装(implements)する。
 *
 * @author Taiga Hirai
 */
public class MypageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tab_page1);
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
//            intent = new Intent(MypageActivity.this,FemaleMyPageActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
        }else if (id == R.id.nav_logout){
            //ユーザーID削除。
            SharedPreferences setting = getSharedPreferences("USER" , 0);
            SharedPreferences.Editor editor = setting.edit();
            editor.remove("ID");
            editor.commit();
            intent = new Intent(MypageActivity.this,MainActivity.class);
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
}