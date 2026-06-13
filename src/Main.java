import java.util.*;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            interactiveMode();
            return;
        }
        String cmd = args[0];
        Map<String, String> flags = parseFlags(args);
        try {
            switch (cmd) {
                case "add":
                    cmdAdd(flags);
                    break;
                case "list":
                    cmdList();
                    break;
                case "delete":
                    cmdDelete(flags);
                    break;
                case "update":
                    cmdUpdate(flags);
                    break;
                case "search":
                    cmdSearch(args);
                    break;
                case "tim":
                case "find":
                    cmdFind(args, flags);
                    break;
                default:
                    printHelp();
                    break;
            }
        } catch (Exception e) {
            System.err.println("Lỗi: " + e.getMessage());
        }
    }

    static void interactiveMode() {
        Scanner sc = new Scanner(System.in, "UTF-8");
        while (true) {
            System.out.println();
            System.out.println("=== QUẢN LÝ SINH VIÊN ===");
            System.out.println("1. Thêm sinh viên");
            System.out.println("2. Danh sách sinh viên (theo id tăng dần)");
            System.out.println("3. Tìm sinh viên theo id");
            System.out.println("4. Cập nhật sinh viên");
            System.out.println("5. Xóa sinh viên");
            System.out.println("6. Thoát");
            System.out.print("Chọn (1-6): ");
            String opt = sc.nextLine().trim();
            try {
                if (opt.equals("1")) {
                    System.out.print("Họ và tên: ");
                    String name = sc.nextLine().trim();
                    System.out.print("Năm (bỏ trống nếu không có): ");
                    String yearS = sc.nextLine().trim();
                    System.out.print("Ngành: ");
                    String major = sc.nextLine().trim();
                    Map<String, String> m = new HashMap<>();
                    m.put("name", name);
                    if (!yearS.isEmpty())
                        m.put("year", yearS);
                    if (!major.isEmpty())
                        m.put("major", major);
                    cmdAdd(m);
                    pause();
                } else if (opt.equals("2")) {
                    cmdList();
                    pause();
                } else if (opt.equals("3")) {
                    System.out.print("Nhập id cần tìm: ");
                    String id = sc.nextLine().trim();
                    cmdFind(new String[] { "tim", id }, new HashMap<>());
                    pause();
                } else if (opt.equals("4")) {
                    System.out.print("Nhập id cần cập nhật: ");
                    String id = sc.nextLine().trim();
                    Map<String, String> m = new HashMap<>();
                    m.put("id", id);
                    System.out.print("Họ và tên mới (bỏ trống để giữ nguyên): ");
                    String name = sc.nextLine().trim();
                    if (!name.isEmpty())
                        m.put("name", name);
                    System.out.print("Năm mới (bỏ trống để giữ nguyên): ");
                    String yearS = sc.nextLine().trim();
                    if (!yearS.isEmpty())
                        m.put("year", yearS);
                    System.out.print("Ngành mới (bỏ trống để giữ nguyên): ");
                    String major = sc.nextLine().trim();
                    if (!major.isEmpty())
                        m.put("major", major);
                    cmdUpdate(m);
                    pause();
                } else if (opt.equals("5")) {
                    System.out.print("Nhập id cần xóa: ");
                    String id = sc.nextLine().trim();
                    Map<String, String> m = new HashMap<>();
                    m.put("id", id);
                    cmdDelete(m);
                    pause();
                } else if (opt.equals("6")) {
                    System.out.println("Tạm biệt!");
                    break;
                } else {
                    System.out.println("Lựa chọn không hợp lệ.");
                }
            } catch (Exception e) {
                System.err.println("Lỗi: " + e.getMessage());
            }
        }
        sc.close();
    }

    static Map<String, String> parseFlags(String[] args) {
        Map<String, String> m = new HashMap<>();
        for (int i = 1; i < args.length; i++) {
            String a = args[i];
            if (a.startsWith("--")) {
                String k = a.substring(2);
                if (i + 1 < args.length) {
                    m.put(k, args[i + 1]);
                    i++;
                } else {
                    m.put(k, "");
                }
            }
        }
        return m;
    }

    static void cmdAdd(Map<String, String> m) {
        String name = m.get("name");
        String yearS = m.get("year");
        String major = m.get("major");
        if (name == null) {
            System.out.println("Thiếu tham số --name");
            return;
        }
        int year = 0;
        try {
            if (yearS != null)
                year = Integer.parseInt(yearS);
        } catch (Exception e) {
        }
        List<Student> list = Storage.load();
        String id = Storage.nextId();
        list.add(new Student(id, name, year, major == null ? "" : major));
        Storage.save(list);
        System.out.println("Đã thêm. ID: " + id);
    }

    static void cmdList() {
        List<Student> list = Storage.load();
        if (list.isEmpty()) {
            System.out.println("Không có sinh viên.");
            return;
        }
        Storage.sortById(list);
        System.out.printf("%-6s | %-30s | %-6s | %-20s\n", "ID", "Họ và tên", "Năm", "Ngành");
        System.out.println("---------------------------------------------------------------------");
        for (Student s : list) {
            System.out.printf("%-6s | %-30s | %-6d | %-20s\n", s.id, s.name, s.year, s.major);
        }
    }

    static void cmdDelete(Map<String, String> m) {
        String id = m.get("id");
        if (id == null) {
            System.out.println("Thiếu tham số --id");
            return;
        }
        List<Student> list = Storage.load();
        Student found = Storage.find(list, id);
        if (found == null) {
            System.out.println("Không tìm thấy sinh viên");
            return;
        }
        list.remove(found);
        Storage.save(list);
        System.out.println("Đã xóa.");
    }

    static void cmdUpdate(Map<String, String> m) {
        String id = m.get("id");
        if (id == null) {
            System.out.println("Thiếu tham số --id");
            return;
        }
        List<Student> list = Storage.load();
        Student s = Storage.find(list, id);
        if (s == null) {
            System.out.println("Không tìm thấy sinh viên");
            return;
        }
        if (m.containsKey("name"))
            s.name = m.get("name");
        if (m.containsKey("year")) {
            try {
                s.year = Integer.parseInt(m.get("year"));
            } catch (Exception e) {
            }
        }
        if (m.containsKey("major"))
            s.major = m.get("major");
        Storage.save(list);
        System.out.println("Đã cập nhật.");
    }

    static void cmdSearch(String[] args) {
        if (args.length < 2) {
            System.out.println("Cần chuỗi tìm kiếm");
            return;
        }
        String q = args[1].toLowerCase();
        List<Student> list = Storage.load();
        boolean any = false;
        for (Student s : list) {
            if (s.id.toLowerCase().contains(q) || s.name.toLowerCase().contains(q)
                    || s.major.toLowerCase().contains(q)) {
                System.out.println(s);
                any = true;
            }
        }
        if (!any)
            System.out.println("Không tìm thấy kết quả.");
    }

    static void cmdFind(String[] args, Map<String, String> flags) {
        String id = null;
        if (args.length >= 2 && !args[1].startsWith("--"))
            id = args[1];
        if (id == null)
            id = flags.get("id");
        if (id == null) {
            System.out.println("Thiếu id để tìm");
            return;
        }
        List<Student> list = Storage.load();
        Student s = Storage.find(list, id);
        if (s == null) {
            System.out.println("Không tìm thấy sinh viên có id: " + id);
            return;
        }
        System.out.println("+------+--------------------------------+------+----------------------+");
        System.out.printf("| %-4s | %-30s | %-4s | %-20s |\n", "ID", "Họ và tên", "Năm", "Ngành");
        System.out.println("+------+--------------------------------+------+----------------------+");
        System.out.printf("| %-4s | %-30s | %-4d | %-20s |\n", s.id, s.name, s.year, s.major);
        System.out.println("+------+--------------------------------+------+----------------------+");
    }

    static void printHelp() {
        System.out.println(
                "Sử dụng: java -cp bin Main <lệnh> [--flags]\nLệnh: add, list, update, delete, search, tim\nVí dụ:\n  java -cp bin Main add --name \"Nguyen Van A\" --year 2024 --major CS\n  java -cp bin Main list\n  java -cp bin Main tim 1");
    }

    static void pause() {
        System.out.println("Nhấn Enter để tiếp tục...");
        try {
            System.in.read();
            // consume rest of line
            while (System.in.available() > 0)
                System.in.read();
        } catch (Exception e) {
        }
    }
}
