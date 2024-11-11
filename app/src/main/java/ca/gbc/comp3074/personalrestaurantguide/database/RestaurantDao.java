package ca.gbc.comp3074.personalrestaurantguide.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import ca.gbc.comp3074.personalrestaurantguide.models.Restaurant;

import java.util.List;

@Dao
public interface RestaurantDao {

    @Insert
    void insert(Restaurant restaurant);

    @Update
    void update(Restaurant restaurant);

    @Delete
    void delete(Restaurant restaurant);

    @Query("SELECT * FROM Restaurant")
    List<Restaurant> getAllRestaurants();

    @Query("SELECT * FROM Restaurant WHERE name LIKE '%' || :searchQuery || '%'")
    List<Restaurant> searchByName(String searchQuery);

    @Query("SELECT * FROM Restaurant WHERE tags LIKE '%' || :tag || '%'")
    List<Restaurant> searchByTag(String tag);


    @Query("SELECT * FROM Restaurant WHERE name LIKE '%' || :searchQuery || '%' OR tags LIKE '%' || :searchQuery || '%'")
    List<Restaurant> searchByNameOrTag(String searchQuery);

}
