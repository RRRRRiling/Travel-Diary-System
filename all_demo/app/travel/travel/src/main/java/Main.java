import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {
    private static final UserService userService = new UserService();
    private static final DiaryService diaryService = new DiaryService();
    private static final ScenicSpotService scenicSpotService = new ScenicSpotService();
    private static User currentUser = null;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        showMainMenu();
    }

    private static void showMainMenu() {
        while (true) {
            System.out.println("\n===== 旅游管理系统 =====");
            if (currentUser == null) {
                System.out.println("1. 注册");
                System.out.println("2. 登录");
                System.out.println("0. 退出");
            } else {
                System.out.println("欢迎, " + currentUser.getUsername() + "!");
                System.out.println("1. 查找景区");
                System.out.println("2. 写日记");
                System.out.println("3. 查看我的日记");
                System.out.println("4. 查看所有日记");
                System.out.println("5. 修改日记");
                System.out.println("6. 删除日记");
                System.out.println("7. 注销");
                System.out.println("0. 退出");
            }

            System.out.print("请选择: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 消耗换行符

            if (currentUser == null) {
                switch (choice) {
                    case 1:
                        register();
                        break;
                    case 2:
                        login();
                        break;
                    case 0:
                        System.out.println("感谢使用，再见！");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("无效选择，请重试！");
                }
            } else {
                switch (choice) {
                    case 1:
                        searchScenicSpots();
                        break;
                    case 2:
                        writeDiary();
                        break;
                    case 3:
                        viewMyDiaries();
                        break;
                    case 4:
                        viewAllDiaries();
                        break;
                    case 5:
                        updateDiary();
                        break;
                    case 6:
                        deleteDiary();
                        break;
                    case 7:
                        currentUser = null;
                        System.out.println("已注销");
                        break;
                    case 0:
                        System.out.println("感谢使用，再见！");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("无效选择，请重试！");
                }
            }
        }
    }

    private static void register() {
        System.out.println("\n===== 用户注册 =====");
        System.out.print("请输入用户名: ");
        String username = scanner.nextLine();
        System.out.print("请输入密码: ");
        String password = scanner.nextLine();

        User newUser = new User(username, password);
        if (userService.register(newUser)) {
            System.out.println("注册成功！");
        } else {
            System.out.println("注册失败，用户名可能已存在！");
        }
    }

    private static void login() {
        System.out.println("\n===== 用户登录 =====");
        System.out.print("请输入用户名: ");
        String username = scanner.nextLine();
        System.out.print("请输入密码: ");
        String password = scanner.nextLine();

        User user = userService.login(username, password);
        if (user != null) {
            currentUser = user;
            System.out.println("登录成功！");
        } else {
            System.out.println("登录失败，用户名或密码错误！");
        }
    }

    private static void searchScenicSpots() {
        System.out.println("\n===== 景区搜索 =====");
        System.out.println("1. 按关键词搜索");
        System.out.println("2. 按标签搜索");
        System.out.println("3. 高级搜索（关键词+标签）");
        System.out.println("4. 查看所有标签");
        System.out.print("请选择搜索方式: ");

        int searchChoice = scanner.nextInt();
        scanner.nextLine(); // 消耗换行符

        switch (searchChoice) {
            case 1:
                keywordSearch();
                break;
            case 2:
                tagSearch();
                break;
            case 3:
                advancedSearch();
                break;
            case 4:
                showAllTags();
                break;
            default:
                System.out.println("无效选择！");
        }
    }

    private static void keywordSearch() {
        System.out.print("请输入搜索关键词: ");
        String keyword = scanner.nextLine();

        List<ScenicSpot> spots = scenicSpotService.searchScenicSpots(keyword);
        displaySearchResults(spots);
    }

    private static void tagSearch() {
        // 先显示可用标签
        Set<String> tags = scenicSpotService.getAllTags();
        System.out.println("可用标签: " + String.join(", ", tags));

        System.out.print("请输入要搜索的标签: ");
        String tag = scanner.nextLine();

        List<ScenicSpot> spots = scenicSpotService.searchByTag(tag);
        displaySearchResults(spots);
    }

    private static void advancedSearch() {
        System.out.print("请输入搜索关键词(不限制请直接回车): ");
        String keyword = scanner.nextLine();
        if (keyword.trim().isEmpty()) {
            keyword = null;
        }

        // 先显示可用标签
        Set<String> tags = scenicSpotService.getAllTags();
        System.out.println("可用标签: " + String.join(", ", tags));

        System.out.print("请输入要搜索的标签(不限制请直接回车): ");
        String tag = scanner.nextLine();
        if (tag.trim().isEmpty()) {
            tag = null;
        }

        List<ScenicSpot> spots = scenicSpotService.advancedSearch(keyword, tag);
        displaySearchResults(spots);
    }

    private static void showAllTags() {
        Set<String> tags = scenicSpotService.getAllTags();
        System.out.println("\n===== 所有标签 =====");
        tags.stream().sorted().forEach(tag -> System.out.println("- " + tag));
    }

    private static void displaySearchResults(List<ScenicSpot> spots) {
        if (spots == null || spots.isEmpty()) {
            System.out.println("没有找到符合条件的景区！");
            return;
        }

        System.out.println("\n找到 " + spots.size() + " 个景区:");
        System.out.println("排序方式：名称(A-Z)");
        System.out.println("----------------------------------");

        for (ScenicSpot spot : spots) {
            System.out.println("ID: " + spot.getId());
            System.out.println("名称: " + spot.getName());
            System.out.println("位置: " + spot.getLocation());
            System.out.println("评分: " + spot.getRating());
            System.out.println("标签: " + (spot.getTags() != null ? String.join(", ", spot.getTags()) : "无"));
            System.out.println("描述: " + spot.getDescription());
            System.out.println("----------------------------------");
        }
    }

    private static void writeDiary() {
        System.out.println("\n===== 写日记 =====");

        // 显示所有景区供选择
        List<ScenicSpot> spots = scenicSpotService.getAllScenicSpots();
        if (spots != null && !spots.isEmpty()) {
            System.out.println("请选择景区:");
            for (int i = 0; i < spots.size(); i++) {
                System.out.println((i + 1) + ". " + spots.get(i).getName());
            }
            System.out.print("选择景区编号: ");
            int spotChoice = scanner.nextInt();
            scanner.nextLine(); // 消耗换行符

            if (spotChoice < 1 || spotChoice > spots.size()) {
                System.out.println("无效选择！");
                return;
            }

            String scenicSpot = spots.get(spotChoice - 1).getName();

            System.out.print("请输入日记标题: ");
            String title = scanner.nextLine();
            System.out.println("请输入日记内容 (输入'END'结束):");
            StringBuilder content = new StringBuilder();
            String line;
            while (!(line = scanner.nextLine()).equals("END")) {
                content.append(line).append("\n");
            }

            Diary diary = new Diary(currentUser.getUsername(), title, content.toString(), scenicSpot);
            if (diaryService.createDiary(diary)) {
                System.out.println("日记保存成功！");
            } else {
                System.out.println("日记保存失败！");
            }
        } else {
            System.out.println("没有可用的景区信息！");
        }
    }

    private static void viewMyDiaries() {
        System.out.println("\n===== 我的日记 =====");
        List<Diary> diaries = diaryService.getDiariesByAuthor(currentUser.getUsername());
        displayDiaries(diaries);
    }

    private static void viewAllDiaries() {
        System.out.println("\n===== 所有日记 =====");
        List<Diary> diaries = diaryService.getAllDiaries();
        displayDiaries(diaries);
    }

    private static void displayDiaries(List<Diary> diaries) {
        if (diaries == null || diaries.isEmpty()) {
            System.out.println("没有找到日记！");
            return;
        }

        for (Diary diary : diaries) {
            System.out.println("ID: " + diary.getId());
            System.out.println("标题: " + diary.getTitle());
            System.out.println("作者: " + diary.getAuthor());
            System.out.println("景区: " + diary.getScenicSpot());
            System.out.println("创建时间: " + diary.getCreateTime());
            System.out.println("更新时间: " + diary.getUpdateTime());
            System.out.println("内容:");
            System.out.println(diary.getContent());
            System.out.println("-------------------");
        }
    }

    private static void updateDiary() {
        System.out.println("\n===== 修改日记 =====");
        List<Diary> myDiaries = diaryService.getDiariesByAuthor(currentUser.getUsername());

        if (myDiaries.isEmpty()) {
            System.out.println("您还没有写过日记！");
            return;
        }

        System.out.println("请选择要修改的日记:");
        for (int i = 0; i < myDiaries.size(); i++) {
            System.out.println((i + 1) + ". " + myDiaries.get(i).getTitle() +
                    " (" + myDiaries.get(i).getCreateTime() + ")");
        }

        System.out.print("选择日记编号: ");
        int diaryChoice = scanner.nextInt();
        scanner.nextLine(); // 消耗换行符

        if (diaryChoice < 1 || diaryChoice > myDiaries.size()) {
            System.out.println("无效选择！");
            return;
        }

        Diary selectedDiary = myDiaries.get(diaryChoice - 1);

        System.out.print("请输入新标题 (留空保持不变): ");
        String newTitle = scanner.nextLine();
        if (newTitle.isEmpty()) {
            newTitle = selectedDiary.getTitle();
        }

        System.out.println("当前内容:");
        System.out.println(selectedDiary.getContent());
        System.out.println("请输入新内容 (单独一行输入'END'结束，留空保持不变):");
        StringBuilder newContent = new StringBuilder();
        String line;
        while (!(line = scanner.nextLine()).equals("END")) {
            newContent.append(line).append("\n");
        }

        String finalContent = newContent.toString().isEmpty() ?
                selectedDiary.getContent() : newContent.toString();

        if (diaryService.updateDiary(selectedDiary.getId(), currentUser.getUsername(), newTitle, finalContent)) {
            System.out.println("日记修改成功！");
        } else {
            System.out.println("日记修改失败！");
        }
    }

    private static void deleteDiary() {
        System.out.println("\n===== 删除日记 =====");
        List<Diary> myDiaries = diaryService.getDiariesByAuthor(currentUser.getUsername());

        if (myDiaries.isEmpty()) {
            System.out.println("您还没有写过日记！");
            return;
        }

        System.out.println("请选择要删除的日记:");
        for (int i = 0; i < myDiaries.size(); i++) {
            System.out.println((i + 1) + ". " + myDiaries.get(i).getTitle() +
                    " (" + myDiaries.get(i).getCreateTime() + ")");
        }

        System.out.print("选择日记编号: ");
        int diaryChoice = scanner.nextInt();
        scanner.nextLine(); // 消耗换行符

        if (diaryChoice < 1 || diaryChoice > myDiaries.size()) {
            System.out.println("无效选择！");
            return;
        }

        Diary selectedDiary = myDiaries.get(diaryChoice - 1);

        System.out.print("确定要删除日记 '" + selectedDiary.getTitle() + "' 吗? (y/n): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("y")) {
            if (diaryService.deleteDiary(selectedDiary.getId(), currentUser.getUsername())) {
                System.out.println("日记删除成功！");
            } else {
                System.out.println("日记删除失败！");
            }
        } else {
            System.out.println("取消删除操作。");
        }
    }
}