# Apache Sling CMS Docker Support

This is a sample Docker Compose configuration for using Apache Sling CMS in a containerized environment.

It will start a container with the Apache Sling CMS backed by MongoDB and a webcache container pre-configured to proxy and cache two URLs.

## Dependencies

This requires:

- [Docker](https://docs.docker.com/install/)
- [Docker Compose](https://docs.docker.com/compose/install/)

## Building

To build, run the command:

`docker-compose build`

## Use

To use the containers, run the command:

`docker-compose up`

Then map the URLs *sling2.apache.org* and *cms.sling.apache.org* to your docker host. On local hosts, you can add the following entries into your /etc/hosts file:

    127.0.0.1 sling2.apache.org
    127.0.0.1 cms.sling.apache.org
