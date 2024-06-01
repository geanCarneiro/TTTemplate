/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gean.tttemplate.model.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;
import com.gean.tttemplate.model.UsersMeResponse;
import java.io.IOException;

/**
 *
 * @author GeanCarneiro
 */
public class UsersMeResponseDeserializer extends StdDeserializer<UsersMeResponse> {

    public UsersMeResponseDeserializer() {
        this(null);
    }
    
    public UsersMeResponseDeserializer(Class<?> vc) {
        super(vc);
    }
    
    @Override
    public UsersMeResponse deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        UsersMeResponse usersMeResponse = new UsersMeResponse();
        TreeNode data = p.getCodec().readTree(p).get("data");
        
        usersMeResponse.setId(((TextNode)data.get("id")).asText());
        usersMeResponse.setName(((TextNode)data.get("name")).asText());
        usersMeResponse.setUsername(((TextNode)data.get("username")).asText());
        
        return usersMeResponse;
        
    }
    
}
