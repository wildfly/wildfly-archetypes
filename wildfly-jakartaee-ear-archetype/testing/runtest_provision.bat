@REM Creates a project from the archetype and creates a provisioned server
@REM using the profile "provision".
@REM Prerequesites: none.
@REM The current archetype version must be the first argument to the batch file call.

@echo off

@if "%1" == "" (
  echo Archetype version must be first argument to the call to the batch file.
  set /p archetypeVersion=Please enter archetype version:
) else (
  set archetypeVersion=%1
)

if exist provision (
  @ECHO delete old test project
  rmdir /S /Q provision
)
@REM if directory still exists then fail
if exist provision (
  @echo [ERROR] directory 'provision' could not be deleted
  goto :exit
)

@ECHO creating test project directory
mkdir provision
cd provision

@ECHO generate project from archetype.
call mvn archetype:generate -DarchetypeCatalog=local -DgroupId=foo.bar -DartifactId=multi -Dversion=0.1-SNAPSHOT -Dpackage=foo.bar.multi -DarchetypeGroupId=org.wildfly.archetype -DarchetypeArtifactId=wildfly-jakartaee-ear-archetype -DarchetypeVersion=%archetypeVersion% -DinteractiveMode=false
if %ERRORLEVEL% NEQ 0 (
  @echo [ERROR] Maven project creation failed. Errorlevel: %ERRORLEVEL%
  cd..
  goto :exit
)

cd multi
@ECHO run test
call mvn verify -Pprovision
cd ..\..



:exit