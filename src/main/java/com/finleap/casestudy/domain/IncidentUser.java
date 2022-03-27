package com.finleap.casestudy.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Objects;

@Document(collection = "users")
public class IncidentUser  {

    @Id
    @Field(value = "_id")
    private String userId;

    @JsonProperty("username")
    @Indexed(unique=true)
    @NotNull(message = "userName cannot be null")
    private String username;

    @JsonProperty("password")
    @Size(min = 5, message = "password length should be at least 5 characters")
    @NotNull(message = "password cannot be null")
    private String password;

    @JsonProperty("isAssigned")
    private boolean isAssigned;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAssigned() {
        return isAssigned;
    }

    public void setAssigned(boolean assigned) {
        isAssigned = assigned;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IncidentUser incidentUser = (IncidentUser) o;
        return isAssigned == incidentUser.isAssigned && Objects.equals(userId, incidentUser.userId) && Objects.equals(username, incidentUser.username) && Objects.equals(password, incidentUser.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, password, isAssigned);
    }

    @Override
    public String toString() {
        return "User {" +
                "userId = '" + userId + '\'' +
                ", userName = '" + username + '\'' +
                ", password = '" + password + '\'' +
                ", isAssigned = " + isAssigned +
                '}';
    }
}
