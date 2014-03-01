set cls=%cd%\bin
cd %cls%
rem -Djava.util.logging.config.file=logging.properties 
call java -cp %cls% dso.test.TestServer

pause