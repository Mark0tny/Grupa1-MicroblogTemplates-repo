#pragma once
#include <string>
using namespace std::literals;
//Code OK - 200
//Code Bad_Request - 400
//Code Service_Unavailable - 503




namespace RoutingConsts
{
    constexpr auto create_user_route = "/createuser";
    constexpr auto login_user = "/login";
    constexpr auto create_microblog = "/createmicroblog";
    constexpr auto update_user = "/updateuser";

    constexpr auto search = "/search";
    constexpr auto get_my_blogs = "/getmymicroblogs";
    constexpr auto get_posts_by_id = "/getposts";
    constexpr auto add_post = "/addpost";
    constexpr auto add_comment = "/addcomment";
    constexpr auto upvote = "/upvote";
    constexpr auto follow = "/follow";
    constexpr auto get_followed_blogs = "/followed";
    constexpr auto get_followers = "/followers";
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
    constexpr auto create_microblog_query = "INSERT INTO microblog (name, author, tags, private, time_created) VALUES($1, $2, $3, $4, current_timestamp) RETURNING id_microblog";
    
    constexpr auto get_my_microblogs = "get users microblogs";
    constexpr auto get_my_microblogs_query = R"(SELECT COALESCE (json_agg(t), '[]'::json) FROM (
                                                SELECT m.name, u.username, m.time_created, m.id_microblog, u.id_user, m.tags
                                                FROM microblog m
                                                join users u on u.id_user = m.author
                                                WHERE u.id_user = $1) t)";
    
    constexpr auto get_posts_by_id = "get posts by id";
    constexpr auto get_posts_by_id_query = R"(SELECT COALESCE (json_agg(t), '[]'::json) FROM (
                                                SELECT u.username, p.views, p.tags, COUNT(c.id_comment), p.time_created, p.id_post, p.id_microblog
                                                FROM post p join users u on p.author = u.id_user
                                                left join comments c on c.post_id = p.id_post
                                                WHERE p.id_microblog = $1
                                                GROUP BY u.username, p.views, p.tags, p.time_created, p.id_post, p.id_microblog
                                            ) t)";
    
    constexpr auto add_post = "add post";
    constexpr auto add_post_query = "INSERT INTO post (author, title, time_created, content, id_microblog, tags, views) VALUES($1, $2, current_timestamp, $3, $4, $5, 0) RETURNING id_post";
    constexpr auto add_comment = "add comment";
    constexpr auto add_comment_query = "INSERT INTO comments (post_id, content, author, time_created) VALUES($1, $2, $3, current_timestamp) RETURNING id_comment";
    constexpr auto upvote = "upvote";
    constexpr auto upvote_query = "UPDATE post SET views = views + 1 WHERE id_post = $1 RETURNING views";
    constexpr auto follow = "follow";
    constexpr auto follow_query = "INSERT INTO follow (userid, blogid) VALUES ($1, $2) RETURNING blogid";
    constexpr auto followed = "followed";
    constexpr auto followed_query = "WITH T AS (SELECT * FROM microblog m inner join follow f on m.id_microblog = f.blogid where f.userid = $1) SELECT json_agg(T) FROM T";
    constexpr auto get_followers = "get_followers";
    constexpr auto get_followers_query = "SELECT COALESCE (json_agg(t), '[]'::json) FROM (SELECT username FROM users u inner join follow f on f.userid = u.id_user where f.blogid = $1) t";
    constexpr auto search_blogs = "search blogs";
    constexpr auto search_blogs_query = R"(SELECT COALESCE (json_agg(t), '[]'::json) FROM (
            SELECT m.name, u.username, m.time_created, m.tags, m.id_microblog, m.author  
            FROM microblog m join users u on u.id_user = m.author 
            WHERE u.username like $1 OR m.name like $1 OR m.tags like $1 
            ORDER BY m.time_created
        ) t)";
    constexpr auto search_posts = "search posts";
    constexpr auto search_posts_query = R"(SELECT COALESCE (json_agg(t), '[]'::json) FROM (
            SELECT u.username, p.views, p.tags, COUNT(c.id_comment), p.time_created, p.id_post, p.id_microblog
            FROM post p join users u on p.author = u.id_user
            left join comments c on c.post_id = p.id_post
            WHERE u.username like $1 OR p.title like $1 OR p.tags like $1 or p.content like $1
            GROUP BY u.username, p.views, p.tags, p.time_created, p.id_post, p.id_microblog
            ORDER BY p.time_created
        ) t)";
    
}