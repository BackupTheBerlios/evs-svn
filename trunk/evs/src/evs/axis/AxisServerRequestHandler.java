/**
 * 
 */
package evs.axis;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.engine.AxisServer;

/**
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 *
 */
public class AxisServerRequestHandler extends AxisServer implements Runnable {
	
	public AxisServerRequestHandler() {
		super(false);
	}

	public void run() {
		try {
			configContext = ConfigurationContextFactory.createConfigurationContextFromFileSystem("axis/repository","axis/conf/axis2.xml");
			start();
		} catch (AxisFault e) {
			e.printStackTrace();
			return;
		}
	}
	
}
