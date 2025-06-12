package com.authyfier.authyfier.repository;

import com.authyfier.authyfier.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepositry extends JpaRepository<UserEntity,Long> {


	Optional<UserEntity> findByEmail(String aLong);

	boolean existsByEmail(String email);

}
