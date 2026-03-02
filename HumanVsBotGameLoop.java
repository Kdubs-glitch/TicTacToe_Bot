import java.util.Random;
import java.util.Scanner;

public class HumanVsBotGameLoop {

    public static String getMove(String curPlayer, Scanner scanner){

        String curMove = "";
        String formattedString = String.format("Where would %s like to move:          ", curPlayer);
        System.out.println(formattedString);
        curMove = scanner.nextLine();

        return(curMove);
    }


    public static void showBoard(String[][] curBoard){
        for (int i = 0; i < 3; i++){
            System.out.printf("\t %1s | %1s | %1s \n", curBoard[i][0], curBoard[i][1], curBoard[i][2]); // %1s reserves 1 space if [i][j] = ""
            if (i < 2){
                System.out.println("\t-----------");
            }
        }
    }


    public static String checkRows(String[][] curBoard, String[] playerNames){
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
                return playerNames[0];
            } else if (player2C == 3){
                return playerNames[1];
            }

        }
        return "No Win";
    }


    public static String checkCols(String[][] curBoard, String[] playerNames){
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
                return playerNames[0];
            } else if (player2C == 3){
                return playerNames[1];
            }
        }
        return "No Win";
    }


    public static String checkDiagonal(String[][] curBoard, String[] playerNames){
        if (curBoard[0][0].equals("X") && curBoard[1][1].equals("X") && curBoard[2][2].equals("X")){
            return playerNames[0];
        }
        else if (curBoard[0][0].equals("O") && curBoard[1][1].equals("O") && curBoard[2][2].equals("O")){
            return playerNames[1];
        }

        if (curBoard[0][2].equals("X") && curBoard[1][1].equals("X") && curBoard[2][0].equals("X")){
            return playerNames[0];
        }
        else if (curBoard[0][2].equals("O") && curBoard[1][1].equals("O") && curBoard[2][0].equals("O")){
            return playerNames[1];
        }
        return "No Win";
    }


    public static String checkWin(String[][] curBoard, String[] playerNames){
        String winner = checkRows(curBoard, playerNames);
        if (!winner.equals("No Win")){
            return winner;
        }
        winner = checkCols(curBoard, playerNames);
        if (!winner.equals("No Win")){
            return winner;
        }
        winner = checkDiagonal(curBoard, playerNames);
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


    public static void updateBoard(String[][] curBoardState, String position, String curPlayer, String playerOne){ 
        
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
        
        if (curPlayer.equals(playerOne)){
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


    public static String game_Loop(Player player_One, RobotV2 player_Two, Scanner scanner){

        Random randint = new Random();
        String p1Name = player_One.get_Player_Num();

        String curPlayer = randint.nextInt(0, 2) == 0 ? p1Name : player_Two.getName();
        String playerOneName = curPlayer;
        String playerTwoName = curPlayer.equals(p1Name) ? player_Two.getName() : p1Name;
        String[] playerNames = {playerOneName, playerTwoName};

        String[][] board = {{"", "", ""}, {"", "", ""}, {"", "", ""}};
        String move = "";
        String whoWon = checkWin(board, playerNames);

        System.out.println("PLAYERS:\t" + playerOneName + " AND " + playerTwoName);
        if (curPlayer.equals(playerOneName)){
            move = getMove(curPlayer, scanner);
        } else{
            move = player_Two.get_Move(board, 0);
        }
        
        while (whoWon.equals("No Win")){
            while (is_Taken(board, move)){
                if (curPlayer.equals(playerOneName)){
                    move = getMove(curPlayer, scanner);
                } else{
                    move = player_Two.get_Move(board, 0);
                }
                System.out.println(curPlayer + "  CHOSE:  " + move);
                
            }
                
            updateBoard(board, move, curPlayer, playerOneName);
            showBoard(board);
            whoWon = checkWin(board, playerNames);

            if (curPlayer.equals(playerOneName)){
                curPlayer = playerTwoName;
            } else {
                curPlayer = playerOneName;
            }

        }
        System.out.println(whoWon + " WON!!");

        return whoWon;
    }
}

