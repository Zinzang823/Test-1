# Test-1
du an test

## Student Manager

Run the simple CLI student manager (requires Python 3.8+):

```bash
python main.py --help
```

Examples:

```bash
# Add a student
python main.py add --id 1 --name "Nguyen Van A" --year 2024 --major "CS"

# List students
python main.py list
```
Java version (no external deps):

```bash
# Compile
javac -d bin src/*.java

# Run (examples)
java -cp bin Main add --id 1 --name "Nguyen Van A" --year 2024 --major CS
java -cp bin Main list
java -cp bin Main search Nguyen
```
