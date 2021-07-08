package com.example.blackjack.Game;

import com.example.blackjack.Game.Deck.Deck;
import com.example.blackjack.Game.Players.Dealer;
import com.example.blackjack.Game.Players.Hand;
import com.example.blackjack.Game.Players.Player;

import java.util.concurrent.TimeUnit;
import java.util.Scanner;

public class Game{
    Deck deck = new Deck();
    Dealer dealer = new Dealer();
    Player player;

    public void menu(Player player) throws InterruptedException {
        this.player = player;
        Scanner scan = new Scanner(System.in);
        boolean playing = true;

        while(playing) {
            System.out.println("\n");
            player.printBalance();
            System.out.println("Please select from the menu... ");
            System.out.println("\tPlay - 1");
            System.out.println("\tExit - 2");

            int menuChoice = scan.nextInt();

            switch (menuChoice) {
                case 1:
                    System.out.println("\n\n");
                    gameLoop();
                    break;
                case 2:
                    playing = false;
                    break;
                default:
                    System.out.println("\n\nError in your input. Please select an option from the menu...");
            }

        }
    }




    public void gameLoop() throws InterruptedException {
        // Declaring variables
        Scanner scan = new Scanner(System.in);
        boolean trying = true;
        boolean busted = false;

        System.out.println("How much will you bet?");
        System.out.print("Enter Amount: $");
        int bet = scan.nextInt();

        // Records where in the deck we are...
        int index = 0;

        // Prepping the game...
        deck.shuffle();
        player.newHand();
        dealer.newHand();

        for(int i = 0; i < 2; i++){
            player.drawCard(deck.getCard(index));
            index += 1;

            dealer.drawCard(deck.getCard(index));
            index += 1;
        }

        dealer.showCard();
        player.printHand();

        if(dealer.getTotal() != 21 && player.getTotal() != 21) {
            while (trying) {

                System.out.println("Draw = 1, Hold = 2");
                int choice = scan.nextInt();
                switch (choice) {
                    case 1:
                        TimeUnit.SECONDS.sleep(2);
                        player.drawCard(deck.getCard(index));
                        index += 1;


                        dealer.showCard();
                        player.printHand();

                        if (player.getTotal() > 21) {
                            System.out.println("Bust!");
                            trying = false;
                            busted = true;
                        }
                        else if (player.getTotal() == 21) {
                            System.out.println("Blackjack!!!");
                            trying = false;
                            TimeUnit.SECONDS.sleep(1);
                        }

                        break;

                    case 2:
                        trying = false;
                        break;

                    default:
                        System.out.println("Error, please select one of the given options...");
                }

                TimeUnit.SECONDS.sleep(1);
            }

            // Now it is the dealers turn to try and get to 21
            trying = true;

            // If the user already busted, no need to draw
            while (trying && !busted) {
                if (dealer.getTotal() > 21){
                    System.out.println("The dealer busted!");
                    trying = false;
                }
                // If the dealer already has more points than the user, there is no need to draw
                else if (dealer.getTotal() > player.getTotal()) {
                    trying = false;
                }
                else if (dealer.getTotal() == 21){
                    System.out.println("Black Jack!");
                }
                // If the dealer already has more than 17, the dealer is done drawing.
                else if (dealer.getTotal() > 17){
                    trying = false;
                }
                // Otherwise, the dealer must draw until they have at least 17
                else{
                    System.out.println("\nThe dealer is drawing...");
                    TimeUnit.SECONDS.sleep(1);

                    dealer.drawCard(deck.getCard(index));
                    index += 1;

                    dealer.printHand();
                    player.printHand();
                }

                TimeUnit.SECONDS.sleep(1);
            }

        }
        else{
            System.out.println("Black Jack!!!");
            TimeUnit.SECONDS.sleep(1);
        }

        System.out.println("\nFinal Hand:");
        dealer.printHand();
        player.printHand();

        TimeUnit.SECONDS.sleep(1);
        if(player.getTotal() > 21){
            System.out.println("You lose :(");
            player.withdraw(bet);
        }
        else if(dealer.getTotal() > 21){
            System.out.println("You win!");
            player.deposit(bet);
        }
        else if(player.getTotal() == dealer.getTotal()){
            System.out.println("Draw!");
        }
        else if(dealer.getTotal() > player.getTotal()){
            System.out.println("You lose :(");
            player.withdraw(bet);
        }
        else if(dealer.getTotal() < player.getTotal()){
            System.out.println("You win!");
            player.deposit(bet);
        }

    }

}
