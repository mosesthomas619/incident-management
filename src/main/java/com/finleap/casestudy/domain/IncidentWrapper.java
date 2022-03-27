package com.finleap.casestudy.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeName("incidents")
public class IncidentWrapper {

	@JsonProperty("incidents")
	private List<IncidentDO> incidents;

	public IncidentWrapper(List<IncidentDO> incidents) {
		this.incidents = incidents;
	}

	public List<IncidentDO> getIncidents() {
		return incidents;
	}

	public void setIncidents(List<IncidentDO> incidents) {
		this.incidents = incidents;
	}

	@Override
	public String toString() {
		return "IncidentWrapper{"
				+ "incidents=" + incidents
				+ '}';
	}
}
