### [2015/08/10]

#### IntelliJ structure

1. The out directory contains all the output.
    * out/production/one_simulator is the directory where all the class files are stored.
    * The directory also contains all the files. 

2. one_simulator is a module.
    * DTNSim configuration has "one_simulator" as a classpath. 
        * So, namespace works without any modification.
    * However, the files are accessed from working directory, so all the file locations should prepend "one_simulator"