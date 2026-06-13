public class Student {
    public String id;
    public String name;
    public int year;
    public String major;

    public Student(String id, String name, int year, String major) {
        this.id = id;
        this.name = name == null ? "" : name;
        this.year = year;
        this.major = major == null ? "" : major;
    }

    public String toLine() {
        String safeName = name.replace("|", " ");
        String safeMajor = major.replace("|", " ");
        return id + "|" + safeName + "|" + year + "|" + safeMajor;
    }

    public static Student fromLine(String line) {
        String[] p = line.split("\\|", -1);
        String id = p.length > 0 ? p[0] : "";
        String name = p.length > 1 ? p[1] : "";
        int year = 0;
        if (p.length > 2 && !p[2].isEmpty()) {
            try {
                year = Integer.parseInt(p[2]);
            } catch (NumberFormatException e) {
                year = 0;
            }
        }
        String major = p.length > 3 ? p[3] : "";
        return new Student(id, name, year, major);
    }

    @Override
    public String toString() {
        return id + ": " + name + " (" + year + ") - " + major;
    }
}
