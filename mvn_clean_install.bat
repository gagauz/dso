@echo off

set BUILDNUMBER=42
set file.encoding=UTF-8
set PROPERTIES_DIR=%cd%/properties/configs
set HOST=tsu
set HOST_TYPE=local
set tapestry.production-mode=false
set MAVEN_OPTS=%MAVEN_OPTS% -Xmx1424m -Xms1424m -XX:PermSize=200M -XX:MaxPermSize=200M

call mvn -e clean install -Dmaven.test.skip=false -DdownloadSources=true -DdownloadSource=true


rem 

rem install
rem -Dmaven.test.skip=true
echo -----------------------------------------------------------------------------------

pause