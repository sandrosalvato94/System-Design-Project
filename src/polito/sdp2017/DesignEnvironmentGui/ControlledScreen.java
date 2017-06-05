package polito.sdp2017.DesignEnvironmentGui;

/**
 * This is interface is implemented by any screen controller of this application; it forces the
 * controllers to implement methods for specifying the application model and for setting the screen
 * parent container.
 */
public interface ControlledScreen {    
    /**
     * Set the main controller, in charge of storing all the screens.
     * @param screenPage : the main screen controller
     */
    public void setScreenParent(ScreensController screenPage);
    
    /**
     * Set the class which acts as model for this application, assuming a Model-View_controller
     * paradigm
     * @param applicationModel : model for this application
     */
    public void setApplicationModel(ApplicationModel applicationModel);
}
