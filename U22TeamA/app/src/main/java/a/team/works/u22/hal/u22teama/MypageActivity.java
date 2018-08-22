package a.team.works.u22.hal.u22teama;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * タブレイアウトサンプル画面のActivityクラス.
 * ViewPager.OnPageChangeListenerを実装(implements)する。
 * 各ページのFragmentのOnFragmentInteractionListenerを実装(implements)する。
 *
 * @author Taiga Hirai
 */
public class MypageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_layout_sample);
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
