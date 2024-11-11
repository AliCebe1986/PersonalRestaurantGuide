package ca.gbc.comp3074.personalrestaurantguide.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import ca.gbc.comp3074.personalrestaurantguide.R;
import ca.gbc.comp3074.personalrestaurantguide.database.AppDatabase;
import ca.gbc.comp3074.personalrestaurantguide.models.Restaurant;

public class AddEditRestaurantActivity extends AppCompatActivity {

    private EditText nameEditText, addressEditText, phoneEditText, descriptionEditText, tagsEditText;
    private RatingBar ratingBar;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_restaurant);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "restaurant-database").allowMainThreadQueries().build();

        nameEditText = findViewById(R.id.nameEditText);
        addressEditText = findViewById(R.id.addressEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        tagsEditText = findViewById(R.id.tagsEditText);
        ratingBar = findViewById(R.id.ratingBar);

        findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRestaurant();
            }
        });
    }

    private void saveRestaurant() {
        String name = nameEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String tags = tagsEditText.getText().toString();
        float rating = ratingBar.getRating();

        Restaurant restaurant = new Restaurant();
        restaurant.setName(name);
        restaurant.setAddress(address);
        restaurant.setPhone(phone);
        restaurant.setDescription(description);
        restaurant.setTags(tags);
        restaurant.setRating(rating);

        db.restaurantDao().insert(restaurant);

        finish();
    }
}