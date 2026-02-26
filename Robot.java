import java.util.Random;

public class Robot {
    
    private Random random = new Random();
    private String roboNum = "";
    private int[][] stateMemory = new int[0][9];
    private String[] moveMemory = new String[0];
    private int[] rewardMemory = new int[0];
    private double[] weights = new double[9];
    private int lastGameEndPosition = rewardMemory.length;
    private double activeEntropy = 0.0;

    public Robot(String pNum, double[] w){
        roboNum = pNum;
        weights = w;
    }


    public int[] getRewardMemory() {
        return rewardMemory;
    }

    public String[] getMove_Memory() {
        return moveMemory;
    }
    
    public int[][] getState_Memory(){
        return stateMemory;
    }

    public String getRoboNum() {
        return roboNum;
    }

    public int getTotal_Rewards(){
        int total = 0;
        for (int i = 0; i < rewardMemory.length; i++){
            total += rewardMemory[i];
        }

        return total;
    }


    public void setWeights(double[] weights) {
        this.weights = weights;
    }


    public double[] getWeights() {
        return weights;
    }


    public void update_last_position(){
        lastGameEndPosition = rewardMemory.length;
    }


    public void update_Move_Mem(String move){
        String[] newMem = new String[moveMemory.length + 1];

        for (int i = 0; i < moveMemory.length; i++){
            newMem[i] = moveMemory[i];
        }
        newMem[newMem.length - 1] = move; 

        moveMemory = newMem;
    }

    
    public void update_Reward_Mem(int reward){
        int[] newMem = new int[rewardMemory.length + 1];

        for (int i = 0; i < rewardMemory.length; i++){
            newMem[i] = rewardMemory[i];
        }
        newMem[newMem.length - 1] = reward;

        rewardMemory = newMem;
    }


    public void update_Reward_Placeholders(int reward){

        for (int i = rewardMemory.length - 1; i >= lastGameEndPosition; i--){
            rewardMemory[i] = reward;
        }
    }


    public void update_State_Mem(int[] state){
        int[][] newMem = new int[stateMemory.length + 1][9];

        for (int i = 0; i < stateMemory.length; i++){
            newMem[i] = stateMemory[i];
        }
        newMem[newMem.length - 1] = state;
        stateMemory = newMem;
    }


    public int[] convert_Move(String position){
        int[] move = new int[2];

        String col = position.substring(1, 2);
        if (col.equals("1")){
            move[0] = 2;
        } else if (col.equals("2")){
            move[0] = 1;
        } else {
            move[0] = 0;
        } 

        if (position.substring(0, 1).toLowerCase().equals("a")){
            move[1] = 0;
        } else if (position.substring(0, 1).toLowerCase().equals("b")){
            move[1] = 1;
        } else {
            move[1] = 2;
        }

        return move;
    }


    public void learn(double lr) {
        for (int game = 0; game < moveMemory.length; game++) {
            int[] moveIndex = convert_Move(moveMemory[game]);
            int position = (moveIndex[0] * 3) + moveIndex[1];
            weights[position] += lr * rewardMemory[game];
        }
    }


    public String most_Alike(String[][] board){
        int[] encodedBoard = new int[9];

        int index = 0;
        for (int col = 0; col < 3; col++){
            for (int row = 0; row < 3; row++){
                if (board[col][row].isBlank()){
                    encodedBoard[index] = 0;
                } else if (board[col][row].equals("X")) {
                    encodedBoard[index] = 1;
                } else {
                    encodedBoard[index] = -1;
                }
            }
        }

        int bestMove = -1;
        int similarity = 0;
        int bestSimilarity = 0;
        for (int game = 0; game < stateMemory.length; game++){
            if (rewardMemory[game] < 0){
                continue;
            }
            for (int move = 0; move < 9; move++){
                if (stateMemory[game][move] == encodedBoard[move]){
                    similarity++;
                }
            }
            if (similarity > bestSimilarity){
                bestSimilarity = similarity;
                bestMove = game;
            }
            similarity = 0;
        }

        if (bestMove == -1){
            return "-1";
        }

        return moveMemory[bestMove];
    }


    public String get_Random_Move(){
        String[] allMoves = {"a3", "b3", "c3", "a2", "b2", "c2", "a1", "b1", "c1"};

        return allMoves[random.nextInt(0, 9)];
    }

    public String get_Move(String[][] board, double entropy){
        String[] allMoves = {"a3", "b3", "c3", "a2", "b2", "c2", "a1", "b1", "c1"};

        if (random.nextDouble() < entropy){
            return get_Random_Move();
        }

        String moveFromAlikeBoard = most_Alike(board);
        if (!moveFromAlikeBoard.equals("-1")){
            return moveFromAlikeBoard;
        }

        double max = -99999;
        int index = 0;
        for (int i = 0; i < weights.length; i++){
            if (weights[i] > max){
                max = weights[i];
                index =i;
            }
        }

        double newMax = -99999;
        if (activeEntropy != entropy){
            for (int i = 0; i < weights.length; i++){
                if (weights[i] > newMax && weights[i] != max){
                    newMax = weights[i];
                    index = i;
                }
            }
        }

        activeEntropy = entropy;
        return allMoves[index];
    }

}
