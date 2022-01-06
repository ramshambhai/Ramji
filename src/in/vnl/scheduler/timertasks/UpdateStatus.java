package in.vnl.scheduler.timertasks;

import java.util.TimerTask;
import in.vnl.api.twog.TwogOperations;
import in.vnl.api.common.livescreens.DeviceStatusServer;

public class UpdateStatus extends TimerTask 
{
	public void run()
	{
		new TwogOperations().updateStatusOfBts("all");
		new DeviceStatusServer().sendText("ok");
	}
}
