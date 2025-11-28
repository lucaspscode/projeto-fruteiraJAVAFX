module com.projeto {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens com.projeto to javafx.fxml;
    opens com.projeto.controllers.cliente to javafx.fxml;
    opens com.projeto.controllers.admin to javafx.fxml;
    exports com.projeto;
    exports com.projeto.models;
    exports com.projeto.controllers.cliente;
    exports com.projeto.controllers.admin;
}
