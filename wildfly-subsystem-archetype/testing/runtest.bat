@REM This batch file:
@REM 1) Creates a project from the subsystem archetype
@REM 2) adds some debugging code and builds the test project
@REM 3) copies the resulting subsystem to "%JBOSS_HOME%\modules\system\layers\base"
@REM 4) starts a local WildFly server
@REM 5) registers the subsystem using a CLI script
@REM 6) waits for the user to check that the debugging output of the subsystem was printed
@REM 7) finally unregisters the subsystem and stops WildFly
@REM Prerequesites:
@REM -the environment variable JBOSS_HOME must point to the WildFly server corresponding to the archetyp version.
@REM -the git tool "patch.exe" must be found in the path
@REM The current archetype version must be the first argument to the batch file call.

@echo off

@REM check that JBOSS_HOME is set:
@if "%JBOSS_HOME%" == "" (
  echo Environment variable JBOSS_HOME is not set
  goto :exit
)

@REM Check for "patch.exe" on path. The switch "/q" returns an exit code instead of printing the found files.
where.exe patch.exe /Q
if ERRORLEVEL 1 (
    @echo The file patch.exe could not be found. It is part of the Git client in the subdir "usr\bin". Ensure this directory is placed in your PATH.
    goto :exit
)

@REM We need the version of the archetype to create the test project from:
@if "%1" == "" (
  echo Archetype version must be first argument to the call to the batch file.
  set /p archetypeVersion=Please enter archetype version:
) else (
  set archetypeVersion=%1
)

if exist example-subsystem (
  @ECHO delete old test project
  rmdir /S /Q example-subsystem
)
@REM if directory still exists then fail
if exist example-subsystem (
  @echo [ERROR] directory 'example-subsystem' could not be deleted
  goto :exit
)

@ECHO generate project from archetype.
call mvn archetype:generate -DarchetypeCatalog=local -DgroupId=com.acme -DartifactId=example-subsystem -Dversion=1.0-SNAPSHOT -Dmodule=org.test.subsystem -Dpackage=com.acme.example -DarchetypeGroupId=org.wildfly.archetype -DarchetypeArtifactId=wildfly-subsystem -DarchetypeVersion=%archetypeVersion% -DinteractiveMode=false
if %ERRORLEVEL% NEQ 0 (
  @echo [ERROR] Maven project creation failed. Errorlevel: %ERRORLEVEL%
  goto :exit
)


@ECHO Applying patch (using the git patch utility)...
patch.exe --verbose -p0 < debugging_output.patch
@ECHO Patch was applied.

cd example-subsystem

@ECHO compiling project...
call mvn install

@REM go back one directory - otherwise all CMD windows that are opened in the next steps block further runs of this script as the "example-subsystem" directory cannot be deleted if a CMD window is open.
cd..

@ECHO Copying module to WildFly...
@REM "/Y" does not ask for overwrite confirmations (if previous script runs did not finish)
xcopy /E /Y example-subsystem\target\module\org %JBOSS_HOME%\modules\system\layers\base\org\

@ECHO WildFly server is starting...
start  %JBOSS_HOME%\bin\standalone.bat
@ECHO Press enter when WildFly was started to continue the test
@pause

@ECHO Configuring subsystem...
@ECHO This might cause errors if a previous test run did not cleanup and e.g. the subsystem already exists.
CALL %JBOSS_HOME%\bin\jboss-cli.bat --file=configure.cli

@ECHO Subsystem was registered - check WildFly console for error messages.
@ECHO If all went well, there will be an output "mysubsystem was successfully initialized".
@REM print blank line (sounds strange, but works)
@ECHO(
@ECHO After you checked this, press enter to unregister the subsystem and do cleanup.
@PAUSE

@ECHO Unregistering subsystem and stopping WildFly...
CALL %JBOSS_HOME%\bin\jboss-cli.bat --file=restore-configuration-and-stop.cli

@REM Delete the subsystem files
@REM "/s" deletes tree, "/q" does not prompt for confirmation
rmdir /s /q %JBOSS_HOME%\modules\system\layers\base\org\test

@ECHO You are done. Now close the CMD window (WildFly console).

:exit