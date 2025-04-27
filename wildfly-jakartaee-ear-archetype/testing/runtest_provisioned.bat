@REM Creates a project from the archetype, copies some additional source files and runs an integration test
@REM using the profile "arq-provisioned".
@REM Prerequesites: none, the tests are run on a WildFly server that is created as part of the build process.
@REM The current archetype version must be the first argument to the batch file call.

@echo off

@if "%1" == "" (
  echo Archetype version must be first argument to the call to the batch file.
  set /p archetypeVersion=Please enter archetype version:
) else (
  set archetypeVersion=%1
)

if exist arq-provisioned (
  @ECHO delete old test project
  rmdir /S /Q arq-provisioned
)
@REM if directory still exists then fail
if exist arq-provisioned (
  @echo [ERROR] directory 'arq-provisioned' could not be deleted
  goto :exit
)

@ECHO creating test project directory
mkdir arq-provisioned
cd arq-provisioned

@ECHO generate project from archetype.
call mvn archetype:generate -DarchetypeCatalog=local -DgroupId=foo.bar -DartifactId=multi -Dversion=0.1-SNAPSHOT -Dpackage=foo.bar.multi -DarchetypeGroupId=org.wildfly.archetype -DarchetypeArtifactId=wildfly-jakartaee-ear-archetype -DarchetypeVersion=%archetypeVersion% -DinteractiveMode=false
if %ERRORLEVEL% NEQ 0 (
  @echo [ERROR] Maven project creation failed. Errorlevel: %ERRORLEVEL%
  cd..
  goto :exit
)

@ECHO copy additional files required for test.
copy ..\additionalfiles\TestBean.java .\multi\ejb\src\main\java\foo\bar\multi\
copy ..\additionalfiles\TestLocal.java .\multi\ejb\src\main\java\foo\bar\multi\
copy ..\additionalfiles\TestRemote.java .\multi\ejb\src\main\java\foo\bar\multi\
copy ..\additionalfiles\ArchetypeIT.java .\multi\integration-tests\src\test\java\foo\bar\multi\test\

cd multi
@ECHO run test
call mvn verify -Parq-provisioned

cd ..\..



:exit