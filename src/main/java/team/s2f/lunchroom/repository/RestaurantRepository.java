package team.s2f.lunchroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import team.s2f.lunchroom.dto.RestaurantTo;
import team.s2f.lunchroom.model.Restaurant;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

    @Transactional
    @Modifying
    @Query("delete from Restaurant r where r.id=:id")
    int delete(@Param("id") int id);

    Optional<Restaurant> getById(int id);

    @Query("select r from Restaurant r where r.name=:name")
    Restaurant getByName(@Param("name") String name);

    @Query("select new team.s2f.lunchroom.dto.RestaurantTo(v.restaurantId, count (v.restaurantId))" +
            "from Vote v group by v.restaurantId order by count (v.restaurantId) desc")
    List<RestaurantTo> getAllWithCount();
}