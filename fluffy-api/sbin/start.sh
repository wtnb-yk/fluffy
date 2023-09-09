#!/bin/sh

# get directory of this script
CWDIR=$(cd "$(dirname "$0")"; pwd)

# move to project directory
PRROJDIR=$(cd ${CWDIR}/..; pwd)
cd ${PRROJDIR}

# stop existing daemons
./gradlew --stop

# check DO_MIGRATE environment variable and run liquibase migration if true
if [ "${DO_MIGRATE}" = "true" ]; then
  ./gradlew update
fi

# start
./gradlew bootRun
