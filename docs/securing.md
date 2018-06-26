[Apache Sling](https://sling.apache.org) > [Sling CMS](https://github.com/apache/sling-org-apache-sling-app-cms) > [Administration](administration.md) > Securing Sling CMS

# Securing Sling CMS

Sling CMS by default is pretty open, so you will want to secure the application with the following steps:

 1. Configure the Apache Sling CMS Security Filter - The Apache Sling CMS Security Filter  allows for limiting access to non-published content and content directly through the CMS domain. To configure the Apache Sling CMS Security Filter:
    - Open the OSGi console to [http://localhost:8080/system/console/configMgr/org.apache.sling.cms.core.filters.CMSSecurityFilter](http://localhost:8080/system/console/configMgr/org.apache.sling.cms.core.filters.CMSSecurityFilter)
    - Configure the Host Domain and the Group
       ![Configure Security Filter](img/configure-security-filter.png)
 2. Configure Apache for Security - Add configurations to make Apache HTTPD secure:
    
        # Security Protection
        Header set X-Frame-Options SAMEORIGIN
        Header set X-XSS-Protection "1; mode=block"
        Header set X-Content-Type-Options "nosniff"
        
        # Harden Apache
        ServerSignature Off
        ServerTokens Prod
        TraceEnable off
        
 3. Ensure sites only allow specific paths - in [Configure Site](configure-site.md), you need to configure the individual site's Virtual Host in Apache. Ensure that only the required paths are proxied. This should never include paths under /etc, /system, /bin, /home or /var
 
 