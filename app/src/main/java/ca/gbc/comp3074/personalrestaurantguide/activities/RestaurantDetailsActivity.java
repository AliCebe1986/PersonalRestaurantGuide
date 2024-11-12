package ca.gbc.comp3074.personalrestaurantguide.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import ca.gbc.comp3074.personalrestaurantguide.R;
import ca.gbc.comp3074.personalrestaurantguide.models.Restaurant;

public class RestaurantDetailsActivity extends AppCompatActivity {

    private TextView nameTextView, addressTextView, phoneTextView, descriptionTextView, tagsTextView;
    private RatingBar ratingBar;
    private Button showOnMapButton, getDirectionsButton, shareButton;
    private Restaurant restaurant;

    private ShareDialog shareDialog; // Facebook

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);


        FacebookSdk.sdkInitialize(getApplicationContext());
        shareDialog = new ShareDialog(this);


        nameTextView = findViewById(R.id.detailsNameTextView);
        addressTextView = findViewById(R.id.detailsAddressTextView);
        phoneTextView = findViewById(R.id.detailsPhoneTextView);
        descriptionTextView = findViewById(R.id.detailsDescriptionTextView);
        tagsTextView = findViewById(R.id.detailsTagsTextView);
        ratingBar = findViewById(R.id.detailsRatingBar);
        showOnMapButton = findViewById(R.id.showOnMapButton);
        getDirectionsButton = findViewById(R.id.getDirectionsButton);
        shareButton = findViewById(R.id.shareButton);


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