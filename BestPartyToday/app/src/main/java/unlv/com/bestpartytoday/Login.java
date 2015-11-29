package unlv.com.bestpartytoday;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

public class Login extends AppCompatActivity {
    Button btnLogin;
    Button btnSignUp;
    EditText usernameTxt;
    EditText passwordTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Firebase.setAndroidContext(this);
        final Firebase ref = new Firebase("https://fiery-heat-4759.firebaseio.com/");

        btnSignUp = (Button) findViewById(R.id.signup_button);
        btnLogin = (Button) findViewById(R.id.login_button);
        usernameTxt = (EditText) findViewById(R.id.usernameTxt);
        passwordTxt = (EditText) findViewById(R.id.passwordTxt);

        btnLogin.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                ref.authWithPassword(String.valueOf(usernameTxt.getText()), String.valueOf(passwordTxt.getText()),
                        new Firebase.AuthResultHandler() {
                            @Override
                            public void onAuthenticated(AuthData authData) {
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onAuthenticationError(FirebaseError firebaseError) {
                                showError();
                            }
                        });
            }
        });


        btnSignUp.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
            }
        });
    }
    private void showError () {
        usernameTxt.setError("Password and username didn't match");
        passwordTxt.setError("Password and username didn't match");
    }

}
