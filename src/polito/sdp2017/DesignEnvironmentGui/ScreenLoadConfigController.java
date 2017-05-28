package polito.sdp2017.DesignEnvironmentGui;

public class ScreenLoadConfigController implements ControlledScreen {
	ApplicationModel applicationModel;
    ScreensController myController;

    @Override
    public void setScreenParent(ScreensController screenParent){
    	myController = screenParent;
    }

	@Override
	public void setApplicationModel(ApplicationModel applicationModel) {
		this.applicationModel = applicationModel;
	}
}
