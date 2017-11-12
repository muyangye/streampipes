package org.streampipes.model.impl;

import org.streampipes.empire.annotations.RdfProperty;
import org.streampipes.empire.annotations.RdfsClass;
import org.apache.commons.lang.RandomStringUtils;
import org.streampipes.model.NamedSEPAElement;
import org.streampipes.model.impl.quality.EventStreamQualityDefinition;
import org.streampipes.model.impl.quality.EventStreamQualityRequirement;
import org.streampipes.model.impl.quality.MeasurementCapability;
import org.streampipes.model.impl.quality.MeasurementObject;
import org.streampipes.model.util.Cloner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@RdfsClass("sepa:EventStream")
@Entity
public class EventStream extends NamedSEPAElement {

	private static final long serialVersionUID = -5732549347563182863L;
	
	private static final String prefix = "urn:fzi.de:eventstream:";

	@OneToMany(fetch = FetchType.EAGER,
			   cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@RdfProperty("sepa:hasEventStreamQualityDefinition")
	private transient List<EventStreamQualityDefinition> hasEventStreamQualities;

	@OneToMany(fetch = FetchType.EAGER,
			   cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@RdfProperty("sepa:hasEventStreamQualityRequirement")
	private transient List<EventStreamQualityRequirement> requiresEventStreamQualities;

	@OneToOne(fetch = FetchType.EAGER,
		   cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@RdfProperty("sepa:hasGrounding")
	private EventGrounding eventGrounding;
	
	@OneToOne(fetch = FetchType.EAGER,cascade = {CascadeType.ALL})
	@RdfProperty("sepa:hasSchema")
	private EventSchema eventSchema;
	
	@RdfProperty("sepa:measurementCapability")
	@OneToMany(fetch = FetchType.EAGER,cascade = {CascadeType.ALL})
	private List<MeasurementCapability> measurementCapability;
	
	@RdfProperty("sepa:measurementObject")
	@OneToMany(fetch = FetchType.EAGER,cascade = {CascadeType.ALL})
	private List<MeasurementObject> measurementObject;

	protected List<String> category;

	public EventStream(String uri, String name, String description, String iconUrl, List<EventStreamQualityDefinition> hasEventStreamQualities,
			EventGrounding eventGrounding, 
			EventSchema eventSchema) {
		super(uri, name, description, iconUrl);
		this.hasEventStreamQualities = hasEventStreamQualities;
		this.eventGrounding = eventGrounding;
		this.eventSchema = eventSchema;
	}
	
	public EventStream(String uri, String name, String description, EventSchema eventSchema)
	{
		super(uri, name, description);
		this.eventSchema = eventSchema;
	}

	public EventStream() {
		super(prefix +RandomStringUtils.randomAlphabetic(6));
	}


	public EventStream(EventStream other) {
		super(other);		
		if (other.getEventGrounding() != null) this.eventGrounding = new EventGrounding(other.getEventGrounding());
		if (other.getEventSchema() != null) this.eventSchema = new EventSchema(other.getEventSchema());
		if (other.getHasEventStreamQualities() != null) this.hasEventStreamQualities = other.getHasEventStreamQualities().stream().map(s -> new EventStreamQualityDefinition(s)).collect(Collectors.toCollection(ArrayList<EventStreamQualityDefinition>::new));
		if (other.getRequiresEventStreamQualities() != null) this.requiresEventStreamQualities = other.getRequiresEventStreamQualities().stream().map(s -> new EventStreamQualityRequirement(s)).collect(Collectors.toCollection(ArrayList<EventStreamQualityRequirement>::new));
		if (other.getMeasurementCapability() != null) this.measurementCapability =  new Cloner().mc(other.getMeasurementCapability());
		if (other.getMeasurementObject() != null) this.measurementObject = new Cloner().mo(other.getMeasurementObject());
	}


	public List<EventStreamQualityDefinition> getHasEventStreamQualities() {
		return hasEventStreamQualities;
	}

	public void setHasEventStreamQualities(
			List<EventStreamQualityDefinition> hasEventStreamQualities) {
		this.hasEventStreamQualities = hasEventStreamQualities;
	}
	

	public List<EventStreamQualityRequirement> getRequiresEventStreamQualities() {
		return requiresEventStreamQualities;
	}

	public void setRequiresEventStreamQualities(
			List<EventStreamQualityRequirement> requiresEventStreamQualities) {
		this.requiresEventStreamQualities = requiresEventStreamQualities;
	}

	public EventSchema getEventSchema() {
		return eventSchema;
	}

	public void setEventSchema(EventSchema eventSchema) {
		this.eventSchema = eventSchema;
	}

	public EventGrounding getEventGrounding() {
		return eventGrounding;
	}

	public void setEventGrounding(EventGrounding eventGrounding) {
		this.eventGrounding = eventGrounding;
	}

	public List<MeasurementCapability> getMeasurementCapability() {
		return measurementCapability;
	}

	public void setMeasurementCapability(
			List<MeasurementCapability> measurementCapability) {
		this.measurementCapability = measurementCapability;
	}

	public List<MeasurementObject> getMeasurementObject() {
		return measurementObject;
	}

	public void setMeasurementObject(List<MeasurementObject> measurementObject) {
		this.measurementObject = measurementObject;
	}

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }
}
