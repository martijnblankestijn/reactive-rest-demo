# Manual

## Tools
The following tools are used:

 * [Glassfish Server Open Source 4.1 Full Platform](https://glassfish.java.net/download.html)
 * [Maven 3] (http://maven.apache.org/download.cgi)
 * [Java SE 8 JDK] (http://www.oracle.com/technetwork/java/javase/downloads/index.html)

## Modules
The modules are

 * backend-resources
 * customeroverview

### Backend-resources
The backend-resources project contains three REST-resources (customer, contracts and communications) that are used in the demo.
The Media-type used in the response is JSON.
This project uses only jersey to deliver the content.

These resources are reachable by the following url's:

 * http://localhost:9998/customers/{username}
 * http://localhost:9998/contracts/{customerId}
 * http://localhost:9998/communications/{customerId}

Valid customer usernames are bbb, ccc and ddd. Valid customer ids are 100, 101, 102.

### Customer overview
The customeroverview is the starting point of the demo and contains one REST-Resource, the customer overview.
This resources aggregates the three resources of the backend-resources project

The url for the customer overview REST-resource is TBD.

## Building and starting

### Glassfish Installation and Configuration
Unzip glassfish ($GLASSFISH_HOME) somewhere, go to the directory $GLASSFISH_HOME/bin and
execute the following command to start the application server:
`./asadmin start-domain`

With the running Glassfish server configure it with the script 'glassfish-config.script'.
Run the script with the command
`./asadmin multimode  --file glassfish-config.script`

**WARNING: This will 'UnlockCommercialFeatures' 'FlightRecorder' as this was used for non-commercial testing.
See [http://www.oracle.com/technetwork/java/javase/terms/products/index.html](http://www.oracle.com/technetwork/java/javase/terms/products/index.html) for licensing.**

### Start backend-resources server
Building and starting the backend-resources server is easy.
Run the script `backend-server.sh` in this directory (linux) or execute the appropriate actions for other operating systems.
The shell scripts builds the artifact and starts the jar-with-dependencies.

Stopping the backend server is just hitting ENTER.

### Deploy the customeroverview application with your IDE
Configure your IDE to use the configured Glassfish Server from within your IDE.
