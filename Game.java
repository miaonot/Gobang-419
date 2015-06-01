package gobang;

//	有关游戏的变量和方法
class Game {
	//	用于记录线上线下选择用的变量
	static boolean isPvp;
	Player player1 = new Player("Player1");
	Player player2 = new Player("Player2");
	String[][] cheesboard = new String[19][19];
	int lastX = 0;
	int lastY = 0;
	int lasterX = 0;
	int lasterY = 0;
	String nowCheese = "X";
	int firstCheesePlayer = 1;
	AI ai = new AI();
	

	//	记录线上线下的选择
	public static void setIsPvp(boolean a) {
		isPvp = a;
	}
	public static boolean isPvp() {
		return isPvp;
	}


	//	初始化游戏
	public void init() {
		//		初始化棋盘
		for (int x = 0; x < 19; x ++) {
			for (int y = 0; y <19; y ++) {
				cheesboard[x][y] = null;
			}
		}

		//		初始化先手玩家
		if(firstCheesePlayer == 1) {
			firstCheesePlayer = 2;
		} else {
			firstCheesePlayer = 1;
		}

		//初始化棋盘
		nowCheese = "X";
	}
	//	落子
	public void putCheese(int x, int y)	{
		cheesboard[x][y] = nowCheese;
		lasterX = lastX;
		lasterY = lastY;
		lastX = x;
		lastY = y;
	}
	//	换手
	public void turnEnd() {
		if (nowCheese == "X") {
			nowCheese = "O";
		} else {
			nowCheese = "X";
		}
	}
	//	 判断当前棋子是否已经胜利
	public boolean judge() {
		for (int x = 0; x <19; x++) {
			for (int y = 0; y < 19; y++) {
				if (x + 4 < 19 && y + 4 < 19) {
					if (cheesboard[x][y] == nowCheese && cheesboard[x + 1][y + 1] == nowCheese && cheesboard[x + 2][y + 2] == nowCheese && cheesboard[x + 3][y + 3] == nowCheese && cheesboard[x + 4][y + 4] == nowCheese) {
						return true;
					}
				}
				if (x + 4 < 19) {
					if (cheesboard[x][y] == nowCheese && cheesboard[x + 1][y] == nowCheese && cheesboard[x + 2][y] == nowCheese && cheesboard[x + 3][y] == nowCheese && cheesboard[x + 4][y] == nowCheese) {
						return true;
					}
				}
				if (y + 4 < 19) {
					if (cheesboard[x][y] == nowCheese && cheesboard[x][y + 1] == nowCheese && cheesboard[x][y + 2] == nowCheese && cheesboard[x][y + 3] == nowCheese && cheesboard[x][y + 4] == nowCheese) {
						return true;
					}
				}
				if (x + 4 < 19 && y - 4 >= 0) {
					if (cheesboard[x][y] == nowCheese && cheesboard[x + 1][y - 1] == nowCheese && cheesboard[x + 2][y - 2] == nowCheese && cheesboard[x + 3][y - 3] == nowCheese && cheesboard[x + 4][y - 4] == nowCheese) {
						return true;
					}
				}
			}
		}
		return false;
	}

	//	判断当前棋子胜负之后的操作
	public void afterJudge() {
		if((firstCheesePlayer == 1 && nowCheese == "X") || (firstCheesePlayer == 2 && nowCheese == "O")) {
			player1.setWincount();
			player2.setLosecount();
		} else if ((firstCheesePlayer == 1 && nowCheese == "O") || (firstCheesePlayer == 2 && nowCheese == "X"))	{
			player1.setLosecount();
			player2.setWincount();
		}
	}

	//	悔棋
	public void withdraw() {
		cheesboard[lastX][lastY] = null;
	}

	//	认输
	public void giveUp() {
		player1.setLosecount();
		player2.setWincount();
	}

	class AI {		
		int temp = -99;
		int max = -99;
		private int[] xy = {9,9};
		private int i;//�����?
		private int t;//�����?
		private void changeXY() {
			if(max<temp)  {
				xy[0]=i;xy[1]=t;
				max=temp;
			}
		}
		//-2&-3
		private void lookFor1() {
			/*--------------------һ----------------------------------------*/
			if(t+1<19) {
				if(null!=cheesboard[i][t+1]) {
					if(cheesboard[i][t+1]==nowCheese) {//��
						temp = -2;
						changeXY();
					} else {
						temp = -3;
						changeXY();
					}
				}
			}
			if(t-1>=0) {
				if(null!=cheesboard[i][t-1]) {
					if(cheesboard[i][t-1]==nowCheese) {//��
						temp = -2;
						changeXY();
					} else {
						temp = -3;
						changeXY();
					}
				}
			}
			if(i+1<19) {
				if(null!=cheesboard[i+1][t]) {
					if(cheesboard[i+1][t]==nowCheese) {//��
						temp = -2;
						changeXY();
					} else {
						temp = -3;
						changeXY();
					}
				}
			}
			if(i-1>=0) {
				if(null!=cheesboard[i-1][t]) {
					if(cheesboard[i-1][t]==nowCheese) {//��
						temp = -2;
						changeXY();
					} else {
						temp = -3;
						changeXY();
					}
				}
			}
			if(i-1>=0&&t-1>=0) {
				if(null!=cheesboard[i-1][t-1]) {
					if(cheesboard[i-1][t-1]==nowCheese) {//�I
						temp = -2;
						changeXY();
					} else {
						temp = -3;
						changeXY();
					}
				}
			}
			if(i+1<19&&t+1<19) {
				if(null!=cheesboard[i+1][t+1]) {
					if(cheesboard[i+1][t+1]==nowCheese) {//�K
						temp = -2;
						changeXY();
					} else {
						temp = -3;
						changeXY();
					}
				}
			}
			if(i-1>=0&&t+1<19) {
				if(null!=cheesboard[i-1][t+1]) {
					if(cheesboard[i-1][t+1]==nowCheese) {//�J
						temp = -2;
						changeXY();
					} else {
						temp = -3;
						changeXY();
					}
				}
			}
			if(i+1<19&&t-1>=0) {
				if(null!=cheesboard[i+1][t-1]) {
					if(cheesboard[i+1][t-1]==nowCheese) {//�L
						temp = -2;
						changeXY();
					} else {
						temp = -3;
						changeXY();
					}
				}
			}
		}
		//0&-1

		private void lookFor2()  {
			/*-----------------------------��------------------------------------*/
			if(t+2<19) {
				if(null!=cheesboard[i][t+1]&&
						cheesboard[i][t+1]==cheesboard[i][t+2]) {
					if(cheesboard[i][t+1]==nowCheese) {//��
						temp = 0;
						changeXY();
					} else {
						temp = -1;
						changeXY();
					}
				}
			}
			if(t-2>=0) {
				if(null!=cheesboard[i][t-1]&&
						cheesboard[i][t-1]==cheesboard[i][t-2]) {
					if(cheesboard[i][t-1]==nowCheese) {//��
						temp = 0;
						changeXY();
					} else {
						temp = -1;
						changeXY();
					}
				}
			}
			if(i+2<19) {
				if(null!=cheesboard[i+1][t]&&
						cheesboard[i+1][t]==cheesboard[i+2][t]) {
					if(cheesboard[i+1][t]==nowCheese) {//��
						temp = 0;
						changeXY();
					} else {
						temp = -1;
						changeXY();
					}
				}
			}
			if(i-2>=0) {
				if(null!=cheesboard[i-1][t]&&
						cheesboard[i-1][t]==cheesboard[i-2][t]) {
					if(cheesboard[i-1][t]==nowCheese) {//��
						temp = 0;
						changeXY();
					} else {
						temp = -1;
						changeXY();
					}
				}
			}
			if(i-2>=0&&t-2>=0) {
				if(null!=cheesboard[i-1][t-1]&&
						cheesboard[i-1][t-1]==cheesboard[i-2][t-2]) {
					if(cheesboard[i][t]==nowCheese) {//�I
						temp = 0;
						changeXY();
					} else {
						temp = -1;
						changeXY();
					}
				}
			}
			if(i+2<19&&t+2<19) {
				if(null!=cheesboard[i+1][t+1]&&
						cheesboard[i+1][t+1]==cheesboard[i+2][t+2]) {
					if(cheesboard[i+1][t+1]==nowCheese) {//�K
						temp = 0;
						changeXY();
					} else {
						temp = -1;
						changeXY();
					}
				}
			}
			if(i-2>=0&&t+2<19) {
				if(null!=cheesboard[i-1][t+1]&&
						cheesboard[i-1][t+1]==cheesboard[i-2][t+2]) {
					if(cheesboard[i-1][t+1]==nowCheese) {//�J
						temp = 0;
						changeXY();
					} else {
						temp = -1;
						changeXY();
					}
				}
			}
			if(i+2<19&&t-2>=0) {
				if(null!=cheesboard[i+1][t-1]&&
						cheesboard[i+1][t-1]==cheesboard[i+2][t-2]) {
					if(cheesboard[i+1][t-1]==nowCheese) {//�L
						temp = 0;
						changeXY();
					} else {
						temp = -1;
						changeXY();
					}
				}
			}
			/*------------------------------һ��һ-------------------------------------------*/
			if(t+1<19&&t-1>=0) {
				if(null!=cheesboard[i][t+1]&&
						cheesboard[i][t+1]==cheesboard[i][t-1]) {
					if(cheesboard[i][t+1]==nowCheese) {
						temp = 0;
						changeXY();
					} else {
						temp = -1;
						changeXY();
					}
				}
			}
			if(i+1<19&&i-1>=0) {
				if(null!=cheesboard[i+1][t]&&
						cheesboard[i+1][t]==cheesboard[i-1][t]) {
					if(cheesboard[i+1][t]==nowCheese) {
						temp = 0;
						changeXY();
					} else {
						temp = -1;
						changeXY();
					}
				}
			}
			if(i-1>=0&&t-1>=0&&i+1<19&&t+1<19) {
				if(null!=cheesboard[i-1][t-1]&&
						cheesboard[i-1][t-1]==cheesboard[i+1][t+1]) {
					if(cheesboard[i-1][t-1]==nowCheese) {
						temp = 0;
						changeXY();
					} else {
						temp = -1;
						changeXY();
					}
				}
			}
			if(i-1>=0&&t+1<19&&i+1<19&&t-1>=0) {
				if(null!=cheesboard[i-1][t+1]&&
						cheesboard[i-1][t+1]==cheesboard[i+1][t-1]) {
					if(cheesboard[i-1][t+1]==nowCheese) {
						temp = 0;
						changeXY();
					} else {
						temp = -1;
						changeXY();
					}
				}
			}
		}
		//2&1
		private void lookFor3() {
			/*-----------------------------------��---------------------------------------------------------*/
			if(t+3<19) {
				if(null!=cheesboard[i][t+1]&&
						cheesboard[i][t+1]==cheesboard[i][t+2]&&
						cheesboard[i][t+2]==cheesboard[i][t+3]) {
					if(cheesboard[i][t+1]==nowCheese) {//��
						temp = 2;
						changeXY();
					} else {
						temp = 1;
						changeXY();
					}
				}
			}
			if(t-3>=0) {
				if(null!=cheesboard[i][t-1]&&
						cheesboard[i][t-1]==cheesboard[i][t-2]&&
						cheesboard[i][t-2]==cheesboard[i][t-3]) {
					if(cheesboard[i][t-1]==nowCheese) {//��
						temp = 2;
						changeXY();
					} else {
						temp = 1;
						changeXY();
					}
				}
			}
			if(i+3<19) {
				if(null!=cheesboard[i+1][t]&&
						cheesboard[i+1][t]==cheesboard[i+2][t]&&
						cheesboard[i+2][t]==cheesboard[i+3][t]) {
					if(cheesboard[i+1][t]==nowCheese) {//��
						temp = 2;
						changeXY();
					} else {
						temp = 1;
						changeXY();
					}
				}
			}
			if(i-3>=0) {
				if(null!=cheesboard[i-1][t]&&
						cheesboard[i-1][t]==cheesboard[i-2][t]&&
						cheesboard[i-2][t]==cheesboard[i-3][t]) {
					if(cheesboard[i-1][t]==nowCheese) {//��
						temp = 2;
						changeXY();
					} else {
						temp = 1;
						changeXY();
					}
				}
			}
			if(i-3>=0&&t-3>=0) {
				if(null!=cheesboard[i-1][t-1]&&
						cheesboard[i-1][t-1]==cheesboard[i-2][t-2]&&
						cheesboard[i-2][t-2]==cheesboard[i-3][t-3]) {
					if(cheesboard[i-1][t-1]==nowCheese) {//�I
						temp = 2;
						changeXY();
					} else {
						temp = 1;
						changeXY();
					}
				}
			}
			if(i+3<19&&t+3<19) {
				if(null!=cheesboard[i+1][t+1]&&
						cheesboard[i+1][t+1]==cheesboard[i+2][t+2]&&
						cheesboard[i+2][t+2]==cheesboard[i+3][t+3]) {
					if(cheesboard[i+1][t+1]==nowCheese) {//�K
						temp = 2;
						changeXY();
					} else {
						temp = 1;
						changeXY();
					}
				}
			}
			if(i-3>=0&&t+3<19) {
				if(null!=cheesboard[i-1][t+1]&&
						cheesboard[i-1][t+1]==cheesboard[i-2][t+2]&&
						cheesboard[i-2][t+2]==cheesboard[i-3][t+3]) {
					if(cheesboard[i-1][t+1]==nowCheese) {//�J
						temp = 2;
						changeXY();
					} else {
						temp = 1;
						changeXY();
					}
				}
			}
			if(i+3<19&&t-3>=0) {
				if(null!=cheesboard[i+1][t-1]&&
						cheesboard[i+1][t-1]==cheesboard[i+2][t-2]&&
						cheesboard[i+2][t-2]==cheesboard[i+3][t-3]) {
					if(cheesboard[i+1][t-1]==nowCheese) {//�L
						temp = 2;
						changeXY();
					} else {
						temp = 1;
						changeXY();
					}
				}
			}
			/*---------------------------һ�Ӷ�-----------------------------------------*/
			if(t+2<19&&t-1>=0) {
				if(cheesboard[i][t+1]!=null&&
						cheesboard[i][t+1]==cheesboard[i][t+2]&&
						cheesboard[i][t+2]==cheesboard[i][t-1]) {
					if(cheesboard[i][t+1]==nowCheese) {//��
						temp = 2;
						changeXY();
					} else {
						temp = 1;
						changeXY();
					}
				}
			}
			if(t-2>=0&&t+1<19) {
				if(null!=cheesboard[i][t-1]&&
						cheesboard[i][t-1]==cheesboard[i][t-2]&&
						cheesboard[i][t-2]==cheesboard[i][t+1]) {
					if(cheesboard[i][t-1]==nowCheese) {//��
						temp = 2;
						changeXY();
					} else {
						temp = 1;
						changeXY();
					}
				}
			}
			if(i+2<19&&i-1>=0) {
				if(null!=cheesboard[i+1][t]&&
						cheesboard[i+1][t]==cheesboard[i+2][t]&&
						cheesboard[i+2][t]==cheesboard[i-1][t]) {
					if(cheesboard[i+1][t]==nowCheese) {//��
						temp = 2;
						changeXY();
					} else {
						temp = 1;
						changeXY();
					}
				}
			}
			if(i-2>=0&&i+1<19) {
				if(null!=cheesboard[i-1][t]&&
						cheesboard[i-1][t]==cheesboard[i-2][t]&&
						cheesboard[i-2][t]==cheesboard[i+1][t]) {
					if(cheesboard[i-1][t]==nowCheese) {//��
						temp = 2;
						changeXY();
					} else {
						temp = 1;
						changeXY();
					}
				}
			}
			if(i-2>=0&&t-2>=0&&i+1<19&&t+1<19) {
				if(null!=cheesboard[i-1][t-1]&&
						cheesboard[i-1][t-1]==cheesboard[i-2][t-2]&&
						cheesboard[i-2][t-2]==cheesboard[i+1][t+1]) {
					if(cheesboard[i-1][t-1]==nowCheese) {//�I
						temp = 2;
						changeXY();
					} else {
						temp = 1;
						changeXY();
					}
				}
			}
			if(i+2<19&&t+2<19&&i-1>=0&&t-1>=0) {
				if(null!=cheesboard[i+1][t+1]&&
						cheesboard[i+1][t+1]==cheesboard[i+2][t+2]&&
						cheesboard[i+2][t+2]==cheesboard[i-1][t-1]) {
					if(cheesboard[i+1][t+1]==nowCheese) {//�K
						temp = 2;
						changeXY();
					} else {
						temp = 1;
						changeXY();
					}
				}
			}
			if(i-2>=0&&t+2<19&&i+1<19&&t-1>=0) {
				if(null!=cheesboard[i-1][t+1]&&
						cheesboard[i-1][t+1]==cheesboard[i-2][t+2]&&
						cheesboard[i-2][t+2]==cheesboard[i+1][t-1]) {
					if(cheesboard[i-1][t+1]==nowCheese) {//�J
						temp = 2;
						changeXY();
					} else {
						temp = 1;
						changeXY();
					}
				}
			}
			if(i+2<19&&t-2>=0&&i-1>=0&&t+1<19) {
				if(null!=cheesboard[i+1][t-1]&&
						cheesboard[i+1][t-1]==cheesboard[i+2][t-2]&&
						cheesboard[i+2][t-2]==cheesboard[i-1][t+1]) {
					if(cheesboard[i+1][t-1]==nowCheese) {//�L
						temp = 2;
						changeXY();
					} else {
						temp = 1;
						changeXY();
					}
				}
			}
		}
		//4&3

		private void lookFor4() {
			/*-------------------------��--------------------------------------*/		
			if(t+4<19) {
				if(cheesboard[i][t+1]!=null&&
						cheesboard[i][t+1]==cheesboard[i][t+2]&&
						cheesboard[i][t+2]==cheesboard[i][t+3]&&
						cheesboard[i][t+3]==cheesboard[i][t+4]) {
					if(cheesboard[i][t+1]==nowCheese) {//��
						temp = 4;
						changeXY();
					} else {
						temp = 3;
						changeXY();
					}
				}
			}
			if(t-4>=0) {
				if(null!=cheesboard[i][t-1]&&
						cheesboard[i][t-1]==cheesboard[i][t-2]&&
						cheesboard[i][t-2]==cheesboard[i][t-3]&&
						cheesboard[i][t-3]==cheesboard[i][t-4]) {
					if(cheesboard[i][t-1]==nowCheese) {//��
						temp = 4;
						changeXY();
					} else {
						temp = 3;
						changeXY();
					}
				}
			}
			if(i+4<19) {
				if(null!=cheesboard[i+1][t]&&
						cheesboard[i+1][t]==cheesboard[i+2][t]&&
						cheesboard[i+2][t]==cheesboard[i+3][t]&&
						cheesboard[i+3][t]==cheesboard[i+4][t]) {
					if(cheesboard[i+1][t]==nowCheese) {//��
						temp = 4;
						changeXY();
					} else {
						temp = 3;
						changeXY();
					}
				}
			}
			if(i-4>=0) {
				if(null!=cheesboard[i-1][t]&&
						cheesboard[i-1][t]==cheesboard[i-2][t]&&
						cheesboard[i-2][t]==cheesboard[i-3][t]&&
						cheesboard[i-3][t]==cheesboard[i-4][t]) {
					if(cheesboard[i-1][t]==nowCheese) {//��
						temp = 4;
						changeXY();
					} else {
						temp = 3;
						changeXY();
					}
				}
			}
			if(i-4>=0&&t-4>=0) {
				if(null!=cheesboard[i-1][t-1]&&
						cheesboard[i-1][t-1]==cheesboard[i-2][t-2]&&
						cheesboard[i-2][t-2]==cheesboard[i-3][t-3]&&
						cheesboard[i-3][t-3]==cheesboard[i-4][t-4]) {
					if(cheesboard[i-1][t-1]==nowCheese) {//�I
						temp = 4;
						changeXY();
					} else {
						temp = 3;
						changeXY();
					}
				}
			}
			if(i+4<19&&t+4<19) {
				if(null!=cheesboard[i+1][t+1]&&
						cheesboard[i+1][t+1]==cheesboard[i+2][t+2]&&
						cheesboard[i+2][t+2]==cheesboard[i+3][t+3]&&
						cheesboard[i+3][t+3]==cheesboard[i+4][t+4]) {
					if(cheesboard[i+1][t+1]==nowCheese) {//�K
						temp = 4;
						changeXY();
					} else {
						temp = 3;
						changeXY();
					}
				}
			}
			if(i-4>=0&&t+4<19) {
				if(null!=cheesboard[i-1][t+1]&&
						cheesboard[i-1][t+1]==cheesboard[i-2][t+2]&&
						cheesboard[i-2][t+2]==cheesboard[i-3][t+3]&&
						cheesboard[i-3][t+3]==cheesboard[i-4][t+4]) {
					if(cheesboard[i-1][t+1]==nowCheese) {//�J
						temp = 4;
						changeXY();
					} else {
						temp = 3;
						changeXY();
					}
				}
			}
			if(i+4<19&&t-4>=0) {
				if(null!=cheesboard[i+1][t-1]&&
						cheesboard[i+1][t-1]==cheesboard[i+2][t-2]&&
						cheesboard[i+2][t-2]==cheesboard[i+3][t-3]&&
						cheesboard[i+3][t-3]==cheesboard[i+4][t-4]) {
					if(cheesboard[i+1][t-1]==nowCheese) {//�L
						temp = 4;
						changeXY();
					} else {
						temp = 3;
						changeXY();
					}
				}
			}
			/*---------------------------һ����----------------------------------------------*/
			if(t+3<19&&t-1>=0) {
				if(null!=cheesboard[i][t-1]&&
						cheesboard[i][t-1]==cheesboard[i][t+1]&&
						cheesboard[i][t-1]==cheesboard[i][t+2]&&
						cheesboard[i][t-1]==cheesboard[i][t+3]) {
					if(cheesboard[i][t-1]==nowCheese) {//��
						temp = 4;
						changeXY();
					} else {
						temp = 3;
						changeXY();
					}
				}
			}
			if(t-3>=0&&t+1<19) {
				if(null!=cheesboard[i][t-1]&&
						cheesboard[i][t-1]==cheesboard[i][t-2]&&
						cheesboard[i][t-1]==cheesboard[i][t-3]&&
						cheesboard[i][t-1]==cheesboard[i][t+1]) {
					if(cheesboard[i][t-1]==nowCheese) {//��
						temp = 4;
						changeXY();
					} else {
						temp = 3;
						changeXY();
					}
				}
			}
			if(i+3<19&&i-1>=0) {
				if(null!=cheesboard[i+1][t]&&
						cheesboard[i+1][t]==cheesboard[i+2][t]&&
						cheesboard[i+1][t]==cheesboard[i+3][t]&&
						cheesboard[i+1][t]==cheesboard[i-1][t]) {
					if(cheesboard[i+1][t]==nowCheese) {//��
						temp = 4;
						changeXY();
					} else {
						temp = 3;
						changeXY();
					}
				}
			}
			if(i-3>=0&&i+1<19) {
				if(null!=cheesboard[i-1][t]&&
						cheesboard[i-1][t]==cheesboard[i-2][t]&&
						cheesboard[i-1][t]==cheesboard[i-3][t]&&
						cheesboard[i-1][t]==cheesboard[i+1][t]) {
					if(cheesboard[i-1][t]==nowCheese) {//��
						temp = 4;
						changeXY();
					} else {
						temp = 3;
						changeXY();
					}
				}
			}
			if(i-3>=0&&t-3>=0&&i+1<19&&t+1<19) {
				if(null!=cheesboard[i-1][t-1]&&
						cheesboard[i-1][t-1]==cheesboard[i-2][t-2]&&
						cheesboard[i-1][t-1]==cheesboard[i-3][t-3]&&
						cheesboard[i-1][t-1]==cheesboard[i+1][t+1]) {
					if(cheesboard[i-1][t-1]==nowCheese) {//�I
						temp = 4;
						changeXY();
					} else {
						temp = 3;
						changeXY();
					}
				}
			}
			if(i+3<19&&t+3<19&&t-1>=0&&i-1>=0) {
				if(null!=cheesboard[i+1][t+1]&&
						cheesboard[i+1][t+1]==cheesboard[i+2][t+2]&&
						cheesboard[i+1][t+1]==cheesboard[i+3][t+3]&&
						cheesboard[i+1][t+1]==cheesboard[i-1][t-1]) {
					if(cheesboard[i+1][t+1]==nowCheese) {//�K
						temp = 4;
						changeXY();
					} else {
						temp = 3;
						changeXY();
					}
				}
			}
			if(i-3>=0&&t+3<19&&t-1>=0&&i+1<19) {
				if(null!=cheesboard[i-1][t+1]&&
						cheesboard[i-1][t+1]==cheesboard[i-2][t+2]&&
						cheesboard[i-1][t+1]==cheesboard[i-3][t+3]&&
						cheesboard[i-1][t+1]==cheesboard[i+1][t-1]) {
					if(cheesboard[i-1][t+1]==nowCheese) {//�J
						temp = 4;
						changeXY();
					} else {
						temp = 3;
						changeXY();
					}
				}
			}
			if(i+3<19&&t-3>=0&&i-1>=0&&t+1<19) {
				if(null!=cheesboard[i+1][t-1]&&
						cheesboard[i+1][t-1]==cheesboard[i+2][t-2]&&
						cheesboard[i+1][t-1]==cheesboard[i+3][t-3]&&
						cheesboard[i+1][t-1]==cheesboard[i-1][t+1]) {
					if(cheesboard[i+1][t-1]==nowCheese) {//�L
						temp = 4;
						changeXY();
					} else {
						temp = 3;
						changeXY();
					}
				}
			}
			/*----------------------���Ӷ�------------------------------------------*/	
			if(t+2<19&&t-2>=0) {
				if(null!=cheesboard[i][t-1]&&
						cheesboard[i][t-1]==cheesboard[i][t+1]&&
						cheesboard[i][t-1]==cheesboard[i][t+2]&&
						cheesboard[i][t-1]==cheesboard[i][t-2]) {
					if(cheesboard[i][t-1]==nowCheese) {//��
						temp = 4;
						changeXY();
					} else {
						temp = 3;
						changeXY();
					}
				}
			}
			if(i+2<19&&i-2>=0) {
				if(null!=cheesboard[i+1][t]&&
						cheesboard[i+1][t]==cheesboard[i+2][t]&&
						cheesboard[i+1][t]==cheesboard[i-2][t]&&
						cheesboard[i+1][t]==cheesboard[i-1][t]) {
					if(cheesboard[i+1][t]==nowCheese) {//��
						temp = 4;
						changeXY();
					} else {
						temp = 3;
						changeXY();
					}
				}
			}
			if(i-2>=0&&t-2>=0&&i+2<19&&t+2<19) {
				if(null!=cheesboard[i-1][t-1]&&
						cheesboard[i-1][t-1]==cheesboard[i-2][t-2]&&
						cheesboard[i-1][t-1]==cheesboard[i+2][t+2]&&
						cheesboard[i-1][t-1]==cheesboard[i+1][t+1]) {
					if(cheesboard[i-1][t-1]==nowCheese) {//�I
						temp = 4;
						changeXY();
					} else {
						temp = 3;
						changeXY();
					}
				}
			}
			if(i-2>=0&&t+2<19&&t-2>=0&&i+2<19) {
				if(null!=cheesboard[i-1][t+1]&&
						cheesboard[i-1][t+1]==cheesboard[i-2][t+2]&&
						cheesboard[i-1][t+1]==cheesboard[i+2][t-2]&&
						cheesboard[i-1][t+1]==cheesboard[i+1][t-1]) {
					if(cheesboard[i-1][t+1]==nowCheese) {//�J
						temp = 4;
						changeXY();
					} else {
						temp = 3;
						changeXY();
					}
				}
			}
		}


		public int[] operate() {
			temp = -99;
			max = -99;
			for(i=0;i<19;i++) {
				for(t=0;t<19;t++) {
					if(cheesboard[i][t]==null) {
						//						lookFor4(cheesboard,nowCheese);
						//						if(max<3){
						//							lookFor3(cheesboard,nowCheese);
						//						}else if(max<1) {
						//							lookFor2(cheesboard,nowCheese);
						//						}else if(max<-1) {
						//							lookFor1(cheesboard,nowCheese);
						//						}
						lookFor1();
						lookFor2();
						lookFor3();
						lookFor4();
					}
				}
			}
			return xy;
		}

	}

}