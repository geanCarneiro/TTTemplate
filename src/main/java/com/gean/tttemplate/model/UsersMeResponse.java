/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gean.tttemplate.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.gean.tttemplate.model.deserializer.UsersMeResponseDeserializer;

/**
 *
 * @author GeanCarneiro
 */
@JsonDeserialize(using = UsersMeResponseDeserializer.class)
public class UsersMeResponse {
    
    private String id;
    private String name;
    private String username;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    
    
}
