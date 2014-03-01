set cls=%cd%\target\classes
cd %cls%
call java -Xmx300m -XX:+HeapDumpOnOutOfMemoryError -cp %cls% dso.test.TestClient

pause