package a.team.works.u22.hal.u22teama;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ProjectSearchMapsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    private GoogleMap mMap;
    private ListView lvProjectList;    // 内容エリア
    private FloatingActionButton fab;
    private LinearLayout linearLayoutArea;

    /**
     * アニメーションにかける時間（ミリ秒）
     */
    private final static int DURATION = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_search_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.projectMaps);
        mapFragment.getMapAsync(this);

//        //ツールバー(レイアウトを変更可)。
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

        setTitle("プロジェクト検索");


        // 内容エリアの結び付け
        lvProjectList = findViewById(R.id.lvProjectList);
        // ボタンの結び付け
        fab = findViewById(R.id.fabOpenList);

        linearLayoutArea = findViewById(R.id.llProjectMapMain);

        // ExpandするViewの元のサイズを保持
        final int originalHeight = linearLayoutArea.getHeight() / 2;

        // 内容エリアを閉じるアニメーション
        ProjectSearchMapsAnimation closeAnimation = new ProjectSearchMapsAnimation(lvProjectList, -originalHeight, originalHeight);
        closeAnimation.setDuration(DURATION);
        lvProjectList.startAnimation(closeAnimation);
    }

    /**
     * レフトナビ以外をクリックした時の動き。
     */
    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = findViewById(R.id.dlMainContent);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
    }

    /**
     * レフトナビをクリックした時。
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
//        int id = item.getItemId();

//        Intent intent;
//        if (id == R.id.nav_map) {
////            intent = new Intent(FemaleStoreMapListActivity.this,FemaleStoreMapListActivity.class);
////            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////            startActivity(intent);
//        } else if (id == R.id.nav_reservation) {
//            intent = new Intent(FemaleStoreMapListActivity.this,FemaleReservationListActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//        } else if (id == R.id.nav_history) {
//            intent = new Intent(FemaleStoreMapListActivity.this,FemaleHistoryListActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//        } else if (id == R.id.nav_my_page) {
//            intent = new Intent(FemaleStoreMapListActivity.this,FemaleMyPageActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//        }else if (id == R.id.nav_logout){
//            //ユーザーID削除。
//            SharedPreferences setting = getSharedPreferences("USER" , 0);
//            SharedPreferences.Editor editor = setting.edit();
//            editor.remove("ID");
//            editor.commit();
//            intent = new Intent(FemaleStoreMapListActivity.this,MainActivity.class);
//            finish();
//            startActivity(intent);
//        }
//
//        DrawerLayout drawer = findViewById(R.id.dlMainContent);
//        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION)  != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            /** fine location のリクエストコード（値は他のパーミッションと被らなければ、なんでも良い）*/
            final int requestCode = 1;

            // いずれも得られていない場合はパーミッションのリクエストを要求する
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, requestCode );
            return;
        }

        // 位置情報を管理している LocationManager のインスタンスを生成する
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        String locationProvider = null;

        // GPSが利用可能になっているかどうかをチェック
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationProvider = LocationManager.GPS_PROVIDER;
        }
        // GPSプロバイダーが有効になっていない場合は基地局情報が利用可能になっているかをチェック
        else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationProvider = LocationManager.NETWORK_PROVIDER;
        }
        // いずれも利用可能でない場合は、GPSを設定する画面に遷移する
        else {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
            return;
        }
        // 最新の位置情報
        Location location = locationManager.getLastKnownLocation(locationProvider);

        if (location != null) {
            // Add a marker in Sydney and move the camera
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,14));
        } else {
            //大阪市役所34.693835, 135.501929
            Toast.makeText(ProjectSearchMapsActivity.this, "現在地情報の取得に失敗しました。", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        // 内容エリアの結び付け
        lvProjectList = findViewById(R.id.lvProjectList);
        // ボタンの結び付け
        fab = findViewById(R.id.fabOpenList);

        // ExpandするViewの元のサイズを保持
        final int originalHeight = linearLayoutArea.getHeight() / 2;

        // 展開ボタン押下時
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (lvProjectList.getHeight() > 0) {       // 内容エリアが開いている時

                    // 内容エリアを閉じるアニメーション
                    ProjectSearchMapsAnimation closeAnimation = new ProjectSearchMapsAnimation(lvProjectList, -originalHeight, originalHeight);
                    closeAnimation.setDuration(DURATION);
                    lvProjectList.startAnimation(closeAnimation);
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) fab.getLayoutParams();
                    params.bottomMargin = 190;
                    fab.setLayoutParams(params);
                } else {

                    // 内容エリアが閉じている時、内容エリアを開くアニメーション
                    ProjectSearchMapsAnimation openAnimation = new ProjectSearchMapsAnimation(lvProjectList, originalHeight, 0);
                    openAnimation.setDuration(DURATION);    // アニメーションにかける時間(ミリ秒)
                    lvProjectList.startAnimation(openAnimation);   // アニメーション開始
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) fab.getLayoutParams();
                    params.bottomMargin = originalHeight + 170;
                    fab.setLayoutParams(params);
                }
            }
        });
    }

    /**
     * フローティングアクションボタンが押された時のイベント処理用メソッド.
     *
     * @param view 画面部品。
     */
    public void onFabOpenListClick(View view) {
    }
}
