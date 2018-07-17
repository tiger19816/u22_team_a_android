package a.team.works.u22.hal.u22teama;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class NewRegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_registration);


        final String TAG = "DialogTest";
        ((Button)findViewById(R.id.Registration)).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                final EditText text_Name =  findViewById(R.id.input_name);
                final EditText text_Birthday =  findViewById(R.id.input_birthday);
                final EditText text_Address =  findViewById(R.id.input_address);
                final EditText text_Phone_Number =  findViewById(R.id.input_phone_number);
                final EditText text_Sex =  findViewById(R.id.input_sex);
                final EditText text_Credit_Card_Number =  findViewById(R.id.input_credit_card_number);
                final EditText text_Password =  findViewById(R.id.input_password);

                CharSequence input_name = text_Name.getText();
                CharSequence input_birthday = text_Birthday.getText();
                CharSequence input_address = text_Address.getText();
                CharSequence input_phone_number = text_Phone_Number.getText();
                CharSequence input_sex = text_Sex.getText();
                CharSequence input_credit_card_number = text_Credit_Card_Number.getText();
                CharSequence input_password = text_Password.getText();


                AlertDialog.Builder builder = new AlertDialog.Builder(NewRegistrationActivity.this, R.style.AlertDialogStyle);
                new AlertDialog.Builder(NewRegistrationActivity.this);

                builder.setTitle("確認");
                builder.setMessage("氏名："+ input_name + "\n"
                        +"生年月日："+ input_birthday +  "\n"
                        + "住所：" + input_address +  "\n"
                        + "電話番号："+ input_phone_number + "\n"
                        + "性別：" + input_sex + "\n"
                        + "クレジット番号：" + input_credit_card_number + "\n"
                        + "パスワード："+ input_password +"");

                //      TextView textView = findViewById(android.R.id.message);
                //      textView.setTextSize(48.0f);

                builder.setPositiveButton("登録する", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                        .setNegativeButton("訂正する", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });

    }



}
