package org.example.traveldiary.controller;

import org.example.traveldiary.model.User;
import org.example.traveldiary.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")  // 基础路径为 /api
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 根路径欢迎信息
     * @return API 运行状态和可用端点
     */
    @GetMapping("")
    public String apiHome() {
        return """
            Travel Diary API is running!
            Available endpoints:
              - POST /api/auth/register  用户注册
              - POST /api/auth/login     用户登录
              - GET  /api/spots          获取所有景区
              - GET  /api/diaries        获取用户日记
            """;
    }

    /**
     * 用户注册
     * @param user 用户信息（username, password）
     * @return 注册成功的用户信息
     * @throws Exception 用户名已存在时抛出异常
     */
    @PostMapping("/auth/register")
    public User register(@RequestBody User user) throws Exception {
        return userService.register(user);
    }

    /**
     * 用户登录
     * @param user 用户凭证（username, password）
     * @return 登录成功的用户信息
     * @throws Exception 用户名或密码错误时抛出异常
     */
    @PostMapping("/auth/login")
    public User login(@RequestBody User user) throws Exception {
        return userService.login(user.getUsername(), user.getPassword());
    }
}