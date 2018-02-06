# Apache Sling CMS Docker image

The Apache Sling CMS Docker integration project is a helper aimed to make it simple to deploy an instance of Apache Sling CMS.

## Building a Docker image

$ docker build -t org.apache.sling.cms .

## Running a standalone Sling instance

To launch a docker instance named 'sling-cms' bound to 
port 80 on the local machine, and with the /opt/sling/sling volume
mounted at /srv/docker/sling in the local machine, execute:

```
$ docker run -ti -p 80:80 -v /srv/docker/sling:/opt/sling/sling \
    --name sling-cms org.apache.sling.cms
```
