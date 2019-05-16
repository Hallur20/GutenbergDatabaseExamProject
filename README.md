# GutenbergDatabaseExamProject

1. You need to do some extra steps to make this project work (apart from running it in Netbeans idé)...



2. go into your docker container containing mysql-server -> /etc/mysql/my.cnf.

change whatever secure-file-priv says to: secure-file-priv=/home

if your docker container name is not "some-mysql" then you can change it to your name at line: 58 in the Main file.

we are using localhost...

we are using root user...

we are using password 123...

