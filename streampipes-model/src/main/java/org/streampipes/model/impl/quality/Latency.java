package org.streampipes.model.impl.quality;

import org.streampipes.empire.annotations.RdfProperty;
import org.streampipes.empire.annotations.RdfsClass;

import javax.persistence.Entity;

@RdfsClass("ssn:Latency")
@Entity
public class Latency extends EventStreamQualityDefinition {

	private static final long serialVersionUID = -9211064635743833555L;
	
	@RdfProperty("sepa:hasQuantityValue")
	private float quantityValue;

	public Latency() {
		super();
	}
	
	public Latency(Latency other) {
		super(other);
		this.quantityValue = other.getQuantityValue();
	}
	
	public Latency(float quantityValue) {
		this.quantityValue = quantityValue;
	}
	
	public float getQuantityValue() {
		return quantityValue;
	}

	public void setQuantityValue(float quantityValue) {
		this.quantityValue = quantityValue;
	}
	

	//@Override
	public int compareTo(EventStreamQualityDefinition o) {
		Latency other = (Latency) o;
		if (other.getQuantityValue() == this.getQuantityValue()) {
			return 0;
			
		} else if ((other).getQuantityValue() > this.getQuantityValue()) {
			return -1;
		} else {
			return 1;
		}
	}

	
}
