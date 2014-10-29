#!/bin/sh
mvn -q clean verify && \
java -jar backend-resources/target/backend-resources-0.1-SNAPSHOT-jar-with-dependencies.jar
