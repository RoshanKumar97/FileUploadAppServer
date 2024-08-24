package com.roshan.learnings.FileUploadAppServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public UserDto saveUser(User user) {
        UserDto userDto = UserDto.builder()
                .email(user.getEmail())
                .mobile(user.getMobile())
                .username(user.getUsername())
                .build();
        userRepository.save(user);
        return userDto;
    }

    public boolean userExistsInDb(User userToCheck) {
        List<User> users = userRepository.findAll();
        return users.stream().anyMatch(user -> user.getEmail().equalsIgnoreCase(userToCheck.getEmail()));
    }

    public Page<User> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
}
