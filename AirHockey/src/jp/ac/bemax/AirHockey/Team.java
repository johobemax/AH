package jp.ac.bemax.AirHockey;

public class Team {
	private String name;
	private Pad[] players;
	private int score, hashcode, count;

	public Team(Player p, int playnum, Field f){
		name = p.name();
		hashcode = p.hashCode();
		players = new Pad[playnum];
		for(int i=0; i<players.length; i++){
			players[i] = new Pad(f, 0, 0, p, Level.NORMAL);
		}
		score = 0;
		count = 0;
	}

	public int getScore(){
		return score;
	}

	public void addScore(int s){
		score += s;
	}

	public void initScore(){
		score = 0;
	}


	public boolean setPad(Pad p){
		boolean result = false;
		if(count < players.length){
			players[count] = p;
			count++;
			result = true;
		}
		return result;
	}
}
