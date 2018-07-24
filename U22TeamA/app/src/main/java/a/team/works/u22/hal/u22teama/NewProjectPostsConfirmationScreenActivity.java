package a.team.works.u22.hal.u22teama;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class NewProjectPostsConfirmationScreenActivity extends AppCompatActivity {

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

        TextView tvTitle = findViewById(R.id.tv_CheckTitle);
        TextView tvPlace = findViewById(R.id.tv_CheckPlace);
        TextView tvCategory = findViewById(R.id.tv_CheckCategory);
        TextView tvContent = findViewById(R.id.tv_CheckContent);
        TextView tvCheckInvestmentAmount = findViewById(R.id.tv_CheckInvestmentAmount);

        tvTitle.setText(NPPSAI.getEdTitle());
        tvPlace.setText(NPPSAI.getEdPlace());
        tvCategory.setText(NPPSAI.getSpinnerCate());
        tvContent.setText(NPPSAI.getEdSConte());
        tvCheckInvestmentAmount.setText(NPPSAI.getEdInvestmentAmount());



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
             * 戻るボタンが押された時
             */
            case android.R.id.home:
                finish();
                return true;


            /**
             * 保存ボタンが押された時の処理
             */
            case R.id.item_send:



                MovePage();
                break;
        }
        return true;
    }

    public void MovePage() {
        Intent intent = new Intent(NewProjectPostsConfirmationScreenActivity.this, TabPage2Fragment.class);
        startActivity(intent);
    }
}

