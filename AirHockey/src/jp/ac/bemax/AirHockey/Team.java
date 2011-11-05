package jp.ac.bemax.AirHockey;

import android.graphics.Canvas;

public class Team {
	private String name;
	private Pad[] players;
	private int score, hashcode, count;
	private Field field;

	public Team(Player p, int playnum, Field f){
		name = p.name();
		hashcode = p.hashCode();
		players = new Pad[playnum];
		for(int i=0; i<players.length; i++){
			players[i] = new Pad(f, 0, 0, p, Level.NORMAL);
		}
		score = 0;
		count = 0;
		field = f;
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

	public Pad getPad(int index){
		return players[index];
	}

	public void move(){
		for(Pad p:players){
			p.move(field);
		}
	}

	public void draw(Canvas c){
		for(Pad p: players){
			p.draw(c);
		}
	}

	public Pad attachId(int id){
		Pad result = null;
		for(Pad p: players){
			if(p.attachId(id)){
				result = p;
				break;
			}
		}
		return result;
	}

	public Pad searchPad(int id){
		Pad result = null;
		for(Pad p: players){
			if(p.isId(id)){
				result = p;
				break;
			}
		}
		return result;
	}

	public void hit(Pack pack){
		for(Pad p:players){
			pack.hit(p);
		}
	}
}
