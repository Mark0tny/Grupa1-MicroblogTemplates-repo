#pragma once
#include <string>
#include <vector>

using std::string;
using std::vector;
using ids = std::vector<unsigned long>;
struct User
{
    unsigned long id;
    string username;
    string email;
    string password;
    ids  microblogs;
    ids followed_blogs;
    ids followed_posts;


    User() = delete;

    User(unsigned long id, string username, string email, 
    string password, ids microblogs, ids followed_blogs, 
    ids followed_posts);
    

};
