package a.team.works.u22.hal.u22teama;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

/**
 * タブレイアウトサンプル画面のActivityクラス.
 * ViewPager.OnPageChangeListenerを実装(implements)する。
 * 各ページのFragmentのOnFragmentInteractionListenerを実装(implements)する。
 *
 * @author Taiga Hirai
 */
public class TabLayoutCleanActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, TabPageAssistFragment.OnFragmentInteractionListener, TabPagePostFragment.OnFragmentInteractionListener,NavigationView.OnNavigationItemSelectedListener {
    private int mood;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_layout_clean);
        Intent intent=getIntent();
        if(intent.getSerializableExtra("mood") !=null) {
            mood = (int) intent.getSerializableExtra("mood");
        }else{
            mood = 0;
        }

        //ツールバー(レイアウトを変更可)。
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //DrawerLayout
        DrawerLayout drawer = findViewById(R.id.dlMainContent);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //レフトナビ本体。
        NavigationView navigationView = findViewById(R.id.nvSideMenuButton);
        navigationView.setNavigationItemSelectedListener(this);

        setTitle("参加プロジェクト");

        //xmlからTabLayoutの取得
        TabLayout  tabLayout = findViewById(R.id.tabs);
        //xmlからViewPagerを取得
        ViewPager viewPager = findViewById(R.id.pager);
        //ページタイトル配列
        final String[] pageTitle = {getString(R.string.tv_mypage_post), getString(R.string.tv_mypage_assist)};

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
                        return TabPagePostFragment.newInstance(position + 1);
                    case 1:
                        return TabPageAssistFragment.newInstance(position + 1);
                    default:
                        return TabPagePostFragment.newInstance(position + 1);
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
        tabLayout.getTabAt(mood).select();

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
            intent = new Intent(TabLayoutCleanActivity.this,MypageActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_join_project) {
//            intent = new Intent(TabLayoutCleanActivity.this,TabLayoutCleanActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
        }else if (id == R.id.nav_project_search) {
            intent = new Intent(TabLayoutCleanActivity.this,ProjectSearchMapsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_project_contribution) {
            intent = new Intent(TabLayoutCleanActivity.this,MoveActivity.class);
            intent.putExtra("page", 1);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_contact) {
            intent = new Intent(TabLayoutCleanActivity.this,ContentEditActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else if (id == R.id.nav_logout){
            //ユーザーID削除。
            SharedPreferences setting = getSharedPreferences("prefUserId" , 0);
            SharedPreferences.Editor editor = setting.edit();
            editor.remove("id");
            editor.commit();
            intent = new Intent(TabLayoutCleanActivity.this, LoginActivity.class);
            finish();
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.dlMainContent);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

}
