package com.example.fooddeliverymarketplace.service.paging;

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
}
