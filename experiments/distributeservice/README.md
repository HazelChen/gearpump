This directory contains the example that distribute a zip file, install and start a service. This README explain how to quick-start this example.

In order to run the example:

  1. Start a gearpump cluster, including Master and Workers.

  2. Start the AppMaster:<br>
  ```bash
  target/pack/bin/gear app -jar experiments/distributeservice/target/$SCALA_VERSION_MAJOR/gearpump-experiments-distributeservice_$VERSION.jar org.apache.gearpump.distributeservice.DistributeService -master 127.0.0.1:3000
  ```
  3. Distribute the service:<br>
  ```bash
  target/pack/bin/gear app -jar experiments/distributeservice/target/$SCALA_VERSION_MAJOR/gearpump-experiments-distributeservice_$VERSION.jar
  org.apache.gearpump.distributeservice.DistributeServiceClient -master 127.0.0.1:3000 -appid $APPID -file ${File_Path}
  -script ${Script_Path} -serviceName ${Service_Name} -target ${Target_Path} -Dkey1=value1 -Dkey2=value2
  ```<br>
  This command will distiribute the service zip file(variable ```file```) to the target path(variable ```target```), then copy the script to
  ```/etc/init.d``` on each machine and install this servcie named with ```serviceName```<br>
  Note that you can pass some varibales when the scaipt file is installed, for example, you can submit a script template with syntax like
  ```
  role=${${hostname}.role}
  ```<br>
  Then when submit the service, you can define ```-Dhost1.role=master```, which will replace the ```role``` property on machine host1.

