package com.example.midtermapplication;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NoLineBreaksInputFilter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        for (int i = start; i < end; i++) {
            if (source.charAt(i) == '\n' || source.charAt(i) == '\r') {
                return "";
            }
        }
        return null;
    }
    public static boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.endsWith(".com");
    }

    public static boolean containsSpecialCharacter(String input) {
        String specialCharacterRegex = "[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]";

        Pattern pattern = Pattern.compile(specialCharacterRegex);

        Matcher matcher = pattern.matcher(input);

        return matcher.find();
    }
}
