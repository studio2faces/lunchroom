package team.s2f.lunchroom.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import team.s2f.lunchroom.DishTestData;
import team.s2f.lunchroom.MenuTestData;
import team.s2f.lunchroom.model.Dish;
import team.s2f.lunchroom.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;

public class DishServiceTest extends AbstractServiceTest {

    @Autowired
    protected DishService dishService;

    @Test
    void delete() {
        dishService.delete(DishTestData.DISH1_ID, MenuTestData.menu_fish_house.id());
        Assertions.assertThrows(NotFoundException.class, () -> dishService.get(DishTestData.DISH1_ID, MenuTestData.menu_fish_house.id()));
    }

    @Test
    void deleteNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> dishService.delete(DishTestData.NOT_FOUND, MenuTestData.menu_fish_house.id()));
    }

    @Test
    void deleteIncorrectMenu() {
        Assertions.assertThrows(NotFoundException.class, () -> dishService.delete(DishTestData.DISH1_ID, MenuTestData.menu_masterskaya.id()));
    }

    @Test
    void create() {
        Dish created = dishService.create(DishTestData.getNew(), MenuTestData.menu_fish_house.id());
        int newId = created.id();
        Dish newDish = DishTestData.getNew();
        newDish.setId(newId);
        DishTestData.DISH_MATCHER.assertMatch(created, newDish);
        DishTestData.DISH_MATCHER.assertMatch(dishService.get(newId, MenuTestData.menu_fish_house.id()), newDish);
    }

    @Test
    void duplicateDishNameCreate() {
        Assertions.assertThrows(DataAccessException.class, () -> dishService.create(DishTestData.getNewDuplicated(), MenuTestData.menu_fish_house.id()));
    }

    @Test
    void get() {
        Dish actual = dishService.get(DishTestData.DISH1_ID, MenuTestData.menu_fish_house.id());
        DishTestData.DISH_MATCHER.assertMatch(actual, DishTestData.dish1);
    }

    @Test
    void getNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> dishService.get(DishTestData.NOT_FOUND, MenuTestData.menu_fish_house.id()));
    }

    @Test
    void getWithIncorrectMenu() {
        Assertions.assertThrows(NotFoundException.class, () -> dishService.get(DishTestData.DISH1_ID, MenuTestData.menu_masterskaya.id()));
    }

    @Test
    void getWithNotFoundMenu() {
        Assertions.assertThrows(NotFoundException.class, () -> dishService.get(DishTestData.DISH1_ID, MenuTestData.NOT_FOUND));
    }

    @Test
    void update() {
        Dish updated = DishTestData.getUpdated();
        dishService.update(updated, MenuTestData.menu_fish_house.id());
        DishTestData.DISH_MATCHER.assertMatch(dishService.get(DishTestData.DISH1_ID, MenuTestData.menu_fish_house.id()), DishTestData.getUpdated());
    }

    @Test
    void updateWithIncorrectMenu() {
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> dishService.update(DishTestData.dish1, MenuTestData.menu_masterskaya.id()));
        Assertions.assertEquals("Not found entity with id=" + DishTestData.DISH1_ID, exception.getMessage());
        DishTestData.DISH_MATCHER.assertMatch(dishService.get(DishTestData.DISH1_ID, MenuTestData.menu_fish_house.id()), DishTestData.dish1);
    }

    @Test
    void getAllByMenu() {
        DishTestData.DISH_MATCHER.assertMatch(dishService.getAllByMenuId(MenuTestData.menu_fish_house.id()), DishTestData.dishes_fish_house);
    }

    @Test
    void getAllByIncorrectMenu() {
        Assertions.assertThrows(NotFoundException.class, () -> dishService.getAllByMenuId(MenuTestData.NOT_FOUND));
    }

    @Test
    void createWithException() {
        validateRootCause(() -> dishService.create(new Dish(null, " ", 100.00, MenuTestData.menu_fish_house), MenuTestData.menu_fish_house.id()), ConstraintViolationException.class);
        validateRootCause(() -> dishService.create(new Dish(null, null, 100.00, MenuTestData.menu_fish_house), MenuTestData.menu_fish_house.id()), ConstraintViolationException.class);
        validateRootCause(() -> dishService.create(new Dish(null, "Milk", 3.00, MenuTestData.menu_fish_house), MenuTestData.menu_fish_house.id()), ConstraintViolationException.class);
        validateRootCause(() -> dishService.create(new Dish(null, "Milk", 2000.00, MenuTestData.menu_fish_house), MenuTestData.menu_fish_house.id()), ConstraintViolationException.class);
    }
}