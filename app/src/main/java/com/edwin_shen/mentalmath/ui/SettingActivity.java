package com.edwin_shen.mentalmath.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.edwin_shen.mentalmath.R;

public class SettingActivity extends AppCompatActivity {
    private EditText amountProblems;
    private EditText fromEditText;
    private EditText toEditText;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button playButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        amountProblems = (EditText) findViewById(R.id.amountEditText);
        fromEditText = (EditText) findViewById(R.id.fromEditText);
        toEditText = (EditText) findViewById(R.id.toEditText);
        playButton = (Button) findViewById(R.id.playButton);

        playButton.setOnClickListener(new View.OnClickListener() {
            // When the playButton is pressed, start the game
            @Override
            public void onClick(View view) {
                String amount = amountProblems.getText().toString();
                String from = fromEditText.getText().toString();
                String to = toEditText.getText().toString();

                // Gets which radio is selected
                RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
                int radioButtonID = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) radioGroup.findViewById(radioButtonID);
                String selectedRadio = (String) radioButton.getText();

                if(amount.isEmpty() ||
                        from.isEmpty() ||
                        to.isEmpty()) {

                    Toast.makeText(SettingActivity.this, R.string.missing_input,
                            Toast.LENGTH_LONG).show();
                }

                else if (Integer.valueOf(from) > Integer.valueOf(to)) {
                    Toast.makeText(SettingActivity.this, R.string.range_incorrect,
                            Toast.LENGTH_LONG).show();
                }

                else if (Integer.valueOf(amount) <= 0 ) {
                    Toast.makeText(SettingActivity.this, R.string.amount_error,
                            Toast.LENGTH_LONG).show();
                }

                else {
                    startGame(Integer.valueOf(amount), Integer.valueOf(from), Integer.valueOf(to), selectedRadio);
                }
            }
        });
    }

    // Starts the game by passing the values into intents
    private void startGame(int amount, int from, int to, String selectedRadio) {
        Intent intent = new Intent(this, GameActivity.class);
        Resources resources = getResources();
        String amountKey = getString(R.string.key_amount);
        String fromKey = getString(R.string.key_from);
        String toKey = getString(R.string.key_to);
        String radioKey = getString(R.string.key_radio);
        intent.putExtra(amountKey, amount);
        intent.putExtra(fromKey, from);
        intent.putExtra(toKey, to);
        intent.putExtra(radioKey, selectedRadio);
        startActivity(intent);

    }
}
