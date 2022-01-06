package in.vnl.api.netscan;

import java.util.TimerTask;
import in.vnl.api.twog.TwogOperations;
import in.vnl.api.common.livescreens.DeviceStatusServer;

public class NetscanTask implements Runnable
{
	public void run()
	{
		new NetscanJob().executeNetscanJob();
	}
}