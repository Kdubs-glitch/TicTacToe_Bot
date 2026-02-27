import java.util.Random;

public class RobotV2 {
    private Random random = new Random();
    
    String name = "";
    int totalWins = 0;
    int totalLoses = 0;
    int numMovesMade = 0;

    // THESE HAVE TO MATCH INDEX WISE ALWAYS - fucking obviously 
    String[][] stateMemory = new String[1][9];
    String[][] moveMemory = new String[1][9];
    int[][] rewardMemory = new int[1][9];

    public RobotV2(String n){
        name = n;
    }

    public void update_Moves_Made(){
        numMovesMade++;
    }

    public void reset_Moves_Made(){
        numMovesMade = 0;
    }

    public void update_Total_Wins(){
        totalWins++;
    }

    public void update_Total_Loses(){
        totalLoses++;
    }

    public String getName() {
        return name;
    }

    public String[][] get_Move_Memory() {
        return moveMemory;
    }

    public int[][] get_Reward_Memory() {
        return rewardMemory;
    }

    public String[][] get_State_Memory() {
        return stateMemory;
    }

    public int get_Total_Loses() {
        return totalLoses;
    }

    public int get_Total_Wins() {
        return totalWins;
    }
    // Get a random move
    // Get a move by looking for a state that is the same as the current one
    // If that previous state's reward is 1 and it has a next move
    // the next move may lead to a win again

    // BEFORE MATCH
    public void fill_Reward_Mem(){
        int[][] newMem = new int[rewardMemory.length + 1][9];
        for (int i = 0; i < rewardMemory.length; i++){
            for (int k = 0; k < rewardMemory[i].length; k++){
                System.out.println(name + " REWARDS: " +"I:K " +i+" : "+k+"\t" +rewardMemory[i][k]);
            }
        }
        rewardMemory = newMem;
    } 


    public int decode_Move(String position){

        // position = (moveIndex[0] * 3) + moveIndex[1];
        int moveIndex = 0;
        String col = position.substring(1, 2);
        if (col.equals("1")){
            moveIndex += 6;
        } else if (col.equals("2")){
            moveIndex += 3;
        } else {
            moveIndex += 0;
        } 
        if (position.substring(0, 1).toLowerCase().equals("a")){
            moveIndex += 0;
        } else if (position.substring(0, 1).toLowerCase().equals("b")){
            moveIndex += 1;
        } else {
            moveIndex += 2;
        }
 
        return moveIndex;
    }

    //AFTER MATCH
    public void update_Reward_Mem(int reward){// Needs to give +-1 to all moves that contributed to the win
        // decode Moves for game, quick maths gives 1-9, reward mem +-= 1 at that position
        int gameIndex = moveMemory.length - 1;

        for (int move = 0; move < numMovesMade; move++){
            rewardMemory[gameIndex][move] = reward;
        }
        for (int i = 0; i < rewardMemory.length; i++){
            for (int k = 0; k < rewardMemory[i].length; k++){
                System.out.println(name + " REWARDS: " +"I:K " +i+" : "+k+"\t" +rewardMemory[i][k]);
            }
        }

    }

    // DURING MATCH 
    public void update_Move_Mem(String move){
        if (numMovesMade == 0){
            String[][] newMem = new String[moveMemory.length + 1][9];
            
            for (int game = 0; game < moveMemory.length; game++){
                newMem[game] = moveMemory[game];
            }
            newMem[newMem.length - 1][0] = move;
            moveMemory = newMem;
            for (int i = 0; i < moveMemory.length; i++){
                for (int k = 0; k < moveMemory[i].length; k++){
                    System.out.println(name + " I:K " +i+" : "+k+" MOVEMEM\t" +moveMemory[i][k]);
                }
            }
        } else{
            int gameIndex = moveMemory.length - 1;
            System.out.println("OMGGGG2"+moveMemory[gameIndex].length);
            moveMemory[gameIndex][numMovesMade] = move;
            for (int i = 0; i < moveMemory.length; i++){
                for (int k = 0; k < moveMemory[i].length; k++){
                    System.out.println(name + " I:K " +i+" : "+k+"MOVEMEM \t" +moveMemory[i][k]);
                }
            }

        }
        
    }

    // DURING MATCH
    public void update_State_Mem(String state){ // Combine update_State & update_Move
        if (numMovesMade == 0){
            String[][] newMem = new String[stateMemory.length + 1][9];
            for (int game = 0; game < stateMemory.length; game++){
                newMem[game] = stateMemory[game];
            }
            newMem[newMem.length - 1][0] = state;
            stateMemory = newMem;
            for (int i = 0; i < stateMemory.length; i++){
                for (int k = 0; k < stateMemory[i].length; k++){
                    System.out.println(name + " I:K " +i+" : "+k+" STATEMEM\t" +stateMemory[i][k]);
                }
            }
        } else {
            int gameIndex = stateMemory.length - 1;

            stateMemory[gameIndex][numMovesMade] = state;
            for (int i = 0; i < stateMemory.length; i++){
                for (int k = 0; k < stateMemory[i].length; k++){
                    System.out.println(name + " I:K " +i+" : "+k+" STATEMEM\t" +stateMemory[i][k]);
                }
            }
        }
    }
     

    public String get_Random_Move(String[][] board){
        String[] allMoves = {"a3", "b3", "c3", "a2", "b2", "c2", "a1", "b1", "c1"};
        String randMove = allMoves[random.nextInt(0,9)];

        while (is_Taken(board, randMove)){
            randMove = allMoves[random.nextInt(0,9)];
        }

        return randMove;
    }

    public int get_Amt_Of_Moves(String[][] board){
        int count = 0;
        for (int col = 0; col < 3; col++){
            for (int row = 0; row < 3; row++){
                if (!board[col][row].isBlank()){
                    count++;
                }
            }
        }
        return count;
    }

    public String encode_Board(String[][] board){
        String encoded_Board = "";

        for (int col = 0; col < 3; col++){
            for (int row = 0; row < 3; row++){
                if (board[col][row].isBlank()){
                    encoded_Board += "0";
                } else if (board[col][row].equals("X")) {
                    encoded_Board += "1";
                } else {
                    encoded_Board += "-1";
                }
            }
        }
        System.out.println("FOR STATE ENCODED BOARD: \t" + encoded_Board);
        return encoded_Board;
    }

    public Boolean is_Taken(String[][] curBoard, String position){
        int po1;
        int po2;

        String col = position.substring(1, 2);
        if (col.equals("1")){
            po1 = 2;
        } else if (col.equals("2")){
            po1 = 1;
        } else {
            po1 = 0;
        } 

        if (position.substring(0, 1).toLowerCase().equals("a")){
            po2 = 0;
        } else if (position.substring(0, 1).toLowerCase().equals("b")){
            po2 = 1;
        } else {
            po2 = 2;
        }

        if (curBoard[po1][po2].equals("")){
            return false;
        }
        return true;
    }

    public String get_Move(String[][] board, double e){
        String[] allMoves = {"a3", "b3", "c3", "a2", "b2", "c2", "a1", "b1", "c1"};
        // states = [["","",""],["","",""],["","",""]], "po1po2po3po4po5" = "a3b3c3a2b2"
        if (random.nextDouble() < e || stateMemory.length < 3){
            return get_Random_Move(board);
        }

        String encodedBoard = encode_Board(board);

        int similarity = 0;
        int bestCount = 0;
        String[] mostSimiliar = new String[0];
        int amtOfMovesInPreviouState = 0;
        int amtOfMovesInState = get_Amt_Of_Moves(board);
        for (int game = 0; game < stateMemory.length; game++){
            for (int move = 0; move < 9; move++){
                if (rewardMemory[game][move] < 0){ // LOOOOOKK
                    System.out.println("rewardMemory[game][move]:   " + rewardMemory[game][move]);
                    continue;
                }
                
                if (stateMemory[game][move] == null){
                    break;
                }
                String previousMove = stateMemory[game][move].substring(move, move + 1);
                String curMove = encodedBoard.substring(move, move + 1);
                amtOfMovesInPreviouState = !previousMove.isBlank() ? amtOfMovesInPreviouState++ : amtOfMovesInPreviouState;

                if (!curMove.isBlank() && curMove.equals(previousMove)){
                    similarity++;
                }
            }
            if (similarity > bestCount && amtOfMovesInPreviouState > amtOfMovesInState){
                bestCount = similarity;
                mostSimiliar = stateMemory[game];
            }
        }
        
        int bestMove = -1;
        for (int index = amtOfMovesInState - 1; index < 9 - amtOfMovesInState; index++){
            System.out.print("Indecies: " + index + ", ");
            String curSubString = mostSimiliar[amtOfMovesInState].substring(index, index + 1);
            if (curSubString.equals("1")){
                bestMove = index;
            }
        }

        String bestMoveIsTaken = "";
        while (is_Taken(board, allMoves[bestMove])){
            bestMoveIsTaken = get_Move(board, e);
            System.out.println(bestMoveIsTaken);
        }
        if (!bestMoveIsTaken.isBlank()){
            return bestMoveIsTaken;
        }

        if (bestMove == -1){
            return get_Random_Move(board);
        }

        return allMoves[bestMove];
    }
}
