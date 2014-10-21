package com.example.victor.swipeviews;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class SignUpActivity extends Activity {
    protected EditText mUserName;
    protected EditText mPassword;
    protected EditText mEmail;
    protected Button mSignUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS); //vazmoznost da pokazva spinner dokato misli

        setContentView(R.layout.activity_sign_up);

        //Vrazvame opciite za spinner
        Spinner spinner = (Spinner) findViewById(R.id.signUpText);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.SexOptions, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //vrazvame ostanalite TextFields i butona
        mUserName = (EditText) findViewById(R.id.sign_up_username);
        mPassword = (EditText) findViewById(R.id.sign_up_password);
        mEmail = (EditText) findViewById(R.id.sign_up_email);
        mSignUpButton = (Button) findViewById(R.id.sign_up_button);

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //razkarvam ako ima intervali v username, passpword i email
                String userName = mUserName.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String email = mEmail.getText().toString().trim();

                if(userName.isEmpty() || password.isEmpty() || email.isEmpty() ) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    builder.setTitle(R.string.sign_up_error_title)
                        .setMessage(R.string.sign_up_error_message)
                        .setPositiveButton(R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

                else {
                //ako username, password, email ne sa prazni gi zapisvame v parse

                    ParseUser newUser = new ParseUser();
                    newUser.setEmail(email);
                    newUser.setPassword(password);
                    newUser.setUsername(userName);

                    setProgressBarIndeterminate(true); //pokazva spiner che se sluchva neshto
                    newUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                    setProgressBarIndeterminate(false); //izkluchva spiner che se sluchva neshto

                            if(e==null) {
                           //User successfully created!.Switch to main screen.
                           Intent intent = new Intent(SignUpActivity.this,Main.class);
                           //dobaviame flagove, za da ne moze usera da se varne pak kam toya ekran
                           intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                           startActivity(intent);



                       }
                            else {
                           AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                           builder.setTitle(R.string.sign_up_error_title)
                                   .setMessage(e.getMessage())
                                   .setPositiveButton(R.string.ok, null);
                           AlertDialog dialog = builder.create();
                           dialog.show();
                       }
                        }
                    });

                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sign_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
