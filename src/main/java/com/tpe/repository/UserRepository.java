package com.tpe.repository;

import com.tpe.domain.User;
import com.tpe.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> { //extend ederken hangi class olduğunu ve id data tipini belirtiriz

    Optional<User> findByUserName(String userName) throws ResourceNotFoundException;


}
