package ca.gbc.comp3074.personalrestaurantguide.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import ca.gbc.comp3074.personalrestaurantguide.R;
import ca.gbc.comp3074.personalrestaurantguide.adapters.RestaurantAdapter;
import ca.gbc.comp3074.personalrestaurantguide.database.AppDatabase;
import ca.gbc.comp3074.personalrestaurantguide.models.Restaurant;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import android.text.Editable;
import android.text.TextWatcher;

public class MainActivity extends AppCompatActivity {

    private AppDatabase db;
    private RecyclerView recyclerView;
    private RestaurantAdapter adapter;
    private EditText searchEditText;
    private List<Restaurant> restaurantList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Set up the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Edge-to-edge handling (if necessary)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize database
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "restaurant-database").allowMainThreadQueries().build();

        // Initialize views
        recyclerView = findViewById(R.id.restaurantRecyclerView);
        searchEditText = findViewById(R.id.searchEditText);

        // Get restaurant list from database
        restaurantList = new ArrayList<>();
        restaurantList.addAll(db.restaurantDao().getAllRestaurants());

        // Set up RecyclerView and adapter
        adapter = new RestaurantAdapter(restaurantList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // "Add Restaurant" button click event
        findViewById(R.id.addRestaurantButton).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditRestaurantActivity.class);
            startActivity(intent);
        });

        // Search functionality
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s,int start,int count,int after){}

            @Override
            public void onTextChanged(CharSequence s,int start,int before,int count){
                String query = s.toString().trim();
                if (!query.isEmpty()) {
                    restaurantList.clear();

                    List<Restaurant> nameResults = db.restaurantDao().searchByName(query);
                    List<Restaurant> tagResults = db.restaurantDao().searchByTag(query);

                    Set<Restaurant> resultSet = new LinkedHashSet<>();
                    resultSet.addAll(nameResults);
                    resultSet.addAll(tagResults);

                    restaurantList.addAll(resultSet);
                    adapter.notifyDataSetChanged();
                } else {
                    restaurantList.clear();
                    restaurantList.addAll(db.restaurantDao().getAllRestaurants());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s){}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        restaurantList.clear();
        restaurantList.addAll(db.restaurantDao().getAllRestaurants());
        adapter.notifyDataSetChanged();
    }

    // Inflate the menu; this adds items to the action bar if present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu); // Make sure you have menu_main.xml
        return true;
    }

    // Handle action bar item clicks here.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId(); // Get the selected menu item's ID

        if (id == R.id.action_about) {
            // User clicked on About
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
