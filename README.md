# ContextSharingSimulation
Context Sharing Simulation on ONE Simulator

## Changes

### [2015/08/17]

#### SimScenario

Line 314, `protected void createHosts()`

    Context c = CREATE CONTEXT;
    String key = c.getId();
    host.addContext(KEY, VALUE)
    hosts.add(host);
    
#### DTNHost

DTNHost now contains the context:

    private Map<String, Context> contexts = new TreeMap<String, Context>();
    
    ...

    public void addContext(String key, Context context) {
        this.contexts.put(key, context);
    }
    public void deleteContext(String key) {
        if (this.contexts.containsKey(key)) {
            this.contexts.remove(key);
        }
    }

### [2015/08/10] Project structure change

All the one simulator code is in one_simulator directory, all the settings should be in the one_simulator directory

The working direcgtory becomes PATH/one_simulator

### [2015/08/10] Applications are listeners.

core/SimScenario.java/createHosts() method: for each application, add them into listener list based on what types it implements.

    protoApp = (Application)t.createIntializedObject(
            APP_PACKAGE + t.getSetting(APPTYPE_S));
    // smcho added
    // If the application implements connectionListner, it should be added to the lister group
    if (protoApp instanceof ConnectionListener) {
        addConnectionListener((ConnectionListener) protoApp);
    }
    if (protoApp instanceof MessageListener) {
        addMessageListener((MessageListener) protoApp);
    }
    if (protoApp instanceof MovementListener) {
        addMovementListener((MovementListener) protoApp);
    }
    if (protoApp instanceof UpdateListener) {
        addUpdateListener((UpdateListener) protoApp);
    }
    if (protoApp instanceof ApplicationListener) {
        addApplicationListener((ApplicationListener) protoApp);
    }
    // smcho added end
