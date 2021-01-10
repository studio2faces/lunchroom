package team.s2f.lunchroom.repository;

import team.s2f.lunchroom.model.Dish;

import java.util.List;

public interface DishRepository {
    Dish save(Dish dish, int menuId);

    Dish get(int id, int menuId);

   boolean delete(int id, int menuId);

    List<Dish> getAllByMenu(int menuId);
}
