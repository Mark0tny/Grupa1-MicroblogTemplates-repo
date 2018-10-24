#include "User.hpp"
User::User(unsigned long id, string username, string email, 
    string password, ids microblog, ids followed_blogs, 
    ids followed_posts)   
{
        this->id = id;
        this->username;
        this->email;
        this->password = password;
	this->microblogs = microblogs;
        this->followed_blogs = followed_blogs;
        this->followed_posts = followed_posts;
}
