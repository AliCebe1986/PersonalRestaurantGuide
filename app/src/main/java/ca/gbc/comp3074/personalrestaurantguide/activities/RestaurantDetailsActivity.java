package ca.gbc.comp3074.personalrestaurantguide.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import ca.gbc.comp3074.personalrestaurantguide.R;
import ca.gbc.comp3074.personalrestaurantguide.database.AppDatabase;
import ca.gbc.comp3074.personalrestaurantguide.models.Restaurant;

public class RestaurantDetailsActivity extends AppCompatActivity {

    private TextView nameTextView, addressTextView, phoneTextView, descriptionTextView, tagsTextView;
    private RatingBar ratingBar;
    private Button showOnMapButton, getDirectionsButton, shareButton, editButton, deleteButton;
    private Restaurant restaurant;
    private AppDatabase db;

    private ShareDialog shareDialog; // Facebook

    private ActivityResultLauncher<Intent> editRestaurantLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);


        FacebookSdk.sdkInitialize(getApplicationContext());
        shareDialog = new ShareDialog(this);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "restaurant-database").allowMainThreadQueries().build();

        nameTextView = findViewById(R.id.detailsNameTextView);
        addressTextView = findViewById(R.id.detailsAddressTextView);
        phoneTextView = findViewById(R.id.detailsPhoneTextView);
        descriptionTextView = findViewById(R.id.detailsDescriptionTextView);
        tagsTextView = findViewById(R.id.detailsTagsTextView);
        ratingBar = findViewById(R.id.detailsRatingBar);
        showOnMapButton = findViewById(R.id.showOnMapButton);
        getDirectionsButton = findViewById(R.id.getDirectionsButton);
        shareButton = findViewById(R.id.shareButton);
        editButton = findViewById(R.id.editRestaurant);
        deleteButton = findViewById(R.id.deleteRestaurantButton);





        if (getIntent().hasExtra("restaurant")) {
            restaurant = (Restaurant) getIntent().getSerializableExtra("restaurant");
        } else {
            finish();
            return;
        }


        nameTextView.setText(restaurant.getName());
        addressTextView.setText(restaurant.getAddress());
        phoneTextView.setText(restaurant.getPhone());
        descriptionTextView.setText(restaurant.getDescription());
        tagsTextView.setText(restaurant.getTags());
        ratingBar.setRating(restaurant.getRating());

        editRestaurantLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            int updatedRestaurantId = data.getIntExtra("UPDATED_RESTAURANT_ID", -1);

                            if (updatedRestaurantId != -1) {
                                // Reload the updated restaurant data
                                restaurant = db.restaurantDao().getById(updatedRestaurantId);
                                if (restaurant != null) {
                                    // Update the UI
                                    nameTextView.setText(restaurant.getName());
                                    addressTextView.setText(restaurant.getAddress());
                                    phoneTextView.setText(restaurant.getPhone());
                                    descriptionTextView.setText(restaurant.getDescription());
                                    tagsTextView.setText(restaurant.getTags());
                                    ratingBar.setRating(restaurant.getRating());
                                }
                            }
                        }
                    }
                }
        );


        showOnMapButton.setOnClickListener(v -> {
            Intent mapIntent = new Intent(RestaurantDetailsActivity.this, MapActivity.class);
            mapIntent.putExtra("restaurant", restaurant);
            startActivity(mapIntent);
        });

        getDirectionsButton.setOnClickListener(v -> {
            String uri = "google.navigation:q=" + Uri.encode(restaurant.getAddress());
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
        });

        shareButton.setOnClickListener(v -> showShareOptions());

        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(RestaurantDetailsActivity.this, EditRestaurantActivity.class);
            intent.putExtra("RESTAURANT_ID", restaurant.getId());
            editRestaurantLauncher.launch(intent);
        });

        deleteButton.setOnClickListener(v -> showDeleteConfirmationDialog());

    }


    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Restaurant")
                .setMessage("Are you sure you want to delete this restaurant?")
                .setPositiveButton("Yes", (dialog, which) -> deleteRestaurant())
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteRestaurant() {
        if (restaurant != null) {
            db.restaurantDao().delete(restaurant);
            Toast.makeText(this, "Restaurant deleted", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Restaurant not found", Toast.LENGTH_LONG).show();
        }
    }


    private void showShareOptions() {

        String[] options = {"Share with Facebook", "Share with Twitter", "Share with Other Apps"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sharing Options");
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0:
                    shareViaFacebook();
                    break;
                case 1:
                    shareViaTwitter();
                    break;
                case 2:
                    shareViaOtherApps();
                    break;
            }
        });
        builder.show();
    }

    private void shareViaFacebook() {
        String shareText = "Restaurant: " + restaurant.getName() + "\nAddress: " + restaurant.getAddress();

        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setQuote(shareText)
                    .setContentUrl(Uri.parse("https://www.yourappwebsite.com"))
                    .build();

            shareDialog.show(content);
        }
    }

    private void shareViaTwitter() {
        String shareText = "Restaurant: " + restaurant.getName() + "\nAddress: " + restaurant.getAddress();

        String tweetUrl = "https://twitter.com/intent/tweet?text=" + Uri.encode(shareText);
        Uri uri = Uri.parse(tweetUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);


        startActivity(intent);
    }

    private void shareViaOtherApps() {
        String shareText = "Restaurant: " + restaurant.getName() + "\nAddress: " + restaurant.getAddress();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, shareText);

        startActivity(Intent.createChooser(intent, "Share"));
    }
}
