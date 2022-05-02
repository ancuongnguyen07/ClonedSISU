package fi.tuni.prog3.sisu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import fi.tuni.prog3.sisu.system.*;

/**
 * App class that initializes the program. It loads necessary scenes, and creates a new SkyNet object
 * for data handle of this use instance.
 * 
 * @author Khoa Nguyen
 */
public class App extends Application {

    private static Scene scene;
    private static LoginController loginController;
    private static MainAppController mainAppController;

    /**
     * Start the app. Select the correct scenes from FXML files to load. Also create a new
     * SkyNet object for data handle of this use instance.
     * 
     * @param stage the Stage to be loaded.
     */
    @Override
    public void start(Stage stage) throws IOException {
        SkyNet sn = new SkyNet("src/main/resources/jsons/users.json",
                "src/main/resources/jsons/studyPlan.json");
        App.loginController = new LoginController(sn);
        App.mainAppController = new MainAppController(sn);

        scene = new Scene(loadFXML("Login.fxml", "login"), 1000, 600);
        stage.setTitle("Sisu Student System");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Call the updateActiveUser() method in the MainAppController to update the GUI accordingly
     * to the active user. Name, role, and study degree data.
     */
    public static void UpdateMainApp() {
        mainAppController.updateActiveUser();
    }

    static void setRoot(String fxml, String controllerName) throws IOException {
        scene.setRoot(loadFXML(fxml, controllerName));
    }

    private static Parent loadFXML(String fxml, String controllerName) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml));
        if (controllerName.equals("login")) {
            loader.setController(loginController);
        } else if (controllerName.equals("main")) {
            loader.setController(mainAppController);
        }
        return loader.load();
    }

    /**
     * Good old main.
     * @param args arguments to pass in.
     */
    public static void main(String[] args) {
        launch();
    }

}