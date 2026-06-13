import json
from pathlib import Path
from typing import List, Optional

DATA_FILE = Path(__file__).parent / "students.json"


def load_students() -> List[dict]:
    if not DATA_FILE.exists():
        return []
    try:
        with open(DATA_FILE, "r", encoding="utf-8") as f:
            return json.load(f)
    except Exception:
        return []


def save_students(data: List[dict]) -> None:
    with open(DATA_FILE, "w", encoding="utf-8") as f:
        json.dump(data, f, ensure_ascii=False, indent=2)


def find_student(data: List[dict], student_id: str) -> Optional[dict]:
    for s in data:
        if str(s.get("id")) == str(student_id):
            return s
    return None
