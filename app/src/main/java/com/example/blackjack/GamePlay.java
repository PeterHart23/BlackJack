package com.example.blackjack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.blackjack.Game.Deck.Deck;
import com.example.blackjack.Game.Players.Dealer;
import com.example.blackjack.Game.Players.Player;

import java.util.concurrent.TimeUnit;

public class GamePlay extends AppCompatActivity {
    protected String display_dealer = "";
    protected String display_player = "";
    protected Deck deck = new Deck();
    protected Dealer dealer = new Dealer();
    protected Player player;
    double bet;
    int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.hide();

        TextView tvName = findViewById(R.id.tvName2);
        String name = getIntent().getExtras().getString("name");
        tvName.setText(name);
        if(this.player == null){
            // Start the user off with $1000.
            player = new Player(name, 1000);

        }


    }
    public void onClickMenu(View view){
        finish();
    }

    public void Deal(View view){
        EditText etBet = (EditText)findViewById(R.id.etBet);
        Button btnDeal = (Button)findViewById(R.id.btnDeal);
        TextView tvPlaceBet = (TextView)findViewById(R.id.lblPlaceBet);
        LinearLayout layout_playing = (LinearLayout)findViewById(R.id.layout_playing);
        LinearLayout layout_betting = (LinearLayout)findViewById(R.id.layout_Bet);
        LinearLayout layout_gameover = (LinearLayout)findViewById(R.id.layout_gameover);
        TextView r = (TextView) findViewById(R.id.result);

        //set bet value
        String bet = etBet.getText().toString();

        if (!bet.isEmpty() && Integer.parseInt(bet) > 0){

            //clear result text
            r.setText("");
            tvPlaceBet.setText("Bet: "+bet);

            //Remove buttons and labels
            layout_gameover.setVisibility(View.GONE);
            layout_betting.setVisibility(View.GONE);

            //add other buttons
            layout_playing.setVisibility(View.VISIBLE);

            display_dealer = "";
            display_player = "";

            updateHandDisplays();
            updateBalance();
            GameLoop(view);
        }

        else {
            r.setText("Please enter a valid Bet");
        }



    }

    // This makes player draw another card and updates the totals
    public void Hit(View view){
        drawCardPlayer();

        // Show the new hand
        display_player = player.getHandAsString();
        updateHandDisplays();

        // If player gets 21 or busted, end game
        if(player.getTotal() >= 21){
            getResult();
        }

    }

    public void Stand(View view) throws InterruptedException {
        // Show the dealer's hand
        display_dealer = dealer.getHandAsString();
        updateHandDisplays();

        // TODO REPLACE WITH AN ANIMATION
        TimeUnit.SECONDS.sleep(1);

        // Have the dealer draw until they are done
        while(!isDealerDone()){
            drawCardDealer();

            // Show the Dealer's new hand
            display_dealer = dealer.getHandAsString();
            updateHandDisplays();

            // TODO REPLACE WITH AN ANIMATION
            TimeUnit.SECONDS.sleep(1);
        }

        // End the game
        getResult();
    }

    public void onPlay(View view){
        // Reset display strings
        display_dealer = "";
        display_player = "";
        updateHandDisplays();

        // Swap visibility of layouts, we are now accepting bets
        //RelativeLayout playing = (RelativeLayout) findViewById(R.id.layout_playing);
        //LinearLayout betting = (LinearLayout) findViewById(R.id.layout_betting);
       // RelativeLayout waiting = (RelativeLayout) findViewById(R.id.layout_waiting);

       // playing.setVisibility(View.GONE);
       // betting.setVisibility(View.VISIBLE);
        //waiting.setVisibility(View.GONE);
    }
    public void GameLoop(View view){
        EditText b = (EditText) findViewById(R.id.etBet);
        this.bet = Double.parseDouble(b.getText().toString());

        // Records where in the deck we are...
        index = 0;

        // Prepping the game...
        deck.shuffle();
        player.newHand();
        dealer.newHand();
        drawHand();

        // Show the new hands
        display_dealer = dealer.getShowCard();
        display_player = player.getHandAsString();
        updateHandDisplays();
//        // Swap visibility of layouts
        LinearLayout playing = (LinearLayout) findViewById(R.id.layout_playing);
        LinearLayout betting = (LinearLayout) findViewById(R.id.layout_Bet);

        // Determine what to show
        //  If the user and the dealer did not get 21, start playing the game
        if(dealer.getTotal() != 21 && player.getTotal() != 21) {
            playing.setVisibility(View.VISIBLE);
            betting.setVisibility(View.GONE);
        }
      //   Otherwise, finish out the round
        else{
            getResult();
        }
    }

    // Returns to MainActivity
    public void onBack(View view){
        // Clear the player's hand
        player.newHand();

        Intent i = new Intent();
        i.putExtra("player_return", this.player);

        setResult(RESULT_OK, i);

        // Return to MainActivity
        finish();
    }


    // This method is used to finish out a round. It determines who won and distributes money accordingly.
    public void getResult(){
        TextView r = (TextView) findViewById(R.id.result);
        TextView dealerhand = (TextView) findViewById(R.id.display_dealer_hand);

        String result;

        // Show the final hands
        display_dealer = dealer.getHandAsString();
        display_player = player.getHandAsString();
        updateHandDisplays();

        if(player.getTotal() > 21){
            result = "You lose :(";
            player.withdraw(bet);
        }
        else if(dealer.getTotal() > 21){
            result = "You win!";
            player.deposit(bet);
        }
        else if(player.getTotal() == dealer.getTotal()){
            result = "Draw!";
        }
        else if(dealer.getTotal() > player.getTotal()){
            result = "You lose :(";
            player.withdraw(bet);
        }
        // else if dealer.getTotal() < player.getTotal()
        else{
            result = "You win!";
            player.deposit(bet);
        }

        r.setText(result);
        updateBalance();
        dealerhand.setText("("+dealer.getTotal()+")");
        // Swap visibility of layouts
        LinearLayout playing = (LinearLayout) findViewById(R.id.layout_playing);
        LinearLayout betting = (LinearLayout) findViewById(R.id.layout_Bet);
        LinearLayout gameover = (LinearLayout) findViewById(R.id.layout_gameover);

        playing.setVisibility(View.GONE);
        betting.setVisibility(View.GONE);
        gameover.setVisibility(View.VISIBLE);


    }
    public void drawHand(){
        for(int i = 0; i < 2; i++){
            drawCardPlayer();
            drawCardDealer();
        }
    }

    public void drawCardPlayer(){
        player.drawCard(deck.getCard(index));
        index += 1;
    }

    public void drawCardDealer(){
        dealer.drawCard(deck.getCard(index));
        index += 1;
    }


    // Checks to see if the dealer is done drawing
    //  Once the dealer has 17 or more points, the dealer is done.
    public boolean isDealerDone() {
        TextView textView_dealer = (TextView) findViewById(R.id.display_dealer_hand);

        int total = dealer.getTotal();
        if (total >= 17) {
            return true;
        }
        else{
            return false;
        }

    }

    // This is used to modify the display of the player's hands
    public void updateHandDisplays(){
        TextView textView_dealer = (TextView) findViewById(R.id.display_dealer_hand);
        TextView textView_player = (TextView) findViewById(R.id.display_player_hand);


        ConstraintLayout layout_game = findViewById(R.id.game);

        ImageView DealerCard1 = findViewById(R.id.dealerCard1);
        ImageView DealerCard2 = findViewById(R.id.dealerCard2);
        ImageView DealerCard3 = findViewById(R.id.dealerCard3);
        ImageView DealerCard4 = findViewById(R.id.dealerCard4);

        ImageView PlayerCard1 = findViewById(R.id.playerCard1);
        ImageView PlayerCard2 = findViewById(R.id.playerCard2);
        ImageView PlayerCard3 = findViewById(R.id.playerCard3);
        ImageView PlayerCard4 = findViewById(R.id.playerCard4);

        String[] playerCards;
        playerCards = display_player.split("[,]",0);
        String[] dealerCards = display_dealer.split("[,]",0);

        clearCards();

        System.out.print(dealerCards.length);
        for (int i=0;i<dealerCards.length;i++){
            System.out.println(dealerCards[i]);
        }

        setCards(playerCards.length,dealerCards.length,playerCards, dealerCards);


        textView_dealer.setText("");
        textView_player.setText(playerCards[0]);
    }
    public void clearCards(){
        ImageView DealerCard1 = findViewById(R.id.dealerCard1);
        ImageView DealerCard2 = findViewById(R.id.dealerCard2);
        ImageView DealerCard3 = findViewById(R.id.dealerCard3);
        ImageView DealerCard4 = findViewById(R.id.dealerCard4);
        ImageView DealerCard5 = findViewById(R.id.dealerCard5);
        ImageView DealerCard6 = findViewById(R.id.dealerCard6);
        ImageView DealerCard7 = findViewById(R.id.dealerCard7);

        ImageView PlayerCard1 = findViewById(R.id.playerCard1);
        ImageView PlayerCard2 = findViewById(R.id.playerCard2);
        ImageView PlayerCard3 = findViewById(R.id.playerCard3);
        ImageView PlayerCard4 = findViewById(R.id.playerCard4);
        ImageView PlayerCard5 = findViewById(R.id.playerCard5);
        ImageView PlayerCard6 = findViewById(R.id.playerCard6);
        ImageView PlayerCard7 = findViewById(R.id.playerCard7);


        DealerCard1.setVisibility(View.INVISIBLE);
        DealerCard2.setVisibility(View.INVISIBLE);
        DealerCard3.setVisibility(View.INVISIBLE);
        DealerCard4.setVisibility(View.INVISIBLE);
        DealerCard5.setVisibility(View.INVISIBLE);
        DealerCard6.setVisibility(View.INVISIBLE);
        DealerCard7.setVisibility(View.INVISIBLE);


        PlayerCard1.setVisibility(View.INVISIBLE);
        PlayerCard2.setVisibility(View.INVISIBLE);
        PlayerCard3.setVisibility(View.INVISIBLE);
        PlayerCard4.setVisibility(View.INVISIBLE);
        PlayerCard5.setVisibility(View.INVISIBLE);
        PlayerCard6.setVisibility(View.INVISIBLE);
        PlayerCard7.setVisibility(View.INVISIBLE);


    }
    public void setCards(int playerCount, int dealerCount, String[] playerCards, String[] dealerCards) {
        ImageView DealerCard1 = findViewById(R.id.dealerCard1);
        ImageView DealerCard2 = findViewById(R.id.dealerCard2);
        ImageView DealerCard3 = findViewById(R.id.dealerCard3);
        ImageView DealerCard4 = findViewById(R.id.dealerCard4);
        ImageView DealerCard5 = findViewById(R.id.dealerCard5);
        ImageView DealerCard6 = findViewById(R.id.dealerCard6);
        ImageView DealerCard7 = findViewById(R.id.dealerCard7);


        ImageView PlayerCard1 = findViewById(R.id.playerCard1);
        ImageView PlayerCard2 = findViewById(R.id.playerCard2);
        ImageView PlayerCard3 = findViewById(R.id.playerCard3);
        ImageView PlayerCard4 = findViewById(R.id.playerCard4);
        ImageView PlayerCard5 = findViewById(R.id.playerCard5);
        ImageView PlayerCard6 = findViewById(R.id.playerCard6);
        ImageView PlayerCard7 = findViewById(R.id.playerCard7);


        switch(playerCount){
            case 3:
                PlayerCard1.setImageResource(getResources().getIdentifier(playerCards[1], "drawable", "com.example.blackjack"));
                PlayerCard2.setImageResource(getResources().getIdentifier(playerCards[2],"drawable","com.example.blackjack"));
                PlayerCard1.setVisibility(View.VISIBLE);
                PlayerCard2.setVisibility(View.VISIBLE);
                break;
            case 4:
                PlayerCard3.setImageResource(getResources().getIdentifier(playerCards[3],"drawable","com.example.blackjack"));
                PlayerCard1.setVisibility(View.VISIBLE);
                PlayerCard2.setVisibility(View.VISIBLE);
                PlayerCard3.setVisibility(View.VISIBLE);
                break;
            case 5:
                PlayerCard4.setImageResource(getResources().getIdentifier(playerCards[4],"drawable","com.example.blackjack"));
                PlayerCard1.setVisibility(View.VISIBLE);
                PlayerCard2.setVisibility(View.VISIBLE);
                PlayerCard3.setVisibility(View.VISIBLE);
                PlayerCard4.setVisibility(View.VISIBLE);
                break;
            case 6:
                PlayerCard5.setImageResource(getResources().getIdentifier(playerCards[5],"drawable","com.example.blackjack"));
                PlayerCard1.setVisibility(View.VISIBLE);
                PlayerCard2.setVisibility(View.VISIBLE);
                PlayerCard3.setVisibility(View.VISIBLE);
                PlayerCard4.setVisibility(View.VISIBLE);
                PlayerCard5.setVisibility(View.VISIBLE);
                break;
            case 7:
                PlayerCard6.setImageResource(getResources().getIdentifier(playerCards[6],"drawable","com.example.blackjack"));
                PlayerCard1.setVisibility(View.VISIBLE);
                PlayerCard2.setVisibility(View.VISIBLE);
                PlayerCard3.setVisibility(View.VISIBLE);
                PlayerCard4.setVisibility(View.VISIBLE);
                PlayerCard5.setVisibility(View.VISIBLE);
                PlayerCard6.setVisibility(View.VISIBLE);
                break;
            case 8:
                PlayerCard7.setImageResource(getResources().getIdentifier(playerCards[7],"drawable","com.example.blackjack"));
                PlayerCard1.setVisibility(View.VISIBLE);
                PlayerCard2.setVisibility(View.VISIBLE);
                PlayerCard3.setVisibility(View.VISIBLE);
                PlayerCard4.setVisibility(View.VISIBLE);
                PlayerCard5.setVisibility(View.VISIBLE);
                PlayerCard6.setVisibility(View.VISIBLE);
                PlayerCard7.setVisibility(View.VISIBLE);
                break;
        }

        switch(dealerCount){
            case 1:
                DealerCard1.setImageResource(getResources().getIdentifier(dealerCards[0], "drawable", "com.example.blackjack"));
                DealerCard1.setVisibility(View.VISIBLE);
                break;
            case 2:
                DealerCard1.setImageResource(getResources().getIdentifier(dealerCards[0], "drawable", "com.example.blackjack"));
                DealerCard2.setImageResource(getResources().getIdentifier(dealerCards[1], "drawable", "com.example.blackjack"));

                DealerCard1.setVisibility(View.VISIBLE);
                DealerCard2.setVisibility(View.VISIBLE);
                break;
            case 3:
                System.out.println("test: "+dealerCards[1]);
                DealerCard1.setImageResource(getResources().getIdentifier(dealerCards[1], "drawable", "com.example.blackjack"));
                DealerCard2.setImageResource(getResources().getIdentifier(dealerCards[2], "drawable", "com.example.blackjack"));

                DealerCard1.setVisibility(View.VISIBLE);
                DealerCard2.setVisibility(View.VISIBLE);
                break;
            case 4:
                DealerCard1.setImageResource(getResources().getIdentifier(dealerCards[1], "drawable", "com.example.blackjack"));
                DealerCard2.setImageResource(getResources().getIdentifier(dealerCards[2], "drawable", "com.example.blackjack"));
                DealerCard3.setImageResource(getResources().getIdentifier(dealerCards[3], "drawable", "com.example.blackjack"));

                DealerCard1.setVisibility(View.VISIBLE);
                DealerCard2.setVisibility(View.VISIBLE);
                DealerCard3.setVisibility(View.VISIBLE);
                break;
            case 5:
                DealerCard1.setImageResource(getResources().getIdentifier(dealerCards[1], "drawable", "com.example.blackjack"));
                DealerCard2.setImageResource(getResources().getIdentifier(dealerCards[2], "drawable", "com.example.blackjack"));
                DealerCard3.setImageResource(getResources().getIdentifier(dealerCards[3], "drawable", "com.example.blackjack"));
                DealerCard4.setImageResource(getResources().getIdentifier(dealerCards[4], "drawable", "com.example.blackjack"));

                DealerCard1.setVisibility(View.VISIBLE);
                DealerCard2.setVisibility(View.VISIBLE);
                DealerCard3.setVisibility(View.VISIBLE);
                DealerCard4.setVisibility(View.VISIBLE);
                break;
            case 6:
                DealerCard1.setImageResource(getResources().getIdentifier(dealerCards[1], "drawable", "com.example.blackjack"));
                DealerCard2.setImageResource(getResources().getIdentifier(dealerCards[2], "drawable", "com.example.blackjack"));
                DealerCard3.setImageResource(getResources().getIdentifier(dealerCards[3], "drawable", "com.example.blackjack"));
                DealerCard4.setImageResource(getResources().getIdentifier(dealerCards[4], "drawable", "com.example.blackjack"));
                DealerCard5.setImageResource(getResources().getIdentifier(dealerCards[5], "drawable", "com.example.blackjack"));

                DealerCard1.setVisibility(View.VISIBLE);
                DealerCard2.setVisibility(View.VISIBLE);
                DealerCard3.setVisibility(View.VISIBLE);
                DealerCard4.setVisibility(View.VISIBLE);
                DealerCard5.setVisibility(View.VISIBLE);
                break;
            case 7:
                DealerCard1.setImageResource(getResources().getIdentifier(dealerCards[1], "drawable", "com.example.blackjack"));
                DealerCard2.setImageResource(getResources().getIdentifier(dealerCards[2], "drawable", "com.example.blackjack"));
                DealerCard3.setImageResource(getResources().getIdentifier(dealerCards[3], "drawable", "com.example.blackjack"));
                DealerCard4.setImageResource(getResources().getIdentifier(dealerCards[4], "drawable", "com.example.blackjack"));
                DealerCard5.setImageResource(getResources().getIdentifier(dealerCards[5], "drawable", "com.example.blackjack"));
                DealerCard6.setImageResource(getResources().getIdentifier(dealerCards[6], "drawable", "com.example.blackjack"));

                DealerCard1.setVisibility(View.VISIBLE);
                DealerCard2.setVisibility(View.VISIBLE);
                DealerCard3.setVisibility(View.VISIBLE);
                DealerCard4.setVisibility(View.VISIBLE);
                DealerCard5.setVisibility(View.VISIBLE);
                DealerCard6.setVisibility(View.VISIBLE);
                break;
            case 8:
                DealerCard1.setImageResource(getResources().getIdentifier(dealerCards[1], "drawable", "com.example.blackjack"));
                DealerCard2.setImageResource(getResources().getIdentifier(dealerCards[2], "drawable", "com.example.blackjack"));
                DealerCard3.setImageResource(getResources().getIdentifier(dealerCards[3], "drawable", "com.example.blackjack"));
                DealerCard4.setImageResource(getResources().getIdentifier(dealerCards[4], "drawable", "com.example.blackjack"));
                DealerCard5.setImageResource(getResources().getIdentifier(dealerCards[5], "drawable", "com.example.blackjack"));
                DealerCard6.setImageResource(getResources().getIdentifier(dealerCards[6], "drawable", "com.example.blackjack"));
                DealerCard7.setImageResource(getResources().getIdentifier(dealerCards[7], "drawable", "com.example.blackjack"));

                DealerCard1.setVisibility(View.VISIBLE);
                DealerCard2.setVisibility(View.VISIBLE);
                DealerCard3.setVisibility(View.VISIBLE);
                DealerCard4.setVisibility(View.VISIBLE);
                DealerCard5.setVisibility(View.VISIBLE);
                DealerCard6.setVisibility(View.VISIBLE);
                DealerCard7.setVisibility(View.VISIBLE);
                break;
        }
    }

    // This method updates the balance display
    public void updateBalance(){
        TextView b = (TextView) findViewById(R.id.tvBalance);
        String balance = String.valueOf(player.getBalance());
        b.setText("$"+balance);

    }
}
