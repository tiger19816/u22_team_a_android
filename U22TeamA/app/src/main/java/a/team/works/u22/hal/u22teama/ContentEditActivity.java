package a.team.works.u22.hal.u22teama;

import android.content.Intent;
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

public class ContentEditActivity extends AppCompatActivity {

    /**
     * 2画面目を開けるときのjava
     * @param savedInstanceState
     */

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
            intent.putExtra("content",content);
            startActivity(intent);
        }

    }
}
