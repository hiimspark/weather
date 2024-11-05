package org.example.weather.repository;

import org.example.weather.entity.FavouriteCityEntity;
import org.example.weather.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FavouriteCityRepository extends JpaRepository<FavouriteCityEntity, Long> {
    List<FavouriteCityEntity> findByUser(UserEntity user);
    boolean existsByCityAndUser(String city, UserEntity user);
}

