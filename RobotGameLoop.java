import java.util.Random;

public class RobotGameLoop {


    public static void showBoard(String[][] curBoard){
        for (int i = 0; i < 3; i++){
            System.out.printf("\t %1s | %1s | %1s \n", curBoard[i][0], curBoard[i][1], curBoard[i][2]); // %1s reserves 1 space if [i][j] = ""
            if (i < 2){
                System.out.println("\t-----------");
            }
        }
    }


    public static String checkRows(String[][] curBoard){
        int player1C = 0;
        int player2C = 0;

        for (int i = 0; i < 3; i++){
            player1C = 0;
            player2C = 0;
            for (int k = 0; k < 3; k++){
                if (curBoard[i][k].equals("X")){
                    player1C++;
                }
                else if (curBoard[i][k].equals("O")){
                    player2C++;
                }
            }
            if (player1C == 3){
                return "Player1";
            } else if (player2C == 3){
                return "Player2";
            }

        }
        return "No Win";
    }


    public static String checkCols(String[][] curBoard){
        int player1C = 0;
        int player2C = 0;

        for (int i = 0; i < 3; i++){
            player1C = 0;
            player2C = 0;
            for (int k = 0; k < 3; k++){
                if (curBoard[k][i].equals("X")){
                    player1C++;
                }
                else if (curBoard[k][i].equals("O")){
                    player2C++;
                }
            }
            if (player1C == 3){
                return "Player1";
            } else if (player2C == 3){
                return "Player2";
            }
        }
        return "No Win";
    }


    public static String checkDiagonal(String[][] curBoard){
        if (curBoard[0][0].equals("X") && curBoard[1][1].equals("X") && curBoard[2][2].equals("X")){
            return "Player1";
        }
        else if (curBoard[0][0].equals("O") && curBoard[1][1].equals("O") && curBoard[2][2].equals("O")){
            return "Player2";
        }

        if (curBoard[0][2].equals("X") && curBoard[1][1].equals("X") && curBoard[2][0].equals("X")){
            return "Player1";
        }
        else if (curBoard[0][2].equals("O") && curBoard[1][1].equals("O") && curBoard[2][0].equals("O")){
            return "Player2";
        }
        return "No Win";
    }


    public static String checkWin(String[][] curBoard){
        String winner = checkRows(curBoard);
        if (!winner.equals("No Win")){
            return winner;
        }
        winner = checkCols(curBoard);
        if (!winner.equals("No Win")){
            return winner;
        }
        winner = checkDiagonal(curBoard);
        if (!winner.equals("No Win")){
            return winner;
        }
        if (check_Draw(curBoard)){
            return "Draw";
        }
        return "No Win";
    }


    public static Boolean check_Draw(String[][] curBoard){
        for (int i = 0; i < curBoard.length; i++){
            for (int k = 0; k < 3; k++){
                if (curBoard[i][k].equals("")){
                    return false;
                }
            }
        }
        return true;
    }


    public static void updateBoard(String[][] curBoardState, String position, String curPlayer){ 
        
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
        
        if (curPlayer.equals("Player1") || curPlayer.equals("Bot1")){
            curBoardState[po1][po2] = "X"; 
            
        } else{
            curBoardState[po1][po2] = "O";
        }
        
    }


    public static Boolean is_Taken(String[][] curBoard, String position){
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


    public static String encode_Board(String[][] board){
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
        System.out.println("ENCODED BOARD: \t" + encoded_Board);
        return encoded_Board;
    }


    public static String robo_Game_Loop(RobotV2[] players){
        Random randint = new Random();

        String[][] board = {{"", "", ""}, {"", "", ""}, {"", "", ""}};
        RobotV2 player_One = players[0];
        RobotV2 player_Two = players[1];
        String whoWon = checkWin(board);
        double entropy = 0.5;
 
        RobotV2 curPlayer = randint.nextInt(0, 2) == 0 ? player_One : player_Two;
        String move = "";


        player_One.fill_Reward_Mem();
        player_Two.fill_Reward_Mem();
        
        while (whoWon.equals("No Win")){
            String name = curPlayer.getName();

            move = curPlayer.get_Move(board, entropy);
            System.out.println(name + " CHOSE MOVE:   " + move);
            curPlayer.update_Move_Mem(move);
            
            
            System.out.println("PLAYER#: " + name + " CHOSE MOVE:  " + move);
            
            updateBoard(board, move, curPlayer.getName());
            
            curPlayer.update_State_Mem(encode_Board(board));
            curPlayer.update_Moves_Made(); // HERE BC STATE MEMORY INDEX GETS FUCKED

            showBoard(board);
            whoWon = checkWin(board);
            
            if (curPlayer.getName().equals("Bot1")){
                curPlayer = player_Two;
            } else {
                curPlayer = player_One;
            }

            entropy *= .99;

        }
        player_One.reset_Moves_Made();
        player_Two.reset_Moves_Made();
        
        if (whoWon.equals("Draw")){
            System.out.println("DRAW!!");
        } else{
            System.out.println(whoWon + " WON!!");
        }

        return whoWon;
       
    }
    
}