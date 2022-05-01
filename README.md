# Programming 3 Project: Sisu Unravel

The project's goal is to create an implementation of the Sisu system, using Java, JavaFX library, and SceneBuilder. The program will have a working GUI, as well as functionalities that will be described briefly in this `README.md` and in more detailed in the [provided documentation]().

Our team included:

- Cuong Nguyen, cuong.nguyen@tuni.fi, `050358715`. Handle the data manipulation with the `SkyNet` class.
- Khoa Nguyen, khoa.h.nguyen@tuni.fi, `50359141`. Handle the GUI part of the program, using the classes provided by An and Cuong.
- An Nguyen, an.nguyen@tuni.fi, `50359099`. Handle the API data fetching with the `APIReader` class.

</br>

---

## Program structure

### **Classes**

The program will have the following **Class Tree**.

1. `AbstractModule` is the basic study module that contains all the courses and study modules for each specified student. This class includes:

   - `DegreeProgram` is the degree of a programme. This class contains information about sub study modules and courses of that degree. This class has the highest level in the class structure.
   - `StudyModule` is the module of courses in the degree programme. Each study module can contain multiple courses, or sub study modules under it. This class is the middle level of the class structure.
   - `CourseUnit` is the courses inside each study module of a program. This class has the lowest level in the class structure.

2. `User` is the user of the Sisu service. They can be students or teachers.

   - `Student` are the students that studying in the degree programs. Every student has:
     - Degree program
     - And study modules with courses under that degree.
   - `Teacher` are the teachers of the courses. Every teacher can **(Not implemented in this version)** :
     - Have access to the courses. They can modify the content of each course.
     - Grade the students.

And the program will also have these utility classes:

- `JsonReader`
- `JsonWriter`
- `APIFetcher`

### **Data Handling**

The data for the application is handled primarily with 2 classes: `SkyNet` (it's a reference!) and `APIReader`. The data is read from JSON files that contains users' data, and the corresponding course data is fetched from the Sisu API.

1. `SkyNet`
2. `APIReader`

</br>

---

## Navigation

The application has 2 windows: the **Login** window and the **Main App** window. After running the application, user will land on the **Login** window.

1. **Login**
2. **Main App**

</br>

---

## GUI

For the GUI, the program will have a `Login` screen that shows at the start. Within this window, the user can **Login:** and identified them as student, or teacher. Upon successfully logged in, the user will be directed to a `MainWindow` that has:

1. **Homepage:** that shows the study structure of the active user, i.e., the degree program, study modules, and courses. This structure is shown in a `TreeView` format.
2. **Courses:** view all of the courses of the active user in the current degree program. They can view the information of a course by clicking on them. They can check their finished courses here. This session also shows the progress of this active user in the degree program: how many credits have they finished, what is the average grade, and how many total credits are there in the current program.
3. **Settings:** change the credentials of the active users, i.e., changing user or password.
4. **Help:** viewing the documentation of the program.

</br>

---

## Testing

The tests are written with JUnit Tests.

[JsonWriter test](./Sisu/src/test/java/fi/tuni/prog3/sisu/utility/JsonWriterTest.java) : testing methods for saving user information - `username`, `full name`, `password`, `salt` into a JSON file  

[SkyNet test](./Sisu/src/test/java/fi/tuni/prog3/sisu/system/SkyNetTest.java) :  
- Testing methods for loading user information - `username`, `full name`, `password`, `salt`, `degreeID`. `modules`, `passedCourses` - into a `Student` object
- Testing methods for validating the password which a user typing when login/change-password. A password is hashed by **PBKDF2** applying **HMAC-SHA1** to the input password along with a `salt` value and repeats the process many times to produce a derived key (hashed-password)
- Testing methods for creating a new `User` which includes checking existed `username`, valid `password`, and then saving to a JSON file


</br>

---

## Using the Application

The project can be compile by:

1. Opening the the folders as a NetBeans project, then compile and run in NetBeans.
2. In the root directory of the project, run

```
$ mvn javafx:run
```

</br>

---

## Finalization
