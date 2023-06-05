# dmn-cc
This repository contains tests and examples for conformance checking with the help of DMN. The Camunda DMN Engine is used for this purpose.
## structure 
In the workspace, more precise [here](Workspace/camunda-engine-dmn-unittest-master/src/test/java/org/camunda/bpm/dmn/unittest/ConformanceTest.java), the java class is located. <br>
Also in the workspace, more precise [here](Workspace/camunda-engine-dmn-unittest-master/src/test/resources/org/camunda/bpm/dmn/unittest/CC.dmn), the CC.dmn file is located. <br>
In the Event Logs folder,more precise [here](Event%20Logs/), the example event logs used to test the different constraints are located. <br>
Structure for the unittest is heavily inspired by https://github.com/camunda/camunda-engine-dmn-unittest <br>
To be able to look at the CC.dmn file and tables, https://camunda.com/download/modeler/ is advised. <br>
## usage
To check the conformance of an event log for given DMN tables, run the ConformanceTest Java class. <br>
There are Event Logs and DMN tables for the different DECLARE constraints available. <br>
## todo
- Add remaining DECLARE constraints
- Make one big test for every constraint (against one event log?)
