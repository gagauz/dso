set file.encoding=UTF-8
call mvn -e clean install -Dmaven.test.skip=false -DdownloadSources=true -DdownloadSource=true


pause