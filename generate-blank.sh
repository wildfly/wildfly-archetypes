#!/bin/sh

set -o errexit

SOURCE="${BASH_SOURCE[0]}"
while [ -h "$SOURCE" ] ; do SOURCE="$(readlink "$SOURCE")"; done
DIR="$( cd -P "$( dirname "$SOURCE" )" && pwd )"

# DEFINE

ARCHETYPES="wildfly-javaee7-webapp-archetype wildfly-javaee7-webapp-ear-archetype wildfly-html5-mobile-archetype"

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

This script generates a "blanked" version of an archetype.

The name of archetype directory must match the artifactId of the archetype. Arcehtypes are placed in $DIR with the suffix -blank

OPTIONS:
   -a      Generate a blank archetype for all archetypes, currently ${HUMAN_READABLE_ARCHETYPES}
   -n      The name of the archetype to blank.
   -c      Removes the blank archetypes
   -h      Shows this message

EOF
}

clean() 
{
    echo "**** Cleaning $DIR/*-blank-archetype"
    rm -rf $DIR/*-blank-archetype
}

blank()
{
   ARCHETYPE_NAME=$1
   ARCHETYPE_BLANK_NAME=${ARCHETYPE_NAME//archetype/blank-archetype}
   ARCHETYPE_DIR=${DIR}/${ARCHETYPE_NAME}
   ARCHETYPE_BLANK_DIR="${DIR}/${ARCHETYPE_BLANK_NAME}"

cat << EOF

**** Generating blank version of ${ARCHETYPE_NAME} into ${ARCHETYPE_BLANK_DIR}
**** Blank artifactId is ${ARCHETYPE_BLANK_NAME}

EOF

   rm -rf ${ARCHETYPE_BLANK_DIR} 

   cp -r ${ARCHETYPE_DIR} ${ARCHETYPE_BLANK_DIR}

   cd $ARCHETYPE_BLANK_DIR

   mv src/main/resources/META-INF/maven/archetype-metadata-blank.xml src/main/resources/META-INF/maven/archetype-metadata.xml

   perl -pi -e "s/${ARCHETYPE_NAME}/${ARCHETYPE_BLANK_NAME}/g" `find . -name pom.xml`
   
   perl -pi -e "s/<activeByDefault>true<\/activeByDefault>/<activeByDefault>false<\/activeByDefault>/g" $ARCHETYPE_BLANK_DIR/pom.xml

}
  
ALL=0
CLEAN=0

while getopts “ahcn:” OPTION
do
     case $OPTION in
         a)
             ALL=1
             ;;
         h)
             usage
             exit
             ;;
         c)
             clean
             CLEAN=1
             ;;
         n) 
             NAME=$OPTARG
             ;;
         [?])
             usage
             exit
             ;;
     esac
done

if [ $ALL -eq 1 ]
then
   for archetype in $ARCHETYPES
   do
      blank $archetype
   done
else
   if [ -z "$NAME"] && [ $CLEAN -ne 1 ]
   then
      echo "No archetype name defined"
      usage
      exit
   elif [ -n "$NAME" ]
   then
      blank $NAME
   fi
fi

