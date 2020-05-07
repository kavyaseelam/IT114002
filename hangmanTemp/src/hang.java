public class hang {
	
	// function to generate a random word
	public static String getWord() {
		String[] words = {"apple", "banana", "orange", "strawberry", "watermelon"};
		String word = words[(int) (Math.random() * words.length)];
		return word;
	}
	
	public static void drawBlanks(String word) {
		int wordLen = word.length();
		for(int i =0; i < wordLen; i++) {
			System.out.print("_ ");
		}
	}
	
	public static void main (String[] args) {
		String word = getWord();
		drawBlanks(word);
	
	}
	
	/*public String checkGuess(String guess, String word) {
		boolean gameOver = false;
		int guessLen = guess.length();
		// this means they guessed a word not a letter
		if(guessLen > 1) {
			if(guess == word) {
				gameOver = true;
			}
		}else {
			char guessLetter = guess;
			for(int i = 0; i < word.length(); i++) {
				if(guess == String.valueOf(word.charAt(i))) {
					// update the dashes
				}
			} 
		}
		
	} */
	
	
	
}
