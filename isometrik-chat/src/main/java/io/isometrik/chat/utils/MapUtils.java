package io.isometrik.chat.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * The utility class to convert a map to string.
 */
public class MapUtils {

  /**
   * Converts a map to string.
   *
   * @param map map to be converted to string
   * @return the string result of map converted to string
   */
  @SuppressWarnings("unchecked")
  public static String mapToString(Map<String, Object> map) {
    StringBuilder stringBuilder = new StringBuilder();

    for (String key : map.keySet()) {
      if (stringBuilder.length() > 0) {
        stringBuilder.append("&");
      }
      List<String> value = (List<String>) map.get(key);
      try {
        stringBuilder.append((key != null ? URLEncoder.encode(key, "UTF-8") : ""));
        stringBuilder.append("=");
        stringBuilder.append(value != null ? URLEncoder.encode(value.get(0), "UTF-8") : "");
      } catch (UnsupportedEncodingException e) {
        throw new RuntimeException("This method requires UTF-8 encoding support", e);
      }
    }

    return stringBuilder.toString();
  }

  /**
   * String list to csv string.
   *
   * @param ids the ids
   * @return the string
   */
  public static String stringListToCsv(List<String> ids) {

    StringBuilder csvBuilder = new StringBuilder();
    for (String id : ids) {
      csvBuilder.append(id);
      csvBuilder.append(",");
    }
    String csv = csvBuilder.toString();

    if (csv.length() > 0) {
      return csv.substring(0, csv.length() - 1);
    } else {
      return "None";
    }
  }

  /**
   * Integer list to csv string.
   *
   * @param ids the ids
   * @return the string
   */
  public static String integerListToCsv(List<Integer> ids) {

    StringBuilder csvBuilder = new StringBuilder();
    for (Integer id : ids) {
      csvBuilder.append(id);
      csvBuilder.append(",");
    }
    String csv = csvBuilder.toString();

    if (csv.length() > 0) {
      return csv.substring(0, csv.length() - 1);
    } else {
      return "None";
    }
  }
}
