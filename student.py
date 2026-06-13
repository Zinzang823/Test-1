class Student:
    def __init__(self, student_id: str, name: str, year: int = None, major: str = ""):
        self.id = str(student_id)
        self.name = name
        self.year = year
        self.major = major

    def to_dict(self) -> dict:
        return {"id": self.id, "name": self.name, "year": self.year, "major": self.major}

    @staticmethod
    def from_dict(d: dict) -> 'Student':
        return Student(d.get("id"), d.get("name"), d.get("year"), d.get("major"))
