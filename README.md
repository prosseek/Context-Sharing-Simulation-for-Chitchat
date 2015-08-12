# ContextSharingSimulation
Context Sharing Simulation on ONE Simulator

## Changes

### [2015/08/10] Project structure change

All the one simulator code is in one_simulator directory, all the settings should be in the one_simulator directory

#### Settings.java change

    public class Settings {
    	/** properties object where the setting files are read into */
    	protected static Properties props;
    	/** file name of the default settings file ({@value}) */
    	public static final String DEF_SETTINGS_FILE ="one_simulator/default_settings.txt";

#### default_settings.txt change

Changes in setup files.

    MapBasedMovement.mapFile1 = one_simulator/data/roads.wkt
    MapBasedMovement.mapFile2 = one_simulator/data/main_roads.wkt
    MapBasedMovement.mapFile3 = one_simulator/data/pedestrian_paths.wkt
    MapBasedMovement.mapFile4 = one_simulator/data/shops.wkt

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
