Wildfly Archetypes Contributing Guide
===================================

Archetype is a Maven project templating toolkit

Basic Steps
-----------

To contribute to the Archetypes, fork the Archetypes repository to your own Git, clone your fork, commit your work on topic branches, and make pull requests.

If you don't have the Git client (`git`), get it from: <http://git-scm.com/>

Here are the steps in detail:

1. [Fork](https://github.com/wildfly/wildfly-archetypes/fork_select) the project. This creates a the project in your own Git.

2. Clone your fork. This creates a directory in your local file system.

        git clone git@github.com:<your-username>/wildfly-archetypes.git

3. Add the remote `upstream` repository.

        git remote add upstream git@github.com:wildfly/wildfly-archetypes.git

4. Get the latest files from the `upstream` repository.

        git fetch upstream

5. Create a new topic branch to contain your features, changes, or fixes.

        git checkout -b <topic-branch-name> upstream/main

6. Contribute new code or make changes to existing files. Make sure that you follow the General Guidelines below.

7. Build the archetypes and install them into your Maven repository.

        mvn clean install


7. Test the changes using Maven.

    1. Navigate to the root of the archetype with the code changes and run `mvn test`.
    2. The project is created in the `target/` directory of the archteype.
    3. Verify the generated project builds and runs as expected.

8. Commit your changes to your local topic branch. You must use `git add filename` for every file you create or change.

        git add <changed-filename>
        git commit -m `Description of change...`

9. Push your local topic branch to your github forked repository. This will create a branch on your Git fork repository with the same name as your local topic branch name.

        git push origin HEAD

10. Browse to the <topic-branch-name> branch on your forked Git repository and [open a Pull Request](http://help.github.com/send-pull-requests/). Give it a clear title and description.

General Guidelines
------------------

* More instructions, see the [Guide to Creating Archetypes](http://maven.apache.org/guides/mini/guide-creating-archetypes.html)

License Information and Contributor Agreement
---------------------------------------------

  All contributions to this repository are licensed under the [Apache License](https://www.apache.org/licenses/LICENSE-2.0), version 2.0 or later, or, if another license is specified as governing the file or directory being modified, such other license.

  All contributions are subject to the [Developer Certificate of Origin (DCO)](https://developercertificate.org/).
  The DCO text is also included verbatim in the [dco.txt](dco.txt) file in the root directory of the repository.

  There is no need to sign a contributor agreement to contribute to WildFly Archetypes. You just need to explicitly license any contribution under the AL 2.0. If you add any new files to WildFly Archetypes, make sure to add the correct header.

### Java

      /*
       * Copyright The WildFly Authors
       * SPDX-License-Identifier: Apache-2.0
       */

### XML

      <!--
        ~ Copyright The WildFly Authors
        ~ SPDX-License-Identifier: Apache-2.0
       -->

### Properties files

      # Copyright The WildFly Authors
      # SPDX-License-Identifier: Apache-2.0
	 
Compliance with Laws and Regulations
------------------------------------

All contributions must comply with applicable laws and regulations, including U.S. export control and sanctions restrictions.
For background, see the Linux Foundation’s guidance:
[Navigating Global Regulations and Open Source: US OFAC Sanctions](https://www.linuxfoundation.org/blog/navigating-global-regulations-and-open-source-us-ofac-sanctions).