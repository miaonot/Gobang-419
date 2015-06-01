package gobang;

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