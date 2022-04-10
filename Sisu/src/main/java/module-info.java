module fi.tuni.prog3.sisu {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens fi.tuni.prog3.sisu to javafx.fxml;
    exports fi.tuni.prog3.sisu;
}
