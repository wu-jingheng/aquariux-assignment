package com.wjh.aquariux_assignment_wjh.repository;

import com.wjh.aquariux_assignment_wjh.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Long, User> {
}
