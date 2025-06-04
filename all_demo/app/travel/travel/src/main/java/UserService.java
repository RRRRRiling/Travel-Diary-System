import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private static final String USER_DIR = "D:\\all_demo\\data\\users";

    static {
        FileUtil.createDirectoryIfNotExists(USER_DIR);
    }

    public boolean register(User user) {
        String userFilePath = USER_DIR + "\\" + user.getUsername() + ".json";
        File userFile = new File(userFilePath);

        if (userFile.exists()) {
            return false; // 用户已存在
        }

        try {
            String userJson = JsonUtil.toJson(user);
            FileUtil.writeToFile(userFilePath, userJson);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User login(String username, String password) {
        String userFilePath = USER_DIR + "\\" + username + ".json";
        File userFile = new File(userFilePath);

        if (!userFile.exists()) {
            return null; // 用户不存在
        }

        try {
            String userJson = FileUtil.readFromFile(userFilePath);
            User user = JsonUtil.fromJson(userJson, User.class);

            if (user.getPassword().equals(password)) {
                return user;
            }
            return null; // 密码错误
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> getAllUsernames() {
        List<String> usernames = new ArrayList<>();
        List<String> files = FileUtil.getAllFilesInDirectory(USER_DIR);

        for (String file : files) {
            if (file.endsWith(".json")) {
                usernames.add(file.substring(0, file.length() - 5));
            }
        }

        return usernames;
    }
}