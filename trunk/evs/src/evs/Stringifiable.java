/**
 * 
 */
package evs;

/**
 * applies to objects, which can be converted to strings and vice versa.
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 */
public interface Stringifiable<T> {
	
	String stringify();
	T destringify(String s);

}
