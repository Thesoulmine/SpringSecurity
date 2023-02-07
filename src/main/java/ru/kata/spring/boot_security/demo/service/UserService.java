package ru.kata.spring.boot_security.demo.service;

import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    String encodePassword(String password);

    List<User> getUsersList();

    void deleteUser(User user);

    Optional<User> getUserById(Long id);

    User getUserByUsername(String username);

    void saveUser(User user);

    @Transactional
    void updateUser(User user);
}
