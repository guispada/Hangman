package com.example.hangman;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Jogo extends AppCompatActivity {

    TextView txtWordToBeGuessed;
    String wordToBeGuessed;
    String wordDisplayedString;
    char[] wordDisplayedCharArray;
    ArrayList<String> myListOfWords;
    EditText edtInput;
    TextView txtLettersTried;
    String lettersTried;
    final String MESSAGE_WITH_LETTERS_TRIED = "Letras usadas: ";
    TextView txtTriesLeft;
    String triesLeft;
    final String WINNING_MESSAGE = "Você Venceu!";
    final String LOSING_MESSAGE = "Você Perdeu!";
    Animation rotateAnimation;
    Animation scaleAnimation;
    Animation scaleAndRotateAnimation;

    void revealLetterInWord(char letter){
        int indexOfLetter = wordToBeGuessed.indexOf(letter);

        while(indexOfLetter >= 0){
            wordDisplayedCharArray[indexOfLetter] = wordToBeGuessed.charAt(indexOfLetter);
            indexOfLetter = wordToBeGuessed.indexOf(letter, indexOfLetter + 1);
        }

        wordDisplayedString = String.valueOf(wordDisplayedCharArray);
    }

    void displayWordOnScreen(){
        String formattedString = "";
        for(char character : wordDisplayedCharArray){
            formattedString += character + "";
        }
        txtWordToBeGuessed.setText(formattedString);
    }

    void initializeGame(){
        Collections.shuffle(myListOfWords);
        wordToBeGuessed = myListOfWords.get(0);
        myListOfWords.remove(0);

        wordDisplayedCharArray = wordToBeGuessed.toCharArray();

        for(int i = 1; i < wordDisplayedCharArray.length - 1; i++){
            wordDisplayedCharArray[i] = '_';
        }

        revealLetterInWord(wordDisplayedCharArray[0]);

        revealLetterInWord(wordDisplayedCharArray[wordDisplayedCharArray.length - 1]);

        wordDisplayedString = String.valueOf(wordDisplayedCharArray);

        displayWordOnScreen();

        edtInput.setText("");

        lettersTried = " ";

        txtLettersTried.setText(MESSAGE_WITH_LETTERS_TRIED);

        triesLeft = " X X X X X";
        txtTriesLeft.setText(triesLeft);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogo);

        myListOfWords = new ArrayList<String>();
        txtWordToBeGuessed = (TextView) findViewById(R.id.textView2);
        edtInput = (EditText) findViewById(R.id.editText);
        txtLettersTried = (TextView) findViewById(R.id.textView3);
        txtTriesLeft = (TextView) findViewById(R.id.textView4);
        rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale);
        scaleAndRotateAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_and_rotate);
        scaleAndRotateAnimation.setFillAfter(true);

        InputStream myInputStream = null;
        Scanner in = null;
        String aWord = "";

        try {
            myInputStream = getAssets().open("database_file.txt");
            in = new Scanner(myInputStream);
            while(in.hasNext()){
                aWord = in.next();
                myListOfWords.add(aWord);
            }
        } catch (IOException e) {
            Toast.makeText(Jogo.this,
                    e.getClass().getSimpleName() + ": " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
        finally {
            if(in != null) {
                in.close();
            }
            try {
                if(myInputStream != null) {
                    myInputStream.close();
                }
            } catch (IOException e) {
                Toast.makeText(Jogo.this,
                        e.getClass().getSimpleName() + ": " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }

        initializeGame();

        edtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() != 0){
                    checkIfLetterIsInWord(charSequence.charAt(0));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    void checkIfLetterIsInWord(char letter){
        closeKeyboard();
        if (wordToBeGuessed.indexOf(letter) >= 0) {
            if (wordDisplayedString.indexOf(letter) < 0) {
                txtWordToBeGuessed.startAnimation(scaleAnimation);

                revealLetterInWord(letter);

                displayWordOnScreen();

                if (!wordDisplayedString.contains("_")) {
                    txtTriesLeft.startAnimation(scaleAndRotateAnimation);
                    txtTriesLeft.setText(WINNING_MESSAGE);
                }
            }
        }
        else {
            decreaseAndDisplayTriesLeft();

            if (triesLeft.isEmpty()) {
                txtTriesLeft.startAnimation(scaleAnimation);

                txtTriesLeft.setText(LOSING_MESSAGE);

                Toast.makeText(Jogo.this, "You Lost", Toast.LENGTH_LONG).show();

            }
        }


        if(lettersTried.indexOf(letter) < 0){
            lettersTried += letter + ", ";
            String messageToBeDisplayed = MESSAGE_WITH_LETTERS_TRIED + lettersTried;
            txtLettersTried.setText(messageToBeDisplayed);
        }
    }

    void decreaseAndDisplayTriesLeft(){
        if(!triesLeft.isEmpty()){
            txtTriesLeft.startAnimation(scaleAndRotateAnimation);
            triesLeft = triesLeft.substring(0, triesLeft.length() - 2);
            txtTriesLeft.setText(triesLeft);
        }
    }

    void closeKeyboard(){
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    public void resetGame(View view) {
        initializeGame();
    }
}
