package a.team.works.u22.hal.u22teama;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ContentEditActivity extends AppCompatActivity {

    /**
     * 2画面目を開けるときのjava
     * @param savedInstanceState
     */

    int loginId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_edit);

        Button button = findViewById(R.id.btConfirming);
        ButtonClickListener listener = new ButtonClickListener();
        button.setOnClickListener(listener);

        //ツールバー(レイアウトを変更可)。
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("お問い合わせ");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        SharedPreferences pref = getSharedPreferences("prefUserId",0);
        loginId = pref.getInt("id", 0);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *
     * ボタンが押されたときの処理が記述されたときのメンバクラス
     */
    private class ButtonClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view){
            Intent intent = new Intent(ContentEditActivity.this,ContentResultActivity.class);
            EditText edContent = findViewById(R.id.edContent);
            String content = edContent.getText().toString();
            if(content.equals("")||content.equals(null)){
                Toast.makeText(ContentEditActivity.this, "お問い合わせ内容を入力してください。", Toast.LENGTH_SHORT).show();
            }else{
                intent.putExtra("loginId", loginId);
                intent.putExtra("content",content);
                startActivity(intent);
            }
        }

    }
}
