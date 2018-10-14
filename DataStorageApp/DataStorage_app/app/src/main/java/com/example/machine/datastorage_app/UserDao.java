package com.example.machine.datastorage_app;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;
@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user where first_name LIKE  :firstName AND last_name LIKE :lastName")
    User findByName(String firstName, String lastName);

    @Query("SELECT COUNT(*) from user")
    int countUsers();

    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);

    @Query("SELECT uid FROM user WHERE email LIKE :email AND password LIKE :password")
    boolean findUidByEmailPassword(String email, String password);

    @Query("SELECT uid FROM user WHERE email LIKE :email AND password LIKE :password")
    int findUID(String email, String password);

    @Query("SELECT first_name FROM user WHERE email LIKE :email AND password LIKE :password")
    String findFirstName(String email, String password);

    @Query("SELECT last_name FROM user WHERE email LIKE :email AND password LIKE :password")
    String findLastName(String email, String password);







}
