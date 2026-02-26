
public class TrainRobot {
    public static Robot train_Bot(){

        double[] bot1Weights = new double[9];
        double[] bot2Weights = new double[9];
        for (int i = 0; i < 9; i++){
            bot1Weights[i] = Math.random()*.2 - .1;
            bot2Weights[i] = Math.random()*.2 - .1;
        }

        Robot bot1 = new Robot("Bot1", bot1Weights);
        Robot bot2 = new Robot("Bot2", bot2Weights);
        Robot[] roboBitches = {bot1, bot2};

        int iter = 500;
        int n = 0;
        while (n < iter){

            bot1.update_last_position();
            bot2.update_last_position();
            String whoWon = RobotGameLoop.robo_Game_Loop(roboBitches);
            
            if (whoWon.equals("Player1")){
                bot1.update_Reward_Placeholders(1);
                bot2.update_Reward_Placeholders(-1);
                bot1.setWeights(apply_Reward(true, bot1.getWeights()));
                bot2.setWeights(apply_Reward(false, bot2.getWeights()));
            } else if (whoWon.equals("Player2")){
                bot2.update_Reward_Placeholders(1);
                bot1.update_Reward_Placeholders(-1);
                bot2.setWeights(apply_Reward(true, bot2.getWeights()));
                bot1.setWeights(apply_Reward(false, bot1.getWeights()));
            } else{
                bot1.update_Reward_Placeholders(0);
                bot2.update_Reward_Placeholders(0);
            }

            bot1.learn(.1);
            bot2.learn(.1);

            n++;
        }

        // int[] mem = bot1.getRewardMemory();
        // for (int i = 0; i < bot1.getRewardMemory().length; i++){
        //     System.out.print(mem[i]);
        // }

        Robot bestRobot = bot1;
        if (bot1.getTotal_Rewards() < bot2.getTotal_Rewards()){
            bestRobot = bot2;
        }

        return bestRobot;
    }


    public static double[] apply_Reward(Boolean won, double[] curWeights){
        double[] newWeights = curWeights.clone();

        for (int i = 0; i < 9; i++){
            if (won){
                newWeights[i] += 1;
            } else {
                newWeights[i] -= 1;
            }
        }

        return newWeights;
    }


    public static int[] encode_Board(String[][] board){
        int[] encoded_Board = new int[9];

        int index = 0;
        for (int col = 0; col < 3; col++){
            for (int row = 0; row < 3; row++){
                if (board[col][row].isBlank()){
                    encoded_Board[index] = 0;
                } else if (board[col][row].equals("X")) {
                    encoded_Board[index] = 1;
                } else {
                    encoded_Board[index] = -1;
                }
            }
        }

        return encoded_Board;
    }

    public static void main(String[] args){
        //ROBOT;
    }
}

