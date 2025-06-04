package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // 替换为您的高德地图API Key
        String apiKey = "508cb050cc0474f0b6e499210468a606";

        // 创建导航实例
        WalkingNavigation navigator = new WalkingNavigation(apiKey);

        // 使用Scanner获取用户输入
        Scanner scanner = new Scanner(System.in);

        System.out.println("请输入起点经纬度(经度,纬度):");
        String origin = scanner.nextLine().trim();

        System.out.println("请输入终点经纬度(经度,纬度):");
        String destination = scanner.nextLine().trim();

        // 获取并解析路线
        System.out.println("正在规划步行路线...");
        String routeData = navigator.getWalkingRoute(origin, destination);
        navigator.parseAndNavigate(routeData);

        scanner.close();
    }
}