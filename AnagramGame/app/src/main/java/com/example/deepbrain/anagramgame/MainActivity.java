package com.example.deepbrain.anagramgame;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView wordTv, score, wordsCount, res;
    private EditText wordEnteredTv;
    private Button validate, newGame, help;
    private String wordToFind;
    public int scoreRes = 0;
    Boolean isOver = false;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wordTv = (TextView) findViewById(R.id.wordTv);
        wordEnteredTv = (EditText) findViewById(R.id.wordEnteredEt);
        validate = (Button) findViewById(R.id.validate);
        validate.setOnClickListener(this);
        newGame = (Button) findViewById(R.id.newGame);
        newGame.setOnClickListener(this);
        score = (TextView) findViewById(R.id.score);
        wordsCount = (TextView) findViewById(R.id.wordsCount);
        res = (TextView) findViewById(R.id.results);


        help = (Button) findViewById(R.id.help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"СЛОВО - "+Anagram.WORDS[Anagram.currentIndex-1], Toast.LENGTH_SHORT).show();
            }
        });

        newGame();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View view) {
        if (view == validate) {
            validate();
        } else if (view == newGame && isOver==false) {
            scoreRes--;
            newGame();
        }
        else
            if(view==newGame && isOver == true){
                Anagram.currentIndex = 0;
                scoreRes = 0;
                validate.setEnabled(true);
                res.setText("");
                newGame.setText("ПРОПУСК");
                newGame();
            }

    }

    private void validate() {
        String w = wordEnteredTv.getText().toString();

        if (wordToFind.equals(w)) {
            Toast.makeText(this, "Поздравляю ! Вы нашли слово " + wordToFind, Toast.LENGTH_SHORT).show();
            scoreRes+=2;
            newGame();
        } else {
            Toast.makeText(this, "Попробуйте еще !", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("ResourceAsColor")
    private void newGame() {
        wordToFind = Anagram.getNext();
        if(!wordToFind.equals("end")) {
            String wordShuffled = Anagram.shuffleWord(wordToFind);
            score.setText("Очков:" + scoreRes);
            wordsCount.setText(Anagram.currentIndex + "/" + Anagram.WORDS.length + "");
            wordTv.setText(wordShuffled);
            wordEnteredTv.setText("");
            isOver = false;
        }
        else
        {
            score.setText("");
            wordsCount.setText("");
            res.setText("Счет:"+scoreRes+"/"+(Anagram.WORDS.length)*2);
            validate.setEnabled(false);
            //newGame.setEnabled(false);
            newGame.setText("Новая игра");

            isOver = true;
        }
    }
}