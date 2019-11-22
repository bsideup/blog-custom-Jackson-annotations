package com.example.model;

import com.example.annotations.FieldName;
import com.example.annotations.FromPrimitive;
import com.example.annotations.IgnoredField;
import com.example.annotations.ToPrimitive;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class User {

    @FieldName("first_name")
    String firstName;

    @FieldName("last_name")
    String lastName;

    int age;

    Sex sex;

    @FieldName("phone_number")
    PhoneNumber phoneNumber;

    @IgnoredField
    public boolean isAdult() {
        return age >= 18;
    }

    @Value
    public static class PhoneNumber {

        int countryCode;

        long number;

        @FromPrimitive
        public static PhoneNumber fromPrimitive(String string) {
            // Don't do this at home :D
            int beginIndex = string.indexOf("+") + 1;
            int endIndex = string.indexOf("-", beginIndex);
            return new PhoneNumber(
                    Integer.parseInt(string.substring(beginIndex, endIndex)),
                    Long.parseLong(string.substring(endIndex + 1))
            );
        }

        @ToPrimitive
        public String toPrimitive() {
            return "+" + countryCode + "-" + number;
        }
    }

    public enum Sex {

        @FieldName("m")
        MALE,

        @FieldName("f")
        FEMALE,

        @FieldName("u")
        UNKNOWN,
        ;
    }
}
