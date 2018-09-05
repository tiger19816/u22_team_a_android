package a.team.works.u22.hal.u22teama;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectSearchMapsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, AdapterView.OnItemSelectedListener {

    private GoogleMap mMap;
    private ListView lvProjectList;    // 内容エリア
    private FloatingActionButton fab;
    private LinearLayout linearLayoutArea;
    private ArrayList<Marker> markers;
    private Intent intent;
    private int searchFlag = -1;

    public ProgressDialog _pDialog;

    private Location location = null;
    private boolean isFirst = true;

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

//      //ツールバー(レイアウトを変更可)。
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

        Spinner spinner = findViewById(R.id.spSelect);
        spinner.setOnItemSelectedListener(this);
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

        if (id == R.id.nav_mypage) {
            intent = new Intent(ProjectSearchMapsActivity.this,MypageActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_join_project) {
            intent = new Intent(ProjectSearchMapsActivity.this,TabLayoutCleanActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else if (id == R.id.nav_project_search) {
//            intent = new Intent(ProjectSearchMapsActivity.this,FemaleReservationListActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
        } else if (id == R.id.nav_project_contribution) {
            intent = new Intent(ProjectSearchMapsActivity.this,NewProjectPostsScreenActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_contact) {
            intent = new Intent(ProjectSearchMapsActivity.this,ContentEditActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else if (id == R.id.nav_logout){
            //ユーザーID削除。
            SharedPreferences setting = getSharedPreferences("prefUserId" , 0);
            SharedPreferences.Editor editor = setting.edit();
            editor.remove("id");
            editor.commit();
            intent = new Intent(ProjectSearchMapsActivity.this, LoginActivity.class);
            finish();
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.dlMainContent);
        drawer.closeDrawer(GravityCompat.START);
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
        location = locationManager.getLastKnownLocation(locationProvider);
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

    /**
     * ボタンが押された時の処理.
     *
     * @param view 画面部品。
     */
    public void onButtonClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btSurroundingProject:
                Button btSurroundingProject = (Button) view;
                btSurroundingProject.setVisibility(View.INVISIBLE);
                for(Marker marker : markers) {
                    marker.remove();
                }
                List<Map<String, String>> projectList = new ArrayList<>();
                SimpleAdapter adapter = new SimpleAdapter(ProjectSearchMapsActivity.this, projectList, R.layout.row_project_list, null, null);
                lvProjectList.setAdapter(adapter);
                //カメラの位置を取得する。
                CameraPosition position = mMap.getCameraPosition();
                //非同期処理を開始する。
                ProjectMapTaskReceiver receiver = new ProjectMapTaskReceiver();
                //ここで渡した引数はLoginTaskReceiverクラスのdoInBackground(String... params)で受け取れる。
                receiver.execute(GetUrl.projectMapUrl, position.target.latitude + "", position.target.longitude + "");
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                searchFlag = -1;
                break;
            case 1:
                searchFlag = 5;
                break;
        }
        if (location != null) {
//            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in Sydney"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,14));

            //非同期処理を開始する。
            ProjectMapTaskReceiver receiver = new ProjectMapTaskReceiver();
            //ここで渡した引数はLoginTaskReceiverクラスのdoInBackground(String... params)で受け取れる。
            receiver.execute(GetUrl.projectMapUrl, location.getLatitude() + "", location.getLongitude() + "");

            location = null;
            isFirst = false;
        } else if(isFirst) {
            Toast.makeText(ProjectSearchMapsActivity.this, "現在地情報の取得に失敗しました。", Toast.LENGTH_SHORT).show();
            //非同期処理を開始する。
            ProjectMapTaskReceiver receiver = new ProjectMapTaskReceiver();
            //大阪市役所34.693835, 135.501929
            //ここで渡した引数はLoginTaskReceiverクラスのdoInBackground(String... params)で受け取れる。
            receiver.execute(GetUrl.projectMapUrl, "34.693835", "135.501929");

            isFirst = false;
        } else {
            for(Marker marker : markers) {
                marker.remove();
            }
            List<Map<String, String>> projectList = new ArrayList<>();
            SimpleAdapter adapter = new SimpleAdapter(ProjectSearchMapsActivity.this, projectList, R.layout.row_project_list, null, null);
            lvProjectList.setAdapter(adapter);
            //カメラの位置を取得する。
            CameraPosition cameraPosition = mMap.getCameraPosition();
            //非同期処理を開始する。
            ProjectMapTaskReceiver receiver = new ProjectMapTaskReceiver();
            //ここで渡した引数はLoginTaskReceiverクラスのdoInBackground(String... params)で受け取れる。
            receiver.execute(GetUrl.projectMapUrl, cameraPosition.target.latitude + "", cameraPosition.target.longitude + "");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    /**
     * 非同期通信を行うAsyncTaskクラスを継承したメンバクラス.
     */
    private class ProjectMapTaskReceiver extends AsyncTask<String, Void, String> {

        private static final String DEBUG_TAG = "RestAccess";

        /**
         * 通信開始前に実行されるメソッド。
         *
         * ここで、プログレスダイアログを生成しましょう。
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // プログレスダイアログの生成。
            _pDialog = new ProgressDialog(ProjectSearchMapsActivity.this);
            _pDialog.setMessage(getString(R.string.progress_message));  // メッセージを設定。

            // プログレスダイアログの表示。
            _pDialog.show();

        }

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
            String lat = params[1];
            String lng = params[2];

            HttpURLConnection con = null;
            InputStream is = null;
            String result = "";
            String postData = "lat=" + lat + "&lng=" + lng + "&flag=" + searchFlag;

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

                result = Tools.is2String(is);
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
            final List<Map<String, String>> projectList = new ArrayList<>();
            try {
                JSONArray rootJson = new JSONArray(result);
                if(rootJson.length() == 0) {
                    Toast.makeText(ProjectSearchMapsActivity.this, "この地域に指定された条件のプロジェクトは在りません。", Toast.LENGTH_SHORT).show();
                    // ロード画面を消す。
                    if (_pDialog != null && _pDialog.isShowing()) {
                        _pDialog.dismiss();
                    }
                    return;
                }
                for(int i = 0; i < rootJson.length(); i++) {
                    Map<String, String> map = new HashMap<>();
                    JSONObject restNow = rootJson.getJSONObject(i);
                    map.put("no",restNow.getString("no"));
                    map.put("member_no", restNow.getString("member_no"));
                    map.put("category_no", restNow.getString("category_no"));
                    map.put("post_date", restNow.getString("post_date"));
                    map.put("place", restNow.getString("place"));
                    map.put("latitude", restNow.getString("latitude"));
                    map.put("longitude", restNow.getString("longitude"));
                    map.put("title", restNow.getString("title"));
                    map.put("content", restNow.getString("content"));
                    map.put("photo", restNow.getString("photo"));
                    map.put("target_money", restNow.getString("target_money"));
                    map.put("cleaning_flag", restNow.getString("cleaning_flag"));
                    projectList.add(map);
                }
            }
            catch (JSONException ex) {
                Log.e(DEBUG_TAG, "JSON解析失敗", ex);
            }

            markers = new ArrayList<>();

            for(Map<String, String> map : projectList) {
                //マーカー表示
                LatLng latLng = new LatLng(Float.parseFloat(map.get("latitude")), Float.parseFloat(map.get("longitude")));
                if(map.get("cleaning_flag").equals("5")) {
                    markers.add(mMap.addMarker(new MarkerOptions().position(latLng).title(map.get("title")).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))));
                } else {
                    markers.add(mMap.addMarker(new MarkerOptions().position(latLng).title(map.get("title"))));
                }

                markers.get(markers.size() - 1).setTag(map);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,14));
            }

            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Intent intent = new Intent(ProjectSearchMapsActivity.this, ProjectDetailActivity.class);
                    Map<String, String> map = (Map<String, String>) marker.getTag();
                    intent.putExtra("projectId", map.get("no"));
                    startActivity(intent);
                }
            });

            mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                private boolean isNotFirst = false;

                @Override
                public void onCameraIdle() {
                    if(isNotFirst) {
                        Button btSurroundingStore = findViewById(R.id.btSurroundingProject);
                        btSurroundingStore.setVisibility(View.VISIBLE);
                    }
                    isNotFirst = true;
                }
            });

            String[] from = {"title", "content"};
            int[] to = {R.id.rowTvProjectTitle, R.id.rowTvProjectContent};
            final SimpleAdapter adapter = new SimpleAdapter(ProjectSearchMapsActivity.this, projectList, R.layout.row_project_list, from, to);
            adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Object data, String textRepresentation) {
                    int id = view.getId();
                    String strData = (String) data;
                    switch (id) {
                        case R.id.rowTvProjectTitle:
                            TextView rowTvProjectTitle = (TextView) view;
                            rowTvProjectTitle.setText(strData);
                            return true;
                        case R.id.rowTvProjectContent:
                            TextView rowTvProjectContent = (TextView) view;
                            rowTvProjectContent.setText(strData);
                            return true;
                    }
                    return false;
                }
            });
            lvProjectList.setAdapter(adapter);
            lvProjectList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(ProjectSearchMapsActivity.this, ProjectDetailActivity.class);
                    Map<String, String> map = (Map<String, String>) adapter.getItem(position);
                    intent.putExtra("projectId", map.get("no"));
                    startActivity(intent);
                }
            });

            mMap.setIndoorEnabled(false);
            mMap.getUiSettings().setTiltGesturesEnabled(false);

            // ロード画面を消す。
            if (_pDialog != null && _pDialog.isShowing()) {
                _pDialog.dismiss();
            }
        }
    }
}
