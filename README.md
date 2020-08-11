# Project Quick Recipes

![Github](https://github.com/eclair11/quick-recipes/blob/dev/front/src/assets/img/fast-food.svg)

An application that allows any user to instantly access to useful informations about a recipe without the need to be registered.

## Members
* Elias Romdan

## Technology
* Front-end: Angular
* Back-end: Spring
* Database: MySQL

## Configure the front-end server
1) Install node.js (version 10.9.0 or higher): [here](https://nodejs.org/en/)
2) Check node.js version using the command `node -v`
3) Check npm version using the command `npm -v`
4) Execute the command `npm install -g @angular/cli`

## Launch the front-end server
1) Open a shell in the folder `quick_recipes/front`
2) Execute the command `ng serve --o`

## Launch the back-end server
1) Open the project in your favorite IDE
2) Launch the server using the spring-boot dashboard

## Configure the database
1) Install MySQL Workbench:
* Linux version: [here](https://freemedforms.com/fr/manuals/freemedforms/install/server_mysql)
* Windows version: [here](https://dev.mysql.com/downloads/installer/)
2) Use `root` as password for the user `root`

## Create the database for the application
1) Open MySQL Notifier
2) Open the shell `MySQL Shell 8.0/bin/mysqlsh.exe`
3) Execute the command `\sql`
4) Execute the command `\connect root@localhost`
5) Enter `root` as password
6) Execute the command `create database qrdb`

## Launch the database
1) Open MySQL Notifier
2) Right click on the MySQL Notifier's icon (found in the hidden tasks icons)
3) Select SQL Editor...
4) Enter `root` as password

## External code sources
* castList: [here](https://stackoverflow.com/questions/367626/how-do-i-fix-the-expression-of-type-list-needs-unchecked-conversion)
* Convert Multipartfile to File: [here](https://onecompiler.com/posts/3sqk7mxmy/how-to-convert-multipartfile-to-java-io-file-in-spring)