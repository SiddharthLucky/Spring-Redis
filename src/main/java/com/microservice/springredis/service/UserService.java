package com.microservice.springredis.service;

import com.microservice.springredis.model.User;
import com.microservice.springredis.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Cacheable(value = "users", key = "#id")
    public Optional<User> getUserById(Long id) {
        log.debug("Finding the user by Id: "+id);
        log.info("Info level log");
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @CacheEvict(value = "users", key = "#user.id")
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @CachePut(value = "users", key = "#user.id")
    public User updateUser(User user) {
        Optional<User> existingUserOptional = userRepository.findById(user.getId());
        if (existingUserOptional.isPresent()) {
            User updatedUser = existingUserOptional.get();
            updatedUser.setUsername(user.getUsername());
            updatedUser.setEmail(user.getEmail());
            return userRepository.save(updatedUser);
        }
        return null;
    }

    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }


}
