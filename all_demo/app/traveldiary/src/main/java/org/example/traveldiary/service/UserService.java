package org.example.traveldiary.service;

import org.example.traveldiary.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class UserService {
    private final FileStorageService fileStorage;

    @Value("${app.data.user-dir}")
    private String userDir;

    public User register(User user) throws IOException {
        String userFile = Paths.get(userDir, user.getUsername() + ".json").toString();
        if (fileStorage.readJsonFile(userFile, User.class) != null) {
            throw new IllegalArgumentException("Username already exists");
        }
        fileStorage.writeJsonFile(userFile, user);
        return user;
    }

    public User login(String username, String password) throws IOException {
        User user = fileStorage.readJsonFile(
                Paths.get(userDir, username + ".json").toString(),
                User.class
        );
        if (user == null || !user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        return user;
    }
}