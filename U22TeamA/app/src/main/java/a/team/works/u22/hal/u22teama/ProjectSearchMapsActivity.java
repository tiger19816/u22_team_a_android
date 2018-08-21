package a.team.works.u22.hal.u22teama;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ProjectSearchMapsActivity extends AppCompatActivity implements OnMapReadyCallback {

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
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
