package com.ipeirotis.readability.engine;

import java.math.BigDecimal;


/**
 * Implements various readability indexes
 * 
 * @author Panos Ipeirotis
 * 
 */
public class Readability {

	private static SentenceExtractor se = new SentenceExtractor();

	Integer sentences;

	Integer complex;

	Integer words;

	Integer syllables;

	Integer characters;


	public Readability(String text) {

		// We add the "." for the sentence extractor to pick the last sentence,
		// if it does not end with a punctuation mark.
		this.sentences = getNumberOfSentences(text + ".");
		this.complex = getNumberOfComplexWords(text);
		this.words = getNumberOfWords(text);
		this.syllables = getNumberOfSyllables(text);
		this.characters = getNumberOfCharacters(text);

	}

	/**
	 * Returns true is the word contains 3 or more syllables
	 * 
	 * @param w
	 * @return
	 */
	public static boolean isComplex(String w) {
		int syllables = Syllabify.syllable(w);
		return (syllables > 2);
	}

	/**
	 * Returns the number of letter characters in the text
	 * 
	 * @return
	 */
	public static int getNumberOfCharacters(String text) {
		String cleanText = cleanLine(text);
		String[] word = cleanText.split(" ");

		int characters = 0;
		for (String w : word) {
			characters += w.length();
		}
		return characters;
	}

	/**
	 * Returns the number of words with 3 or more syllables
	 * 
	 * @param text
	 * @return the number of words in the text with 3 or more syllables
	 */
	public static int getNumberOfComplexWords(String text) {
		String cleanText = cleanLine(text);
		String[] words = cleanText.split(" ");
		int complex = 0;
		for (String w : words) {
			if (isComplex(w))
				complex++;
		}
		return complex;
	}

	public static int getNumberOfWords(String text) {
		String cleanText = cleanLine(text);
		String[] word = cleanText.split(" ");
		int words = 0;
		for (String w : word) {
			if (w.length() > 0)
				words++;
		}
		return words;
	}

	/**
	 * Returns the total number of syllables in the words of the text
	 * 
	 * @param text
	 * @return the total number of syllables in the words of the text
	 */
	public static int getNumberOfSyllables(String text) {
		String cleanText = cleanLine(text);
		String[] word = cleanText.split(" ");
		int syllables = 0;
		for (String w : word) {
			if (w.length() > 0) {
				syllables += Syllabify.syllable(w);
			}
		}
		return syllables;
	}

	private static String cleanLine(String line) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);
			if (c < 128 && Character.isLetter(c)) {
				buffer.append(c);
			} else {
				buffer.append(' ');
			}
		}
		return buffer.toString().toLowerCase();
	}

	public static int getNumberOfSentences(String text) {
		int l = se.getSentences(text).length;
		if (l > 0)
			return l;
		else if (text.length() > 0)
			return 1;
		else
			return 0;
	}

	/**
	 * 
	 * http://en.wikipedia.org/wiki/SMOG_Index
	 * 
	 * @param text
	 * @return The SMOG index of the text
	 */
	public double getSMOGIndex() {
		double score = Math.sqrt(complex * (30.0 / sentences)) + 3;
		return round(score, 3);
	}

	/**
	 * 
	 * http://en.wikipedia.org/wiki/SMOG
	 * 
	 * @param text
	 * @return Retugns the SMOG value for the text
	 */
	public double getSMOG() {
		double score = 1.043 * Math.sqrt(complex * (30.0 / sentences)) + 3.1291;
		return round(score, 3);
	}

	/**
	 * 
	 * http://en.wikipedia.org/wiki/Flesch-Kincaid_Readability_Test
	 * 
	 * @param text
	 * @return Returns the Flesch_Reading Ease value for the text
	 */
	public double getFleschReadingEase() {

		double score = 206.835 - 1.015 * words / sentences - 84.6 * syllables
				/ words;

		return round(score, 3);
	}

	/**
	 * 
	 * http://en.wikipedia.org/wiki/Flesch-Kincaid_Readability_Test
	 * 
	 * @param text
	 * @return Returns the Flesch-Kincaid_Readability_Test value for the text
	 */
	public double getFleschKincaidGradeLevel() {
		double score = 0.39 * words / sentences + 11.8 * syllables / words
				- 15.59;
		return round(score, 3);
	}

	/**
	 * 
	 * http://en.wikipedia.org/wiki/Automated_Readability_Index
	 * 
	 * @param text
	 * @return the Automated Readability Index for text
	 */
	public double getARI() {
		double score = 4.71 * characters / words + 0.5 * words / sentences
				- 21.43;
		return round(score, 3);
	}

	/**
	 * 
	 * http://en.wikipedia.org/wiki/Gunning-Fog_Index
	 * 
	 * @param text
	 * @return the Gunning-Fog Index for text
	 */
	public double getGunningFog() {
		double score = 0.4 * (words / sentences + 100 * complex / words);
		return round(score, 3);
	}

	/**
	 * 
	 * http://en.wikipedia.org/wiki/Coleman-Liau_Index
	 * 
	 * @return The Coleman-Liau_Index value for the text
	 */
	public double getColemanLiau() {
		double score = (5.89 * characters / words) - (30 * sentences / words)
				- 15.8;
		return round(score, 3);
	}

	private static Double round(double d, int decimalPlace) {
		// see the Javadoc about why we use a String in the constructor
		// http://java.sun.com/j2se/1.5.0/docs/api/java/math/BigDecimal.html#BigDecimal(double)
		BigDecimal bd = new BigDecimal(Double.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.doubleValue();
	}
} 