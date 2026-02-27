import java.util.Scanner;

public class PlayerVsBot {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Enter player's' name:\t");
        String playerName = scanner.nextLine();

        Player human = new Player(playerName);
        RobotV2 bot = TrainRobot.train_Bot();

        int humanWins = 0;
        int botWins = 0;
        for (int i = 0; i < 3; i++){
            String winner = HumanVsBotGameLoop.game_Loop(human, bot, scanner);
            if (winner.equals(bot.getName())){
                botWins++;
            } else if (winner.equals(playerName)){
                humanWins++;
            }
            
        }
        
        String whoWon = "";
        if (botWins > humanWins){
            whoWon = "Bot Wins";
        } else {
            whoWon = "Human Wins";
        }
        System.out.println(whoWon);

    }

}
