import argparse
from student import Student
from storage import load_students, save_students, find_student


def cmd_add(args):
    data = load_students()
    if find_student(data, args.id):
        print("Student with this ID already exists")
        return
    s = Student(args.id, args.name, args.year, args.major)
    data.append(s.to_dict())
    save_students(data)
    print("Added.")


def cmd_list(args):
    data = load_students()
    if not data:
        print("No students.")
        return
    for s in data:
        print(f"{s.get('id')}: {s.get('name')} ({s.get('year')}) - {s.get('major')}")


def cmd_delete(args):
    data = load_students()
    s = find_student(data, args.id)
    if not s:
        print("Not found")
        return
    data = [x for x in data if str(x.get("id")) != str(args.id)]
    save_students(data)
    print("Deleted.")


def cmd_update(args):
    data = load_students()
    s = find_student(data, args.id)
    if not s:
        print("Not found")
        return
    if args.name:
        s["name"] = args.name
    if args.year is not None:
        s["year"] = args.year
    if args.major is not None:
        s["major"] = args.major
    save_students(data)
    print("Updated.")


def cmd_search(args):
    data = load_students()
    q = args.query.lower()
    results = [s for s in data if q in str(s.get('id','')).lower() or q in s.get('name','').lower() or q in s.get('major','').lower()]
    if not results:
        print("No matches.")
        return
    for s in results:
        print(f"{s.get('id')}: {s.get('name')} ({s.get('year')}) - {s.get('major')}")


def main():
    parser = argparse.ArgumentParser(prog="Student Manager")
    sub = parser.add_subparsers(dest="cmd")

    p_add = sub.add_parser("add")
    p_add.add_argument("--id", required=True)
    p_add.add_argument("--name", required=True)
    p_add.add_argument("--year", type=int)
    p_add.add_argument("--major", default="")
    p_add.set_defaults(func=cmd_add)

    p_list = sub.add_parser("list")
    p_list.set_defaults(func=cmd_list)

    p_delete = sub.add_parser("delete")
    p_delete.add_argument("--id", required=True)
    p_delete.set_defaults(func=cmd_delete)

    p_update = sub.add_parser("update")
    p_update.add_argument("--id", required=True)
    p_update.add_argument("--name")
    p_update.add_argument("--year", type=int)
    p_update.add_argument("--major")
    p_update.set_defaults(func=cmd_update)

    p_search = sub.add_parser("search")
    p_search.add_argument("query", help="query string")
    p_search.set_defaults(func=cmd_search)

    args = parser.parse_args()
    if not hasattr(args, "func"):
        parser.print_help()
        return
    args.func(args)


if __name__ == "__main__":
    main()
