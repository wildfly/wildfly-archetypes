#!/bin/sh

# Require BASH 3 or newer

REQUIRED_BASH_VERSION=3.0.0

if [[ $BASH_VERSION < $REQUIRED_BASH_VERSION ]]; then
  echo "You must use Bash version 3 or newer to run this script"
  exit
fi


# Canonicalise the source dir, allow this script to be called anywhere
DIR=$(cd -P -- "$(dirname -- "$0")" && pwd -P)

# DEFINE

# EAP team email subject
EMAIL_SUBJECT="\${RELEASEVERSION} of JBoss EAP Archetypes released "
# EAP team email To ?
EMAIL_TO="pgier@redhat.com kpiwko@redhat.com"
EMAIL_FROM="\"JDF Publish Script\" <benevides@redhat.com>"


# SCRIPT

usage()
{
cat << EOF
usage: $0 options

This script performs a release of the JBoss AS Archetypes 

OPTIONS:
   -s      Snapshot version number to update from
   -n      New snapshot version number to update to, if undefined, defaults to the version number updated from
   -r      Release version number
   -t      Deploy to staged repo instead of 'temp-maven-repo'
EOF
}

useenvrepo(){
  if [[ -z "$RELEASE_REPO_URL" ]]; then
    echo "You must set the RELEASE_REPO_URL environment variable to your local checkout of https://github.com/jboss-developer/temp-maven-repo"
    exit
  fi
}

notify_email()
{
   echo "***** Performing JBoss AS Archetypes release notifications"
   echo "*** Notifying JBoss team"
   subject=`eval echo $EMAIL_SUBJECT`
   echo "Email from: " $EMAIL_FROM
   echo "Email to: " $EMAIL_TO
   echo "Subject: " $subject
   # send email using sendmail
   printf "Subject: $subject\nSee \$subject :)\n" | /usr/bin/env sendmail -f "$EMAIL_FROM" "$EMAIL_TO"
}

release()
{
   if [[ -z "$USE_STAGE" ]]
   then
     useenvrepo
   fi 
   echo "Cleaning Archetypes"
   $DIR/release-utils.sh -c
   echo "Rebuilding blank archetypes"
   $DIR/generate-blank.sh -a
   git commit -a -m"Update blank archetypes"
   echo "Releasing JBoss Archetypes version $RELEASEVERSION"
   $DIR/release-utils.sh -u -o $SNAPSHOTVERSION -n $RELEASEVERSION
   git commit -a -m "Prepare for $RELEASEVERSION release"
   git tag -a $RELEASEVERSION -m "Tag $RELEASEVERSION"
   if [[ -z "$USE_STAGE" ]]
   then
      $DIR/release-utils.sh -r
   else
      $DIR/release-utils.sh -t
   fi
   echo "Cleaning Archetypes"
   $DIR/release-utils.sh -c
   $DIR/release-utils.sh -u -o $RELEASEVERSION -n $NEWSNAPSHOTVERSION
   git commit -a -m "Prepare for development of $NEWSNAPSHOTVERSION"
   echo "***** JBoss Archetypes released"
   read -p "Do you want to send release notifcations to $EAP_EMAIL_TO[y/N]? " yn
   case $yn in
       [Yy]* ) notify_email;;
   esac
   echo "Don't forget to push the tag and the branch"
   echo "   git push --tags upstream master"
   echo
   echo "You will need to close and release the Staging repo manually"
   echo "    https://repository.jboss.org/nexus/"
}

SNAPSHOTVERSION="UNDEFINED"
RELEASEVERSION="UNDEFINED"
NEWSNAPSHOTVERSION="UNDEFINED"

while getopts “n:r:s:t” OPTION

do
     case $OPTION in
         h)
             usage
             exit
             ;;
         s)
             SNAPSHOTVERSION=$OPTARG
             ;;
         r)
             RELEASEVERSION=$OPTARG
             ;;
         n)
             NEWSNAPSHOTVERSION=$OPTARG
             ;;
         t)
             USE_STAGE=true;
             ;;
         [?])
             usage
             exit
             ;;
     esac
done

if [ "$NEWSNAPSHOTVERSION" == "UNDEFINED" ]
then
   NEWSNAPSHOTVERSION=$SNAPSHOTVERSION
fi

if [ "$SNAPSHOTVERSION" == "UNDEFINED" -o  "$RELEASEVERSION" == "UNDEFINED" ]
then
   echo "\nMust specify -r and -s\n"
   usage
else  
   release
fi


