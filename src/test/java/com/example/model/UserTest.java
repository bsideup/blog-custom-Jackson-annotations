package com.example.model;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserTest {

    static final ObjectMapper objectMapper = new ObjectMapper()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .setAnnotationIntrospector(new CustomJacksonAnnotationIntrospector());

    static final User USER = new User(
            "John",
            "Smith",
            25,
            User.Sex.MALE,
            new User.PhoneNumber(1, 2065550100)
    );

    static final String JSON = "{" +
            "\"age\":25," +
            "\"sex\":\"m\"," +
            "\"first_name\":\"John\"," +
            "\"last_name\":\"Smith\"," +
            "\"phone_number\":\"+1-2065550100\"" +
            "}";

    @Test
    public void testSerialization() throws Exception {
        assertEquals(JSON, objectMapper.writeValueAsString(USER));
    }

    @Test
    public void testDeserialization() throws Exception {
        assertEquals(USER, objectMapper.readValue(JSON, User.class));
    }
}