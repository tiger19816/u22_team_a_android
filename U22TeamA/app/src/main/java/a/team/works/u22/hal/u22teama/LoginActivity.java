package a.team.works.u22.hal.u22teama;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class LoginActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

    }
    private static final String LOGIN_URL = GetUrl.LoginUrl;
}
