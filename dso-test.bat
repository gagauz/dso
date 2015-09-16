set JAVA_HOME=r:/jdk1.7
set Path=%JAVA_HOME%/bin;%Path%
set cls=%cd%\target\classes
cd %cls%
call java -Xbootclasspath/p:%cls%\..\.;R:\maven_repo\javassist\javassist\3.12.1.GA\javassist-3.12.1.GA.jar dso.test.TestApp

pause