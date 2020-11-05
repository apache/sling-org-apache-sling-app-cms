# Apache Sling CMS Standalone Helm Chart

A Helm template for running Apache Sling CMS as a standalone instance in Kubernetes.

## Local Setup

Setup a Sling CMS standalone instance running in a local minukube Kubernetes instance. 

Requires [minikube](https://minikube.sigs.k8s.io/docs/start/)

Steps: 

 1. Start minikube in a VM (for ingress support) `minikube start --vm=true`
 2. Enable ingress support: `minikube addons enable ingress`
 3. Set minkube to use the docker env `eval $(minikube docker-env)`
 4. Build the cms image `docker build -t org.apache.sling.cms.docker.cms:0.16.3-snapshot ../../docker/cms`
 5. Build the web image `docker build -t org.apache.sling.cms.docker.web:0.16.3-snapshot ../../docker/webcache`
 6. Get minikube's IP: `minikube ip`
 7. Update /etc/hosts with entries for `sling2.apache.local` and `cms.sling.apache.local` for minikube's IP\
 8. Install the helm chart with: `helm install [name] .`