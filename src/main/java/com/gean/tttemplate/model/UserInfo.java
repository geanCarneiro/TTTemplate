/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gean.tttemplate.model;

import com.twitter.clientlib.TwitterCredentialsOAuth2;
import java.io.Serializable;

/**
 *
 * @author GeanCarneiro
 */
public class UserInfo implements Serializable{
    
    private TwitterCredentialsOAuth2 credentials;
    private UsersMeResponse user;

    public TwitterCredentialsOAuth2 getCredentials() {
        return credentials;
    }

    public void setCredentials(TwitterCredentialsOAuth2 credentials) {
        this.credentials = credentials;
    }

    public UsersMeResponse getUser() {
        return user;
    }

    public void setUsers(UsersMeResponse user) {
        this.user = user;
    }
    
    
    
}
