#!/bin/bash
# Creates a project from the archetype and creates a provisioned server
# using the profile "provision".
# Prerequesites: none.
# The current archetype version must be the first argument to the batch file call.

if [ -z "$1" ]
  then
    echo Archetype version must be first argument to the call to the batch file.
    read -p "Please enter archetype version: " archetypeVersion
  else
    archetypeVersion=$1
fi

if [ -d "provision" ]; then
  echo "delete old test project"
  rm -rf provision
fi


# if directory still exists then fail
if [ -d "provision" ]; then
  echo "[ERROR] directory 'provision' could not be deleted"
  exit 1
fi

echo "creating test project directory"
mkdir provision
cd provision

echo "generate project from archetype."
mvn archetype:generate -DarchetypeCatalog=local -DgroupId=foo.bar -DartifactId=multi -Dversion=0.1-SNAPSHOT -Dpackage=foo.bar.multi -DarchetypeGroupId=org.wildfly.archetype -DarchetypeArtifactId=wildfly-jakartaee-ear-archetype -DarchetypeVersion=$archetypeVersion -DinteractiveMode=false
retVal=$?
if [ $retVal -ne 0 ]; then
  echo "[ERROR] Maven project creation failed. Errorcode: $retVal"
  cd ..
  exit $retVal
fi

cd multi
echo "run test"
mvn verify -Pprovision
retVal=$?
cd ../..
exit $retVal