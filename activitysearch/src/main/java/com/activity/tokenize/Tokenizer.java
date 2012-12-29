package com.activity.tokenize;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;





public class Tokenizer {
	private static Set<String> stopWords;
	private static Stemmer stemmer;

	public List<String> tokenize(String sentence) {
		List<String> words = delimiter(sentence);
		words = stopWords(words);
		words = stemmer(words);
		return words;
	}
	
	public static synchronized void initializeStopWords() {		
		try {
			if(stopWords==null) {
				stopWords = new HashSet<String>();
				InputStream stopWordsFile = Tokenizer.class.getClassLoader().getResourceAsStream("stopwords.txt");
				BufferedReader reader = new BufferedReader(new InputStreamReader(stopWordsFile));
				String line;
				while((line=reader.readLine())!=null) {
					String[] words = line.split(",");
					for(String word: words){
						stopWords.add(word);
					}
				}
				reader.close();
			}
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static synchronized void initializeStemmer() {
		stemmer = new Stemmer();
	}

	
	public List<String> delimiter(String sentence) {
		String[] splits = sentence.split("[\\s]");
		List<String> processedTerms = new ArrayList<String>();
		for(String term: splits) {
			String processedTerm = term.replaceAll("[^A-Za-z$]", " "); //Remove all special chars if any
			processedTerm = processedTerm.trim();
			String[] subSplits = processedTerm.split("[\\s]");
			
			for(String t: subSplits) {
				t = t.trim();
				if(t.length()<=1 || t==""){
					continue;
				}
				processedTerms.add(t);
			}
		}
		return processedTerms;
	}
	
	public List<String> stopWords(List<String> words) {
		Tokenizer.initializeStopWords();
		List<String> filteredWords = new ArrayList<String>();
		for(String word: words) {
			word = word.toLowerCase();
			if(!stopWords.contains(word)) {
				filteredWords.add(word);
			}
		}
		return filteredWords;
	}
	
	public List<String> stemmer(List<String> words) {
		Tokenizer.initializeStemmer();
		List<String> processedTerms = new ArrayList<String>();
		for(String word: words) {
			stemmer.add(word.toCharArray(), word.length());
			stemmer.stem();
			processedTerms.add(stemmer.toString());
		}
		return processedTerms;
	}
}
