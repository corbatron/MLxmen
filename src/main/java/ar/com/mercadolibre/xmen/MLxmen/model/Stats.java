package ar.com.mercadolibre.xmen.MLxmen.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter 
@JsonPropertyOrder({ "count_mutant_dna", "count_human_dna","ratio" })
public class Stats implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9022478844203421564L;
	@JsonProperty("count_mutant_dna")
	@NonNull
	private Long mutantDna;
	@JsonProperty("count_human_dna")
	@NonNull
	private Long humanDna;
	private float ratio=0.00F;
	
	public Stats(Long mutantDna, Long humanDna) {
		super();
		this.mutantDna = mutantDna;
		this.humanDna = humanDna;
		if(humanDna!=0) {
			ratio = mutantDna.floatValue()/humanDna.floatValue();
		    BigDecimal bd = new BigDecimal(ratio);
		    ratio = bd.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
		}
	}
	
}
