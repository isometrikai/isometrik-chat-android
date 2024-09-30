package io.isometrik.ui.messages.media.location;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import io.isometrik.ui.IsometrikChatSdk;
import io.isometrik.chat.R;
import io.isometrik.chat.databinding.IsmActivityShareLocationBinding;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * The activity to share location with option to drag to change selected place and search for a
 * location.
 */
public class ShareLocationActivity extends AppCompatActivity implements OnMapReadyCallback {
  private GoogleMap googleMap;
  private LatLng pinnedAtCenterLatLong;
  private String selectedLatitude = "0.0", selectedLongitude = "0.0";
  private View mapView;

  private AddressResultReceiver mResultReceiver;

  private String selectedLocationName;
  private String selectedLocationAddress;
  private IsmActivityShareLocationBinding ismActivityShareLocationBinding;

  private FusedLocationProviderClient fusedLocationClient;
  private LocationCallback locationCallback;
  private ActivityResultLauncher<Intent> placeAutocompleteActivityLauncher;

  @SuppressLint("MissingPermission")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ismActivityShareLocationBinding = IsmActivityShareLocationBinding.inflate(getLayoutInflater());
    View view = ismActivityShareLocationBinding.getRoot();
    setContentView(view);

    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    SupportMapFragment mapFragment =
        (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.frMap);
    if (mapFragment != null) {
      mapView = mapFragment.getView();
      mapFragment.getMapAsync(this);
    }
    mResultReceiver = new AddressResultReceiver(new Handler());

    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
      if (location != null) {

        repositionGoogleMap(location);
      } else {

        fusedLocationClient.requestLocationUpdates(LocationRequest.create()
                .setInterval(10000)
                .setFastestInterval(5000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY), locationCallback,
            Looper.getMainLooper());
      }
    });

    locationCallback = new LocationCallback() {
      @Override
      public void onLocationResult(@NotNull LocationResult locationResult) {

        Location location = locationResult.getLocations().get(0);

        if (location != null) {
          repositionGoogleMap(location);
          fusedLocationClient.removeLocationUpdates(locationCallback);
        }
      }
    };

    ismActivityShareLocationBinding.btShareLocation.setOnClickListener(v -> {
      Intent intent = new Intent();
      intent.putExtra("latitude", selectedLatitude);
      intent.putExtra("longitude", selectedLongitude);
      intent.putExtra("locationAddress", selectedLocationAddress);
      intent.putExtra("locationName", selectedLocationName);
      setResult(Activity.RESULT_OK, intent);
      supportFinishAfterTransition();
    });

    placeAutocompleteActivityLauncher =
        registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
          if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {

            // Get the user's selected place from the Intent.
            Place place = Autocomplete.getPlaceFromIntent(result.getData());

            LatLng latLong = place.getLatLng();

            if (latLong == null) {

              Toast.makeText(this, R.string.ism_location_selection_failed, Toast.LENGTH_SHORT)
                  .show();
            } else {
              //To handle the case when user immediately click on send location after coming back from autocomplete places screen, without waiting for camera to reposition
              if (place.getName() != null) {
                selectedLocationName = place.getName();
              } else {
                selectedLocationName = getString(R.string.ism_not_applicable);
              }

              if (place.getAddress() != null) {
                selectedLocationAddress = place.getAddress();
              } else {
                selectedLocationAddress = getString(R.string.ism_not_applicable);
              }

              ismActivityShareLocationBinding.tvLocation.setText(selectedLocationAddress);
              selectedLatitude = String.valueOf(latLong.latitude);
              selectedLongitude = String.valueOf(latLong.longitude);
              CameraPosition cameraPosition =
                  new CameraPosition.Builder().target(latLong).zoom(17f).tilt(0).build();

              googleMap.setMyLocationEnabled(true);
              googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
          } else {
            Toast.makeText(this, R.string.ism_location_selection_canceled, Toast.LENGTH_SHORT)
                .show();
          }
        });
    ismActivityShareLocationBinding.rlSearch.setOnClickListener(v -> openAutocompleteActivity());
    ismActivityShareLocationBinding.ibBack.setOnClickListener(v -> onBackPressed());
  }

  private void openAutocompleteActivity() {

    if (!Places.isInitialized()) {

      Places.initialize(getApplicationContext(),
          IsometrikChatSdk.getInstance().getIsometrik().getConfiguration().getGooglePlacesApiKey());
    }

    // Set the fields to specify which types of place data to return.
    List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

    // Start the autocomplete intent.
    placeAutocompleteActivityLauncher.launch(
        new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this));
  }

  /**
   * Manipulates the map once available.
   * This callback is triggered when the map is ready to be used.
   * This is where we can add markers or lines, add listeners or move the camera. In this case,
   * we just add a marker near own current location.
   * If Google Play services is not installed on the device, the user will be prompted to install
   * it inside the SupportMapFragment. This method will only be triggered once the user has
   * installed Google Play services and returned to the app.
   */
  @Override
  public void onMapReady(@NotNull GoogleMap googleMap) {
    this.googleMap = googleMap;

    // Change MyLocationButton position
    if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
      // Get the button view
      View locationButton =
          ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(
              Integer.parseInt("2"));
      // and next place it, on bottom right (as Google Maps app)
      RelativeLayout.LayoutParams layoutParams =
          (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
      // position on right bottom
      layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
      layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
      layoutParams.setMargins(0, 0, 30, 30);
    }

    this.googleMap.setOnCameraIdleListener(() -> {
      pinnedAtCenterLatLong = this.googleMap.getCameraPosition().target;
      this.googleMap.clear();
      try {
        Location mLocation = new Location("");
        mLocation.setLatitude(pinnedAtCenterLatLong.latitude);
        mLocation.setLongitude(pinnedAtCenterLatLong.longitude);

        startIntentService(mLocation);
        selectedLatitude = String.valueOf(pinnedAtCenterLatLong.latitude);
        selectedLongitude = String.valueOf(pinnedAtCenterLatLong.longitude);

      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }

  @SuppressLint("MissingPermission")
  private void repositionGoogleMap(Location location) {
    // check if map is created successfully or not
    if (googleMap != null) {
      googleMap.getUiSettings().setZoomControlsEnabled(false);
      LatLng latLong;

      latLong = new LatLng(location.getLatitude(), location.getLongitude());

      CameraPosition cameraPosition =
          new CameraPosition.Builder().target(latLong).zoom(17f).tilt(0).build();

      googleMap.setMyLocationEnabled(true);
      googleMap.getUiSettings().setMyLocationButtonEnabled(true);
      googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
      googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

      selectedLatitude = String.valueOf(location.getLatitude());
      selectedLongitude = String.valueOf(location.getLongitude());

      startIntentService(location);
    } else {
      Toast.makeText(getApplicationContext(), getString(R.string.ism_unable_to_show_maps),
          Toast.LENGTH_SHORT).show();
    }
  }

  /**
   * Receiver for data sent from FetchAddressIntentService.
   */
  private class AddressResultReceiver extends ResultReceiver {
    /**
     * Instantiates a new Address result receiver.
     *
     * @param handler the handler
     */
    AddressResultReceiver(Handler handler) {
      super(handler);
    }

    /**
     * Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
     */
    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {

      // Display the address string or an error message sent from the intent service.
      String selectedAddress = resultData.getString(LocationConstants.RESULT_DATA_KEY);
      String selectedArea = resultData.getString(LocationConstants.LOCATION_DATA_AREA);
      String selectedCity = resultData.getString(LocationConstants.LOCATION_DATA_CITY);
      String selectedStreet = resultData.getString(LocationConstants.LOCATION_DATA_STREET);
      String selectedName = resultData.getString(LocationConstants.LOCATION_DATA_NAME);

      if (selectedStreet != null && !selectedStreet.isEmpty()) {
        ismActivityShareLocationBinding.tvLocation.setText(selectedStreet);
        selectedLocationAddress = selectedStreet;
      } else {

        if (selectedAddress != null && !selectedAddress.isEmpty()) {
          ismActivityShareLocationBinding.tvLocation.setText(selectedAddress);
          selectedLocationAddress = selectedAddress;
        } else if (selectedArea != null && !selectedArea.isEmpty()) {
          ismActivityShareLocationBinding.tvLocation.setText(selectedArea);
          selectedLocationAddress = selectedArea;
        } else if (selectedCity != null && !selectedCity.isEmpty()) {
          ismActivityShareLocationBinding.tvLocation.setText(selectedCity);
          selectedLocationAddress = selectedCity;
        }
      }

      if (selectedName != null && !selectedName.isEmpty()) {
        selectedLocationName = selectedName;
      } else {
        selectedLocationName = getString(R.string.ism_not_applicable);
      }
    }
  }

  /**
   * Creates an intent, adds location data to it as an extra, and starts the intent service for
   * fetching an address.
   *
   * @param mLocation the m location
   */
  protected void startIntentService(Location mLocation) {
    // Create an intent for passing to the intent service responsible for fetching the address.
    Intent intent = new Intent(this, FetchAddressIntentService.class);

    // Pass the result receiver as an extra to the service.
    intent.putExtra(LocationConstants.RECEIVER, mResultReceiver);

    // Pass the location data as an extra to the service.
    intent.putExtra(LocationConstants.LOCATION_DATA_EXTRA, mLocation);

    // Start the service. If the service isn't already running, it is instantiated and started
    // (creating a process for it if needed); if it is running then it remains running. The
    // service kills itself automatically once all intents are processed.
    startService(intent);
  }



  @Override
  public void onBackPressed() {
    supportFinishAfterTransition();
  }
}
