package com.finleap.casestudy.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeName("users")
public class UserWrapper {

	@JsonProperty("users")
	private List<IncidentUser> incidentUsers;

	public UserWrapper(List<IncidentUser> incidentUsers) {
		this.incidentUsers = incidentUsers;
	}

	public List<IncidentUser> getUsers() {
		return incidentUsers;
	}

	public void setUsers(List<IncidentUser> incidentUsers) {
		this.incidentUsers = incidentUsers;
	}

	@Override
	public String toString() {
		return "UserWrapper{"
				+ "users=" + incidentUsers
				+ '}';
	}
}
