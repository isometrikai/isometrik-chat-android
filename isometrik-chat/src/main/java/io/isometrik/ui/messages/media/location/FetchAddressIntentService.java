package io.isometrik.ui.messages.media.location;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Asynchronously handles an intent using a worker thread. Receives a ResultReceiver object and a
 * location through an intent. Tries to fetch the address for the location using a Geocoder, and
 * sends the result to the ResultReceiver.
 */
public class FetchAddressIntentService extends IntentService {
  private static final String TAG = "FetchAddressIS";

  /**
   * The receiver where results are forwarded from this service.
   */
  protected ResultReceiver mReceiver;

  /**
   * This constructor is required, and calls the super IntentService(String)
   * constructor with the name for a worker thread.
   */
  public FetchAddressIntentService() {
    // Use the TAG to name the worker thread.
    super(TAG);
  }

  /**
   * Tries to get the location address using a Geocoder. If successful, sends an address to a
   * result receiver. If unsuccessful, sends an error message instead.
   * Note: We define a {@link ResultReceiver} in * MainActivity to process content
   * sent from this service.
   * <p>
   * This service calls this method from the default worker thread with the intent that started
   * the service. When this method returns, the service automatically stops.
   */
  @Override
  protected void onHandleIntent(Intent intent) {
    String errorMessage = "";

    mReceiver = intent.getParcelableExtra(LocationConstants.RECEIVER);

    // Check if receiver was properly registered.
    if (mReceiver == null) {
      Log.wtf(TAG, "No receiver received. There is nowhere to send the results.");
      return;
    }
    // Get the location passed to this service through an extra.
    Location location = intent.getParcelableExtra(LocationConstants.LOCATION_DATA_EXTRA);

    // Make sure that the location data was really sent over through an extra. If it wasn't,
    // send an error error message and return.
    if (location == null) {
      errorMessage = "No location data provided";
      Log.wtf(TAG, errorMessage);
      deliverResultToReceiver(LocationConstants.FAILURE_RESULT, errorMessage, null);
      return;
    }

    // Errors could still arise from using the Geocoder (for example, if there is no
    // connectivity, or if the Geocoder is given illegal location data). Or, the Geocoder may
    // simply not have an address for a location. In all these cases, we communicate with the
    // receiver using a resultCode indicating failure. If an address is found, we use a
    // resultCode indicating success.

    // The Geocoder used in this sample. The Geocoder's responses are localized for the given
    // Locale, which represents a specific geographical or linguistic region. Locales are used
    // to alter the presentation of information such as numbers or dates to suit the conventions
    // in the region they describe.
    Geocoder geocoder = new Geocoder(this, Locale.getDefault());

    // Address found using the Geocoder.
    List<Address> addresses = null;

    try {
      // Using getFromLocation() returns an array of Addresses for the area immediately
      // surrounding the given latitude and longitude. The results are a best guess and are
      // not guaranteed to be accurate.
      addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),
          // In this sample, we get just a single address.
          1);
    } catch (IOException ioException) {
      // Catch network or other I/O problems.
      errorMessage = "Service not available";
      Log.e(TAG, errorMessage, ioException);
    } catch (IllegalArgumentException illegalArgumentException) {
      // Catch invalid latitude or longitude values.
      errorMessage = "Invalid LatLong Used";
      Log.e(TAG,
          errorMessage + ". " + "Latitude = " + location.getLatitude() + ", Longitude = " + location
              .getLongitude(), illegalArgumentException);
    }

    // Handle case where no address was found.
    if (addresses == null || addresses.size() == 0) {
      if (errorMessage.isEmpty()) {
        errorMessage = "No address found";
        Log.e(TAG, errorMessage);
      }
      deliverResultToReceiver(LocationConstants.FAILURE_RESULT, errorMessage, null);
    } else {
      Address address = addresses.get(0);
      ArrayList<String> addressFragments = new ArrayList<>();

      for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
        addressFragments.add(address.getAddressLine(i));
      }
      deliverResultToReceiver(LocationConstants.SUCCESS_RESULT,
          TextUtils.join(System.getProperty("line.separator"), addressFragments), address);
      //TextUtils.split(TextUtils.join(System.getProperty("line.separator"), addressFragments), System.getProperty("line.separator"));
    }
  }

  /**
   * Sends a resultCode and message to the receiver.
   */
  private void deliverResultToReceiver(int resultCode, String message, Address address) {
    try {
      Bundle bundle = new Bundle();
      bundle.putString(LocationConstants.RESULT_DATA_KEY, message);
      if (resultCode == LocationConstants.SUCCESS_RESULT && address != null) {

        bundle.putString(LocationConstants.LOCATION_DATA_AREA, address.getSubLocality());
        bundle.putString(LocationConstants.LOCATION_DATA_CITY, address.getLocality());
        bundle.putString(LocationConstants.LOCATION_DATA_COUNTRY, address.getCountryName());
        bundle.putString(LocationConstants.LOCATION_DATA_STREET, address.getAddressLine(0));
        bundle.putString(LocationConstants.LOCATION_DATA_NAME, address.getFeatureName());
        mReceiver.send(resultCode, bundle);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}