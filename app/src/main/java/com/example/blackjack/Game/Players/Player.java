package com.example.blackjack.Game.Players;

import java.io.Serializable;

public class Player extends User implements Serializable {
    public Player(String name, double balance) {
        super(name, balance);
    }

    public void withdraw(double amount){
        this.balance -= amount;
    }

    public void deposit(double amount){
        this.balance += amount;
    }

    public double getBalance(){
        return this.balance;
    }

    public void printBalance(){
        System.out.println("You currently have a balance of $" + String.format("%.2f", this.balance));
    }
}
