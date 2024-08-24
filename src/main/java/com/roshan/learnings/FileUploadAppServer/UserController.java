package com.roshan.learnings.FileUploadAppServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/upload")
    public ResponseEntity<List<UserDto>> saveUsersFromUpload (@RequestParam("file")MultipartFile file) throws IOException {
        List<User> users = new ArrayList<>();
        List<UserDto> userDtos = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                User user = new User();
                user.setUsername(userData[0]);
                user.setEmail(userData[1]);
                user.setPassword(userData[2]);
                user.setMobile(userData[3]);
                users.add(user);
            }
            users.forEach((user) -> {
                if (!userService.userExistsInDb(user)) {
                    userService.saveUser(user);
                    UserDto userDto = Adaptor.convertToDto(user);
                    userDtos.add(userDto);
                }
                else
                    System.out.println("User already exits with the same email!");
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    @GetMapping
    public Page<User> getUsers(@RequestParam int page, @RequestParam int size) {
        return userService.getUsers(PageRequest.of(page, size));
    }
}
