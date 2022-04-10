# Programming 3 Project: Sisu Unravel

Team members:

- Cuong Nguyen
- Khoa Nguyen
- An Nguyen

The project's goal is to create an implementation of the Sisu system, using Java and the JavaFX library. The program will have a working GUI, as well as functionalities that will be described briefly in this `README.md` and in more detailed in the [provided documentation]().

# Program structure

## Classes

The program will have the following `Class Tree`

1. `AbstractModule`
   This is the basic study module that contains all the courses and study modules for each specified student. This class includes:

- `DegreeProgram`
- `CourseUnit`
- `StudyModule`

1.  `StudyPlan` the study plans for each student. Each student can have multiple different study plans at once. Each study plan can contains multiple `AbstractModule`
2.  `User` can be students or teachers.

- `Student`
- `Teacher`

And the program will also have these utility classes:

- `JsonReader`
- `JsonWriter`
- `APIFetcher`

## GUI

For the GUI, the program will have a `MainWindow` that shows at the start. Within this window, the user can:

1. **Login:** and identified them as student, or teacher.
2. **View a Homepage:** that shows basic information based on the logged in user, i.e., credits, finished and on-going courses for a student; or courses and student information for a teacher.
3. **Courses:** edit and configure the courses. Could be changing the courses in a `DegreeProgram`, or something else.
4. **Settings:** change the settings of the program. Maybe change the password and view finished courses?
