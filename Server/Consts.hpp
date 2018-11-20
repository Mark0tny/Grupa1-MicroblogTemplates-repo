#pragma once
#include <string>


//Code OK - 200
//Code Bad_Request - 400
//Code Service_Unavailable - 503




namespace RoutingConsts
{
    constexpr auto create_user_route = "/createuser";
    constexpr auto login_user = "/login";
    constexpr auto create_microblog = "/createmicroblog";
    constexpr auto update_user = "/updateuser";
    constexpr auto follow_blog = "/followblog";
    constexpr auto follow_user = "/followuser";
    constexpr auto search = "/search";
    constexpr auto get_my_blogs = "/getmymicroblogs";
    constexpr auto get_posts_by_id = "/getposts";
    constexpr auto add_post = "/addpost";
    constexpr auto add_comment = "/addcomment";
    constexpr auto upvote = "/upvote";
}


namespace QueriesConsts
{
    constexpr auto find_user = "find user";
    constexpr auto find_user_query = "SELECT id_user FROM users WHERE email = $1 OR username = $2;";
    constexpr auto create_user = "create user";
    constexpr auto create_user_query = "INSERT INTO users (email, username, password) VALUES($1, $2, $3) RETURNING id_user, username";
    constexpr auto login_user = "login user";
    constexpr auto login_user_query = "SELECT id_user, username FROM users where ( email = $1 OR username = $1) AND password = $2 LIMIT 1";
    constexpr auto create_microblog = "create microblog";
    constexpr auto create_microblog_query = "INSERT INTO microblog (name, author, tags, private) VALUES($1, $2, $3, $4) RETURNING id_microblog";
    constexpr auto get_my_microblogs = "get users microblogs";
    constexpr auto get_my_microblogs_query = "WITH T AS (SELECT id_microblog, name, author, tags, private FROM microblog WHERE author = $1) SELECT json_agg(T) FROM T";
    constexpr auto get_posts_by_id = "get posts by id";
    constexpr auto get_posts_by_id_query ="WITH T AS (SELECT id_post, author, title, content  FROM post p join users u on p.author = u.id_user where p.author = $1 ORDER BY time_created ASC) SELECT json_agg(T) FROM T";
    constexpr auto add_post = "add post";
    constexpr auto add_post_query = "INSERT INTO post (author, title, time_created, content, id_microblog, tags) VALUES($1, $2, current_timestamp, $3, $4, $5) RETURNING id_post";
    constexpr auto add_comment = "add comment";
    constexpr auto add_comment_query = "INSERT INTO comments (post_id, content, author, time_created) VALUES($1, $2, $3, current_timestamp) RETURNING id_comment";
    constexpr auto upvote = "upvote";
    constexpr auto upvote_query = "UPDATE post SET views = views + 1 WHERE id_post = $1 RETURNING views";
}

