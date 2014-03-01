set cls=%cd%\bin
cd %cls%
call java -Xmx300m -XX:+HeapDumpOnOutOfMemoryError -cp %cls% dso.test.TestClient

pause