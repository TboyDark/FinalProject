![](https://img.shields.io/badge/Project%20Manager-Maven-blue?style=for-the-badge&logo=apachemaven)
![](https://img.shields.io/badge/Authentication-JWT-blue?style=for-the-badge&logo=jwt)
![](https://img.shields.io/github/languages/top/TboyDark/FinalProject?style=for-the-badge)
![](https://img.shields.io/badge/Framework-Spring%20boot-blue?style=for-the-badge&logo=springboot)
# Final Project

Welcome To my Final project for Start 2 Impact, Backend Master.


# What it is this project?

This project is a Social Network! I called it Thinkee.

## How do i use it?

With the use of Postman or any API platform, you can test my social network API to CREATE, READ, UPDATE, DELETE
any User, Profile Setting, Post, Comment and Likes!

## In depth description for the services.
- First make sure to register a new user into the db (you can find the migrations.sql to migrate my db tables) with POSTMAN in this api:
Select type POST and digit this URL:
``` bash
http://localhost:8080/api/v1/auth/register
```
then you have to insert a json by selecting Body -> raw -> JSON where you'll find 'TEXT', with these data:
``` bash
{
	 "firstname":"YourName",
	 "lastname":"YourSurname",
	 "email":"YourEmail@email.com",
	 "password":"YourPassword"
}
```
Like in the screenshot:
![](screenshots/json.png?raw=true "token screenshot")
- Then open a new tab (You'll need this!) and type this url with the type POST:
``` bash
http://localhost:8080/api/v1/auth/authenticate
```
and pass a JSON with these data:
``` bash
{ 
	 "email":"YourEmail@email.com",
	 "password":"YourPassword"
}
```
if your user is inside the db, you wil get a response with a token in a json data,
(note i just typed some gibberish code for example of a token):
``` bash
{ 
	 "token":"EFWEFEWFEF:FDDSGGgSHHSH.ASFAFFFaf"
}
```
Now for every request you make for the API i created, is suggested to create a new token and paste it in the 
Authorization -> Bearer token -> paste the token in the box on the right.
![](screenshots/token.png?raw=true "token screenshot")

## Now i will show you the routes for the API and the needed JSON Data body:
- ### Posts
To create a new post (POST):
``` bash
http://localhost:8080/api/v1/posts
```
JSON:
``` bash
{
	"content":"The post content!"
}
```
you can GET and DELETE a post with this URL:

``` bash
http://localhost:8080/api/v1/posts/id
```
Also you can PUT a post with the same URL but you also need the json
``` bash
{
	"content":"The post content!"
}
```
you can also GET the posts by an user:
``` bash
http://localhost:8080/api/v1/posts/user/id
```
and a specified post and a specified user:
``` bash
http://localhost:8080/api/v1/posts/id/users/id
```
- ### Comments
To create a new comment (POST):
``` bash
http://localhost:8080/api/v1/posts/postId/comments
```
JSON:
``` bash
{
	"message":"Your comment!"
}
```
you can GET , PUT and DELETE a comment with this URL:

``` bash
http://localhost:8080/api/v1/posts/postId/comments/commentId
```
In the PUT method you need the json
``` bash
{
	"message":"Your new comment!"
}
```
- ### Likes

To create a new Like (POST):
``` bash
http://localhost:8080/api/v1/posts/postId/likes
```
JSON:
``` bash
{
	"status":"LIKED"
}
```
you can GET and DELETE the post's likes with this URL:

``` bash
http://localhost:8080/api/v1/posts/postId/likes
```
The PUT method need this URL:
``` bash
http://localhost:8080/api/v1/posts/postId/likes/likeId
```

In the PUT  method you need the json
``` bash
{
	"status":"UNLIKED"
}
```
Also there is count method for all the posts likes, you can access it by this URL:
``` bash
http://localhost:8080/api/v1/posts/postId/likes/totalamount
```
- ### Profile Setting
To create a new profile setting (POST):
``` bash
http://localhost:8080/api/v1/profile
```
JSON:
``` bash
{
	"profilesetting":  "PUBLIC"
}
```
OR
``` bash
{
	"profilesetting":  "PRIVATE"
}
```
you can GET , PUT and DELETE a profile setting with this URL:

``` bash
http://localhost:8080/api/v1/profile/userId
```
In the PUT method you need the json
``` bash
{
	"profilesetting":  "PRIVATE"
}
```
OR
``` bash
{
	"profilesetting":  "PUBLIC"
}
```
Remember that with these settings, only the users with the profile setting **"PUBLIC"** can show you the posts thus comments and like could not be posted.

- ### Logout

To logout just type this URL:
``` bash
http://localhost:8080/api/v1/auth/logout
```

## How to compile this project?

You can compile this project as follows: after you downloaded this project and exported into a folder, you have to open the cmd window, write :
``` bash
cd /C C:/path/to/project/folder
```
when the cmd has changed the directory of the folder, you have to write:
``` bash
mvn compile package
```
the following line, will compile all the .java files into the .class file in the target directory and then will create the Thinkee-1.0.0.jar into the target folder.

## How to execute the jar file?

You can exectue the jar file as follows:
``` bash
java -jar target/Thinkee-1.0.0.jar
```
after you press enter, the program will start.
!!! Note that if you want to shut down the program, you have to CTRL+C in the terminal. !!!


# Authors

[@TboyDark](https://github.com/TboyDark), My twitter [@TboyDark98](https://twitter.com/TboyDark98) Email: tommasobaldan1998@gmail.com .

## Acknowledgments
- [Start 2 Impact](https://www.start2impact.it/)
- Web Guides
- [Amigoscode](https://www.youtube.com/@amigoscode)
- [Bouali Ali](https://www.youtube.com/@BoualiAli)
