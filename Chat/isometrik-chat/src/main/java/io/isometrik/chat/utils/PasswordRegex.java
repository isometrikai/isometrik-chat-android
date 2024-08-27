package io.isometrik.chat.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The helper class to validate the password using regex.
 */
//"(?=.*[0-9])" +         //at least 1 digit
//    "(?=.*[a-z])" +         //at least 1 lower case letter
//    "(?=.*[A-Z])" +         //at least 1 upper case letter
//    "(?=.*[a-zA-Z])" +      //any letter
//    "(?=.*[@#$%^&+=])" +    //at least 1 special character
//    "(?=\\S+$)" +           //no white spaces
//    ".{8,}" +               //at least 8 characters
//    "$")
public class PasswordRegex {
  /**
   * Is valid password boolean.
   *
   * @param password the password
   * @return the boolean
   */
  public static boolean isValidPassword(final String password) {

    Pattern pattern;
    Matcher matcher;

    final String PASSWORD_PATTERN =
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$";

    pattern = Pattern.compile(PASSWORD_PATTERN);
    matcher = pattern.matcher(password);

    return matcher.matches();
  }
}
