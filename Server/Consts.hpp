#pragma once
#include <string>

namespace RoutingConsts
{
    static const std::string create_user_route = "/email/:e/nick/:n/haslo/:h";
    static const std::string login_user = "/email/:e/haslo/:h";
    static const std::string update_user = "/email/:e/nick/:n/haslo/:h";
    static const std::string follow_blog = "/userid/:uid/blogid/:bid";
    static const std::string follow_user = "/follower/:followerid/followed/:followedid";
    static const std::string search = "/searchfor/:sf/searchby/:sb/keyword/:k/orderby/:ob";
}


namespace QueriesConsts
{
    static const std::string find_user = "find user";
    static const std::string find_user_query = "SELECT id_user FROM users WHERE email = $1 OR username = $2;";
    static const std::string create_user = "create user";
    static const std::string create_user_query = "INSERT INTO users (email, username, password) VALUES($1, $2, $3)";
    static const std::string login_user = "login user";
    static const std::string login_user_query = "SELECT id_user FROM users where ( email = $1 OR username = $1) AND password = $2 LIMIT 1";
}