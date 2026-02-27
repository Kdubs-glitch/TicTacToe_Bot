
public class TrainRobot {
    public static RobotV2 train_Bot(){
        
        RobotV2 bot1 = new RobotV2("Bot1");
        RobotV2 bot2 = new RobotV2("Bot2");
        RobotV2[] roboBitches = {bot1, bot2};

        int iter = 10;
        int n = 0;
        while (n < iter){

            String whoWon = RobotGameLoop.robo_Game_Loop(roboBitches);
            
            if (whoWon.equals("Player1")){
                bot1.update_Reward_Mem(1);
                bot2.update_Reward_Mem(-1);
                bot1.update_Total_Wins();
                bot2.update_Total_Loses();
            } else if (whoWon.equals("Player2")){
                bot1.update_Reward_Mem(-1);
                bot2.update_Reward_Mem(1);
                bot1.update_Total_Loses();
                bot2.update_Total_Wins();
            }

            n++;
        }

        RobotV2 bestRobot = bot1;
        if (bot1.get_Total_Wins() < bot2.get_Total_Wins()){
            bestRobot = bot2;
        }

        return bestRobot;
    }

    public static void main(String[] args){

        RobotV2 bestBot = train_Bot();

        System.out.println(bestBot.getName());
        System.out.println(bestBot.get_Total_Wins());
    }
}

