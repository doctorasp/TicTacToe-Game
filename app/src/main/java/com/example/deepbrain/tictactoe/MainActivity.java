package com.example.deepbrain.tictactoe;

import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Random;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //region GlobalValues
    public int c[][];
    public int i, j, k = 0;
    Button b[][];
    TextView textView;
    TextView playerView;
    TextView aiView;
    MediaPlayer mediaPlayer;
    Button songVol;
    Boolean vol = true;
    int click = 0;
    int playerScore = 0;
    int aiScore = 0;
    int player1Score = 0;
    int player2Score = 0;
    Boolean AImode = false;
    Boolean PtoPmode = false;
    Boolean globalGameOver = false;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //PlayerToAI Default
        AImode = true;
        PtoPmode = false;
        setBoard();

        //region MusicPlayer
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.back_song);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);

        songVol = (Button) findViewById(R.id.songVolume);
        songVol.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if(vol==true){
                    //onPause();
                    mediaPlayer.setVolume(0,0);
                    vol = false;
                    songVol.setBackgroundTintList(getResources().getColorStateList(R.color.colorGray));
                }
                else if(vol==false)
                {
                    //mediaPlayer.start();
                    mediaPlayer.setVolume(1,1);
                    vol = true;
                    songVol.setBackgroundTintList(getResources().getColorStateList(R.color.colorOrange));
                }

            }
        });
        //endregion

        TableLayout tlayout = (TableLayout) findViewById(R.id.mainLayout);
        tlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AImode == true && PtoPmode == false && globalGameOver == true ){
                    setBoard();
                }
                else if(AImode == false && PtoPmode == true && globalGameOver == true)
                {
                    setBoardPlayerToPlayer();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.stop();
        mediaPlayer.release();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem item = menu.add(Menu.NONE,1,Menu.NONE,"Режим 'Игрок - AI'");
        menu.add(Menu.NONE,2,Menu.NONE,"Режим 'Игрок - Игрок'");
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case 1:
                Toast.makeText(getApplicationContext(),"Режим Игрок - AI",
                        Toast.LENGTH_SHORT).show();
                setBoard();
                AImode = true;
                PtoPmode = false;
                return true;
            case 2:
                Toast.makeText(getApplicationContext(),"Режим Игрок - Игрок ",
                        Toast.LENGTH_SHORT).show();
                setBoardPlayerToPlayer();
                AImode = false;
                PtoPmode = true;
                return true;
            default:
                return false;
        }
    }

    private void setBoardPlayerToPlayer() {

        b = new Button[4][4];
        c = new int[4][4];

        textView = (TextView) findViewById(R.id.dialogue);

        b[1][3] = (Button) findViewById(R.id.one);
        b[1][2] = (Button) findViewById(R.id.two);
        b[1][1] = (Button) findViewById(R.id.three);

        b[2][3] = (Button) findViewById(R.id.four);
        b[2][2] = (Button) findViewById(R.id.five);
        b[2][1] = (Button) findViewById(R.id.six);

        b[3][3] = (Button) findViewById(R.id.seven);
        b[3][2] = (Button) findViewById(R.id.eight);
        b[3][1] = (Button) findViewById(R.id.nine);

        for (i = 1; i <= 3; i++) {
            for (j = 1; j <= 3; j++)
                c[i][j] = 2;
        }

        textView.setText("Нажмите кнопку для начала.");

        playerView.setText("Игрок 1:"+player1Score);
        aiView.setText("Игрок 2:"+player2Score);


        // add the click listeners for each button
        for (i = 1; i <= 3; i++) {
            for (j = 1; j <= 3; j++) {
                b[i][j].setOnClickListener(new MyClickListenerPlayerToPlayer(i, j));
                if(!b[i][j].isEnabled()) {
                    b[i][j].setText(" ");
                    b[i][j].setEnabled(true);
                    b[i][j].setBackgroundResource(R.drawable.my_button_bg);
                }
            }
        }
    }

    class MyClickListenerPlayerToPlayer implements View.OnClickListener {
        int x;
        int y;

        public MyClickListenerPlayerToPlayer(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void movePlayer1(){
            if (b[x][y].isEnabled()) {
                b[x][y].setEnabled(false);
                b[x][y].setText("O");
                c[x][y] = 0;
                textView.setText("");

                Toast.makeText(getApplicationContext(),"Игрок 1 походил",
                        Toast.LENGTH_SHORT).show();
            click = 1;
            }
        }

        public void movePlayer2(){

            if (b[x][y].isEnabled()) {
                b[x][y].setEnabled(false);
                b[x][y].setText("X");
                c[x][y] = 1;
                textView.setText("");

                Toast.makeText(getApplicationContext(), "Игрок 2 походил",
                        Toast.LENGTH_SHORT).show();
                click = 0;
            }
        }

        //Click when Player:Player Game
        public void onClick(View view) {
                if(click == 0){
                    movePlayer1();
                }
                else
                    movePlayer2();
                checkBoardPlayerToPlayer();
        }
    }

    public void setEnabledBtn(){
        for (i = 1; i <= 3; i++) {
            for (j = 1; j <= 3; j++)
                b[i][j].setEnabled(false);
        }
    }

    public boolean checkBoardPlayerToPlayer() {
        //region checkBoardPlayerToPlayer
        boolean gameOver = false;
        globalGameOver = false;

        if ((c[1][1] == 0 && c[2][2] == 0 && c[3][3] == 0)
                || (c[1][3] == 0 && c[2][2] == 0 && c[3][1] == 0)
                || (c[1][2] == 0 && c[2][2] == 0 && c[3][2] == 0)
                || (c[1][3] == 0 && c[2][3] == 0 && c[3][3] == 0)
                || (c[1][1] == 0 && c[1][2] == 0 && c[1][3] == 0)
                || (c[2][1] == 0 && c[2][2] == 0 && c[2][3] == 0)
                || (c[3][1] == 0 && c[3][2] == 0 && c[3][3] == 0)
                || (c[1][1] == 0 && c[2][1] == 0 && c[3][1] == 0)) {
            textView.setText("Игра завершена. Игрок 1 победил!");
            gameOver = true;
            globalGameOver = true;
            player1Score +=1;
            setEnabledBtn();

            if((c[1][1] == 0 && c[2][2] == 0 && c[3][3] == 0)) {
                b[1][1].setBackgroundResource(R.drawable.my_button_filled);
                b[2][2].setBackgroundResource(R.drawable.my_button_filled);
                b[3][3].setBackgroundResource(R.drawable.my_button_filled);
            }
            else if((c[1][3] == 0 && c[2][2] == 0 && c[3][1] == 0)){
                b[1][3].setBackgroundResource(R.drawable.my_button_filled);
                b[2][2].setBackgroundResource(R.drawable.my_button_filled);
                b[3][1].setBackgroundResource(R.drawable.my_button_filled);
            }
            else if((c[1][2] == 0 && c[2][2] == 0 && c[3][2] == 0)){
                b[1][2].setBackgroundResource(R.drawable.my_button_filled);
                b[2][2].setBackgroundResource(R.drawable.my_button_filled);
                b[3][2].setBackgroundResource(R.drawable.my_button_filled);
            }
            else if((c[1][3] == 0 && c[2][3] == 0 && c[3][3] == 0)){
                b[1][3].setBackgroundResource(R.drawable.my_button_filled);
                b[2][3].setBackgroundResource(R.drawable.my_button_filled);
                b[3][3].setBackgroundResource(R.drawable.my_button_filled);
            }
            else if((c[1][1] == 0 && c[1][2] == 0 && c[1][3] == 0)){
                b[1][1].setBackgroundResource(R.drawable.my_button_filled);
                b[1][2].setBackgroundResource(R.drawable.my_button_filled);
                b[1][3].setBackgroundResource(R.drawable.my_button_filled);
            }
            else if((c[2][1] == 0 && c[2][2] == 0 && c[2][3] == 0)){
                b[2][1].setBackgroundResource(R.drawable.my_button_filled);
                b[2][2].setBackgroundResource(R.drawable.my_button_filled);
                b[2][3].setBackgroundResource(R.drawable.my_button_filled);
            }
            else if((c[3][1] == 0 && c[3][2] == 0 && c[3][3] == 0)){
                b[3][1].setBackgroundResource(R.drawable.my_button_filled);
                b[3][2].setBackgroundResource(R.drawable.my_button_filled);
                b[3][3].setBackgroundResource(R.drawable.my_button_filled);
            }
            else if((c[1][1] == 0 && c[2][1] == 0 && c[3][1] == 0)){
                b[1][1].setBackgroundResource(R.drawable.my_button_filled);
                b[2][1].setBackgroundResource(R.drawable.my_button_filled);
                b[3][1].setBackgroundResource(R.drawable.my_button_filled);
            }

        } else if ((c[1][1] == 1 && c[2][2] == 1 && c[3][3] == 1)
                || (c[1][3] == 1 && c[2][2] == 1 && c[3][1] == 1)
                || (c[1][2] == 1 && c[2][2] == 1 && c[3][2] == 1)
                || (c[1][3] == 1 && c[2][3] == 1 && c[3][3] == 1)
                || (c[1][1] == 1 && c[1][2] == 1 && c[1][3] == 1)
                || (c[2][1] == 1 && c[2][2] == 1 && c[2][3] == 1)
                || (c[3][1] == 1 && c[3][2] == 1 && c[3][3] == 1)
                || (c[1][1] == 1 && c[2][1] == 1 && c[3][1] == 1)) {
            textView.setText("Игра завершена. Игрок 2 победил!");
            gameOver = true;
            globalGameOver = true;
            setEnabledBtn();
            player2Score+=1;

            if((c[1][1] == 1 && c[2][2] == 1 && c[3][3] == 1)) {
                b[1][1].setBackgroundResource(R.drawable.my_button_filled);
                b[2][2].setBackgroundResource(R.drawable.my_button_filled);
                b[3][3].setBackgroundResource(R.drawable.my_button_filled);
            }
            else if((c[1][3] == 1 && c[2][2] == 1 && c[3][1] == 1)){
                b[1][3].setBackgroundResource(R.drawable.my_button_filled);
                b[2][2].setBackgroundResource(R.drawable.my_button_filled);
                b[3][1].setBackgroundResource(R.drawable.my_button_filled);
            }
            else if((c[1][2] == 1 && c[2][2] == 1 && c[3][2] == 1)){
                b[1][2].setBackgroundResource(R.drawable.my_button_filled);
                b[2][2].setBackgroundResource(R.drawable.my_button_filled);
                b[3][2].setBackgroundResource(R.drawable.my_button_filled);
            }
            else if((c[1][3] == 1 && c[2][3] == 1 && c[3][3] == 1)){
                b[1][3].setBackgroundResource(R.drawable.my_button_filled);
                b[2][3].setBackgroundResource(R.drawable.my_button_filled);
                b[3][3].setBackgroundResource(R.drawable.my_button_filled);
            }
            else if((c[1][1] == 1 && c[1][2] == 1 && c[1][3] == 1)){
                b[1][1].setBackgroundResource(R.drawable.my_button_filled);
                b[1][2].setBackgroundResource(R.drawable.my_button_filled);
                b[1][3].setBackgroundResource(R.drawable.my_button_filled);
            }
            else if((c[2][1] == 1 && c[2][2] == 1 && c[2][3] == 1)){
                b[2][1].setBackgroundResource(R.drawable.my_button_filled);
                b[2][2].setBackgroundResource(R.drawable.my_button_filled);
                b[2][3].setBackgroundResource(R.drawable.my_button_filled);
            }
            else if((c[3][1] == 1 && c[3][2] == 1 && c[3][3] == 1)){
                b[3][1].setBackgroundResource(R.drawable.my_button_filled);
                b[3][2].setBackgroundResource(R.drawable.my_button_filled);
                b[3][3].setBackgroundResource(R.drawable.my_button_filled);
            }
            else if((c[1][1] == 1 && c[2][1] == 1 && c[3][1] == 1)){
                b[1][1].setBackgroundResource(R.drawable.my_button_filled);
                b[2][1].setBackgroundResource(R.drawable.my_button_filled);
                b[3][1].setBackgroundResource(R.drawable.my_button_filled);
            }
        } else {
            boolean empty = false;
            for(i=1; i<=3; i++) {
                for(j=1; j<=3; j++) {
                    if(c[i][j]==2) {
                        empty = true;
                        break;
                    }
                }
            }
            if(!empty) {
                gameOver = true;
                globalGameOver = true;
                textView.setText("Игра завершена. Это ничия!");
                setEnabledBtn();
            }
        }
        return gameOver;
        //endregion
    }

    //AI Methods

    private void setBoard() {
        //region setBoardAI
        b = new Button[4][4];
        c = new int[4][4];


        textView = (TextView) findViewById(R.id.dialogue);
        playerView = (TextView) findViewById(R.id.playerText);
        aiView = (TextView) findViewById(R.id.aiText);

        b[1][3] = (Button) findViewById(R.id.one);
        b[1][2] = (Button) findViewById(R.id.two);
        b[1][1] = (Button) findViewById(R.id.three);


        b[2][3] = (Button) findViewById(R.id.four);
        b[2][2] = (Button) findViewById(R.id.five);
        b[2][1] = (Button) findViewById(R.id.six);


        b[3][3] = (Button) findViewById(R.id.seven);
        b[3][2] = (Button) findViewById(R.id.eight);
        b[3][1] = (Button) findViewById(R.id.nine);

        for (i = 1; i <= 3; i++) {
            for (j = 1; j <= 3; j++)
                c[i][j] = 2;
        }


        textView.setText("Нажмите кнопку для начала.");
        playerView.setText("Игрок:"+playerScore);
        aiView.setText("AI:"+aiScore);
        // add the click listeners for each button
        for (i = 1; i <= 3; i++) {
            for (j = 1; j <= 3; j++) {
                b[i][j].setOnClickListener(new MyClickListener(i, j));
                if(!b[i][j].isEnabled()) {
                    b[i][j].setText(" ");
                    b[i][j].setEnabled(true);
                    b[i][j].setBackgroundResource(R.drawable.my_button_bg);
                }
            }
        }
        //endregion
    }

    public boolean checkBoard() {
        //region checkBoardAI
        boolean gameOver = false;
        globalGameOver = false;

        if ((c[1][1] == 0 && c[2][2] == 0 && c[3][3] == 0)
                || (c[1][3] == 0 && c[2][2] == 0 && c[3][1] == 0)
                || (c[1][2] == 0 && c[2][2] == 0 && c[3][2] == 0)
                || (c[1][3] == 0 && c[2][3] == 0 && c[3][3] == 0)
                || (c[1][1] == 0 && c[1][2] == 0 && c[1][3] == 0)
                || (c[2][1] == 0 && c[2][2] == 0 && c[2][3] == 0)
                || (c[3][1] == 0 && c[3][2] == 0 && c[3][3] == 0)
                || (c[1][1] == 0 && c[2][1] == 0 && c[3][1] == 0)) {
            textView.setText("Игра завершена. Вы победили!");
            gameOver = true;
            playerScore+=1;
            setEnabledBtn();
            globalGameOver = true;

            if((c[1][1] == 0 && c[2][2] == 0 && c[3][3] == 0)) {
                b[1][1].setBackgroundResource(R.drawable.my_button_filled);
                b[2][2].setBackgroundResource(R.drawable.my_button_filled);
                b[3][3].setBackgroundResource(R.drawable.my_button_filled);
            }
            else if((c[1][3] == 0 && c[2][2] == 0 && c[3][1] == 0)){
                b[1][3].setBackgroundResource(R.drawable.my_button_filled);
                b[2][2].setBackgroundResource(R.drawable.my_button_filled);
                b[3][1].setBackgroundResource(R.drawable.my_button_filled);
            }
            else if((c[1][2] == 0 && c[2][2] == 0 && c[3][2] == 0)){
                b[1][2].setBackgroundResource(R.drawable.my_button_filled);
                b[2][2].setBackgroundResource(R.drawable.my_button_filled);
                b[3][2].setBackgroundResource(R.drawable.my_button_filled);
            }
            else if((c[1][3] == 0 && c[2][3] == 0 && c[3][3] == 0)){
                b[1][3].setBackgroundResource(R.drawable.my_button_filled);
                b[2][3].setBackgroundResource(R.drawable.my_button_filled);
                b[3][3].setBackgroundResource(R.drawable.my_button_filled);
            }
            else if((c[1][1] == 0 && c[1][2] == 0 && c[1][3] == 0)){
                b[1][1].setBackgroundResource(R.drawable.my_button_filled);
                b[1][2].setBackgroundResource(R.drawable.my_button_filled);
                b[1][3].setBackgroundResource(R.drawable.my_button_filled);
            }
            else if((c[2][1] == 0 && c[2][2] == 0 && c[2][3] == 0)){
                b[2][1].setBackgroundResource(R.drawable.my_button_filled);
                b[2][2].setBackgroundResource(R.drawable.my_button_filled);
                b[2][3].setBackgroundResource(R.drawable.my_button_filled);
            }
            else if((c[3][1] == 0 && c[3][2] == 0 && c[3][3] == 0)){
                b[3][1].setBackgroundResource(R.drawable.my_button_filled);
                b[3][2].setBackgroundResource(R.drawable.my_button_filled);
                b[3][3].setBackgroundResource(R.drawable.my_button_filled);
            }
            else if((c[1][1] == 0 && c[2][1] == 0 && c[3][1] == 0)){
                b[1][1].setBackgroundResource(R.drawable.my_button_filled);
                b[2][1].setBackgroundResource(R.drawable.my_button_filled);
                b[3][1].setBackgroundResource(R.drawable.my_button_filled);
            }

        } else if ((c[1][1] == 1 && c[2][2] == 1 && c[3][3] == 1)
                || (c[1][3] == 1 && c[2][2] == 1 && c[3][1] == 1)
                || (c[1][2] == 1 && c[2][2] == 1 && c[3][2] == 1)
                || (c[1][3] == 1 && c[2][3] == 1 && c[3][3] == 1)
                || (c[1][1] == 1 && c[1][2] == 1 && c[1][3] == 1)
                || (c[2][1] == 1 && c[2][2] == 1 && c[2][3] == 1)
                || (c[3][1] == 1 && c[3][2] == 1 && c[3][3] == 1)
                || (c[1][1] == 1 && c[2][1] == 1 && c[3][1] == 1)) {
            textView.setText("Игра завершена. Победил AI!");
            aiScore+=1;
            gameOver = true;
            globalGameOver = true;
            setEnabledBtn();

            if((c[1][1] == 1 && c[2][2] == 1 && c[3][3] == 1)) {
                b[1][1].setBackgroundResource(R.drawable.my_button_filled);
                b[2][2].setBackgroundResource(R.drawable.my_button_filled);
                b[3][3].setBackgroundResource(R.drawable.my_button_filled);
            }
            else if((c[1][3] == 1 && c[2][2] == 1 && c[3][1] == 1)){
                b[1][3].setBackgroundResource(R.drawable.my_button_filled);
                b[2][2].setBackgroundResource(R.drawable.my_button_filled);
                b[3][1].setBackgroundResource(R.drawable.my_button_filled);
            }
            else if((c[1][2] == 1 && c[2][2] == 1 && c[3][2] == 1)){
                b[1][2].setBackgroundResource(R.drawable.my_button_filled);
                b[2][2].setBackgroundResource(R.drawable.my_button_filled);
                b[3][2].setBackgroundResource(R.drawable.my_button_filled);
            }
            else if((c[1][3] == 1 && c[2][3] == 1 && c[3][3] == 1)){
                b[1][3].setBackgroundResource(R.drawable.my_button_filled);
                b[2][3].setBackgroundResource(R.drawable.my_button_filled);
                b[3][3].setBackgroundResource(R.drawable.my_button_filled);
            }
            else if((c[1][1] == 1 && c[1][2] == 1 && c[1][3] == 1)){
                b[1][1].setBackgroundResource(R.drawable.my_button_filled);
                b[1][2].setBackgroundResource(R.drawable.my_button_filled);
                b[1][3].setBackgroundResource(R.drawable.my_button_filled);
            }
            else if((c[2][1] == 1 && c[2][2] == 1 && c[2][3] == 1)){
                b[2][1].setBackgroundResource(R.drawable.my_button_filled);
                b[2][2].setBackgroundResource(R.drawable.my_button_filled);
                b[2][3].setBackgroundResource(R.drawable.my_button_filled);
            }
            else if((c[3][1] == 1 && c[3][2] == 1 && c[3][3] == 1)){
                b[3][1].setBackgroundResource(R.drawable.my_button_filled);
                b[3][2].setBackgroundResource(R.drawable.my_button_filled);
                b[3][3].setBackgroundResource(R.drawable.my_button_filled);
            }
            else if((c[1][1] == 1 && c[2][1] == 1 && c[3][1] == 1)){
                b[1][1].setBackgroundResource(R.drawable.my_button_filled);
                b[2][1].setBackgroundResource(R.drawable.my_button_filled);
                b[3][1].setBackgroundResource(R.drawable.my_button_filled);
            }
        } else {
            boolean empty = false;
            for(i=1; i<=3; i++) {
                for(j=1; j<=3; j++) {
                    if(c[i][j]==2) {
                        empty = true;
                        break;
                    }
                }
            }
            if(!empty) {
                gameOver = true;
                globalGameOver = true;
                textView.setText("Игра завершена. Это ничия!");
                setEnabledBtn();
            }
        }
        return gameOver;
        //endregion
    }

    class MyClickListener implements View.OnClickListener {
        int x;
        int y;


        public MyClickListener(int x, int y) {
            this.x = x;
            this.y = y;
        }


        public void onClick(View view) {
            if (b[x][y].isEnabled()) {
                b[x][y].setEnabled(false);
                b[x][y].setText("O");
                c[x][y] = 0;
                textView.setText("");
                if (!checkBoard()) {
                    takeTurn();
                }
            }
        }
    }

    public void takeTurn() {
        //region takeTurnAI
        if (c[1][1] == 2 &&
                ((c[1][2] == 0 && c[1][3] == 0) ||
                        (c[2][2] == 0 && c[3][3] == 0) ||
                        (c[2][1] == 0 && c[3][1] == 0))) {
            markSquare(1, 1);
        } else if (c[1][2] == 2 &&
                ((c[2][2] == 0 && c[3][2] == 0) ||
                        (c[1][1] == 0 && c[1][3] == 0))) {
            markSquare(1, 2);
        } else if (c[1][3] == 2 &&
                ((c[1][1] == 0 && c[1][2] == 0) ||
                        (c[3][1] == 0 && c[2][2] == 0) ||
                        (c[2][3] == 0 && c[3][3] == 0))) {
            markSquare(1, 3);
        } else if (c[2][1] == 2 &&
                ((c[2][2] == 0 && c[2][3] == 0) ||
                        (c[1][1] == 0 && c[3][1] == 0))) {
            markSquare(2, 1);
        } else if (c[2][2] == 2 &&
                ((c[1][1] == 0 && c[3][3] == 0) ||
                        (c[1][2] == 0 && c[3][2] == 0) ||
                        (c[3][1] == 0 && c[1][3] == 0) ||
                        (c[2][1] == 0 && c[2][3] == 0))) {
            markSquare(2, 2);
        } else if (c[2][3] == 2 &&
                ((c[2][1] == 0 && c[2][2] == 0) ||
                        (c[1][3] == 0 && c[3][3] == 0))) {
            markSquare(2, 3);
        } else if (c[3][1] == 2 &&
                ((c[1][1] == 0 && c[2][1] == 0) ||
                        (c[3][2] == 0 && c[3][3] == 0) ||
                        (c[2][2] == 0 && c[1][3] == 0))) {
            markSquare(3, 1);
        } else if (c[3][2] == 2 &&
                ((c[1][2] == 0 && c[2][2] == 0) ||
                        (c[3][1] == 0 && c[3][3] == 0))) {
            markSquare(3, 2);
        } else if (c[3][3] == 2 &&
                ((c[1][1] == 0 && c[2][2] == 0) ||
                        (c[1][3] == 0 && c[2][3] == 0) ||
                        (c[3][1] == 0 && c[3][2] == 0))) {
            markSquare(3, 3);
        } else {
            Random rand = new Random();

            int a = rand.nextInt(4);
            int b = rand.nextInt(4);
            while (a == 0 || b == 0 || c[a][b] != 2) {
                a = rand.nextInt(4);
                b = rand.nextInt(4);
            }
            markSquare(a, b);
        }
        //endregion
    }

    public void markSquare(int x, int y) {
        b[x][y].setEnabled(false);
        b[x][y].setText("X");
        c[x][y] = 1;
        checkBoard();
    }

}
