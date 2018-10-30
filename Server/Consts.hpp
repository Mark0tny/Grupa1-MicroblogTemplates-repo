#pragma once
#include <string>


//Code OK - 200
//Code Bad_Request - 400
//Code Service_Unavailable - 503




namespace RoutingConsts
{
    constexpr auto create_user_route = "/email/:e/nick/:n/haslo/:h";
    constexpr auto login_user = "/email/:e/haslo/:h";
    constexpr auto create_microblog = "/create/microblog/:name/author/:id/private/:p";
    constexpr auto update_user = "/email/:e/nick/:n/haslo/:h";
    constexpr auto follow_blog = "/userid/:uid/blogid/:bid";
    constexpr auto follow_user = "/follower/:followerid/followed/:followedid";
    constexpr auto search = "/searchfor/:sf/searchby/:sb/keyword/:k/orderby/:ob";
}


namespace QueriesConsts
{
    constexpr auto find_user = "find user";
    constexpr auto find_user_query = "SELECT id_user FROM users WHERE email = $1 OR username = $2;";
    constexpr auto create_user = "create user";
    constexpr auto create_user_query = "INSERT INTO users (email, username, password) VALUES($1, $2, $3) RETURNING id_user, username";
    constexpr auto login_user = "login user";
    constexpr auto login_user_query = "SELECT id_user FROM users where ( email = $1 OR username = $1) AND password = $2 LIMIT 1 RETURNING id_user, username";
    constexpr auto create_microblog = "create microblog";
    constexpr auto create_microblog_query = "INSERT INTO microblog (name, author, private) VALUES($1, $2, $3) RETURNING id_microblog";
}