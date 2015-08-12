package applications;

import core.*;

public class ContextSharingApplication extends Application implements ConnectionListener {
/**
 * An application to share contexts when participants are connected.
 *
 * The corresponding <code>ContextSharingAppReporter</code> class can be used to record
 * information about the application behavior.
 *
 * @see ContextSharingAppReporter
 * @author smcho
 */
    /** Size of the ping message */
    public static final String CONTEXT_SIZE = "contextSize";

    /** Application ID */
    public static final String APP_ID = "edu.texas.mpc.ContextSharingApplication";

    // Private vars
    private int     contextSize = 0;

    // CONSTRUCTORS

    /**
     * Creates a new ping application with the given settings.
     *
     * @param s	Settings to use for initializing the application.
     */
    public ContextSharingApplication(Settings s) {
        if (s.contains(CONTEXT_SIZE)){
            this.contextSize = s.getInt(CONTEXT_SIZE);
        }
        super.setAppID(APP_ID);
    }

    /**
     * Copy-constructor
     *
     * @param a the input object
     */
    public ContextSharingApplication(ContextSharingApplication a) {
        super(a);
        this.contextSize = a.getContextSize();
    }

    /* OVERRIDE methods */

    /**
     * Handles an incoming message. If the message is a ping message replies
     * with a pong message. Generates events for ping and pong messages.
     *
     * @param msg	message received by the router
     * @param host	host to which the application instance is attached
     */
    @Override
    public Message handle(Message msg, DTNHost host) {
        String type = (String)msg.getProperty("type");
        if (type==null) return msg; // Not a ping/pong message

        // Respond with pong if we're the recipient
        if (msg.getTo().getAddress() == host.getAddress()) {
            // The message is mine
            return null;
        }

        return msg;
    }

    @Override
    public Application replicate() {
        return new ContextSharingApplication(this);
    }

    /**
     * This method is invoked when 
     *
     * @param host to which the application instance is attached
     */
    @Override
    public void update(DTNHost host) {
        double curTime = SimClock.getTime();
        //System.out.println(curTime);
    }

    @Override
    public void hostsConnected(DTNHost host1, DTNHost host2) {
        System.out.printf("Connected: %d <-> %d\n", host1.getAddress(), host2.getAddress());
        // Each host sends context to the other side
        Message m1 = createContext(host1);
        Message m2 = createContext(host2);

        sendContext(host1, host2, m1);
        sendContext(host2, host1, m2);

        smcho.Arith a = new smcho.Arith();
        System.out.println(a.add(20, 20));
    }

    @Override
    public void hostsDisconnected(DTNHost host1, DTNHost host2) {
        System.out.printf("Disconnected: %d <-> %d\n", host1.getAddress(), host2.getAddress());
    }

    // PROPERTIES

    /**
     * @return the lastPing
     */
    public int getContextSize() {
        return contextSize;
    }

    // PRIVATE METHODS

    /**
     *
     * @param host1
     * @param host2
     * @param m1
     */
    private void sendContext(DTNHost host1, DTNHost host2, Message m1) {

    }

    /**
     * @param host
     * @return
     */
    protected Message createContext(DTNHost host) {
        return null;
    }
}