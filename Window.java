package gobang;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;

class Window {

	// 用于登录界面转到主界面
	public static void loginToMain() {
		lw.setVisible(false);
		mw.init();
		mw.setVisible(true);
		game.init();
	}

	// 主界面转到新游戏界面
	public static void mainToNewGame() {
		ngw.setVisible(true);
	}

	public static void newGameToLogin() {
		ngw.setVisible(false);
		mw.setVisible(false);
		lw.setVisible(true);
	}

	// 初始化窗体
	static LoginWindow lw = new LoginWindow();
	static MainWindow mw = new MainWindow();
	static NewGameWindow ngw = new NewGameWindow();
	static Game game = new Game();

}

// 登录界面
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
			public void actionPerformed(ActionEvent e) {
				Game.setIsPvp(true);
				// loginToConnect();
				Window.loginToMain();
			}
		});

		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Game.setIsPvp(false);
				Window.loginToMain();
			}
		});

		add(new JLabel("选择你要的游戏方式："));
		add(button1);
		add(button2);

		setVisible(true);
	}
}

// 主界面
class MainWindow extends JFrame {
	Yuan[][] button = new Yuan[19][19];
	panel panel1 = new panel();
	JPanel panel2 = new JPanel(new GridLayout(2, 1));
	JButton button1 = new JButton("悔棋");
	JButton button2 = new JButton("认输");

	public MainWindow() {
		super("五子棋");

		setSize(760, 630);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLayout(null);

		panel1.setLayout(new GridLayout(19, 19, 15, 15));
		panel1.setBounds(0, 0, 610, 600);
		panel1.setOpaque(false);

		panel2.setBounds(610, 0, 150, 600);

		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Window.game.withdraw();
				withdraw();
			}
		});
		button1.setEnabled(true);

		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Window.game.giveUp();
				lock();
				Window.mainToNewGame();
			}
		});

		for (int i = 0; i < 19; i++) {
			for (int j = 0; j < 19; j++) {
				button[i][j] = new Yuan();
				button[i][j].yuan("", null);

				panel1.add(button[i][j]);

				button[i][j].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						for (int x = 0; x < 19; x++) {
							for (int y = 0; y < 19; y++) {
								if (e.getSource() == button[x][y]) {
									putcheese(x, y);

									System.out.println("(" + x + "," + y + ")");

									Window.game.putCheese(x, y);
									boolean isWin = Window.game.judge();
									if (isWin == true) {
										Window.game.afterJudge();
										lock();
										Window.mainToNewGame();
									}
									Window.game.turnEnd();

									// 判断机器端是否需要下
									if (!Game.isPvp()) {
										int[] xy = Window.game.ai.operate();
										int a = xy[0];
										int b = xy[1];
										System.out.println("(" + a + "," + b
												+ ")");

										putcheese(a, b);
										repaint();

										Window.game.putCheese(a, b);
										isWin = Window.game.judge();
										if (isWin == true) {
											Window.game.afterJudge();
											lock();
											Window.mainToNewGame();
										}

										Window.game.turnEnd();
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
		add(BorderLayout.WEST, panel1);
		add(BorderLayout.EAST, panel2);

	}

	// 棋盘初始化
	public void init() {
		for (int x = 0; x < 19; x++) {
			for (int y = 0; y < 19; y++) {
				button[x][y].setOpaque(false);
				button[x][y].setEnabled(true);
			}
		}
		button1.setEnabled(true);
		button2.setEnabled(true);
	}

	// 悔棋
	public void withdraw() {
		button[Window.game.lastX][Window.game.lastY].setOpaque(false);
		button[Window.game.lastX][Window.game.lastY].setEnabled(true);
		button[Window.game.lasterX][Window.game.lasterY].setOpaque(false);
		button[Window.game.lasterX][Window.game.lasterY].setEnabled(true);
	}

	// 锁定棋盘
	public void lock() {
		for (int x = 0; x < 19; x++) {
			for (int y = 0; y < 19; y++) {
				button[x][y].setEnabled(false);
			}
		}
		button1.setEnabled(false);
		button2.setEnabled(false);
	}

	public void putcheese(int x, int y) {
		if (Window.game.nowCheese == "X") {
			button[x][y].yuan("", Color.black);
		} else {
			button[x][y].yuan("", Color.white);
		}
		button[x][y].setOpaque(true);
		button[x][y].setEnabled(false);
	}

}

// 询问是否开始新游戏的窗口
class NewGameWindow extends JFrame {
	JButton button1 = new JButton("是");
	JButton button2 = new JButton("否");

	public NewGameWindow() {
		super("新游戏");

		setSize(300, 100);
		setLayout(new FlowLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				Window.game.init();
				Window.mw.init();
			}
		});

		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Window.newGameToLogin();
			}
		});

		add(new JLabel("胜负已分。是否开始新的游戏"));
		add(button1);
		add(button2);
	}
}

// 圆形棋子的类
class Yuan extends JButton {
	Shape shape;
	Color bg = SystemColor.control;

	public void yuan(String label, Color bg) {
		// 调用父类构造方法
		if (bg != null) {
			this.bg = bg;
		}

		Dimension size = this.getPreferredSize();
		size.width = size.height = Math.max(size.width, size.height);
		this.setSize(25, 25);
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
		g.drawOval(0, 0, this.getSize().width - 1, this.getSize().height - 1); // 绘制边框
		g.fillOval(0, 0, this.getSize().width - 1, this.getSize().height - 1); // 绘制圆形背景区域
	}

	public boolean contains(int x, int y) {
		if ((shape == null) || (!shape.getBounds().equals(this.getBounds()))) {
			this.shape = new Ellipse2D.Float(0, 0, this.getWidth(),
					this.getHeight());
		}
		return shape.contains(x, y);
	}
}

// 棋盘界面
class panel extends JPanel {
	public void paint(Graphics g) {
		int i = 16;

		// 棋盘横线
		for (i = 0; i < 304; i += 16)
			g.drawLine(20, 15 + i * 2, 596, 15 + i * 2);

		// 棋盘竖线
		for (i = 0; i < 304; i += 16)
			g.drawLine(20 + i * 2, 15, 20 + i * 2, 591);

	}
}
