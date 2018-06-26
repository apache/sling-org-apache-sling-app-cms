[Apache Sling](https://sling.apache.org) > [Sling CMS](https://github.com/apache/sling-org-apache-sling-app-cms) > [Developers](developers.md) > Building Sling CMS

# Building Sling CMS

To nuild an instance with of the Sling CMS, you'll need Apache Maven 3.0+ and Git.

First clone the code:

    git clone https://github.com/apache/sling-org-apache-sling-app-cms.git
    
Next build the code with:

    mvn clean install
    
The JAR will be located under: `builder/target/org.apache.sling.cms-{VERSION].jar`

## Running

To run the Sling CMS, build the code and copy the files `builder/src/main/scripts/start.sh` `builder/src/main/scripts/stop.sh` and `builder/target/org.apache.sling.cms-{VERSION].jar` to a directory. Execute the script `./start.sh` to start Sling CMS.

## Login

Navigate to [http://localhost:8080/](http://localhost:8080/). The default credentials are *admin*/*admin*.
