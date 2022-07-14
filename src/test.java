import java.util.LinkedHashMap;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		LinkedHashMap<String, Double> test = new LinkedHashMap<String, Double>();
		test.put("score", 100.0);
		test.put("score2", 90.0);
		
		System.out.println(test.keySet().toArray()[1]);
		
		
	}

}
