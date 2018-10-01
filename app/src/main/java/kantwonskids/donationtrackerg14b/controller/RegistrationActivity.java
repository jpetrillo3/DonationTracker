package kantwonskids.donationtrackerg14b.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import kantwonskids.donationtrackerg14b.R;
import kantwonskids.donationtrackerg14b.model.*;

public class RegistrationActivity extends AppCompatActivity {


    // username and password field values
    private EditText usernameField;
    private EditText passwordField;
    private EditText confirmField;

    // valid user types
    private String [] userTypes = {"User", "LocationEmployee", "Admin"};
    private Spinner accTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // get references
        usernameField = findViewById(R.id.registration_usernameField);
        passwordField = findViewById(R.id.registration_passwordField);
        confirmField = findViewById(R.id.registration_confirmField);

        // Create a spinner and populate with valid values
        accTypeSpinner = findViewById(R.id.registration_accountType_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, userTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accTypeSpinner.setAdapter(adapter);

        // Attempt to create an account if the account doesn't already exist
        Button createAccountButton = findViewById(R.id.registration_createAccount_button);
        createAccountButton.setOnClickListener((view) -> {
            attemptRegistration();
        });


        // Return to welcome screen if cancel button is pressed
        Button cancelButton = findViewById(R.id.registration_cancel_button);
        cancelButton.setOnClickListener((view) -> {
            Intent intent_cancel = new Intent(this, WelcomeActivity.class);
            startActivity(intent_cancel);
        });
    }

     /**
     * Attempts to register.  Either succeeds or presents a relevent error message
     */
    private void attemptRegistration() {
        usernameField.setError(null);
        passwordField.setError(null);
        confirmField.setError(null);


        String u = usernameField.getText().toString();
        String p = passwordField.getText().toString();
        String c = confirmField.getText().toString();
        String accType = (String) accTypeSpinner.getSelectedItem();

        // get reference to the model
        Model model = Model.getInstance();
        // check to see if the username exists in the list
        if (model._userList.usernameTaken(u)) {
            usernameField.setError("Username is taken");
        }
        else if (!validUsername(u).equals("VALID")) {
            usernameField.setError(validUsername(u));
        }
        else if (!p.equals(c)) {
            passwordField.setError("Passwords do not match!");
        }
        else if (!validPassword(p).equals("VALID")) {
            passwordField.setError(validPassword(p));
        }
        else
        {
            model._userList.addUser(new User(u, p, accType));
            Intent intent_welcome = new Intent(this, MainActivity.class);
            startActivity(intent_welcome);
        }


    }

    /**
     * Checks a username's validity and returns a message indicating
     *
     * @param u username
     * @return message
     */
    private String validUsername(String u) {
        String msg = "VALID";

        if (u.length() < 4) {
            msg = "Invalid Username.  Must be 4 or more characters.";
        } else {
            if (!Character.isLetter(u.charAt(0)))
            {
                msg = "Invalid Username.  Must begin with a letter.";
            } else {
                for (int i = 0; i < u.length(); i++) {
                    if (!Character.isLetterOrDigit(u.charAt(i))) {
                        msg = "Invalid Username.  Must consist of letters and numbers only.";
                    }
                }
            }
        }
        return msg;
    }

    /**
     * Checks a passwords's validity and returns a message indicating
     *
     * @param p password
     * @return message
     */
    private String validPassword(String p) {
        String msg = "VALID";

        if (p.length() < 8) {
            msg = "Invalid Password.  Must be 8 or more characters.";
        } else {

            for (int i = 0; i < p.length(); i++) {
                if (!Character.isLetterOrDigit(p.charAt(i))) {
                    msg = "Invalid Password.  Must consist of letters and numbers only.";
                }
            }
        }
        return msg;
    }
}
