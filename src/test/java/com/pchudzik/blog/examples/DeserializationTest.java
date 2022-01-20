package com.pchudzik.blog.examples;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.google.gson.Gson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class DeserializationTest {
    static class SampleObject {
        public String message;
        public Code code;
        public String extraProperty;

        enum Code {
            OK,
            FAIL
        }
    }

    static class OtherObject {
        public String message;
        public String code;
        public int extraProperty;
    }

    static final String SampleObjectJson = """
            {
                "message":"Hello World",
                "code": "OK",
                "extraProperty": "cool"
            }
            """;

    static final String OtherObjectJson = """
            {
                "message": "Other Hello World",
                "code": "All Good",
                "extraProperty": 42
            }
            """;

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Gson gson = new Gson();

    @Test
    public void jackson_deserializes_objects() throws JsonProcessingException {
        var sampleObject = objectMapper.readValue(SampleObjectJson, SampleObject.class);
        assertEquals(SampleObject.Code.OK, sampleObject.code);
        assertEquals("Hello World", sampleObject.message);
        assertEquals("cool", sampleObject.extraProperty);


        var otherObject = objectMapper.readValue(OtherObjectJson, OtherObject.class);
        assertEquals("All Good", otherObject.code);
        assertEquals("Other Hello World", otherObject.message);
        assertEquals(42, otherObject.extraProperty);
    }

    @Test
    public void gson_deserializes_objects() {
        var sampleObject = gson.fromJson(SampleObjectJson, SampleObject.class);
        assertEquals(SampleObject.Code.OK, sampleObject.code);
        assertEquals("Hello World", sampleObject.message);
        assertEquals("cool", sampleObject.extraProperty);

        var otherObject = gson.fromJson(OtherObjectJson, OtherObject.class);
        assertEquals("All Good", otherObject.code);
        assertEquals("Other Hello World", otherObject.message);
        assertEquals(42, otherObject.extraProperty);
    }

    @Test
    public void jackson_fails_when_deserializing_wrong_object() {
        assertThrows(
                InvalidFormatException.class,
                () -> objectMapper.readValue(OtherObjectJson, SampleObject.class)
        );
    }

    @Test
    @Disabled
    public void print_jackson_error() throws JsonProcessingException {
        objectMapper.readValue(OtherObjectJson, SampleObject.class);
    }

    @Test
    public void gson_works_when_deserializing_wrong_object() {
        var sampleObject = gson.fromJson(OtherObjectJson, SampleObject.class);
        assertNull(sampleObject.code);
        assertEquals("Other Hello World", sampleObject.message);
        assertEquals("42", sampleObject.extraProperty);
        assertNull(sampleObject.code);
    }
}
