set cls=%cd%\target\classes
cd %cls%
rem -Djava.util.logging.config.file=logging.properties 
call java -cp %cls% dso.test.TestServer

pause