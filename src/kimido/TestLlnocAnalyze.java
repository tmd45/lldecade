package kimido;

import java.io.File;

public class TestLlnocAnalyze {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LlnocAnalyze la = new LlnocAnalyze();
		File file = new File("secrecy.txt");
		String str = la.getAnalyzedSentence(file);

		System.out.println("RESULT > " + str);
		System.out.println("URI    > " + str.toLowerCase());
	}

}
