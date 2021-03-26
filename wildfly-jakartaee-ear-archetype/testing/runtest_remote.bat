@REM Creates a project from the archetype, copies some additional source files and runs an integration test
@REM using the profile "arq-remote".
@REM Prerequesites: a WildFly server corresponding to the archetyp version must be running on localhost.
@REM The current archetype version must be the first argument to the batch file call. 

@echo off

@if "%1" == "" (
  echo Archetype version must be first argument to the call to the batch file.
  set /p archetypeVersion=Please enter archetype version: 
) else (
  set archetypeVersion=%1
)

if exist arq-remote (
  @ECHO delete old test project
  rmdir /S /Q arq-remote
)
@REM if directory still exists then fail
if exist arq-remote (
  @echo [ERROR] directory 'arq-remote' could not be deleted
  goto :exit
)

@ECHO creating test project directory
mkdir arq-remote
cd arq-remote

@ECHO generate project from archetype.
call mvn archetype:generate -DgroupId=foo.bar -DartifactId=multi -Dversion=0.1-SNAPSHOT -Dpackage=foo.bar.multi -DarchetypeGroupId=org.wildfly.archetype -DarchetypeArtifactId=wildfly-jakartaee-ear-archetype -DarchetypeVersion=%archetypeVersion% -DinteractiveMode=false
if %ERRORLEVEL% NEQ 0 (
  @echo [ERROR] Maven project creation failed. Errorlevel: %ERRORLEVEL%
  cd..
  goto :exit
)

@ECHO copy additional files required for test.
copy ..\additionalfiles\TestBean.java .\multi\multi-ejb\src\main\java\foo\bar\multi\
copy ..\additionalfiles\TestLocal.java .\multi\multi-ejb\src\main\java\foo\bar\multi\
copy ..\additionalfiles\TestRemote.java .\multi\multi-ejb\src\main\java\foo\bar\multi\
copy ..\additionalfiles\ArchetypeIT.java .\multi\multi-web\src\test\java\foo\bar\multi\test\

cd multi
@ECHO run test
call mvn verify -Parq-remote
cd ..\..



:exit