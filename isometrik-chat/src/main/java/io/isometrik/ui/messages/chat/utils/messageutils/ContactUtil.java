package io.isometrik.ui.messages.chat.utils.messageutils;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The helper class to parse contact message data and handle click on contact message cell.
 */
public class ContactUtil {

    /**
     * Parse contacts data json object.
     *
     * @param data            the data
     * @param contentResolver the content resolver
     * @return the json object
     */
    public static JSONObject parseContactsData(Intent data, ContentResolver contentResolver) {
        JSONObject contacts = new JSONObject();
        JSONArray array = new JSONArray();
        JSONObject contactDetails = null;

        try (Cursor cursor = contentResolver.query(data.getData(), null, null, null, null)) {
            cursor.moveToFirst();
            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            contactDetails = new JSONObject();
            contactDetails.put("contactName", cursor.getString(nameIndex));
            contactDetails.put("contactIdentifier", cursor.getString(phoneIndex).replaceAll("\\s+", ""));
            contactDetails.put("contactImageUrl", "");

            array.put(contactDetails);
            contacts.put("contacts", array);
            return contacts;
        } catch (Exception ignore) {
            return contactDetails;
        }
    }

    /**
     * Parse contacts data json object.
     *
     * @param contactName       the contact name
     * @param contactIdentifier the contact identifier
     * @param contactImageUrl   the contact image url
     * @return the json object
     */
    public static JSONObject parseContactsData(String contactName, String contactIdentifier, String contactImageUrl) {

        JSONObject contactDetails = new JSONObject();
        try {
            contactDetails.put("contactName", contactName);
            contactDetails.put("contactIdentifier", contactIdentifier);
            contactDetails.put("contactImageUrl", contactImageUrl);
        } catch (JSONException ignore) {

        }
        return contactDetails;

    }
}
