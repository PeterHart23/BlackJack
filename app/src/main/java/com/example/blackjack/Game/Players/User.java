package com.example.blackjack.Game.Players;


import com.example.blackjack.Game.Deck.Card;

import java.io.Serializable;

abstract class User implements Serializable {
    protected String name;
    protected double balance;
    protected Hand hand;

    public User(String name, double balance){
        this.name = name;
        this.balance = balance;
    }

    public User(String name){
        this.name = name;
        this.balance = balance;
    }


    public String getName(){
        return this.name;
    }

    public void newHand(){
        this.hand = new Hand();
    }

    public void drawCard(Card c){
        this.hand.drawCard(c);
    }

    public void printHand(){
        this.hand.printHand(this);
    }

    public String getHandAsString(){
        return this.hand.getHandString(this);
    }

    public int getTotal(){
        return this.hand.getTotal();
    }

}
