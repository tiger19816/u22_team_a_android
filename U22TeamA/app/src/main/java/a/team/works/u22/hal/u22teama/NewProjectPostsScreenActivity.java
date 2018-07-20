package a.team.works.u22.hal.u22teama;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

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
     * アクションバーの戻るボタン機能
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
                EditText edTitel = findViewById(R.id.ed_Title);
                EditText etPlace = findViewById(R.id.et_Place);
                Spinner spinnerCate = findViewById(R.id.spinner_Category);
                EditText etSConte = findViewById(R.id.et_Content);
                EditText etInvestmentAmount  = findViewById(R.id.et_InvestmentAmount);


                break;
        }
        return true;
    }
}
