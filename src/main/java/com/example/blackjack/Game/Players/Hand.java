package com.example.blackjack.Game.Players;

import com.example.blackjack.Game.Deck.Card;

import java.io.Serializable;
import java.util.ArrayList;

public class Hand implements Serializable {
    private ArrayList<Card> hand;
    private int total = 0;

    public Hand(){
        this.hand = new ArrayList<Card>();
    }

    public void drawCard(Card card){
        this.hand.add(card);
        calculateTotal();
    }

    public void calculateTotal(){
        // Reset total
        total = 0;

        int numberOfAces = 0;

        // Add all of the card values together, saving the aces for the end
        for (Card c : hand) {
            int rank = c.getRankAsInt();

            if (rank == 1) {
                numberOfAces += 1;
            } else {
                total += Math.min(rank, 10);
            }
        }

        // If you have more than one ace, only one ace has the chance to be worth 11
        if(numberOfAces > 1){
            // Add all other aces to the total first
            total += numberOfAces - 1;
            // Then check to see if the last ace will be worth 11 or 1
            total += aceValue();
        }
        else if(numberOfAces == 1){
            // Check to see if the ace will be worth 11 or 1
            total += aceValue();
        }

    }

    public int aceValue(){
        int ace;
        if(total > 10){
            ace = 1;
        }
        else{
            ace = 11;
        }
        return ace;
    }

    public String getHandString(User user){
        String h = "";

       // h = h + user.getName() + "'s hand (" + user.getTotal() + "): \n";
        h = h + "("+ user.getTotal()+"),";
       // h = h + "\t";

        // Get every card in the hand except for the last one
        for (int i = 0; i < hand.size()-1; i++) {
            Card c = hand.get(i);
       //     h = h + "\t";
            h = h + c.getCardAsString();
            h = h + ",";
        }
        // Get final card in the hand
        Card c = hand.get(hand.size()-1);
     //   h = h + "\t";
        h = h + c.getCardAsString();

      //  h = h + "\n";

        return h;
    }

    public void printHand(User user){
        //System.out.println(user.getName() + "'s hand (" + user.getTotal() + "): ");
        System.out.print("\t");

        for (int i = 0; i < hand.size()-1; i++) {
            Card c = hand.get(i);
            c.printCard();
            System.out.print(", ");
        }
        Card c = hand.get(hand.size()-1);
        c.printCard();

        System.out.println("\n");
    }


    public Card getCard(int index){
        Card c = hand.get(index);
        return c;
    }

    public int getHandSize(){
        return hand.size();
    }

    public int getTotal(){
        return total;
    }

}
