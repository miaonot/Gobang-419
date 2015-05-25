import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class Gobang {
//	用于记录线上线下选择用的变量
	static boolean isPvp;

//	记录线上线下的选择
	public static void setIsPvp(boolean a) {
		isPvp = a;
	}
	public static boolean isPvp() {
		return isPvp;
	}

	public static void main(String[] args) {
	new Window();
	}

}


class Window {
	//	登录界面初版
	class LoginWindow extends JFrame {
		JButton button1 = new JButton("pvp");
		JButton button2 = new JButton("pvc");
		
		public LoginWindow() {
			 super("欢迎使用五子棋程序");

			setLayout(new FlowLayout());
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setSize(300, 100);
			setResizable(false);

			
			button1.addActionListener(new ActionListener() {
				public void actionPerformed (ActionEvent e) {
					Gobang.setIsPvp(true);
//					loginToConnect();
					loginToMain();
				}
			});

			
			button2.addActionListener(new ActionListener() {
				public void actionPerformed (ActionEvent e) {
					Gobang.setIsPvp(false);
					loginToMain();
				}
			});

			add(new JLabel("选择你要的游戏方式："));
			add(button1);
			add(button2);

			setVisible(true);
		}
	}

	//	收集连接服务器所需信息的窗口
	class ConnectWindow extends JFrame {
		final JTextField ip = new JTextField("IP",20);
		final JTextField spot = new JTextField("spot",8);
		JButton button = new JButton("确定");
		
		public ConnectWindow() {
			super("连接服务器");

			setSize(600,100);
			setLayout(new FlowLayout());
			setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			setResizable(false);

			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int i = Integer.parseInt(spot.getText());
					net.setIP(ip.getText());
					net.setPort(i);
					try {
						net.connect();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					connectToMain();
				}
			});

			add(new JLabel("服务器地址："));
			add(ip);
			add(new JLabel(":"));
			add(spot);
			add(button);
		}
	}

	//	主界面
	class MainWindow extends JFrame{
		Yuan [][] button = new Yuan[19][19];
		panel panel1 = new panel();
		JPanel panel2 = new JPanel(new GridLayout(2,1));
		JButton button1 = new JButton("悔棋");
		JButton button2 = new JButton("认输");
		
		public MainWindow() {
			super ("五子棋");
			
			setSize(760,630);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setResizable(false);
			setLayout(null);

			panel1.setLayout(new GridLayout(19,19,15,15));
			panel1.setBounds(0,0,610,600);
            panel1.setOpaque(false);
            
			panel2.setBounds(610,0,150,600);
			
			button1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					game.withdraw();
					withdraw();
				}
			});
			button1.setEnabled(true);
			
			button2.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						game.giveUp();
						lock();
						mainToNewGame();
					}
				});

			for (int i = 0; i < 19; i ++) {
				for (int j = 0; j < 19; j ++) {
					button[i][j] = new Yuan();
					button[i][j].yuan("",null);

					panel1.add(button[i][j]);
					
					button[i][j].addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							for (int x = 0; x < 19; x ++) {
								for (int y = 0; y < 19; y ++) {
									if (e.getSource() == button[x][y]) {
										putcheese(x, y);
										
										System.out.println("(" + x + "," + y + ")");
										
										game.putCheese(x, y);
										boolean	isWin = game.judge();
										if(isWin == true) {
											game.afterJudge();
											lock();
											mainToNewGame();
										}
										game.turnEnd();
										
										//判断机器端是否需要下
										if (!Gobang.isPvp()) {
											int[] xy = game.ai.operate();
											int a = xy[0];
											int b = xy[1];
											System.out.println("(" + a + "," + b + ")");
											
											putcheese(a, b);
											
											game.putCheese(a, b);
											isWin = game.judge();
											if(isWin == true) {
												game.afterJudge();
												lock();
												mainToNewGame();
											}
											
											game.turnEnd();
										}
									}
								}
							}
						}
					});
				}
			}

			panel2.add(button1);
			panel2.add(button2);
			add(BorderLayout.WEST,panel1);
			add(BorderLayout.EAST,panel2);


		}

		// 棋盘初始化
		public void init() {
			for (int x = 0; x < 19; x ++) {
				for (int y = 0; y < 19; y ++) {
					button[x][y].setOpaque(false);
					button[x][y].setEnabled(true);
				}
			}
			button1.setEnabled(true);
			button2.setEnabled(true);
		}
		
		//悔棋
		public void withdraw() {
			button[game.lastX][game.lastY].setOpaque(false);
			button[game.lastX][game.lastY].setEnabled(true);
			button[game.lasterX][game.lasterY].setOpaque(false);
			button[game.lasterX][game.lasterY].setEnabled(true);
		}
		
		//锁定棋盘
		public void lock() {
			for (int x = 0; x < 19; x ++) {
				for (int y = 0; y < 19; y ++) {
					button[x][y].setEnabled(false);
				}
			}
			button1.setEnabled(false);
			button2.setEnabled(false);
		}
		
		public void putcheese(int x, int y) {
			if (game.nowCheese == "X") {
				button[x][y].yuan("",Color.black);
			} else {
				button[x][y].yuan("",Color.white);
			}
			button[x][y].setOpaque(true);
			button[x][y].setEnabled(false);
		}
		
	}
	
	//询问是否开始新游戏的窗口
	class NewGameWindow extends JFrame{
		JButton button1 = new JButton("是");
		JButton button2 = new JButton("否");
		
		public NewGameWindow() {
			super("新游戏");

			setSize(300,100);
			setLayout(new FlowLayout());
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setResizable(false);

			
			button1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
					game.init();
					mw.init();
				}
			});
			
			button2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					newGameToLogin();
				}
			});

			add(new JLabel("胜负已分。是否开始新的游戏"));
			add(button1);
			add(button2);
		}
	}
	
	//圆形棋子的类
	class Yuan extends JButton{
		Shape shape;
		Color bg = SystemColor.control;
		
		public void yuan(String label,Color bg) {
	 	// 调用父类构造方法
		  	if (bg != null) {
		      		this.bg = bg;
		    	}

			Dimension size = this.getPreferredSize();
			size.width = size.height = Math.max(size.width, size.height);
			this.setSize(25,25);
			this.setBorderPainted(false);
			this.setPreferredSize(size); // 设置宽高等距
			this.setContentAreaFilled(false); // 不绘制内容区域
			this.setFocusPainted(false); // 不绘制焦点状态
		}

    		protected void paintComponent(Graphics g) {
        		if (this.getModel().isArmed()) {
        			g.setColor(java.awt.SystemColor.controlHighlight);
        		} else {
        			g.setColor(java.awt.SystemColor.controlShadow);
        			g.setColor(this.bg); // 设置背景颜色
	    		}
	    		g.drawOval(0, 0, this.getSize().width - 1, this.getSize().height - 1); // 绘制边框线
			g.fillOval(0, 0, this.getSize().width - 1, this.getSize().height - 1); // 绘制圆形背景区域
		}

    		public boolean contains(int x, int y) {
        		if ((shape == null) || (!shape.getBounds().equals(this.getBounds()))) {
          			this.shape = new Ellipse2D.Float(0, 0, this.getWidth(), this.getHeight());
        		}
        		return shape.contains(x, y);
    		}
	}

	// 棋盘界面
	class panel extends JPanel {
		public void paint(Graphics g)
 		{
 			int i=16;

			//棋盘横线
 			for(i=0;i<304;i+=16)
 			g.drawLine(20,15+i*2,596,15+i*2);

			//棋盘竖线
 			for(i=0;i<304;i+=16)
 			g.drawLine(20+i*2,15,20+i*2,591);

 		}
	}



	//	用于登录界面转到主界面
	public void loginToMain() {
		lw.setVisible(false);
		mw.init();
		mw.setVisible(true);
		game.init();
	}

	//	登录界面打开连接服务器界面
	public void loginToConnect() {
		cw.setVisible(true);
	}

	//	连接服务器界面转到主界面
	public void connectToMain() {
		lw.setVisible(false);
		cw.setVisible(false);
		mw.setVisible(true);
	}

	//	主界面转到新游戏界面
	public void mainToNewGame() {
		ngw.setVisible(true);
	}

	public void newGameToLogin() {
		ngw.setVisible(false);
		mw.setVisible(false);
		lw.setVisible(true);
	}
	//	初始化窗口
	LoginWindow lw = new LoginWindow();
	ConnectWindow cw = new ConnectWindow();
	MainWindow mw = new MainWindow();
	NewGameWindow ngw = new NewGameWindow();
	Game game = new Game();
	ClientNet net = new ClientNet();

}



//	有关玩家的变量
class Player {
	String name;
	int wincount;
	int losecount;
	int tiecount;
	
	public Player(String name) {
		this.name = name;
	}
	public int getWincount() {
		return wincount;
	}
	public void setWincount() {
		wincount ++;
	}
	public int getLosecount() {
		return losecount;
	}
	public void setLosecount() {
		losecount ++;
	}
	public int getTiecount() {
		return tiecount;
	}
	public void setTiecount() {
		tiecount ++;
	}
}

//	有关游戏的变量和方法
class Game {
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
		
		//初始化棋子
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
		private int i;//�����
		private int t;//�����
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

//设置客户端网络模块
class ClientNet {
	int x,y;//己方所下位置坐标
	int m,n;//对方所下位置坐标
	String IP;//IP
	int port;//端口
	Socket Client;
	public InputStream DataIn;
	public OutputStream DataOut;
	
	//设置IP
	public void setIP(String IP) {
		this.IP = IP;
	}
	
	//设置端口
	public void setPort(int port) {
		this.port = port;
	}
	
	//玩家所下位置
		public void Input(int x,int y) {
			this.x=x;
			this.y=y;
		}
		//对手所下位置
		public void Output(int m,int n){
			this.m = m;
			this.n = n;
		}
	
	//连接服务机
	public void connect() throws Exception{
		Client = new Socket(IP,port); 
		if(Client.isConnected()) {
			JOptionPane.showInputDialog("已连接","进入游戏");
		}
	}
	
		//获取对方下棋位置
		public int[] getEnemyAddress() throws IOException {
			int[] num = new int[2];
			DataIn = Client.getInputStream();
			
			byte []a = {(byte) m};
			byte []b = {(byte) n};
			DataIn.read(a);
			String str1 = new String(a);
			num[0] = Integer.getInteger(str1); //横行坐标
			DataIn.read(b);
			String str2 = new String(b);
			num[1] = Integer.getInteger(str2); //纵行坐标
			return num;
		}
		//输出己方下棋位置
		public void InputMyAddress()  {
			try{
				DataOut = Client.getOutputStream();
				
				DataOut.write(x); //输出横行坐标
				DataOut.write(y); //输出纵行坐标
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			
		}
	
}
