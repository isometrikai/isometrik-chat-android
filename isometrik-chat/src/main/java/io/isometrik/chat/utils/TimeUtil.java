package io.isometrik.chat.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import io.isometrik.ui.IsometrikChatSdk;
import io.isometrik.chat.R;

/**
 * The helper class to format epoch timestamp to human readable datetime.
 */
public class TimeUtil {

  /**
   * Format timestamp to only date string.
   *
   * @param epochTimestamp the epoch timestamp
   * @return the string
   */
  public static String formatTimestampToOnlyDate(long epochTimestamp) {

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
      LocalDateTime localDateTimeFromEpochTimestamp =
          LocalDateTime.ofEpochSecond(epochTimestamp / 1000, 0,
              OffsetDateTime.now(ZoneId.systemDefault()).getOffset());

      LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.systemDefault());
      int hourValue = localDateTimeFromEpochTimestamp.getHour();
      int minuteValue = localDateTimeFromEpochTimestamp.getMinute();

      if (localDateTimeFromEpochTimestamp.toString()
          .substring(0, 10)
          .equals(currentDateTime.toString().substring(0, 10))) {
        String hour;
        String minute;
        if (hourValue < 10) {
          hour = "0" + hourValue;
        } else {
          hour = String.valueOf(hourValue);
        }
        if (minuteValue < 10) {
          minute = "0" + minuteValue;
        } else {
          minute = String.valueOf(minuteValue);
        }
        return hour + ":" + minute;
      } else if (localDateTimeFromEpochTimestamp.getYear() == currentDateTime.getYear()) {
        if (Math.abs(
            localDateTimeFromEpochTimestamp.getDayOfYear() - currentDateTime.getDayOfYear()) == 1) {
          return IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_yesterday);
        }
      }
      return localDateTimeFromEpochTimestamp.toString().substring(0, 10);
    } else {

      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS z", Locale.US);
      dateFormat.setTimeZone(TimeZone.getDefault());

      String currentDateTimeString = dateFormat.format(new Date());
      String localDateTimeFromEpochTimestampString = dateFormat.format(new Date(epochTimestamp));

      if ((currentDateTimeString.substring(0, 8)).equals(
          (localDateTimeFromEpochTimestampString.substring(0, 8)))) {

        //return "Today";
        return localDateTimeFromEpochTimestampString.substring(8, 10)
            + ":"
            + localDateTimeFromEpochTimestampString.substring(10, 12);
      } else if ((Long.parseLong((currentDateTimeString.substring(0, 8))) - Long.parseLong(
          (localDateTimeFromEpochTimestampString.substring(0, 8)))) == 1) {

        return IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_yesterday);
      } else {
        return localDateTimeFromEpochTimestampString.substring(0, 4)
            + "-"
            + localDateTimeFromEpochTimestampString.substring(4, 6)
            + "-"
            + localDateTimeFromEpochTimestampString.substring(6, 8);
      }
    }
  }

  /**
   * Format timestamp to both date and time string.
   *
   * @param epochTimestamp the epoch timestamp
   * @return the string
   */
  public static String formatTimestampToBothDateAndTime(long epochTimestamp) {

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
      LocalDateTime localDateTimeFromEpochTimestamp =
          LocalDateTime.ofEpochSecond(epochTimestamp / 1000, 0,
              OffsetDateTime.now(ZoneId.systemDefault()).getOffset());

      LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.systemDefault());
      int hourValue = localDateTimeFromEpochTimestamp.getHour();
      int minuteValue = localDateTimeFromEpochTimestamp.getMinute();
      String hour;
      String minute;
      if (hourValue < 10) {
        hour = "0" + hourValue;
      } else {
        hour = String.valueOf(hourValue);
      }
      if (minuteValue < 10) {
        minute = "0" + minuteValue;
      } else {
        minute = String.valueOf(minuteValue);
      }

      if (localDateTimeFromEpochTimestamp.toString()
          .substring(0, 10)
          .equals(currentDateTime.toString().substring(0, 10))) {
        return IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_today)
            + IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_at)
            + hour
            + ":"
            + minute;
      } else if (localDateTimeFromEpochTimestamp.getYear() == currentDateTime.getYear()) {
        if (Math.abs(
            localDateTimeFromEpochTimestamp.getDayOfYear() - currentDateTime.getDayOfYear()) == 1) {
          return IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_yesterday)
              + IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_at)
              + hour
              + ":"
              + minute;
        }
      }
      return localDateTimeFromEpochTimestamp.toString().substring(0, 10)
          + IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_at)
          + hour
          + ":"
          + minute;
    } else {

      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS z", Locale.US);
      dateFormat.setTimeZone(TimeZone.getDefault());

      String currentDateTimeString = dateFormat.format(new Date());
      String localDateTimeFromEpochTimestampString = dateFormat.format(new Date(epochTimestamp));

      if ((currentDateTimeString.substring(0, 8)).equals(
          (localDateTimeFromEpochTimestampString.substring(0, 8)))) {

        return IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_today)
            + IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_at)
            + localDateTimeFromEpochTimestampString.substring(8, 10)
            + ":"
            + localDateTimeFromEpochTimestampString.substring(10, 12);
      } else if ((Long.parseLong((currentDateTimeString.substring(0, 8))) - Long.parseLong(
          (localDateTimeFromEpochTimestampString.substring(0, 8)))) == 1) {

        return IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_yesterday)
            + IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_at)
            + localDateTimeFromEpochTimestampString.substring(8, 10)
            + ":"
            + localDateTimeFromEpochTimestampString.substring(10, 12);
      } else {
        return

            localDateTimeFromEpochTimestampString.substring(0, 4)
                + "-"
                + localDateTimeFromEpochTimestampString.substring(4, 6)
                + "-"
                + localDateTimeFromEpochTimestampString.substring(6, 8)
                + IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_at)
                + localDateTimeFromEpochTimestampString.substring(8, 10)
                + ":"
                + localDateTimeFromEpochTimestampString.substring(10, 12);
      }
    }
  }

  //private static String getTimeAfterFormatting(String dateAfterFormat) {
  //  return convert24To12HourFormat(dateAfterFormat.substring(0, 9));
  //}
  //
  //private static String formatDate(String timestamp) {
  //
  //  String s = null;
  //  Date date;
  //
  //  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS z", Locale.US);
  //
  //  try {
  //    date = dateFormat.parse(timestamp);
  //
  //    dateFormat = new SimpleDateFormat("HH:mm:ss EEE dd/MMM/yyyy z", Locale.US);
  //    if (date != null) {
  //      s = dateFormat.format(date);
  //    }
  //  } catch (ParseException ignore) {
  //  }
  //
  //  return s;
  //}
  //
  //private static String timestampInGmt() {
  //
  //  Date date =
  //      new Date(System.currentTimeMillis());// - AppController.getInstance().getTimeDelta());
  //
  //  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS z", Locale.US);
  //  dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
  //
  //  return dateFormat.format(date);
  //}
  //
  //private static String timestampFromGmt(String tsInGMT) {
  //
  //  Date date;
  //  String s = null;
  //
  //  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS z", Locale.US);
  //
  //  try {
  //    date = new Date(Long.parseLong(tsInGMT));//dateFormat.parse(tsInGMT);
  //    TimeZone timeZone = TimeZone.getDefault();
  //    dateFormat.setTimeZone(timeZone);
  //
  //    if (date != null) {
  //      s = dateFormat.format(date);
  //    }
  //  } catch (Exception ignore) {
  //    ignore.printStackTrace();
  //  }
  //
  //  return s;
  //}
  //
  ///*
  // *
  // * To convert date from  24 hour format to the 12 hour format
  // * */
  //
  ///**
  // * @param date date in 24 hour format
  // * @return date in 12 hour format
  // */
  //
  //private static String convert24To12HourFormat(String date) {
  //
  //  String dateIn12hourFormat = null;
  //
  //  try {
  //    final SimpleDateFormat dateFormat = new SimpleDateFormat("H:mm", Locale.US);
  //    final Date dateObj = dateFormat.parse(date);
  //
  //    if (dateObj != null) {
  //      dateIn12hourFormat = new SimpleDateFormat("h:mm a", Locale.US).format(dateObj);
  //    }
  //  } catch (final ParseException ignore) {
  //  }
  //
  //  return dateIn12hourFormat;
  //}
}
