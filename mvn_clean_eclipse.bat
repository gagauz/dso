set JAVA_HOME=r:/jdk1.7
set Path=%JAVA_HOME%/bin;%Path%
call mvn -e eclipse:clean eclipse:eclipse -DdownloadSources=true -DdownloadJavadocs=true

pause
