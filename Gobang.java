import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.io.IOException;
import java.net.Socket;


public class Gobang {
//	用于记录线上线下选择用的变量
	static boolean isP2p;

//	记录线上线下的选择
	public static void setIsP2p(boolean a) {
		isP2p = a;
	}
	public static boolean isP2p() {
		return isP2p;
	}

	public static void main(String[] args) {
	new Window();
	}

}


class Window {
	//	登录界面初版
	class LoginWindow extends JFrame {
		public LoginWindow() {
			 super("欢迎使用五子棋程序");

			setLayout(new FlowLayout());
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setSize(300, 100);
			setResizable(false);

			JButton button1 = new JButton("线上");
			button1.addActionListener(new ActionListener() {
				public void actionPerformed (ActionEvent e) {
					Gobang.setIsP2p(true);
					loginToConnect();
				}
			});

			JButton button2 = new JButton("线下");
			button2.addActionListener(new ActionListener() {
				public void actionPerformed (ActionEvent e) {
					Gobang.setIsP2p(true);
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
		public ConnectWindow() {
			super("连接服务器");

			setSize(600,100);
			setLayout(new FlowLayout());
			setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			setResizable(false);

			final JTextField ip = new JTextField("IP",20);
			final JTextField spot = new JTextField("spot",8);

			JButton button = new JButton("确定");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int i = Integer.parseInt(spot.getText());
					net.setIp(ip.getText());
					net.setSpot(i);
					net.connectToServer();
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
		final  Yuan [][] button = new Yuan[19][19];
		public MainWindow() {
			super ("五子棋");



			setSize(760,630);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setResizable(false);
			setLayout(null);



			panel panel1 = new panel();
			panel1.setLayout(new GridLayout(19,19,15,15));
			panel1.setBounds(0,0,610,600);

            panel1.setOpaque(false);
			JPanel panel2 = new JPanel(new GridLayout(2,1));
			panel2.setBounds(610,0,150,600);



			JButton button1 = new JButton("悔棋");
			button1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					game.withdraw();

				}
			});
			button1.setEnabled(true);
			JButton button2 = new JButton("认输");
			button2.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						game.giveUp();
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
										if (game.nowCheese == "X") {
											button[x][y].yuan("",Color.white);
										} else {
											button[x][y].yuan("",Color.black);
										}
										button[x][y].setOpaque(true);

										button[x][y].setEnabled(false);
										game.turnEnd();
										boolean	isWin = game.judge();
										if(isWin == true) {
											game.afterJudge();
											game.init();
											init();
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
					
				}
			}
		}
	}
	
	//询问是否开始新游戏的窗口
	class NewGameWindow extends JFrame{
		public NewGameWindow() {
			super("新游戏");

			setSize(300,100);
			setLayout(new FlowLayout());
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setResizable(false);

			JButton button1 = new JButton("是");
			button1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
					game.init();
				}
			});
			JButton button2 = new JButton("否");
			button2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
				}
			});

			add(new JLabel("是否开始新的游戏"));
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
		mw.setVisible(true);
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

	//	初始化窗口
	LoginWindow lw = new LoginWindow();
	ConnectWindow cw = new ConnectWindow();
	MainWindow mw = new MainWindow();
	NewGameWindow ngw = new NewGameWindow();
	Game game = new Game();
	Net net = new Net();

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
	}
	//	落子
	public void putCheese(int x, int y)	{
		cheesboard[x][y] = nowCheese;
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
					if (cheesboard[x][y] == nowCheese && cheesboard[x + 1][y +1] == nowCheese && cheesboard[x + 2][y +2] == nowCheese && cheesboard[x + 3][y +3] == nowCheese && cheesboard[x + 4][y +4] == nowCheese) {
						return true;
					}
				}
				if (x + 4 < 19) {
					if (cheesboard[x][y] == nowCheese && cheesboard[x + 1][y] == nowCheese && cheesboard[x + 2][y] == nowCheese && cheesboard[x + 3][y] == nowCheese && cheesboard[x + 4][y] == nowCheese) {
						return true;
					}
				}
				if (y + 4 < 19) {
					if (cheesboard[x][y] == nowCheese && cheesboard[x][y +1] == nowCheese && cheesboard[x][y +2] == nowCheese && cheesboard[x][y +3] == nowCheese && cheesboard[x][y +4] == nowCheese) {
						return true;
					}
				}
				if (x + 4 < 19 && y - 4 >= 0) {
					if (cheesboard[x][y] == nowCheese && cheesboard[x + 1][y +1] == nowCheese && cheesboard[x + 2][y +2] == nowCheese && cheesboard[x + 3][y +3] == nowCheese && cheesboard[x + 4][y +4] == nowCheese) {
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

}

//	网络模块
class Net {
	String ip;
	int spot;
	 Socket server;

	 //	设置ip
	public void setIp(String ip) {
		this.ip = ip;
	}
	//	设置端口
	public void setSpot(int spot) {
		this.spot = spot;
	}
	//	连接服务器
	public void connectToServer() {
		try {
			server = new Socket(ip,spot);
		} catch(IOException ioe) {
		}
	}
}
