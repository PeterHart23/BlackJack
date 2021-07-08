package com.example.blackjack.Game.Deck;

public class Card {
    /*
        Clubs       -> 0
        Diamonds    -> 1
        Hearts      -> 2
        Spades      -> 3
     */
    private final int suit;
    private final String[] suits = {"clubs", "diamonds", "hearts", "spades"};

    /*
        Ace         -> 1
        Jack        -> 11
        Queen       -> 12
        King        -> 13
     */
    private final int rank;
    private final String[] ranks = {null, "a", "2", "3", "4", "5", "6",
            "7", "8", "9", "10", "j", "q", "k"};

    public Card(int rank, int suit){
        this.rank = rank;
        this.suit = suit;
    }

    public String getRankAsString(){
        return this.ranks[this.rank];
    }

    public String getCardImg(){return "card"+getSuitAsString()+getRankAsString();}

    public int getRankAsInt(){
        return this.rank;
    }

    public String getSuitAsString(){
        return this.suits[this.suit];
    }

    public String getCardAsString(){
      // return getRankAsString() + " of " + getSuitAsString();
       return "card"+getSuitAsString()+getRankAsString();
    }

    public int getSuitAsInt(){
        return this.suit;
    }

    public void printCard(){
        System.out.print(getRankAsString() + " of " + getSuitAsString());
    }


}
