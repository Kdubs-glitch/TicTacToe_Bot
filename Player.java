
public class Player {
    
    private String player_Num = "";
    private int[] total_wins = new int[0];
    private String[][] every_Move_For_every_Game = new String[0][0];
    private int[][] every_Time_Player_Moved = new int[0][0];

    public Player(String pNum){
        player_Num = pNum;
    }

    public String[][] getEvery_Move_For_every_Game() {
        return every_Move_For_every_Game;
    }

    public int[][] getEvery_Time_Player_Moved() {
        return every_Time_Player_Moved;
    }

    public String get_Player_Num() {
        return player_Num;
    }

    public int get_Total_Wins() {
        int sum = 0;
        for (int i = 0; i < total_wins.length; i++){
            sum += total_wins[i];
        }
        return sum;
    }

    public int[] get_Wins(){
        return total_wins;
    }

    public void update_total_Wins(Boolean won, Boolean noWin){
        int[] tempArr = new int[total_wins.length + 1];
        for (int i = 0; i < total_wins.length; i++){
            tempArr[i] = total_wins[i];
        }

        total_wins = tempArr;
        if (noWin){
            total_wins[total_wins.length - 1] = -1;
        }
        if (won){
            total_wins[total_wins.length - 1] = 1;
        } else {
            total_wins[total_wins.length - 1] = 0;
        }
    }

    public void update_All_Moves(int[] all_Moves){
        int[][] tempArr = new int[every_Move_For_every_Game.length + 1][9];
        
        for(int i = 0; i < every_Move_For_every_Game.length; i++){
            tempArr[i] = every_Time_Player_Moved[i];
        }
        tempArr[tempArr.length - 1] = all_Moves;
        every_Time_Player_Moved = tempArr;
    }


    public void update_All_Actual_Moves(String[] all_Moves){
        String[][] tempArr = new String[every_Move_For_every_Game.length + 1][9];
        
        for(int i = 0; i < every_Move_For_every_Game.length; i++){
            tempArr[i] = every_Move_For_every_Game[i];
        }

        tempArr[tempArr.length - 1] = all_Moves;
        every_Move_For_every_Game = tempArr;
    }

}
