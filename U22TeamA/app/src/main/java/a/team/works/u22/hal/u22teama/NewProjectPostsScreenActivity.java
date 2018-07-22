package a.team.works.u22.hal.u22teama;

import android.app.Activity;
import android.app.assist.AssistContent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;

public class NewProjectPostsScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project_posts_screen);
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
                NPPSAI.setEdTitel(edTitle);

                EditText etPlace = findViewById(R.id.et_Place);
                NPPSAI.setEtPlace(etPlace);

                Spinner spinnerCate = findViewById(R.id.spinner_Category);
                NPPSAI.setSpinnerCate((String)spinnerCate.getSelectedItem());

                EditText etSConte = findViewById(R.id.et_Content);
                NPPSAI.setEdSConte(etSConte);

                EditText etInvestmentAmount = findViewById(R.id.et_InvestmentAmount);
                NPPSAI.setEdInvestmentAmount(etInvestmentAmount);
                //値を送る


                MovePage(NPPSAI);
                break;
        }
        return true;
    }
    final int REQUEST_CAPTURE_IMAGE = 100;

    public void onClickPhoto(View view) {
        final int REQUEST_CAPTURE_IMAGE = 100;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQUEST_CAPTURE_IMAGE);

    }

    /**
     * カメラ機能
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(REQUEST_CAPTURE_IMAGE == requestCode && resultCode == Activity.RESULT_OK){

            Bitmap capturedImage =
                    (Bitmap) data.getExtras().get("data");
            ImageView imCheckPhots = findViewById(R.id.im_CheckPhots);
            imCheckPhots.setImageBitmap(capturedImage);
        }
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
