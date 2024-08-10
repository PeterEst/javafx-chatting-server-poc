module com.peterestephan.chattingserver {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.peterestephan.chattingserver to javafx.fxml;
    exports com.peterestephan.chattingserver;
}