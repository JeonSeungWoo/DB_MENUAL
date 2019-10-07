package haksa;

public class Test {
	public static void main(String[] args) {
		String plainText = "Hello, World!";

		String key = "secret key";

		try {
			System.out.println("MD5 : " + plainText + " - " + CryptoUtil.md5(plainText));

			System.out.println("SHA-256 : " + plainText + " - " + CryptoUtil.sha256(plainText));

			String encrypted = CryptoUtil.encryptAES256("Hello, World!", key);

			System.out.println("AES-256 : enc - " + encrypted);

			System.out.println("AES-256 : dec - " + CryptoUtil.decryptAES256(encrypted, key));

		} catch (Exception e) {
			
		}

	}

}
