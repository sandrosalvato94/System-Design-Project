package polito.sdp2017.DesignEnvironmentGui;

import java.util.HashMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

/**
 * ScreenController is a container class, able to store all the screens the application needs to
 * display. It offers some utilities to load all the screens given their fxml files and to display
 * each screen when it is requested.
 */
public class ScreensController extends StackPane {
    private HashMap<String, Node> screens = new HashMap<>();
    
    /**
     * Constructor for ScreensController
     */
    public ScreensController() {
        super();
    }

    /**
     * Add a screen to the collection of screens.
     * @param name   : identifier for the screen to be stored
     * @param screen : screen to be stored
     */
    public void addScreen(String name, Node screen) {
        screens.put(name, screen);
    }

    /**
     * Returns a screen previously uploaded given its identifier.
     * @param name : if for the screen requested
     * @return the requested Node
     */
    public Node getScreen(String name) {
        return screens.get(name);
    }

    /**
     * Loads the FXML file, add the screen to the screens map and finally injects the screenPane
     * to the controller.
     * @param name             : identifier for the screen to be loaded
     * @param resource         : fxml file of the screen
     * @param applicationModel : model of the application, for the Model-View-Controller paradigm
     * @return true if the screen is correctly loaded, false otherwise
     */
    public boolean loadScreen(String name, String resource, ApplicationModel applicationModel) {
        try {
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource(resource));
            Parent loadScreen = (Parent) myLoader.load();            
            ControlledScreen myScreenController = ((ControlledScreen) myLoader.getController());
            myScreenController.setScreenParent(this);
            myScreenController.setApplicationModel(applicationModel);
            addScreen(name, loadScreen);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method retrieve a screen previously uploaded given its name and displays it. If another
     * screen is already displayed, the current screen is removed, otherwise the specified screen
     * is just added to the root.
     * @param name : identifier of the screen to be displayed
     * @return true if the screen is correctly displayed, false otherwise
     */
    public boolean setScreen(final String name) {       
        if (screens.get(name) != null) {   					//	if the screen name do not belong by
        													//	any existing screen, an exception is
        													//	thrown
            if (!getChildren().isEmpty()) {
            	getChildren().remove(0);                    //	if a screen is already present
            												//	the current one is removed
            	getChildren().add(0, screens.get(name));    //	add the screen to be displayed
            } else {
                getChildren().add(screens.get(name));       //	if no screen is displayed, just show
                											//	the new one
            }
            return true;
        } else {
            System.out.println("screen hasn't been loaded!!!\n");
            return false;
        }
    }
}

