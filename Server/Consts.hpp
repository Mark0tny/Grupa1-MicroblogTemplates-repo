#pragma once

namespace RoutingConsts
{
    auto create_user_route = "/email/:e/nick/:n/haslo/:h";
    auto login_user = "/email/:e/haslo/:h";
    auto update_user = "/email/:e/nick/:n/haslo/:h";
    auto follow_blog = "/userid/:uid/blogid/:bid";
    auto follow_user = "/follower/:followerid/followed/:followedid";
    auto search = "/searchfor/:sf/searchby/:sb/keyword/:k/orderby/:ob";
}


namespace QueriesConsts
{
    auto find_user = "find user";
    auto find_user_query = "SELECT id_user FROM users WHERE email = $1 AND username = $2;";
    auto create_user = "create user";
    auto create_user_query = "INSERT INTO users (email, username, password) VALUES($1, $2, $3)";

}