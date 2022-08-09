```shell
# which
$ which mysql
/usr/bin/mysql

# login
$ mysql -u root -proot

# create user
CREATE USER microservices IDENTIFIED BY 'microservices';

# create DATABASE
# user
# drop database ?
DROP DATABASE IF EXISTS user;
# create database
CREATE DATABASE IF NOT EXISTS user DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATE utf8mb4_unicode_ci;
GRANT ALL ON user.* TO microservices;
# flush
FLUSH privileges;

```
