package applications;

import core.*;
import smcho.ContextMessage;
import smcho.Database;
import smcho.DatabaseWithStrategy;

import java.util.List;

public class ContextSharingApplication extends Application implements ConnectionListener, MessageListener {
/**
 * An application to share contexts when hosts are connected in a DTN.
 *
 * 1. The corresponding <code>ContextSharingAppReporter</code> class can be used to record
 * information about the application behavior.
 *
 * 2. The mechanism is as follows:
 *    2.1 Each host makes their own context to be shared.
 *    2.2 When a message (context) is received, the context database is updated, and the next set of contexts
 *        are calculated.
 *    2.3 When hosts are connected, the created context at 2.2 is made into message.
 *
 * @see ContextSharingAppReporter
 * @author smcho
 */
    /** Size of the ping message */
    //public static final String CONTEXT_SIZE = "contextSize";
    public static final String SUMMARYTYPE = "summaryType";
    public static final String CONTEXTSUMMARY = "ContextSummary";
    public static final String STRAGETY = "strategy";
    public static final String DIRECTORY = "directory";

    /** Application ID */
    public static final String APP_ID = "edu.texas.mpc.ContextSharingApplication";

    private static final String MESSAGE_FORMAT = "(%d->%d[%d])";

    // Private vars
    //private int     contextSize = 0;
    private String   summaryType = "";
    private String   strategy = "";
    private String   directory = "";

    public Database database = null;

    //region CONSTRUCTORS
    /**
     * Creates a new ping application with the given settings.
     *
     * @param s	Settings to use for initializing the application.
     */
    public ContextSharingApplication(Settings s) {
//        if (s.contains(CONTEXT_SIZE)){
//            this.contextSize = s.getInt(CONTEXT_SIZE);
//        }
        Settings s2 = new Settings(CONTEXTSUMMARY);
        this.summaryType = s2.getSetting(SUMMARYTYPE);
        this.strategy = s2.getSetting(STRAGETY);
        this.directory = s2.getSetting(DIRECTORY);

        this.database = new DatabaseWithStrategy(this.strategy);
        this.database.load(this.directory);

        super.setAppID(APP_ID);
    }

    /**
     * Copy-constructor
     *
     * @param a the input object
     */
    public ContextSharingApplication(ContextSharingApplication a) {
        super(a);
        //this.contextSize = a.getContextSize();
    }
    //endregion

    //region OVERRIDE METHODS
    /**
     * Handles an incoming message. If the message is a ping message replies
     * with a pong message. Generates events for ping and pong messages.
     *
     *  handle() implements the 2.2 in the description
     *  2.2 When a message (context) is received, the context database is updated, and the next set of contexts
     *      are calculated.
     *
     * @param msg	message received by the router
     * @param host	host to which the application instance is attached
     */
    @Override
    public Message handle(Message msg, DTNHost host) {
        String type = (String)msg.getProperty("type");
        if (type==null) return msg;

        if (type == "context") { // message transferred
            if (msg.getTo().getAddress() == host.getAddress()) {
                System.out.printf(">>> %5.3f %s\n", SimClock.getTime(), msg.getId());
                ContextMessage contextMessage = messageToContextMessage(msg);
                //database.addToHistory(host.getAddress(), contextMessage.getId());

                msg.getFrom().deleteMessage(msg.getId(), true);
                return null;
            }
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
    }

    /**
     * 2.3 When hosts are connected, the created context at 2.2 is made into message.
     *
     * @param host1 Host that initiated the connection
     * @param host2 Host that was connected to
     */
    @Override
    public void hostsConnected(DTNHost host1, DTNHost host2) {
        // Show message
        System.out.printf("!!!! %5.3f, Connected: %d <-> %d\n", SimClock.getTime(), host1.getAddress(), host2.getAddress());

        // get Context
        int h1 = host1.getAddress();
        int h2 = host2.getAddress();
        double simTime = SimClock.getTime();

        ContextMessage c1 = database.get(h1);
        c1.setHost1(h1); c1.setHost2(h2); c1.setTime(simTime);
        ContextMessage c2 = database.get(host2.getAddress());
        c2.setHost1(h2); c1.setHost2(h1); c1.setTime(simTime);

        // Message is created from the context
        // todo:: Better exception handling than printing the trace
        try {
            Message m1 = contextMessageToMessage(host1.getAddress(), host2.getAddress(), c1);
            Message m2 = contextMessageToMessage(host2.getAddress(), host1.getAddress(), c2);
            host1.createNewMessage(m1);
            host2.createNewMessage(m2);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void hostsDisconnected(DTNHost host1, DTNHost host2) {
        System.out.printf("%5.3f, Disconnected: %d <-> %d\n", SimClock.getTime(), host1.getAddress(), host2.getAddress());
    }
    //endregion

    //region PROPERTIES
    //endregion

    //region PRIVATE METHODS
    private DTNHost getHost(int id) throws Exception {
        SimScenario sim = SimScenario.getInstance();
        List<DTNHost> hosts = sim.getHosts();

        for (DTNHost h: hosts) {
            if (h.getAddress() == id) return h;
        }
        throw new Exception(String.format("No matching host id %d", id));
    }

    private Message contextMessageToMessage(int host1, int host2, ContextMessage contextMessage) throws Exception {
        contextMessage.setHost1(host1);
        contextMessage.setHost2(host2);
        return contextMessageToMessage(contextMessage);
    }

    private Message contextMessageToMessage(ContextMessage contextMessage) throws Exception {
        int host1 = contextMessage.getHost1();
        int host2 = contextMessage.getHost2();
        DTNHost dtnhost1 = getHost(host1);
        DTNHost dtnhost2 = getHost(host2);
        int size = contextMessage.getSize();
        String message = contextMessage.getContent() + String.format(MESSAGE_FORMAT, host1, host2, size);

        Message m1 = new Message(dtnhost1, dtnhost2, message, size);
        m1.addProperty("type", "context");
        m1.setAppID(APP_ID);
        return m1;
    }

    /**
     *
     * Given a message, make it into a context
     *
     * @param msg
     * @return
     */
    private ContextMessage messageToContextMessage(Message msg) {
        double clock = SimClock.getTime();
        return new ContextMessage(
                msg.getFrom().getAddress(),
                msg.getTo().getAddress(),
                msg.getSize(),
                clock,
                msg.getId());
    }
    //endregion

    //region MESSAGE listener implementation
    @Override
    public void newMessage(Message m) {
        System.out.printf("1) Message created: %s\n", m.getId());
    }

    @Override
    public void messageTransferStarted(Message m, DTNHost from, DTNHost to) {
        System.out.printf("2) %5.3f Message transfer started %d -> %d (%s)\n", SimClock.getTime(), from.getAddress(), to.getAddress(), m.getId());
    }

    @Override
    public void messageDeleted(Message m, DTNHost where, boolean dropped) {
        System.out.printf("XXX: Message deleted - %s at %d\n", m.getId(), where.getAddress());
    }

    @Override
    public void messageTransferAborted(Message m, DTNHost from, DTNHost to) {
        System.out.printf("???: Message aborted - %s\n", m.getId());
    }

    @Override
    public void messageTransferred(Message m, DTNHost from, DTNHost to, boolean firstDelivery) {
        System.out.printf("3) %5.3f Message transferred %d -> %d (%s) %b\n", SimClock.getTime(), from.getAddress(), to.getAddress(), m.getId(), firstDelivery);
    }
    //endregion

}