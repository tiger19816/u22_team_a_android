package a.team.works.u22.hal.u22teama;

import android.Manifest;
import android.app.Activity;
import android.app.assist.AssistContent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class NewProjectPostsScreenActivity extends AppCompatActivity implements LocationListener {

    LocationManager locationManager;

    //緯度
    String latitude;
    //軽度
    String longtude;

    String longitudeRef;
    String latitudeRef;

    //onActivityResultメソッドで受け取るコード
    private static final int CAMERA_REQUEST_CODE = 1;

    //インテント
    Intent _intent;

    //カメラ画像を保存するディレクトリ
    static File mediaStorage;
    static File mediaFile;
    private String StrImgName;
    final static String StrFileName  ="U22";


    //画像を表示する部分
    ImageView ivDisplay;

    //保存ボタン
    Button btSave;

    Bitmap _bitmap;

    //Exifインスタンス取得
    ExifInterface exif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project_posts_screen);

        ivDisplay = findViewById(R.id.iv_CheckPhots);

        //パーミッションチェックコードの追記
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {  // （1）
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};  // （2）
            ActivityCompat.requestPermissions(this, permissions, 1000);  // （3）
            return;  // （4）
        }

        //ストレージへのアクセス許可の確認
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
            }, 1000);
            return;
        }
        else {
            //ストレージのアクセスの許可を求める
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
            }
            //拒否された場合
            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,}, 1000);
            }

            //位置情報のアクセス許可へ
            RequestLocationPermission();
        }

    }

    //位置情報へのアクセス許可の確認
    public void RequestLocationPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                String[] permissions ={Manifest.permission.ACCESS_FINE_LOCATION};
                ActivityCompat.requestPermissions(this, permissions, 1000);
                Log.d("message","一段階目おｋ");
            return;         //add
        }
        else {
            locationStart();

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 50, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 50, this);
        }
    }

    public boolean locationStart() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Log.e("LocationManager",locationManager.toString());
        if (locationManager != null && (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))) {
            //if( 位置情報モード != null && (GPS利用可能 || ネットワークが利用可能))
            Log.d("debug", "location manager Enabled");
        }else {
            // GPSを設定するように促す
            Log.e("GPSNotOn","GPS画面表示");
            Toast.makeText(this, "GPSの機能をオンにしてください", Toast.LENGTH_SHORT).show();
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
            Log.d("debug", "not g   psEnable, startActivity");
            return false;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);

            Log.d("debug", "checkSelfPermission false");
            return false;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 50, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 50, this);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[]permissions, @NonNull int[] grantResults) {
        if (requestCode == 1000) {
            // 使用が許可された
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("debug","checkSelfPermission true");

                locationStart();
            }
            else {
                // 拒否された時の対応
                Toast.makeText(this, "これ以上なにもできません", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
            case LocationProvider.AVAILABLE:
                Log.d("debug", "LocationProvider.AVAILABLE");
                break;
            case LocationProvider.OUT_OF_SERVICE:
                Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                break;
        }
    }

    /**
     * 緯度経度が更新されたときに動く
     */
    @Override
    public void onLocationChanged(Location location) {
        Log.d("地点位置情報", "突入");
        encodeGpsToExifFormat(location);
    }

    /**
     * Exif形式にGPS Locationを変換して返す。
     * longitudeRef => W or E
     * latitudeRef => N or S
     * latitude and longitude => num1/denom1,num2/denom2,num3/denom3
     * ex) 12/1,34/1,56789/1000
     *
     * @param location
     * @return ExifLocation
     */
    public void encodeGpsToExifFormat(Location location) {
        //ExifLocation exifLocation = new ExifLocation();
        // 経度の変換(正->東, 負->西)
        // convertの出力サンプル => 73:9:57.03876
        String[] lonDMS = Location.convert(location.getLongitude(), Location.FORMAT_SECONDS).split(":");
        StringBuilder lon = new StringBuilder();

        // 経度の正負でREFの値を設定（経度からは符号を取り除く）
        if (lonDMS[0].contains("-")) {
            longitudeRef = "W";
        } else {
            longitudeRef = "E";
        }
        lon.append(lonDMS[0].replace("-", ""));
        lon.append("/1,");
        lon.append(lonDMS[1]);
        lon.append("/1,");

        // 秒は小数の桁を数えて精度を求める
        int index = lonDMS[2].indexOf('.');
        if (index == -1) {
            lon.append(lonDMS[2]);
            lon.append("/1");
        } else {
            int digit = lonDMS[2].substring(index + 1).length();
            int second = (int) (Double.parseDouble(lonDMS[2]) * Math.pow(10, digit));
            lon.append(String.valueOf(second));
            lon.append("/1");
            for (int i = 0; i < digit; i++) {
                lon.append("0");
            }
        }
        longtude = lon.toString();

        // 緯度の変換(正->北, 負->南)
        // convertの出力サンプル => 73:9:57.03876
        String[] latDMS = Location.convert(location.getLatitude(), Location.FORMAT_SECONDS).split(":");
        StringBuilder lat = new StringBuilder();

        // 経度の正負でREFの値を設定（経度からは符号を取り除く）
        if (latDMS[0].contains("-")) {
            latitudeRef = "S";
        } else {
            latitudeRef = "N";
        }
        lat.append(latDMS[0].replace("-", ""));
        lat.append("/1,");
        lat.append(latDMS[1]);
        lat.append("/1,");

        // 秒は小数の桁を数えて精度を求める
        index = latDMS[2].indexOf('.');
        if (index == -1) {
            lat.append(latDMS[2]);
            lat.append("/1");
        } else {
            int digit = latDMS[2].substring(index + 1).length();
            int second = (int) (Double.parseDouble(latDMS[2]) * Math.pow(10, digit));
            lat.append(String.valueOf(second));
            lat.append("/1");
            for (int i = 0; i < digit; i++) {
                lat.append("0");
            }
        }
        latitude = lat.toString();
    }

    //Exif情報を書き込むメソッド
    public void exifWrite(File file) {
        try {
            //Exifinterfaceのインスタンス取得
            exif = new ExifInterface(file.toString());

            //位置情報をExifにセット
            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, latitude); //緯度
            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, latitudeRef); //緯度の符号（北か南か）
            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, longtude); //軽度
            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, longitudeRef);

            //保存
            exif.saveAttributes();
        }
        catch (Exception e) {
            Log.e("例外", e.toString());
        }
    }

    //ファイル名生成用メソッド
    protected String getFileName(){
        Calendar c = Calendar.getInstance();
        String s = c.get(Calendar.YEAR)
                + "_" + (c.get(Calendar.MONTH)+1)
                + "_" + c.get(Calendar.DAY_OF_MONTH)
                + "_" + c.get(Calendar.HOUR_OF_DAY)
                + "_" + c.get(Calendar.MINUTE)
                + "_" + c.get(Calendar.SECOND)
                + "_" + c.get(Calendar.MILLISECOND)
                + ".jpeg";
        Log.d("名前", s);
        return s;
    }

    //カメラ起動ボタンが押されたとき
    public void onClickCameraStart(View view) {

        //インテントの生成
        _intent = new Intent();

        //インテントのアクションを指定する
        _intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);



        //カメラへのアクセス許可の確認
        if(locationStart()) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.CAMERA,
                }, 1000);
            } else {
                //カメラのアクセスの許可を求める
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1000);
                }
                //拒否された場合
                else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,}, 1000);
                }
                //標準搭載されているカメラアプリのアクティビティを起動する
                startActivityForResult(_intent, CAMERA_REQUEST_CODE);
            }
        }
    }

    /**
     * 撮影された画像を保存する
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == this.CAMERA_REQUEST_CODE) {
            switch(resultCode) {
                case RESULT_OK:    //撮影完了
                    Toast.makeText(NewProjectPostsScreenActivity.this, "保存完了", Toast.LENGTH_SHORT).show();

                    //撮影した画像を取得
                    _bitmap = (Bitmap) data.getExtras().get("data");
                    //撮影した画像の名前を日付から確定
                    StrImgName = getFileName();

                    try {
                        //取得した画像をファイルに保存（※保存完了が反映するまで少し時間がかかります）
                        mediaStorage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

                        //フォルダーがあるかの確認、なければ作成する。
                        mediaFile = new File(mediaStorage.getAbsolutePath() + "/" + StrFileName);

                        //フォルダーの確認
                        if(!mediaFile.exists()){
                            Log.e("create", "in");
                            boolean result = mediaFile.mkdirs();
                            if(! result){
                                Log.e("error", "brack");
                                break;
                            }
                        }

                        mediaFile = new File(mediaFile.getPath() + "/" + StrImgName);
                       System.out.println(mediaFile);
                        FileOutputStream outStream = new FileOutputStream(mediaFile);
                        _bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                        exifWrite(mediaFile);
                        outStream.close();

                        float[] latLong = new float[2];
                        exif.getLatLong(latLong);
                        String info = String.format("%f,%f", latLong[0],latLong[1]);
                        Log.e("mediaFile",mediaFile.toString());
                        Log.e("info", info);

                    }
                    catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    ivDisplay.setImageBitmap(_bitmap);
                    break;
                case RESULT_CANCELED:    //撮影が途中で中止
                    Toast.makeText(NewProjectPostsScreenActivity.this, "撮影が中止されました。", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }


    /**
     * オプションメニュー表示の秘密の言葉
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu_activity_new_project_posts_screen, menu);
        return true;
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
             * 保存ボタンが押された時の処理
             */
            case R.id.menuNewProjectPostsScreenActivitySave:

                //収納用クラスに値収納
                NewProjectPostsScreenActivityInfomation NPPSAI = new NewProjectPostsScreenActivityInfomation();

                EditText edTitle = findViewById(R.id.ed_Title);
                EditText etPlace = findViewById(R.id.et_Place);
                Spinner spinnerCate = findViewById(R.id.spinner_Category);
                EditText etContent = findViewById(R.id.et_Content);
                EditText etInvestmentAmount = findViewById(R.id.et_InvestmentAmount);
               //すべての項目が入力されているかの確認
                if("".equals(edTitle.getText().toString())) {
                    Toast.makeText(NewProjectPostsScreenActivity.this,"タイトルが入力されていません", Toast.LENGTH_SHORT).show();
                }else if(_bitmap == null) {
                    Toast.makeText(NewProjectPostsScreenActivity.this,"写真を撮影してください", Toast.LENGTH_SHORT).show();
                }else if("".equals(etPlace.getText().toString())){
                    Toast.makeText(NewProjectPostsScreenActivity.this,"場所が入力されていません", Toast.LENGTH_SHORT).show();
                }else if("選択してください".equals(spinnerCate.getSelectedItem())) {
                    Toast.makeText(NewProjectPostsScreenActivity.this,"カテゴリーが選択されていません", Toast.LENGTH_SHORT).show();
                }else if("".equals(etContent.getText().toString())) {
                    Toast.makeText(NewProjectPostsScreenActivity.this,"内容が入力されていません", Toast.LENGTH_SHORT).show();
                }else if("".equals(etInvestmentAmount.getText().toString())) {
                    Toast.makeText(NewProjectPostsScreenActivity.this,"募金額が入力されていません", Toast.LENGTH_SHORT).show();
                }else{
                    //NewProhectPostsScreenActivityInfomationに値を格納
                    NPPSAI.setEdTitel(edTitle.getText().toString());
                    NPPSAI.setImgUrl(mediaFile.toString());
                    NPPSAI.setImgName(StrImgName);
                    NPPSAI.setEtPlace(etPlace.getText().toString());
                    NPPSAI.setSpinnerCate((String) spinnerCate.getSelectedItem());
                    NPPSAI.setEdSConte(etContent.getText().toString());
                    NPPSAI.setEdInvestmentAmount(etInvestmentAmount.getText().toString());
                    //値を送る
                    MovePage(NPPSAI);
                }
                break;
        }
        return true;
    }



    /**
     * ページ移動
     *
     * @param info
     */
    public void MovePage(NewProjectPostsScreenActivityInfomation info) {
        Intent intent = new Intent(NewProjectPostsScreenActivity.this, NewProjectPostsConfirmationScreenActivity.class);
        intent.putExtra("value", info);
        startActivity(intent);
    }

}


