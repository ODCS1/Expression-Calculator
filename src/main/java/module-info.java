module com.projeto.projeto {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.project.project to javafx.fxml;
    exports com.project.project;
}