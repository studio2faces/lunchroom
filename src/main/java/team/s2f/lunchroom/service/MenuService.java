package team.s2f.lunchroom.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import team.s2f.lunchroom.model.Menu;
import team.s2f.lunchroom.repository.MenuRepository;
import team.s2f.lunchroom.repository.RestaurantRepository;
import team.s2f.lunchroom.util.ValidationUtil;

import java.time.LocalDate;
import java.util.List;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    @Autowired
    public MenuService(MenuRepository menuRepository, RestaurantRepository restaurantRepository) {
        this.menuRepository = menuRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public Menu create(Menu menu, int restaurantId) {
        Assert.notNull(menu, "Menu must not be null.");
        ValidationUtil.checkNotFoundWithId(restaurantRepository.getById(restaurantId), restaurantId);
        return menuRepository.create(menu, restaurantId);
    }

    public void delete(int id) {
        ValidationUtil.checkNotFoundWithId(menuRepository.delete(id), id);
    }

    public Menu getByRestaurant(int restaurantId, LocalDate date) {
        return ValidationUtil.checkNotFoundWithId(menuRepository.getByRestaurantId(restaurantId, date), restaurantId);
    }

    public List<Menu> getAllWithRestaurants(LocalDate date) {
        return menuRepository.getAllWithRestaurant(date);
    }
}
