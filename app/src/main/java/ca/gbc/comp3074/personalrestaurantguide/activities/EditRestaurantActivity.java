package ca.gbc.comp3074.personalrestaurantguide.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import ca.gbc.comp3074.personalrestaurantguide.R;
import ca.gbc.comp3074.personalrestaurantguide.database.AppDatabase;
import ca.gbc.comp3074.personalrestaurantguide.models.Restaurant;

public class EditRestaurantActivity extends AppCompatActivity {

    private EditText nameEditText, addressEditText, phoneEditText, descriptionEditText, tagsEditText;
    private RatingBar ratingBar;
    private AppDatabase db;
    private int restaurantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_restaurant);

        // Initialize Room database
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "restaurant-database").allowMainThreadQueries().build();

        // Find views
        nameEditText = findViewById(R.id.nameEditText);
        addressEditText = findViewById(R.id.addressEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        tagsEditText = findViewById(R.id.tagsEditText);
        ratingBar = findViewById(R.id.ratingBar);

        // Get restaurant ID from intent
        restaurantId = getIntent().getIntExtra("RESTAURANT_ID", -1);
        if (restaurantId != -1) {
            loadRestaurantData(restaurantId);
        } else {
            Toast.makeText(this, "Error: No restaurant ID provided.", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Save button listener
        findViewById(R.id.saveButton).setOnClickListener(v -> updateRestaurant());
    }

    private void loadRestaurantData(int restaurantId) {
        Restaurant restaurant = db.restaurantDao().getById(restaurantId);
        if (restaurant != null) {
            populateFields(restaurant);
        } else {
            Toast.makeText(this, "Error: Restaurant not found.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void populateFields(Restaurant restaurant) {
        nameEditText.setText(restaurant.getName());
        addressEditText.setText(restaurant.getAddress());
        phoneEditText.setText(restaurant.getPhone());
        descriptionEditText.setText(restaurant.getDescription());
        tagsEditText.setText(restaurant.getTags());
        ratingBar.setRating(restaurant.getRating());
    }

    private void updateRestaurant() {
        // Get updated data from user inputs
        String name = nameEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String tags = tagsEditText.getText().toString();
        float rating = ratingBar.getRating();

        if (name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Name, Address, and Phone are required.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create and update the restaurant object
        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId); // Ensure we are updating the correct entity
        restaurant.setName(name);
        restaurant.setAddress(address);
        restaurant.setPhone(phone);
        restaurant.setDescription(description);
        restaurant.setTags(tags);
        restaurant.setRating(rating);

        db.restaurantDao().update(restaurant);

        Toast.makeText(this, "Restaurant updated successfully!", Toast.LENGTH_SHORT).show();
        Intent resultIntent = new Intent();
        resultIntent.putExtra("UPDATED_RESTAURANT_ID", restaurantId);
        setResult(RESULT_OK, resultIntent);
        finish(); // Close the activity
    }
    }