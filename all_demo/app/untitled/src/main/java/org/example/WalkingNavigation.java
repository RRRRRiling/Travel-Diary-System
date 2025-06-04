package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

public class WalkingNavigation {

    private final String apiKey;
    private final String baseUrl = "https://restapi.amap.com/v3/direction/walking";

    private static final Map<String, String> ACTION_MAP = new HashMap<>();
    private static final Map<Integer, String> WALK_TYPE_MAP = new HashMap<>();

    static {
        // 初始化ACTION_MAP
        ACTION_MAP.put("左转", "turn left");
        ACTION_MAP.put("右转", "turn right");
        ACTION_MAP.put("向左前方", "turn slightly left");
        ACTION_MAP.put("向右前方", "turn slightly right");
        ACTION_MAP.put("向左后方", "turn sharply left");
        ACTION_MAP.put("向右后方", "turn sharply right");
        ACTION_MAP.put("直行", "go straight");
        ACTION_MAP.put("进入环岛", "enter roundabout");
        ACTION_MAP.put("通过人行横道", "cross crosswalk");
        ACTION_MAP.put("通过过街天桥", "cross overpass");
        ACTION_MAP.put("通过地下通道", "cross underground passage");

        // 初始化WALK_TYPE_MAP
        WALK_TYPE_MAP.put(0, "普通道路");
        WALK_TYPE_MAP.put(1, "人行横道");
        WALK_TYPE_MAP.put(3, "地下通道");
        WALK_TYPE_MAP.put(4, "过街天桥");
        WALK_TYPE_MAP.put(5, "地铁通道");
        WALK_TYPE_MAP.put(6, "公园");
        WALK_TYPE_MAP.put(7, "广场");
        WALK_TYPE_MAP.put(8, "扶梯");
        WALK_TYPE_MAP.put(9, "直梯");
        WALK_TYPE_MAP.put(10, "索道");
        WALK_TYPE_MAP.put(11, "空中通道");
        WALK_TYPE_MAP.put(12, "建筑物穿越通道");
        WALK_TYPE_MAP.put(13, "行人通道");
        WALK_TYPE_MAP.put(14, "游船路线");
        WALK_TYPE_MAP.put(15, "观光车路线");
        WALK_TYPE_MAP.put(16, "滑道");
        WALK_TYPE_MAP.put(18, "扩路");
        WALK_TYPE_MAP.put(19, "道路附属连接线");
        WALK_TYPE_MAP.put(20, "阶梯");
        WALK_TYPE_MAP.put(21, "斜坡");
        WALK_TYPE_MAP.put(22, "桥");
        WALK_TYPE_MAP.put(23, "隧道");
        WALK_TYPE_MAP.put(30, "轮渡");
    }

    public WalkingNavigation(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getWalkingRoute(String origin, String destination) {
        String urlStr = String.format("%s?origin=%s&destination=%s&key=%s",
                baseUrl, origin, destination, apiKey);

        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), "UTF-8"));
                StringBuilder response = new StringBuilder();
                char[] buffer = new char[1024];
                int charsRead;

                while ((charsRead = in.read(buffer)) != -1) {
                    response.append(buffer, 0, charsRead);
                }
                in.close();

                return response.toString();
            } else {
                System.out.println("GET请求失败，响应码: " + responseCode);
                return null;
            }
        } catch (Exception e) {
            System.out.println("请求API时出错: " + e.getMessage());
            return null;
        }
    }

    public void parseAndNavigate(String jsonData) {
        if (jsonData == null || jsonData.isEmpty()) {
            System.out.println("无有效路线数据");
            return;
        }

        Gson gson = new Gson();
        JsonObject response = gson.fromJson(jsonData, JsonObject.class);

        if (!response.get("status").getAsString().equals("1")) {
            System.out.println("错误: " + response.get("info").getAsString());
            return;
        }

        JsonObject route = response.getAsJsonObject("route");
        JsonArray paths = route.getAsJsonArray("paths");
        JsonObject path = paths.get(0).getAsJsonObject();

        String distance = path.get("distance").getAsString();
        int duration = path.get("duration").getAsInt() / 60;

        System.out.println("\n步行路线规划完成:");
        System.out.println("总距离: " + distance + "米");
        System.out.println("预计耗时: " + duration + "分钟");
        System.out.println("\n开始导航:\n");

        JsonArray steps = path.getAsJsonArray("steps");
        for (int i = 0; i < steps.size(); i++) {
            JsonObject step = steps.get(i).getAsJsonObject();

            String instruction = step.get("instruction").getAsString();
            String road = step.get("road").getAsString();
            String stepDistance = step.get("distance").getAsString();
            int stepDuration = step.get("duration").getAsInt() / 60;
            String action = step.get("action").getAsString();
            String assistantAction = step.has("assistant_action") ?
                    step.get("assistant_action").getAsString() : "";
            int walkType = step.has("walk_type") ?
                    step.get("walk_type").getAsInt() : 0;

            System.out.println("步骤 " + (i+1) + ":");
            System.out.println("指令: " + formatInstruction(instruction));
            System.out.println("道路: " + road);
            System.out.println("距离: " + stepDistance + "米");
            System.out.println("预计时间: " + stepDuration + "分钟");
            System.out.println("动作: " + ACTION_MAP.get(action) != null ? ACTION_MAP.get(action) : action);
            if (!assistantAction.isEmpty()) {
                System.out.println("辅助动作: " +
                        (ACTION_MAP.get(assistantAction) != null ? ACTION_MAP.get(assistantAction) : assistantAction));
            }
            System.out.println("道路类型: " +
                    (WALK_TYPE_MAP.get(walkType) != null ? WALK_TYPE_MAP.get(walkType) : "未知道路类型"));

            // Java 8兼容的分隔线
            System.out.println("--------------------------------------------------");

            if (i < steps.size() - 1) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        System.out.println("\n已到达目的地！");
    }

    private String formatInstruction(String instruction) {
        return instruction.replace("然后", "\n然后")
                .replace("，", "，\n");
    }
}
