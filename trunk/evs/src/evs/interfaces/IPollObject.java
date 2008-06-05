/**
 * 
 */
package evs.interfaces;

/**
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 *
 */
public interface IPollObject {
	
	boolean isResultAvailable();
	void setResult(Object result);
	void setException(Exception e);
	Object getResult() throws Exception;
	void reset();

}
