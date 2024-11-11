package ca.gbc.comp3074.personalrestaurantguide.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ca.gbc.comp3074.personalrestaurantguide.models.Restaurant;
import ca.gbc.comp3074.personalrestaurantguide.database.RestaurantDao;

@Database(entities = {Restaurant.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract RestaurantDao restaurantDao();
}
