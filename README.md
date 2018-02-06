# Apache Sling CMS

A reference implementation of a simple Content Management System built in Apache Sling.

## Build

Build an instance with of the Sling CMS

    mvn clean install
    
The JAR will be located under: `builder/target/org.apache.sling.cms-{VERSION].jar`

## Running

To run the Sling CMS, build the code and copy the files `builder/src/main/scripts/start.sh` `builder/src/main/scripts/stop.sh` and `builder/target/org.apache.sling.cms-{VERSION].jar` to a directory. Execute the script `./start.sh` to start Sling CMS.

## Login

Navigate to [http://localhost:8080/](http://localhost:8080/). The default credentials are *admin*/*admin*.
