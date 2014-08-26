 #!/bin/sh

set -o errexit

SOURCE="${BASH_SOURCE[0]}"
while [ -h "$SOURCE" ] ; do SOURCE="$(readlink "$SOURCE")"; done
DIR="$( cd -P "$( dirname "$SOURCE" )" && pwd )"

# DEFINE

ARCHETYPES="wildfly-javaee7-webapp-archetype wildfly-javaee7-webapp-blank-archetype wildfly-javaee7-webapp-ear-archetype  wildfly-javaee7-webapp-ear-blank-archetype"

SNAPSHOT_REPO_ID="jboss-snapshots-repository"
RELEASE_REPO_ID="jboss-releases-repository"
# SCRIPT

usage()
{

HUMAN_READABLE_ARCHETYPES=""
i=0
for archetype in $ARCHETYPES
do
   if [ $i -ne 0 ]
   then
      HUMAN_READABLE_ARCHETYPES="${HUMAN_READABLE_ARCHETYPES}, "
   fi
   echo ""
   HUMAN_READABLE_ARCHETYPES="${HUMAN_READABLE_ARCHETYPES}${archetype}"
   i=$[$i+1]
done


cat << EOF
usage: $0 options

This script aids with releases. Remember to generate new blank archetypes before release

OPTIONS:
   -u      Updates version numbers in all POMs, used with -o and -n
   -c      Clean Archetypes target Dir
   -l      Install all archetypes locally: $HUMAN_READABLE_ARCHETYPES.
   -o      Old version number to update from
   -n      New version number to update to
   -s      Deploy a snapshot of the archetypes
   -r      Deploy a release of the archetypes
   -t      Deploy a stage of the archetypes
   -h      Shows this message

EOF
}

clean()
{
  for archetype in $ARCHETYPES
  do
     FILE=${archetype}/pom.xml
     if [ -f $FILE ]; then
        echo "\n**** Cleaning $archetype\n"
        cmd="mvn clean  -f ${archetype}/pom.xml"
     fi
     $cmd
  done
}

update()
{
cd $DIR
echo "Updating versions from $OLDVERSION TO $NEWVERSION for all Java and XML files under $PWD"
perl -pi -e "s/${OLDVERSION}/${NEWVERSION}/g" `find . -name \*.xml -or -name \*.md  -type f -maxdepth 3`
}

install()
{
   for archetype in $ARCHETYPES
   do
      echo "\n**** Installing $archetype\n"
      cmd="mvn clean install -f ${archetype}/pom.xml"
      if [ -n $DEST ]; then
        cmd="$cmd -Dmaven.repo.local=$DEST"
      fi
      $cmd
   done
}

snapshot()
{
   for archetype in $ARCHETYPES
   do
      echo "\n**** Deploying $archetype to ${SNAPSHOT_REPO_URL} \n"
      mvn clean deploy -f ${archetype}/pom.xml -DaltDeploymentRepository=${SNAPSHOT_REPO_ID}::default::${SNAPSHOT_REPO_URL} 
   done

}

release()
{
   for archetype in $ARCHETYPES
   do
      echo "\n**** Deploying $archetype to ${RELEASE_REPO_URL} \n"
      mvn clean deploy -f ${archetype}/pom.xml -DaltDeploymentRepository=${RELEASE_REPO_ID}::default::${RELEASE_REPO_URL} -Prelease
   done

}

stage()
{
   rm -rf ./local-stage
   for archetype in $ARCHETYPES
   do
      echo "\n**** Deploying $archetype to https://repository.jboss.org/nexus \n"
      mvn -f ${archetype}/pom.xml deploy -DskipRemoteStaging=true -DaltDeploymentRepository="=local::default::file:./local-stage"
   done
   mvn -e org.sonatype.plugins:nexus-staging-maven-plugin:deploy-staged-repository -DrepositoryDirectory="./local-stage" -DstagingProfileId=2161b7b8da0080 -DnexusUrl=https://repository.jboss.org/nexus -DserverId=jboss-releases-repository -Prelease -Djava.net.preferIPv4Stack=true -Dhttp.connection.stalecheck=false -Dhttp.connection.timeout=0 -Dhttp.socket.timeout=0
}

OLDVERSION="1.0.0-SNAPSHOT"
NEWVERSION="1.0.0-SNAPSHOT"
CMD="usage"
DEST=""

while getopts “crl:uo:n:t” OPTION

do
     case $OPTION in
         u)
             CMD="update"
             ;;
         c)
             CMD="clean"
             ;;
         h)
             usage
             exit
             ;;
         o)
             OLDVERSION=$OPTARG
             ;;
         n)
             NEWVERSION=$OPTARG
             ;;
         s)
             CMD="snapshot"
             ;;
         t)
             CMD="stage"
             ;;
         r)  
             CMD="release"
             ;;
         l)
             CMD="install"
             eval DEST=$OPTARG
             ;;
         [?])
             usage
             exit
             ;;
     esac
done

$CMD

