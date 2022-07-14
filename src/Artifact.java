import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Artifact {
	
	public static void main(String[] args) {
		Artifact flower = new Artifact("sands");
		flower.setPosMainStat();
		flower.setMainStat(flower.getPosMainStat()[0], 7.0);
		flower.setPosSubStat();
		flower.setSubStat(flower.getPosSubStat()[0], 3.0, flower.getPosSubStat()[1], 7.0, flower.getPosSubStat()[2], 10.0, flower.getPosSubStat()[3], 16.0);
		System.out.println(flower.getType());
		System.out.println(flower.getMainStat());
		System.out.println("--------");
		System.out.println(flower.getSubStat());
		System.out.println("--------");
		for (String i : flower.getPosMainStat()) System.out.println(i);
		System.out.println("--------");
		for (String j : flower.getPosSubStat()) System.out.println(j);

	}

	//TODO make roll function which will increase artifact stats
	//dw abt getting all pos combo of substat for arti yet
	
	private String type;
	private LinkedHashMap<String, Double> mainStat = new LinkedHashMap<String, Double>();
	private LinkedHashMap<String, Double> subStat = new LinkedHashMap<String, Double>();
	private String[] posMainStat;
	private String[] posSubStat;

	public Artifact (String artifactType) {
		type = artifactType;
	}
	
	public void init() {
		this.setPosMainStat();
		this.setPosSubStat();
	}
	
	public String getType() {
		return type;
	}
	
	public LinkedHashMap<String, Double> getMainStat() {
		return mainStat;
	}
	
	public LinkedHashMap<String, Double> getSubStat() {
		return subStat;
	}
	
	public String[] getPosMainStat() {
		return posMainStat;
	}
	
	public String[] getPosSubStat() {
		return posSubStat;
	}
	
	public void setMainStat(String m, Double d) {
		mainStat.put(m, d);
	}
	
	public void setSubStat(String a, Double aa, String b, Double bb, String c, Double cc, String d, Double dd) {
		subStat.put(a, aa);
		subStat.put(b, bb);
		subStat.put(c, cc);
		subStat.put(d, dd);
	}
	
	public void setPosMainStat() {
		if (type.equalsIgnoreCase("flower")) {
			posMainStat = new String[] {"flatHP"};
		} else if (type.equalsIgnoreCase("feather")) {
			posMainStat = new String[] {"flatAtk"};
		} else if (type.equalsIgnoreCase("sands")) {
			posMainStat = new String[] {"Atk%", "EM", "ER", "HP%", "Def%"};
		} else if (type.equalsIgnoreCase("goblet")) {
			posMainStat = new String[] {"ElementalDmg%", "Atk%", "EM", "HP%", "Def%"};
		} else if (type.equalsIgnoreCase("circlet")) {
			posMainStat = new String[] {"CR", "CD", "Atk%", "EM", "HP%", "Def%", "Heal%"};
		}
	}
	
	public void setPosSubStat() {
		String[] totalPos = {"CR", "CD", "Atk%", "ER", "EM", "HP%", "Def%", "flatAtk", "flatHP", "flatDef"};
		ArrayList<String> temp = new ArrayList<String>();
		for (String s : totalPos) {
			if (!(mainStat.keySet().contains(s))) {
				temp.add(s);
			}
		}

		String[] arr = new String[temp.size()];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (String) temp.get(i);
		}
		
		posSubStat = arr;
	}
	
}
