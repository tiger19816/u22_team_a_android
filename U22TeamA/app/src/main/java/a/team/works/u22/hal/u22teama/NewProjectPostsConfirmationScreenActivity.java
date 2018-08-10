package a.team.works.u22.hal.u22teama;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class NewProjectPostsConfirmationScreenActivity extends AppCompatActivity {
    //画像保存用
    private  Bitmap image;
    private  NewProjectPostsScreenActivityInfomation NPPSAI;
    final static private String URL = "http://192.168.100.101:8080/u22_team_a_web/NewProjectPostsConfirmationScreenActivityServlet";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project_posts_confirmation_screen);

        //戻るボタン
        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeButtonEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);

        Intent intent=getIntent();
        NPPSAI = (NewProjectPostsScreenActivityInfomation)intent.getSerializableExtra("value");

        //Viewの取得
        TextView tvTitle = findViewById(R.id.tv_CheckTitle);
        ImageView ivImage = findViewById(R.id.iv_CheckPhots);
        TextView tvPlace = findViewById(R.id.tv_CheckPlace);
        TextView tvCategory = findViewById(R.id.tv_CheckCategory);
        TextView tvContent = findViewById(R.id.tv_CheckContent);
        TextView tvCheckInvestmentAmount = findViewById(R.id.tv_CheckInvestmentAmount);

        //デバイス内から画像を見つける
        ParcelFileDescriptor parcelFileDescriptor = null;
        try{
            Uri uri;
            uri =  Uri.parse("file://"+ NPPSAI.getImgUrl() +"/");

            System.out.println(uri);
            parcelFileDescriptor =
                    getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            image = BitmapFactory.decodeFileDescriptor(fileDescriptor);

        }catch(IOException e){
            e.printStackTrace();
        }finally {
            try {
                parcelFileDescriptor.close();
            }catch(IOException e) {
                e.printStackTrace();
            }
        }
        //値の貼り付け
        tvTitle.setText(NPPSAI.getEdTitle());
        ivImage.setImageBitmap(image);
        tvPlace.setText(NPPSAI.getEdPlace());
        tvCategory.setText(NPPSAI.getSpinnerCate());
        tvContent.setText(NPPSAI.getEdSConte());
        tvCheckInvestmentAmount.setText(NPPSAI.getEdInvestmentAmount());

        //画像をbyte型に変換;
        NPPSAI.setByteImg(img2byte(image));
        String name ="";
        int i = 0;
        for(byte b : NPPSAI.getByteImg()){
            name += b;
            i++;
        }

        Log.e("byte", name);
        Log.e("byteA", NPPSAI.getByteImg().toString());


    }


    /**
     * Bitmapを Byte[]へ変換
     * @param img
     * @return
     */
    public byte[] img2byte(Bitmap img){
        //BitmapをByte[]へ変換するためのStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imgByte;
        try{
            //BitmapをJPEGへ変換
            img.compress(Bitmap.CompressFormat.JPEG,100,baos);
            baos.flush();
            //JPEGをbyte[]へ変換
            imgByte= baos.toByteArray();
            //送信データに画像のbyte[]を追加する

        }catch (Exception e) {
            imgByte =new byte[10240];
        }finally {
            try {
                baos.close();
            } catch (IOException e) {}
        }
        return imgByte;
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
        inflater.inflate(R.menu.option_menu_activity_new_project_posts_confirmation_screan_activity, menu);
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
             * 戻るボタンが押された時
             */
            case android.R.id.home:
                finish();
                return true;


            /**
             * 保存ボタンが押された時の処理
             */
            case R.id.item_send:
                Log.e("item_Send", "送信処理開始");
                //非同期処理を開始する。
                NewProjectsConfirmationScreenActivityTaskReceiver receiver = new NewProjectsConfirmationScreenActivityTaskReceiver();
                //ここで渡した引きすはdoInBackgroundで受け取れる。
                receiver.execute(NPPSAI.getEdTitle(), NPPSAI.getImgName(), NPPSAI.getEdPlace(), NPPSAI.getSpinnerCate(), NPPSAI.getEdSConte(), NPPSAI.getEdInvestmentAmount());
                Log.e("item_Send", "送信処理中");
                //MovePage();
                break;
        }
        return true;
    }

    public void MovePage() {
        Intent intent = new Intent(NewProjectPostsConfirmationScreenActivity.this, TabPage2Fragment.class);
        startActivity(intent);
    }

    /**
     * 非同期通信を行うAsyncTaskクラスを継承したメンバクラス
     */
    private class NewProjectsConfirmationScreenActivityTaskReceiver extends AsyncTask<String, String, String>{


        final static private String TAG = "HttpPost";
        //データの送信先URLアドレス
        private static final int RESULT_PICK_IMAGEFILE = 1000;
        private static final String DEBUG_TAG = "ResAccess";

        @Override
        public String doInBackground(String... params){
            //送信する値をセット
            String postData = "title="+ params[0] +               //タイトル
                    "&imgName=" + params[1] +                     //画像名
                    "&place=" + params[2] +                       //場所
                    "&category=" + params[3] +                //カテゴリー
                    "&content=" + params[4] +                    //内容
                    "&InvestmentAmount=" + params[5]  //寄付金
 //                   "&ImgFile=" + params[6]                       //画像（byte型）
                    ;

            HttpURLConnection con = null;
            InputStream is = null;
            String result = "";
            try{
            URL url = new URL(URL);
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

                Log.e("send","遅れたー");
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

            return "";
        }


        public void onPostExecute(String result) {}








    }
}

