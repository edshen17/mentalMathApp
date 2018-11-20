package com.edwin_shen.mentalmath.ui;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import com.edwin_shen.mentalmath.R;
import com.edwin_shen.mentalmath.model.Question;

// main GameActivity class
public class GameActivity extends Activity implements TextToSpeech.OnInitListener {
    private TextView scoreText;
    private TextView questionText;
    private EditText answerText;
    private Button breakDownButton;
    private Button repeatButton;
    private Button unblurButton;
    private Button enterButton;
    private int index;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        tts = new TextToSpeech(this, this);
        scoreText = (TextView) findViewById(R.id.scoreTextView);
        questionText = (TextView) findViewById(R.id.questionTextView);
        answerText = (EditText) findViewById(R.id.answerEditText);
        breakDownButton = (Button) findViewById(R.id.breakButton);
        repeatButton = (Button) findViewById(R.id.repeatButton);
        unblurButton = (Button) findViewById(R.id.unblurButton);
        enterButton = (Button) findViewById(R.id.enterButton);
        Intent intent = getIntent();
        startGameActivity(intent);
        speak();

        // sets focus on answerText, so the keyboard automatically pops up
        answerText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        // repeats question when clicked
        repeatButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                speak();
            }

        });
    }

    // shuts down text to speech
    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    // sets the settings on tts on initialization
    @Override
    public void onInit(int status) {
        // TODO Auto-generated method stub

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.CHINESE);

            // tts.setPitch(5); // set pitch level

            // tts.setSpeechRate(2); // set speech speed rate

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(getString(R.string.tts), getString(R.string.lang_not_supported));
            } else {
                repeatButton.setEnabled(true);
                speak();
            }

        } else {
            Log.e(getString(R.string.tts), getString(R.string.init_failed));
        }

    }

    // reads the text
    private void speak() {
        String text = questionText.getText().toString();
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }



    // starts the game activity by initializing the questions/answers from the intent
    // then adds questions to questions array based off the operation
    private void startGameActivity(Intent intent) {
        int amount = intent.getIntExtra(getString(R.string.key_amount), -1000);
        int from = intent.getIntExtra(getString(R.string.key_from), -1000);
        int to = intent.getIntExtra(getString(R.string.key_to), -1000);
        String operation = intent.getStringExtra(getString(R.string.key_radio));
        ArrayList<Question> questions = new ArrayList<Question>();

        for(int i =0; i < amount; i ++) {
            if (operation.equals("Addition")) {
                questions.add(new Question(from, to, "+"));
            } else if (operation.equals("Subtraction")) {
                questions.add(new Question(from, to, "-"));
            } else {
                questions.add(new Question(from, to, "*"));
            }
        }

        loadQuestions(questions, intent);
    }

    // loads the page with the score and question and checks if the input is correct.
    // if it is, advances the question to the next one
    private void loadQuestions(final ArrayList<Question> questions, Intent intent) {

        scoreText.setText("Score: " + index);
        questionText.setText(questions.get(index).question);
        questionText.setVisibility(View.INVISIBLE);
        speak();

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 if (answerText.getText().toString().isEmpty()) {
                    Toast.makeText(GameActivity.this, "Enter a number!", Toast.LENGTH_LONG).show();

                }


                else if (Integer.valueOf(answerText.getText().toString()) == questions.get(index).answer)  {
                    tts.speak("=" + questions.get(index).answer, TextToSpeech.QUEUE_FLUSH, null);
                    index++;
                    questionText.setText(questions.get(index).question);
                    scoreText.setText("Score: " + index);
                    answerText.setText("");
                    speak();

                }


                else {
                    Toast.makeText(GameActivity.this, "Try again!", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Blurs/unblurs question
        unblurButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (questionText.getVisibility() == View.VISIBLE) {
                    questionText.setVisibility(View.INVISIBLE);
                }

                else {
                    questionText.setVisibility(View.VISIBLE);
                }
            }
        });



    }

    // clears array/resets everything in the questions array when back button is pressed
    @Override
    public void onBackPressed() {
        finish();
    }


}