import java.util.*;

class Game_HT {
  static int gm_Sze = 3;
  static int ai_score = 0;
  static int pl_score = 0;
  static Scanner in;

  public static void main(String args[]) {
    in = new Scanner(System.in);
    int gm_Matrix[][] = new int[gm_Sze][gm_Sze];
    instruc();
    Str_Game(gm_Matrix);
    print_Game(gm_Matrix);
    System.out.print("Best of ? ");
    int num_games = in.nextInt();
    gerir_Game(gm_Matrix, num_games);
  }

  public static int gerir_Game(int mtx[][], int num_games) {
    int player_won = 0;

    for (int i = 1; i <= num_games; i++) {
      int win_flag = 0;
      System.out.println("Game " + i);
      System.out.println("Score :");
      System.out.println("IA : "+ ai_score + " | P1 : " + pl_score);
      if (i!=1) {
        Str_Game(mtx);
      }
      if (ai_score > num_games/2) {
        break;
      } else if (pl_score > num_games/2) {
        break;
      }
      while (win_flag==0) {
        if (player_won == 0 || player_won == 2) {
          get_IA_Move(mtx, 1);
          if (checkWin(mtx) == 1) {
            player_won = 1;
            win_flag = 1;
            ai_score++;
            break;
          } else if (checkWin(mtx) == -1) {
            player_won = 2;
            win_flag = 1;
            pl_score++;
            break;
          } else if (checkWin(mtx) == 0) {
            win_flag = 1;
            player_won = 1;
            break;
          }
          get_Player_Move(mtx,0);
        } else {
          get_Player_Move(mtx,1);
          if (checkWin(mtx) == 1) {
            player_won = 1;
            win_flag = 1;
            ai_score++;
            break;
          } else if (checkWin(mtx) == -1) {
            player_won = 2;
            win_flag = 1;
            pl_score++;
            break;
          } else if (checkWin(mtx) == 0) {
            win_flag = 1;
            player_won = 0;
            break;
          }
          get_IA_Move(mtx, 0);
        }
      }
    }
    if (ai_score > pl_score) {
      System.out.println("Ai has won!");
      return ai_score;
    } else if (pl_score > ai_score) {
      System.out.println("Player has won!");
      return pl_score;
    }
    System.out.println("Tie!");
    return 0;
  }

  public static void Str_Game(int mtx[][]) {
    for (int x = 0; x < gm_Sze; x++) {
      for (int y = 0; y < gm_Sze; y++)
        mtx[x][y]= -1;
    }
  }

  public static void print_Game(int mtx[][]) {
    for (int x = 0; x < gm_Sze; x++) {
      for (int y = 0; y < gm_Sze; y++) {
        System.out.print("\t");
        if (mtx[x][y] == 0)
          System.out.print("O");
        else if (mtx[x][y] == 1)
          System.out.print("X");
        else
          System.out.print("_");
      }
      System.out.println();System.out.println();
    }
  }

  public static void instruc() {
    System.out.println("Welcome");
    System.out.println("Grid Coord: ");
    for (int i = 0; i < gm_Sze; i++) {
      for (int j = 0; j < gm_Sze; j++) {
        System.out.print("\t");
        System.out.print("("+i+","+j+")");
      }
      System.out.println();
    }
  }

  public static void get_IA_Move(int mtx[][], int str_flg) {
    System.out.println("AI G : ");
    int sv_i = 0; int sv_j = 0;
    int best_scr = Integer.MIN_VALUE;
    for (int i = 0; i < gm_Sze; i++) {
      for (int j = 0; j < gm_Sze; j++) {
        if (mtx[i][j] == -1) {
          mtx[i][j] = 1;
          int scr_now = minimax(mtx,0,false);
          mtx[i][j] = -1;
          if (scr_now > best_scr) {
            best_scr = scr_now;
            sv_i = i; sv_j = j;
          }
        }
      }
    }
    mtx[sv_i][sv_j] = 1;
    if (str_flg == 1)
      print_Game(mtx);
  }

  public static void get_Player_Move(int mtx[][], int str_flg) {
    boolean flg_invalid = false;

    while (!flg_invalid) {
      if (str_flg == 1)
        print_Game(mtx);
      System.out.println("Player");
      System.out.print("? ");
      int atk_x = in.nextInt();
      int atk_y = in.nextInt();
      if (mtx[atk_x][atk_y] == -1) {
        mtx[atk_x][atk_y] = 0;
        flg_invalid = true;
      } else {
        flg_invalid = false;
        System.out.println("Invalid, try again");
      }
    }
  }

  public static int checkWin(int mtx[][]) {
    int count_line_p1 = 0; int count_diag1_p1 = 0;
    int count_line_p2 = 0; int count_diag1_p2 = 0;

    int count_col_p1 = 0; int count_diag2_p1 = 0;
    int count_col_p2 = 0; int count_diag2_p2 = 0;

    boolean gameNotOver = false;

    for (int i = 0; i < gm_Sze; i++) { // Check line
      for (int j = 0; j < gm_Sze; j++) {
        if (mtx[j][i] == 1)
          count_line_p1++;
        else if (mtx[j][i] == 0)
          count_line_p2++;
        else if (mtx[j][i] == -1)
          gameNotOver = true;

        if (mtx[i][j] == 1)
          count_col_p1++;
        else if (mtx[i][j] == 0)
          count_col_p2++;
        else if (mtx[i][j] == -1)
          gameNotOver = true;
      }
      if (count_line_p1 == 3 || count_col_p1 == 3) {
        return 1;
      }
      if (count_line_p2 == 3 || count_col_p2 == 3) {
        return -1;
      }
      count_line_p1 = 0; count_line_p2 = 0;
      count_col_p1 = 0; count_col_p2 = 0;
    }

    for (int j = 0; j < gm_Sze; j++) {
        if (mtx[j][j] == 1)
          count_diag1_p1++;
        else if (mtx[j][j] == 0)
          count_diag1_p2++;
        else if (mtx[j][j] == -1)
          gameNotOver = true;

        if (mtx[j][2-j] == 1)
          count_diag2_p1++;
        else if (mtx[j][2-j] == 0)
          count_diag2_p2++;
        else if (mtx[j][2-j] == -1)
          gameNotOver = true;
    }

    if (count_diag1_p1 == 3 || count_diag2_p1 == 3) {
      return 1;
    }
    if (count_diag1_p2 == 3 || count_diag2_p2 == 3) {
      return -1;
    }
    if (gameNotOver) {
      return 9;
    }

    return 0;
  }

  public static int minimax(int mtx[][], int depth, boolean isMaximizing) {
    int result = checkWin(mtx);
    if (result != 9) {
      int score_end = result; // p1 - 1 | p2 - (-1) | tie - 0
      return score_end;
    }

    if (isMaximizing) { // Player 1
      int best_scr = Integer.MIN_VALUE;
      for (int i = 0; i < gm_Sze; i++) {
        for (int j = 0; j < gm_Sze; j++) {
          if (mtx[i][j] == -1) {
            mtx[i][j] = 1;
            int score = minimax(mtx,depth+1,false);
            mtx[i][j] = -1;
            best_scr = Math.max(score,best_scr);
          }
        }
      }
      return best_scr; // Best value
    } else {
      int best_scr = Integer.MAX_VALUE;
      for (int i = 0; i < gm_Sze; i++) {
        for (int j = 0; j < gm_Sze; j++) {
          if (mtx[i][j] == -1) {
            mtx[i][j] = 0;
            int score = minimax(mtx,depth+1,true);
            mtx[i][j] = -1;
            best_scr = Math.min(score,best_scr);
          }
        }
      }
      return best_scr;
    }
  }

}
