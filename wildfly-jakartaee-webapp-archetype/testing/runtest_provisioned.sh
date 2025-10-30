#!/bin/bash
# Creates a project from the archetype, copies some additional source files and runs an integration test
# using the profile "arq-provisioned".
# Prerequesites: none, the tests are run on a WildFly server that is created as part of the build process.
# The current archetype version must be the first argument to the batch file call.

if [ -z "$1" ]
  then
    echo Archetype version must be first argument to the call to the batch file.
    read -p "Please enter archetype version: " archetypeVersion
  else
    archetypeVersion=$1
fi

if [ -d "arq-provisioned" ]; then
  echo "delete old test project"
  rm -rf arq-provisioned
fi


# if directory still exists then fail
if [ -d "arq-provisioned" ]; then
  echo "[ERROR] directory 'arq-provisioned' could not be deleted"
  exit 1
fi

echo "creating test project directory"
mkdir arq-provisioned
cd arq-provisioned

echo "generate project from archetype."
mvn archetype:generate -DarchetypeCatalog=local -DgroupId=foo.bar -DartifactId=multi -Dversion=0.1-SNAPSHOT -Dpackage=foo.bar.multi -DarchetypeGroupId=org.wildfly.archetype -DarchetypeArtifactId=wildfly-jakartaee-webapp-archetype -DarchetypeVersion=$archetypeVersion -DinteractiveMode=false
retVal=$?
if [ $retVal -ne 0 ]; then
  echo "[ERROR] Maven project creation failed. Errorcode: $retVal"
  cd ..
  exit $retVal
fi


echo "copy additional files required for test."
cp ../additionalfiles/TestBean.java ./multi/src/main/java/foo/bar/multi/
cp ../additionalfiles/TestLocal.java ./multi/src/main/java/foo/bar/multi/
cp ../additionalfiles/TestRemote.java ./multi/src/main/java/foo/bar/multi/
cp ../additionalfiles/ArchetypeIT.java ./multi/src/test/java/foo/bar/multi/test/

cd multi
echo "run test"
mvn verify -Parq-provisioned
retVal=$?
cd ../..
exit $retVal