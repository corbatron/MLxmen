package ar.com.mercadolibre.xmen.MLxmen.model;

import java.io.Serializable;
import java.util.regex.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class DNA implements Serializable{

	private static final long serialVersionUID = -7003933868662096317L;
	private static int minSequence = 4;
	private static final String mutantRegex = "A{"+minSequence+"}|C{"+minSequence+"}|G{"+minSequence+"}|T{"+minSequence+"}";
	private static final Pattern mutantPattern = Pattern.compile(mutantRegex);

	private String[] dna;
	private boolean mutant;

	public DNA(String[] dna) {
		this.dna=dna;
	}
	public static boolean matchMutantPattern(String s) {
		return mutantPattern.matcher(s).find();
	}
	
	public static int numberOfMatches(String input) {
		int originalLenght = input.length();
		int replacedLenght = input.replaceAll(mutantRegex, "").length();			
		return (originalLenght-replacedLenght)/minSequence;
	}
	
}
