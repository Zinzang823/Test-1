import java.nio.file.*;
import java.io.IOException;
import java.util.*;

public class Storage {
    private static Path DATA = Paths.get("students.txt");

    public static List<Student> load() {
        List<Student> res = new ArrayList<>();
        if (!Files.exists(DATA))
            return res;
        try {
            List<String> lines = Files.readAllLines(DATA);
            for (String l : lines) {
                if (l.trim().isEmpty())
                    continue;
                res.add(Student.fromLine(l));
            }
        } catch (IOException e) {
            System.err.println("Failed to read students: " + e.getMessage());
        }
        return res;
    }

    public static void save(List<Student> list) {
        List<String> lines = new ArrayList<>();
        for (Student s : list)
            lines.add(s.toLine());
        try {
            Files.write(DATA, lines);
        } catch (IOException e) {
            System.err.println("Failed to save students: " + e.getMessage());
        }
    }

    public static Student find(List<Student> list, String id) {
        for (Student s : list)
            if (s.id.equals(id))
                return s;
        return null;
    }
}
