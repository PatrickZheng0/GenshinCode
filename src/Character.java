
public class Character {

	public static void main(String[] args) {
		
		Character Ganyu = new Character(5, 88.4, 335, 0, 100, 0, 630, 0, 9797, 0);
		Ganyu.initArtifacts();
		System.out.println(Ganyu.getCirclet().getType());
		
	}
	
	private double CR;
	private double CD;
	private double flatAtk;
	private double AtkPercent;
	private double ER;
	private double EM;
	private double flatDef;
	private double DefPercent;
	private double flatHP;
	private double HPPercent;
	
	private Artifact flower;
	private Artifact feather;
	private Artifact sands;
	private Artifact goblet;
	private Artifact circlet;


	
	public Character(double a, double b, double c, double d, double e, double f, double g, double h, double i, double j) {
		CR = a;
		CD = b;
		flatAtk = c;
		AtkPercent = d;
		ER = e;
		EM = f;
		flatDef = g;
		DefPercent = h;
		flatHP = i;
		HPPercent = j;
	}
	
	public void initArtifacts() {
		flower = new Artifact("flower");
		feather = new Artifact("feather");
		sands = new Artifact("sands");
		goblet = new Artifact("goblet");
		circlet = new Artifact("circlet");
	}
	
	public Artifact getFlower() {
		return flower;
	}

	public Artifact getFeather() {
		return feather;
	}
	
	public Artifact getSands() {
		return sands;
	}
	
	public Artifact getGoblet() {
		return goblet;
	}
	
	public Artifact getCirclet() {
		return circlet;
	}
}
