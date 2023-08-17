#!/bin/bash
# This script:
# 1) Creates a project from the subsystem archetype
# 2) adds some debugging code and builds the test project
# 3) copies the resulting subsystem to "%JBOSS_HOME%\modules\system\layers\base"
# 4) starts a local WildFly server
# 5) registers the subsystem using a CLI script
# 6) waits for the user to check that the debugging output of the subsystem was printed
# 7) finally unregisters the subsystem and stops WildFly
# Prerequesites: 
# -the environment variable JBOSS_HOME must point to the WildFly server corresponding to the archetyp version.
# -the git tool "patch.exe" must be found in the path
# The current archetype version must be the first argument to the script call. 


# check that JBOSS_HOME is set:
if [ -z $JBOSS_HOME ]
  then
  echo "Environment variable JBOSS_HOME is not set"
  exit 1
fi

# Check for "patch" command.
if ! command -v patch &> /dev/null
  then
  echo "The command patch could not be found. Please install GNU patch"
  exit 1
fi

# We need the version of the archetype to create the test project from:
if [ -z "$1" ]
  then
    echo "Archetype version must be first argument to the call to the script."
    read -p "Please enter archetype version: " archetypeVersion
  else
    archetypeVersion=$1
fi


if [ -d "example-subsystem" ]; then
  echo "delete old test project"
  rm -rf example-subsystem
fi

#if directory still exists then fail
if [ -d "example-subsystem" ]; then
  echo "[ERROR] directory 'example-subsystem' could not be deleted"
  exit 1
fi

echo "generate project from archetype."
mvn archetype:generate -DgroupId=com.acme -DartifactId=example-subsystem -Dversion=1.0-SNAPSHOT -Dmodule=org.test.subsystem -Dpackage=com.acme.example -DarchetypeGroupId=org.wildfly.archetype -DarchetypeArtifactId=wildfly-subsystem -DarchetypeVersion=$archetypeVersion -DinteractiveMode=false
retVal=$?
if [ $retVal -ne 0 ]; then
  echo "[ERROR] Maven project creation failed. Errorcode: $retVal"
  exit $retVal
fi


echo "Applying patch (using the git patch utility)..."
patch --verbose -p0 < debugging_output.patch
echo "Patch was applied."

cd example-subsystem

echo "compiling project..."
mvn install

# go back one directory (just to keep the script similar to the bat file, see there).
cd ..


echo "Copying module to WildFly..."
cp -r example-subsystem/target/module/org $JBOSS_HOME/modules/system/layers/base


echo "WildFly server is starting..."
$JBOSS_HOME/bin/standalone.sh &

sleep 20

echo "Configuring subsystem..."
echo "This might cause errors if a previous test run did not cleanup and e.g. the subsystem already exists."
$JBOSS_HOME/bin/jboss-cli.sh --file=configure.cli
if [ $retVal -ne 0 ]; then
  echo "[ERROR] Configuration failed"
  exit $retVal
fi

echo "Subsystem was registered - check WildFly console for error messages."
echo "If all went well, there will be an output 'mysubsystem was successfully initialized'".

grep "mysubsystem was successfully initialized" $JBOSS_HOME/standalone/log/server.log
if [ $retVal -ne 0 ]; then
  echo "[ERROR] The subsystem was not properly initialized"
  exit $retVal
fi


echo "Unregistering subsystem and stopping WildFly..."
$JBOSS_HOME/bin/jboss-cli.sh --file=restore-configuration-and-stop.cli

# Delete the subsystem files
rm -r $JBOSS_HOME/modules/system/layers/base/org/test
