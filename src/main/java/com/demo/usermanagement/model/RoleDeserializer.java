package com.demo.usermanagement.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

public class RoleDeserializer extends JsonDeserializer<User.Role> {
    @Override
    public User.Role deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText();
        return User.Role.valueOf(value.toUpperCase());
    }
}
