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

        Intent intent=getIntent();
        NewProjectPostsScreenActivityInfomation NPPSAI = (NewProjectPostsScreenActivityInfomation)intent.getSerializableExtra("value");

        TextView tvTitle = findViewById(R.id.tv_Title);
        TextView tvPlace = findViewById(R.id.tv_Place);
        TextView tvCategory = findViewById(R.id.tv_Category);
        TextView tvContent = findViewById(R.id.tv_Content);
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


                MovePage();
                break;
        }
        return true;
    }

    public void MovePage() {
        Intent intent = new Intent(NewProjectPostsConfirmationScreenActivity.this, .class);
        startActivity(intent);
    }
}

