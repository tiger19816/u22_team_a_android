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
import java.util.Map.Entry;

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

public class NewProjectPostsConfirmationScreenActivity extends AppCompatActivity implements HttpPostListener{

    //画像保存用
    private  Bitmap image;
    private  NewProjectPostsScreenActivityInfomation NPPSAI;
    final static private String TAG = "HttpPost";

    // 検証用
    final static private String URL = "http://192.168.100.100:8080/u22_team_a_web/NewProjectPostsConfirmationScreenActivityServlet";
    //サーバー用
    //final static private String URL = GetUrl.LoginUrl;
    //final static private String URL = GetUrl.MyPostsUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project_posts_confirmation_screen);

        //戻るボタン
        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeButtonEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);

        Intent intent=getIntent();
        NewProjectPostsScreenActivityInfomation NPPSAI = (NewProjectPostsScreenActivityInfomation)intent.getSerializableExtra("value");

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
        //Spinner（array）の値をすべて取得

        String[] category = getResources().getStringArray(R.array.spinner_activity_new_project_posts_category);


        //値の貼り付け
        tvTitle.setText(NPPSAI.getEdTitle());
        ivImage.setImageBitmap(image);
        tvPlace.setText(NPPSAI.getEdPlace());
        tvCategory.setText(category[Integer.parseInt(NPPSAI.getSpinnerCate())   ]);

        tvContent.setText(NPPSAI.getEdSConte());
        tvCheckInvestmentAmount.setText(NPPSAI.getEdInvestmentAmount());


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

                NewProjectsConfirmationScreenActivityTaskReceiver task = new NewProjectsConfirmationScreenActivityTaskReceiver(URL);

                //POSTデータ作成のためのデータ格納
                task.addText("title",NPPSAI.getEdTitle());
                task.addText("place",NPPSAI.getEdPlace());
                task.addText("category", NPPSAI.getSpinnerCate());
                task.addText("content", NPPSAI.getEdSConte());
                task.addText("InvestmentAmount", NPPSAI.getEdInvestmentAmount());
                task.addText("userId", NPPSAI.getUserId());
                task.addText("latitude",NPPSAI.getLatitude());
                task.addText("longitude",NPPSAI.getLongitude());
                //
                //画像をbyte型に変換 + 格納
                task.addImage("filename", img2byte(image));

                // リスナーをセットする
                task.setListener( NewProjectPostsConfirmationScreenActivity.this);

                Log.e("item_Send", "送信処理開始");
                //ここで渡した引きすはdoInBackgroundで受け取れる。

                task.execute();

                Log.e("item_Send", "送信処理中");

                MovePage();
                break;
        }
        return true;
    }

    public void MovePage() {

        Intent intent = new Intent(NewProjectPostsConfirmationScreenActivity.this, ProjectDetailActivyty.class);
        startActivity(intent);

    }


    @Override
    public void postCompletion(byte[] response) {
        Log.i(TAG, "post completion!");
        Log.i(TAG, new String(response));
    }

    @Override
    public void postFialure() {
        Log.i(TAG, "post failure!");
    }

    /**
     * 非同期通信を行うAsyncTaskクラスを継承したメンバクラス
     */
    private class NewProjectsConfirmationScreenActivityTaskReceiver extends AsyncTask<Void, Void, byte[]>{
        final static private String BOUNDARY = "MyBoundaryString";
        private String mURL;
        private HttpPostListener mListener;
        private HashMap<String, String> mTexts;
        private HashMap<String, byte[]> mImages;

         public NewProjectsConfirmationScreenActivityTaskReceiver (){

        }

        public NewProjectsConfirmationScreenActivityTaskReceiver (String url){
            super();
            mURL = url;
            mListener = null;
            mTexts = new HashMap<String, String>();
            mImages = new HashMap<String, byte[]>();
        }

        /**
              * 送信するテキストを追加する。
              * @param text
              */
        public void addText(String key, String text){
            mTexts.put(key, text);
        }

        /**
              * 送信する画像を追加する。
              * @param image
              */
        public void addImage(String key, byte[] data) {
            mImages.put(key, data);
        }

        /**
         * リスナーをセットする。
         * @param listener
         */
        public void setListener(HttpPostListener listener)
        {
            mListener = listener;
        }

        @Override
        public byte[] doInBackground(Void... params){
            byte[] data = makePostData();
            byte[] result = send(data);

            return result;
        }

        /**
         * 送信を行う。
         * @return レスポンスデータ
         */
        private byte[] send(byte[] data)
        {
            if (data == null)
                return null;

            byte[] result = null;
            HttpURLConnection connection = null;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            InputStream is = null;

            try {
                URL url = new URL(mURL);
                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                // 接続
                connection.connect();

                // 送信
                OutputStream os = connection.getOutputStream();
                os.write(data);
                os.close();

                // レスポンスを取得する
                byte[] buf = new byte[10240];
                int size;
                is = connection.getInputStream();
                while ((size = is.read(buf)) != -1)
                {
                    baos.write(buf, 0, size);
                }
                result = baos.toByteArray();
            } catch(Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (Exception e) {}

                try {
                    connection.disconnect();
                } catch (Exception e) {}

                try {
                    baos.close();
                } catch (Exception e) {}
            }

            return result;
        }

        /**
         * POSTするデータを作成する。
         * @return
         */
        private byte[] makePostData()
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            try {
                // テキスト部分の設定
                for (Entry<String, String> entry : mTexts.entrySet())
                {
                    String key = entry.getKey();
                    String text = entry.getValue();

                    baos.write(("--" + BOUNDARY + "\r\n").getBytes());
                    baos.write(("Content-Disposition: form-data;").getBytes());
                    baos.write(("name=\"" + key + "\"\r\n\r\n").getBytes());
                    baos.write((text + "\r\n").getBytes());
                }

                // 画像の設定
                int count = 1;
                for (Entry<String, byte[]> entry: mImages.entrySet())
                {
                    String key = entry.getKey();
                    byte[] data = entry.getValue();
                    //複数送る場合
                    //String name = "filename" + count++;
                    //一つの場合
                    String name = "filename";

                    baos.write(("--" + BOUNDARY + "\r\n").getBytes());
                    baos.write(("Content-Disposition: form-data;").getBytes());
                    baos.write(("name=\"" + name + "\";").getBytes());
                    baos.write(("filename=\"" + key + "\"\r\n").getBytes());
                    baos.write(("Content-Type: image/jpeg\r\n\r\n").getBytes());
                    baos.write(data);
                    baos.write(("\r\n").getBytes());
                }

                // 最後にバウンダリを付ける
                baos.write(("--" + BOUNDARY + "--\r\n").getBytes());

                return baos.toByteArray();
            } catch(Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                try {
                    baos.close();
                } catch (Exception e) {}
            }
        }

        @Override
        protected void onPostExecute(byte[] result) {
            if (mListener != null) {
                if (result != null) {
                    mListener.postCompletion(result);
                } else {
                    mListener.postFialure();
                }
            }
        }
    }
}

