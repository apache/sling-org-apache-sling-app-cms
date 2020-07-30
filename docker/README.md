# Apache Sling CMS Docker Support

This is a sample Docker Compose configuration for using Apache Sling CMS in a containerized environment.

It will start a container with the Apache Sling CMS backed by a shared volume and a webcache container pre-configured to proxy and cache two URLs.

## Dependencies

This requires:

- [Docker](https://docs.docker.com/install/)
- [Docker Compose](https://docs.docker.com/compose/install/)

## Building

To build, run the command:

`docker-compose build`

If you are using snapshots, to force a rebuild, run:

`docker-compose build --no-cache --force-rm`

## Volume

This Docker Compose setup creates a volume _docker_sling-repository_ for the Apache Sling CMS repository. To destroy this volume call:

`docker rm docker_sling-repository`

## Use

To use the containers, run the command:

`docker-compose up`

Then map the URLs *sling2.apache.org* and *cms.sling.apache.org* to your docker host. On local hosts, you can add the following entries into your /etc/hosts file:

    127.0.0.1 sling2.apache.org
    127.0.0.1 cms.sling.apache.org
