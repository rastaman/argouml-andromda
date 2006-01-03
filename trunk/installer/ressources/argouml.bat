@echo off

REM  Startup script for ArgoUML-MDR Win32
REM  (derived from the Ant startup script)

if "%OS%"=="Windows_NT" @setlocal
if "%OS%"=="WINNT" @setlocal

rem %~dp0 is expanded pathname of the current script under NT
set ARGO_HOME=%~dp0

rem Special opts for AndroMDA / ArgoMDR
set ARGO_OPTS=-Xmx500m -Dlog4j.configuration=org/argouml/resource/info.lcf
set ARGO_OPTS=%ARGO_OPTS% -Dorg.netbeans.mdr.storagemodel.StorageFactoryClassName=org.netbeans.mdr.persistence.btreeimpl.btreestorage.BtreeFactory
set ARGO_OPTS=%ARGO_OPTS% -Dorg.netbeans.lib.jmi.Logger=0 -Dorg.netbeans.mdr.Logger=0
set ARGO_OPTS=%ARGO_OPTS% -Dargo.defaultModel=$INSTALL_PATH\andromda\xml.zips\andromda-profile-${andromda.version}.zip
set JAVA_HOME=$JDKPath

if "%JAVA_HOME%" == "" goto noJavaHome
if not exist "%JAVA_HOME%\bin\java.exe" goto noJavaHome
set _JAVACMD="%JAVA_HOME%\bin\java.exe"
goto runArgo

:noJavaHome
echo JAVA_HOME not set or java.exe not find! Please adjust it in your environment settings.
goto end

:runArgo
%_JAVACMD% %ARGO_OPTS% -Dargouml.model.implementation=org.argouml.model.mdr.MDRModelImplementation -jar "%ARGO_HOME%argouml-mdr.jar" %*
goto end

:end
set _JAVACMD=
set ARGO_HOME=
if "%OS%"=="Windows_NT" @endlocal
if "%OS%"=="WINNT" @endlocal
