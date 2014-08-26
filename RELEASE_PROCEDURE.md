# wildfly-javaee7 Archetypes Release Procedure


## Publishing builds to Maven

  1. You must have gpg set up and your key registered, as described at <http://www.sonatype.com/people/2010/01/how-to-generate-pgp-signatures-with-maven/>
  2. You must provide a property `gpg.passphrase` in your `settings.xml` in the `release` profile e.g.

          <profile>
                <id>release</id>
                <properties>
                    <gpg.passphrase>myPassPhrase</gpg.passphrase>
                </properties>
          </profile>
  3. You must have a JBoss Nexus account, configured with the server id in `settings.xml` with the id `jboss-releases-repository` e.g.

          <server>
              <id>jboss-releases-repository</id>
              <username>myUserName</username>
              <password>myPassword</password>
          </server>

  4. Add `org.sonatype.plugins` plugin group to your `settings.xml` so nexus plugin can be available for publishing scripts.

          <pluginGroups>
              <pluginGroup>org.sonatype.plugins</pluginGroup>
          </pluginGroups>

## Run the tests


The Archetype tests is a way to get sure that all quickstarts are running perfectly. The tests do the following steps:

- install each archtype. This will trigger the synchronization with the quickstarts
- generate a new project
- compile the generated project with a clear settings.xml

Test all archetypes running:

      mvn test

_Note 1: Sometimes developers want to run a test with unreleased artifacts. In this case, it's possible to disable the use of a empty settings.xml. In this case the developer need to run:_

    mvn test -DonlyMavenCentral=false

 This will disable the clear settings.xml and use the settings.xml from your environment

_Note 2: If you need to clear the archetypes target/ folder and force a new git checkout, you can run with the cleanArchetypes=true property. Example:_

     mvn test -DcleanArchetypes=true

This will trigger a clean on each archetype before running the install.


## Release


The release can be perfomed to a [staged repo](http://jboss-developer.github.io/temp-maven-repo/) or to [JBoss Nexus](https://repository.jboss.org/nexus/)

### Release to staged repo

To release the tested archetypes to the staged repo you will need to clone the repo to YOUR FILESYSTEM through git:

    git clone http://jboss-developer.github.io/temp-maven-repo/

Then, create an environment variable pointing to your local checkout of https://github.com/jboss-developer/temp-maven-repo

     export RELEASE_REPO_URL=file://Users/rafaelbenevides/projects/jboss-developer/temp-maven-repo

After, simply run:  

      ./release.sh -s <old snapshot version> -r <release version>

  This will  update the version number, commit and tag and publish the Archetypes to the staged repository. Then it will reset the version number back to the snapshot version number. 
  
On the temp-maven-repo run the scripts
  
    ./update-archetype-catalog.sh 
    ./generate-index.sh

Finally, commit the artifacts to git and push to upstream

### Release to JBoss Nexus

Simply run:

      ./release.sh -s <old snapshot version> -r <release version> -t

  This will  update the version number, commit and tag and publish the Archetypes to the staged repository. Then it will reset the version number back to the snapshot version number. 

  Go to <https://repository.jboss.org/nexus/index.html#stagingRepositories> and release the staging repository. The artifacts will be replicated to Maven central within 24 hours.

#### Troubleshoot

If you have any connections issues to JBoss Nexus, you can retry the deployment by running:

    mvn -X org.sonatype.plugins:nexus-staging-maven-plugin:deploy-staged-repository -DrepositoryDirectory="./local-stage" -DstagingProfileId=2161b7b8da0080 -DnexusUrl=https://repository.jboss.org/nexus -DserverId=jboss-releases-repository -Prelease -Djava.net.preferIPv4Stack=true -Dhttp.connection.stalecheck=false -Dhttp.connection.timeout=0 -Dhttp.socket.timeout=0

###Final step

  Push commits and tags. Run:

      git push upstream master --tags

