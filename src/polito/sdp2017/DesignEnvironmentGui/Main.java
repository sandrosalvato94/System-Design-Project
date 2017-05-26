package polito.sdp2017.DesignEnvironmentGui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static final String screenStartID = "start";
    public static final String screenManageIPID = "manageIP";
    public static final String screenCreateConfigID = "createConfig";
    public static final String screenLoadConfigID = "loadConfig.fxml";
    public static final String screenStartFile = "ScreenStart.fxml";
    public static final String screenManageIPFile = "ScreenManageIP.fxml";
    public static final String screenCreateConfigFile = "ScreenCreateConfig.fxml";
    public static final String screenLoadConfigFile = "ScreenLoadConfig.fxml";
    
    @Override
    public void start(Stage primaryStage) {     
        ScreensController mainContainer = new ScreensController();
        mainContainer.loadScreen(Main.screenStartID, Main.screenStartFile);
        mainContainer.loadScreen(Main.screenManageIPID, Main.screenManageIPFile);
        mainContainer.loadScreen(Main.screenCreateConfigID, Main.screenCreateConfigFile);
        mainContainer.loadScreen(Main.screenLoadConfigID, Main.screenLoadConfigFile);
        
        mainContainer.setScreen(Main.screenStartID);
        
        Group root = new Group();
        root.getChildren().addAll(mainContainer);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Design environment for LATTICE based FPGA boards");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
