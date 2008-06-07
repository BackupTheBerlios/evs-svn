package evsbsp.junit;

import evs.core.ACT;

public class TestACT extends ACT {

    private Integer i;
    
    public TestACT (Integer i) {
        super ();
        this.i = i;
    }
    
    public Integer getI () {
        return this.i;
    }
}
