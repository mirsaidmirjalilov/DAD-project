package com.example.fooddeliverymarketplace.service.specification;

import com.example.fooddeliverymarketplace.entity.Restaurant;
import com.example.fooddeliverymarketplace.entity.User;
import com.example.fooddeliverymarketplace.utils.Role;
import com.example.fooddeliverymarketplace.utils.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpecificationService {
    public Specification<User> getUserSpecification(Role role, UserStatus userStatus) {
        Specification<User> specification = null;

        if (role != null) {
            specification = specification.and(filterByRole(role));
        }

        if (userStatus != null) {
            specification = specification.and(filterByUserStatus(userStatus));
        }

        return specification;
    }

    public Specification<User> filterByUserStatus(UserStatus userStatus) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), userStatus);
    }

    public static Specification<User> filterByRole(Role role) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("role"), role);
    }

    public Specification<Restaurant> getRestarauntSpecification(String restaurantName, Float rating, Boolean active) {
        Specification<Restaurant> specification = null;

        if (restaurantName != null) {
            specification = specification.and(filterByRestarauntName(restaurantName));
        }

        if (rating != null) {
            specification = specification.and(filterByRating(rating));
        }

        if (active) {
            specification = specification.and(filterByActive(active));
        }

        return specification;
    }

    private Specification<Restaurant> filterByActive(Boolean active) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("active"), active);
    }

    private Specification<Restaurant> filterByRating(Float rating) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("rating"), rating);
    }

    private Specification<Restaurant> filterByRestarauntName(String restaurantName) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("name"), restaurantName);
    }


}
