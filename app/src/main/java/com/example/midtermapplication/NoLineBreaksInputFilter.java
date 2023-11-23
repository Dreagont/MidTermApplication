package com.example.midtermapplication;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NoLineBreaksInputFilter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        // Iterate through the source characters
        for (int i = start; i < end; i++) {
            // If the character is a line break (newline or carriage return), ignore it
            if (source.charAt(i) == '\n' || source.charAt(i) == '\r') {
                return "";
            }
        }
        // If no line breaks are found, accept the input
        return null;
    }
    public static boolean isValidEmail(String email) {
        // Check if the email contains '@' and ends with '.com'
        return email != null && email.contains("@") && email.endsWith(".com");
    }

    public static boolean containsSpecialCharacter(String input) {
        // Define a regular expression for special characters
        String specialCharacterRegex = "[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]";

        // Compile the regex pattern
        Pattern pattern = Pattern.compile(specialCharacterRegex);

        // Create a matcher object
        Matcher matcher = pattern.matcher(input);

        // Check if the input contains a special character
        return matcher.find();
    }
}
