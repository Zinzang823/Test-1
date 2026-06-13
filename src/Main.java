import java.util.*;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            printHelp();
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
                default:
                    printHelp();
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
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
        String id = m.get("id");
        String name = m.get("name");
        String yearS = m.get("year");
        String major = m.get("major");
        if (id == null || name == null) {
            System.out.println("Missing --id or --name");
            return;
        }
        int year = 0;
        try {
            if (yearS != null)
                year = Integer.parseInt(yearS);
        } catch (Exception e) {
        }
        List<Student> list = Storage.load();
        if (Storage.find(list, id) != null) {
            System.out.println("Student exists");
            return;
        }
        list.add(new Student(id, name, year, major == null ? "" : major));
        Storage.save(list);
        System.out.println("Added.");
    }

    static void cmdList() {
        List<Student> list = Storage.load();
        if (list.isEmpty()) {
            System.out.println("No students.");
            return;
        }
        for (Student s : list)
            System.out.println(s);
    }

    static void cmdDelete(Map<String, String> m) {
        String id = m.get("id");
        if (id == null) {
            System.out.println("Missing --id");
            return;
        }
        List<Student> list = Storage.load();
        Student found = Storage.find(list, id);
        if (found == null) {
            System.out.println("Not found");
            return;
        }
        list.remove(found);
        Storage.save(list);
        System.out.println("Deleted.");
    }

    static void cmdUpdate(Map<String, String> m) {
        String id = m.get("id");
        if (id == null) {
            System.out.println("Missing --id");
            return;
        }
        List<Student> list = Storage.load();
        Student s = Storage.find(list, id);
        if (s == null) {
            System.out.println("Not found");
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
        System.out.println("Updated.");
    }

    static void cmdSearch(String[] args) {
        if (args.length < 2) {
            System.out.println("Provide query");
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
            System.out.println("No matches.");
    }

    static void printHelp() {
        System.out.println(
                "Usage: java -cp bin Main <command> [--flags]\nCommands: add, list, update, delete, search\nExamples:\n  java -cp bin Main add --id 1 --name \"Nguyen Van A\" --year 2024 --major CS\n  java -cp bin Main list");
    }
}
