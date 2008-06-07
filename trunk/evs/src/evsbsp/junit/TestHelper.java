package evsbsp.junit;

import evs.main.Peer;

public class TestHelper {

    private static Peer peer;

    public static Peer getPeer () {
        if (peer != null) {
            return peer;
        }

        String[] args = {};
        peer = new Peer (args);
        peer.run ();
        peer.processCommand ("port=31337");
        peer.processCommand ("listen");
        return peer;
    }
}
