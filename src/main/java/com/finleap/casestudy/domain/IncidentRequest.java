package com.finleap.casestudy.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;


public class IncidentRequest {

    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    //@NotNull(message = "Incident title cannot be null")
    @NotBlank(message = "Incident title cannot be empty")
    private String title;

    @JsonProperty("status")
    private Status status;

    @JsonProperty("assignee")
    private String assignee;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IncidentRequest that = (IncidentRequest) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title) && status == that.status && Objects.equals(assignee, that.assignee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, status, assignee);
    }

    @Override
    public String toString() {
        return "IncidentUpdateRequest{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", assignee='" + assignee + '\'' +
                '}';
    }
}
