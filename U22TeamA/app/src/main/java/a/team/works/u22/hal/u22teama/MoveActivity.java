package a.team.works.u22.hal.u22teama;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Switch;

public class MoveActivity extends AppCompatActivity {
    private int flag =0;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int page = intent.getIntExtra("page",0) ;
        switch(page){
            case 1:
                intent = new Intent(MoveActivity.this, NewProjectPostsScreenActivity.class);
                startActivity(intent);
                break;
            case 2:
                intent = new Intent(MoveActivity.this, TabLayoutCleanActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
            case 3:
                String projectNo = intent.getStringExtra("projectNo");
                String donationMoney =intent.getStringExtra("donationMoney");
                String cleaningFlag =intent.getStringExtra("cleaningFlag");
                String prSecondMax =intent.getStringExtra("TargetMoney");
                String title = intent.getStringExtra("title");

                intent = new Intent(MoveActivity.this, DonationActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("cleaningFlag",cleaningFlag);
                intent.putExtra("donationMoney", donationMoney );
                intent.putExtra("projectNo",projectNo);
                intent.putExtra("TargetMoney",prSecondMax);
                startActivity(intent);
                break;
            case 4:
                intent = new Intent(MoveActivity.this, TabLayoutCleanActivity.class);
                intent.putExtra("mood", 1);
                startActivity(intent);
                break;
            default: flag++;
                break;
        }


    }

    /**
     * 確認処理が終わった場合自動で終了 +
     * 戻るボタンが押された場合この１つ下のところまで戻る処理
     */
    @Override
    public void onResume(){
        super.onResume();
        if(flag == 0 ) {
            flag++;
        }else{
            finish();
        }
    }
}
