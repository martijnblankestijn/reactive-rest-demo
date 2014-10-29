#!/bin/bash
: ${GLASSFISH_HOME:? "Environment variable GLASSFISH_HOME is required!"}

# Given DOMAIN environment variable is used or else 'domain1'
DOMAIN=${DOMAIN:-domain1}
DOMAIN_LOGIN_CONF=$GLASSFISH_HOME/glassfish/domains/${DOMAIN}/config/login.conf

# Get the directory of this bash script
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
SCRIPT_DIR=${SCRIPT_DIR:-$DIR}

[ -d ${GLASSFISH_HOME} ] || { echo "GLASSFISH_HOME ${GLASSFISH_HOME} is not a directory"; exit 1; };
[ -f ${DOMAIN_LOGIN_CONF} ] || { echo "File ${DOMAIN_LOGIN_CONF} does not exists. Is the domain valid?"; exit 1; }

PATH=${GLASSFISH_HOME}/bin:$PATH

echo "+++ Creating backend service url for the domain $DOMAIN"
asadmin multimode  --file $SCRIPT_DIR/glassfish-config.script || { echo "--- Creating of urls for the domain $DOMAIN failed"; exit 1; };


echo "+++ END OF SCRIPT +++"
