package a.team.works.u22.hal.u22teama;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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
public class TabLayoutSampleActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, TabPage1Fragment.OnFragmentInteractionListener, TabPage2Fragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_layout_sample);

        //xmlからTabLayoutの取得
        TabLayout  tabLayout = findViewById(R.id.tabs);
        //xmlからViewPagerを取得
        ViewPager viewPager = findViewById(R.id.pager);
        //ページタイトル配列
        final String[] pageTitle = {getString(R.string.tv_mypage_title), getString(R.string.tv_mypage_info)};

        //表示Pageに必要な項目を設定
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            /**
             * Tabの内を売を設定するメソッド.
             * switch文で設定するFragmentを分けている。
             *
             * @param position 設定するTabのページ番号(0から)。
             * @return Tabページに設定するFragment。
             */
            @Override
            public Fragment getItem(int position) {
                //各タブに設定するFragmentを選択する。
                switch (position) {
                    case 0:
                        return TabPage1Fragment.newInstance(position + 1);
                    case 1:
                        return TabPage2Fragment.newInstance(position + 1);
                    default:
                        return TabPage1Fragment.newInstance(position + 1);
                }
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return pageTitle[position];
            }

            @Override
            public int getCount() {
                return pageTitle.length;
            }
        };
        //ViewPagerにページを設定
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);

        //ViewPagerをTabLayoutを設定
        tabLayout.setupWithViewPager(viewPager);

        //初期に選択されているTabを設定(0始まり)
        tabLayout.getTabAt(0).select();
    }

    //以下の内容を実装する必要がある。（空で問題ない）

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    /**
     * 変更ボタンを押下したときの処理を記述したメソッド
     *
     * @param view button
     */
    public void onChangeClick(View view) {
        Intent intent = new Intent(TabLayoutSampleActivity.this, MypageChangeActivity.class);

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
