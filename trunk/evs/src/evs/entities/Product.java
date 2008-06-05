/**
 * 
 */
package evs.entities;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Gerald Scharitzer
 *
 */
public class Product implements Serializable {
	
	private int id;
	private String name;
        private BigDecimal price;
        
        public Product () {
            
        }
        
        public Product (String name, BigDecimal price) {
            this.name = name;
            this.price = price;
        }
        
        public int getId () {
            return this.id;
        }
        
        public void setId (int id) {
            this.id = id;
        }
        
        public String getName () {
            return this.name;
        }
        
        public void setName (String name) {
            this.name = name;
        }
        
        public BigDecimal getPrice () {
            return this.price;
        }
        
        public void setPrice (BigDecimal price) {
            this.price = price;
        }

}
