package aaa;

public class aaa {
	
	public static void main(String[] args) {
		for (Long i = 1000000000000l; i > i-1; i++) {
			System.out.println(i);
			fo(i);
		}
	}

	private static void fo(Long i) {
		
		System.out.println(i);
		fo(i);
	}

}
