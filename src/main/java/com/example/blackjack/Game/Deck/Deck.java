package com.example.blackjack.Game.Deck;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    ArrayList<Card> cards = new ArrayList<Card>();
    int deckSize = 52;

    public Deck(){
        // Generate a new deck
        for(int suit = 0; suit <= 3; suit++){
            for(int rank = 1; rank <= 13; rank++){
                cards.add(new Card(rank, suit));
            }
        }

        // Shuffle deck
        shuffle();
    }

    public void shuffle(){
        Collections.shuffle(cards);
    }

    public Card getCard(int index){
        return cards.get(index);
    }

}
