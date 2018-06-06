package com.example.opilane.tictactoe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //deklareerin muutujad
    //kahemõõteline array
    private Button[][] buttons = new Button[3][3];
    //player1 ehk X alustab alati ennem
    private boolean player1Turn = true;
    //loeb rounde ehk kordi
    private int roundCount;

    private int player1Points;
    private int player2Points;
    //loeb mängijate punkte
    private TextView textViewPlayer1;
    private TextView textViewPlayer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //lähtestan muutujad
        textViewPlayer1 = findViewById(R.id.text_view_p1);
        textViewPlayer2 = findViewById(R.id.text_view_p2);

        //Siin ma kirjutan viited oma tekstile, aga selle asemel, et kirjutan kõik 9 nuppu üks haaval välja ma panen need nested loopi.
        //we increment I with each round in this loop
        //siin me kinnitame oma nuppe nii selle nested loopi silmuse kaudu, et me kajastaksime kõik meie read ja veerud meie kahemõõtmelise nupude arrays
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                //button_ on iga nuppu ID algus
                //loopib läbi kõik nuppude ID-d
                String buttonID = "button_" + i + j;
                //see on ressursi ID, mille me peame läbima, et leidma view by ID
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                //sel moel saame viiteid kõigile oma nuppudele, ilma et peaksite neid ükshaaval määrama
                buttons[i][j] = findViewById(resID);
                //me edastame oma Main Activity kui onclick kuulajana
                buttons[i][j].setOnClickListener(this);
            }
        }

        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();

            }
        });
    }
    //see on click meetod kuulab kõigi üheksa nuppu vajudust
    @Override
    public void onClick(View v) {
        //Kontrollib, kas see nupp, millele klõpsati, et sellel oleks tühi string. Kui ei,l see tähedab seda, et selle nuppule on juba vajutatud
        if (!((Button) v).getText().toString().equals("")){
            return;
        }
        //Muudab nuppude teksti
        //Kontrollib kas on mängija ühe kord
        if (player1Turn) {
            ((Button) v).setText("X");
        //Mängija kahe kord
        } else {
            ((Button) v).setText("O");
        }
        //Kui keegi on oma käigu ära teinud, me tahame incrementida roundcounti nii et me teaksime, et üks round on läbi
        roundCount++;
        //Kontrollib milline mängija on võitnud
        if (checkForWin()) {
            //Kui on player1 kord siis mängija üks võidab
            if (player1Turn){
                player1Wins();
            //Kui ei ole mängija ühe kord siis mängija 2 võidab
            } else {
                player2Wins();
            }
            //Kui üheksa käiku on möödas siis on viik
        } else if (roundCount == 9) {
            draw();
            //Kui pole võitjat ega viiki siis on mängija ühe kord
        } else {
            player1Turn = !player1Turn;
        }
    }
    //Kontrollib kas keegi on võitnud või ei
    //Rida, veerude või diagonaali kontroll peab võrduma trueiga, et selguks võitja
    private boolean checkForWin() {
        //Siin me peame läbima kõik oma read, veerud ja diagonaalid ja kontrollima kas keegi on võitnud. Selle jaoks me salvestame oma nuppude teksti
        String[][] field = new String[3][3];
        //Siin ma tahan sisetada kõik nuppude tekstid string arryse ja seda ma teen nested loopiga.
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                //Siin me läbime kõik oma nuppud ja salvestame nad kõik oma string arryse
                field[i][j] = buttons[i][j].getText().toString();
            }
        }
        //Siin me kasutame string arry-d, et kontrollida kõik oma read
        for (int i = 0; i < 3; i++) {
            //Siin me ei tee loopi ja vaid läbime otse kõik oma veerud
            //Võrdleb kõiki kolme fieldi mis asuvad üksteise kõrval
            if (field[i][0].equals(field[i][1])
                    && field[i][0].equals(field[i][2])
                    //Teeb kindlaks, et kõik kolm fieldi ei oleks tühjad. Sest kui oleksid, siis meil poleks võitjat
                    && !field[i][0].equals("")){
                //Mis iganes asi kutsub checkForWin meetodit, saab return true ja teeb, et võitja on olemas
                return true;
            }
        }
        //Siin me kasutame string arry-d, et kontrollida kõik oma veerud
        for (int i = 0; i < 3; i++) {
            if (field[0][i].equals(field[1][i])
                    && field[0][i].equals(field[2][i])
                    && !field[0][i].equals("")){
                return true;
            }
        }
        //Kontrollib esimest diagonaali
        if (field[0][0].equals(field[1][1])
                && field[0][0].equals(field[2][2])
                && !field[0][0].equals("")) {
            return true;
        }
        //Kontrollib teist diagonaali
        if (field[0][2].equals(field[1][1])
                && field[0][2].equals(field[2][0])
                && !field[0][2].equals("")) {
            return true;
        }
        //Kui kõik kolm Rida, veerude või diagonaali kontroll ei võrdu true-ga siis see returnib flase-ina ehk võitjat pole ja tekkib viigi seis
        return false;
    }
    //Kui mängija üks võidab, tuleb ekraanile sõnum: Player 1 wins!
    private void player1Wins(){
        player1Points++;
        Toast.makeText(this, "Player 1 wins!", Toast.LENGTH_SHORT).show();
        //Uuendab textViewe nii et need näitaksid mängija ühe punktide kogust
        updatePointsText();
        //Lähestab meie mängu ala et uus round saaks alata
        resetBoard();
    }
    //Kui mängija kaks võidab, tuleb ekraanile sõnum: Player 2 wins!
    private void player2Wins(){
        player2Points++;
        Toast.makeText(this, "Player 2 wins!", Toast.LENGTH_SHORT).show();
        //Uuendab textViewe nii et need näitaksid mängija ühe punktide kogust
        updatePointsText();
        //Lähestab meie mängu ala et uus round saaks alata
        resetBoard();
    }
    //Kui tuleb viik, tuleb ekraanile sõnum: Draw!
    private void draw(){
        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
        //Lähestab meie mängu ala et uus round saaks alata
        resetBoard();
    }
    //Mängijate punktide koguste meetod
    private void updatePointsText() {
        //Uuendab textView viiteid
        textViewPlayer1.setText("Player 1: " + player1Points);
        textViewPlayer2.setText(("Player 2: " + player2Points));
    }
    //Selles meetodis me tahame lähestada kõik oma nuppud tühjale stringle, lähestada oma roundcount nulli ja panna oma player1Turn trueks, et järgmises roundis player1 alustab esimesena
    private void resetBoard() {
        for (int i=0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                //loopib läbi kõik meie nuppud ja määrab need tühjale stringile
                buttons[i][j].setText("");
            }
        }

        roundCount = 0;
        player1Turn = true;
    }

    private void resetGame() {
        //Resetib mõlema mängija punktid nulli
        player1Points = 0;
        player2Points = 0;
        updatePointsText();
        resetBoard();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Kui seadet pöörata landsacpe modei siis äpp hävitab roundcounti, nuppudel oleva teksti ja mängija punktid ära.
        //Selle jaoks, et need andmed jääksid alles ma loon üleval oleva meetodi mis salvestab need andmed
        //Määran mis andmed salvestatakse kui seade peaks minema landscape modei
        outState.putInt("roundCount", roundCount);
        outState.putInt("player1Points", player1Points);
        outState.putInt("player2Points", player2Points);
        outState.putBoolean("player1Turn", player1Turn);
    }
    //Taastab originaal asendise pärast orientatsiooni muutust
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        roundCount = savedInstanceState.getInt("roundCount");
        player1Points = savedInstanceState.getInt("player1Points");
        player2Points = savedInstanceState.getInt("player2Points");
        player1Turn = savedInstanceState.getBoolean("player1Turn");
    }
}
