package polito.sdp2017.DesignEnvironmentGui;

public interface ControlledScreen {    
    //This method will allow the injection of the Parent ScreenPane
    public void setScreenParent(ScreensController screenPage);
    
    public void setApplicationModel(ApplicationModel applicationModel);
}
