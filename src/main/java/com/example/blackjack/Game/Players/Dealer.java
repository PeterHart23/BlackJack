package com.example.blackjack.Game.Players;

import com.example.blackjack.Game.Deck.Card;

import java.io.Serializable;

public class Dealer extends User implements Serializable {
    public Dealer() {
        super("Dealer");
    }

    public void showCard(){
        int handSize = hand.getHandSize();

        //System.out.println("Dealer's hand: ");
        // Show the first card, keep others hidden
        Card c = hand.getCard(0);
        System.out.print("\t");
        //c.printCard();

        System.out.println("\n");
    }

    public String getShowCard(){
        String s = "";
        int handSize = hand.getHandSize();

        //s = s + "Dealer's hand: ";
        // Show the first card, keep others hidden
        Card c = hand.getCard(0);
        //s = s + "\t";
        s = s + c.getCardAsString();
        //     s = s + "\n";

        return s;
    }
}
