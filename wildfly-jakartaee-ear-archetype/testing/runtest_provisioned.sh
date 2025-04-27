#!/bin/bash
# Creates a project from the archetype, copies some additional source files and runs an integration test
# using the profile "arq-managed".
# Prerequesites: the environment variable JBOSS_HOME must point to the WildFly server corresponding to the archetyp version.
# The current archetype version must be the first argument to the batch file call.

if [ -z "$1" ]
  then
    echo Archetype version must be first argument to the call to the batch file.
    read -p "Please enter archetype version: " archetypeVersion
  else
    archetypeVersion=$1
fi

if [ -d "arq-managed" ]; then
  echo "delete old test project"
  rm -rf arq-managed
fi


# if directory still exists then fail
if [ -d "arq-managed" ]; then
  echo "[ERROR] directory 'arq-managed' could not be deleted"
  exit 1
fi

echo "creating test project directory"
mkdir arq-managed
cd arq-managed

echo "generate project from archetype."
mvn archetype:generate -DarchetypeCatalog=local -DgroupId=foo.bar -DartifactId=multi -Dversion=0.1-SNAPSHOT -Dpackage=foo.bar.multi -DarchetypeGroupId=org.wildfly.archetype -DarchetypeArtifactId=wildfly-jakartaee-ear-archetype -DarchetypeVersion=$archetypeVersion -DinteractiveMode=false
retVal=$?
if [ $retVal -ne 0 ]; then
  echo "[ERROR] Maven project creation failed. Errorcode: $retVal"
  cd ..
  exit $retVal
fi


echo "copy additional files required for test."
cp ../additionalfiles/TestBean.java ./multi/ejb/src/main/java/foo/bar/multi/
cp ../additionalfiles/TestLocal.java ./multi/ejb/src/main/java/foo/bar/multi/
cp ../additionalfiles/TestRemote.java ./multi/ejb/src/main/java/foo/bar/multi/
cp ../additionalfiles/ArchetypeIT.java ./multi/web/src/test/java/foo/bar/multi/test/

cd multi
echo "run test"

# We need two steps: first we build a provisioned server, then we execute the arquillian tests using this server:
# Step 1: provision a server. No arquillian tests are executed in this profile.
echo "provisioning server..."
mvn clean install -Pprovision
retVal=$?
if [ $retVal -ne 0 ]; then
  echo "[ERROR] Maven project provisioning failed. Errorcode: $retVal"
  cd ..
  exit $retVal
fi

@REM Step 2: execute the arquillian tests using the provisioned server. No 'clean' is allowed here, as this would delete the provisioned server.
echo "running test..."
mvn verify -Parq-provisioned
retVal=$?

cd ../..
exit $retVal
