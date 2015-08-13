package applications;

import core.*;
import smcho.Context;
import smcho.Database;

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

    //region CONSTRUCTORS
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
    //endregion

    //region OVERRIDE METHODS
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
        Database d = Database.get();
        Context c1 = d.getContext(host1.getAddress());
        Context c2 = d.getContext(host2.getAddress());
        int size1 = c1.getSize();
        int size2 = c2.getSize();

        Message m1 = new Message(host1, host2, getId(), size1);
        m1.addProperty("type", "context");
        m1.setAppID(APP_ID);
        host1.createNewMessage(m1);

        Message m2 = new Message(host2, host1, getId(), size2);
        m2.addProperty("type", "context");
        m2.setAppID(APP_ID);
        host2.createNewMessage(m2);

        d.add(host1.getAddress(), host2.getAddress(), c1);
        d.add(host2.getAddress(), host1.getAddress(), c2);
    }

    @Override
    public void hostsDisconnected(DTNHost host1, DTNHost host2) {
        System.out.printf("Disconnected: %d <-> %d\n", host1.getAddress(), host2.getAddress());
    }
    //endregion

    //region PROPERTIES
    /**
     * @return the lastPing
     */
    public int getContextSize() {
        return contextSize;
    }
    //endregion

    //region PRIVATE METHODS
    /**
     *
     * @param host1
     * @param host2
     * @param m1
     */
    private void sendContext(DTNHost host1, DTNHost host2, Message m1) {
        //Database.connect(host1.getAddress(), host2.getAddress(), m1);
    }

    private Context messageToContext(Message m) {
        Context c =  Context.create();
        return c;
    }

    private String getId() {
        return "";
    }
    //endregion
}