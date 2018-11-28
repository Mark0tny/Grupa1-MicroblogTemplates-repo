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
    constexpr auto get_post = "/getpost";
    constexpr auto get_comments = "/comments";
    constexpr auto delete_post = "/deletepost";
    constexpr auto delete_blog = "/deleteblog";
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
                                                SELECT m.name, u.username, m.time_created::timestamp(0), m.id_microblog, u.id_user, m.tags
                                                FROM microblog m
                                                join users u on u.id_user = m.author
                                                WHERE u.id_user = $1) t)";
    
    constexpr auto get_posts_by_id = "get posts by id";
    constexpr auto get_posts_by_id_query = R"(SELECT COALESCE (json_agg(t), '[]'::json) FROM (
                                                SELECT u.username, p.views, p.tags, p.title,  COUNT(c.id_comment), p.time_created::timestamp(0), p.id_post, p.id_microblog
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
    constexpr auto followed_query = R"(SELECT COALESCE (json_agg(t), '[]'::json) FROM (
            SELECT DIStinct m.name, u.username, m.time_created::timestamp(0), m.tags, m.id_microblog, m.author  
            FROM follow f
            Join microblog m on m.id_microblog = f.blogid
            JOIN users u on u.id_user = m.author
            WHERE f.userid = $1
            ORDER BY m.time_created::timestamp(0)
        ) t)";
    constexpr auto get_followers = "get_followers";
    constexpr auto get_followers_query = "SELECT COALESCE (json_agg(t), '[]'::json) FROM (SELECT username FROM users u inner join follow f on f.userid = u.id_user where f.blogid = $1) t";
    constexpr auto search_blogs = "search blogs";
    constexpr auto search_blogs_query = R"(SELECT COALESCE (json_agg(t), '[]'::json) FROM (
            SELECT m.name, u.username, m.time_created::timestamp(0), m.tags, m.id_microblog, m.author  
            FROM microblog m join users u on u.id_user = m.author 
            WHERE u.username like $1 OR m.name like $1 OR m.tags like $1 
            ORDER BY m.time_created
        ) t)";
    constexpr auto search_posts = "search posts";
    constexpr auto search_posts_query = R"(SELECT COALESCE (json_agg(t), '[]'::json) FROM (
            SELECT u.username, p.views, p.tags, COUNT(c.id_comment), p.time_created::timestamp(0), p.id_post, p.id_microblog, p.title
            FROM post p join users u on p.author = u.id_user
            left join comments c on c.post_id = p.id_post
            WHERE u.username like $1 OR p.title like $1 OR p.tags like $1 or p.content like $1
            GROUP BY u.username, p.views, p.tags, p.time_created::timestamp(0), p.id_post, p.id_microblog
            ORDER BY p.time_created
        ) t)";
    constexpr auto get_post = "get post";
    constexpr auto get_post_query = R"(SELECT COALESCE (json_agg(t), '[]'::json) FROM (
        SELECT u.username, p.content, p.views, p.tags, p.time_created::timestamp(0), p.id_post, p.id_microblog, p.title
        FROM post p
        JOIN users u ON u.id_user = p.author 
        WHERE id_post = $1
        ORDER BY time_created
    ) t)";
    constexpr auto get_comments = "get comments";
    constexpr auto get_comments_query = R"(SELECT COALESCE (json_agg(t), '[]'::json) FROM (
        SELECT u.username, c.content, time_created::timestamp(0)
            FROM comments c 
            JOIN users u on u.id_user = c.author
        WHERE c.post_id = $1
        ORDER BY c.time_created::timestamp(0)
        ) t)";
    constexpr auto delete_post = "delete post";
    constexpr auto delete_post_query = "DELETE FROM post where id_post = $1";
    constexpr auto delete_comments = "delete comments";
    constexpr auto delete_comments_query = "DELETE FROM comments WHERE post_id = $1";

    constexpr auto delete_blogs_posts = "delete blogs posts";
    constexpr auto delete_blogs_posts_query = "DELETE FROM post where id_microblog = $1";
    constexpr auto delete_posts_comments = "delete posts comments";
    constexpr auto delete_posts_comments_query = "DELETE FROM comments WHERE post_id IN (SELECT id_post FROM post where id_microblog = $1)";
    constexpr auto delete_blog = "delete blog";
    constexpr auto delete_blog_query = "DELETE FROM microblog WHERE id_microblog = $1";
    constexpr auto is_blog_author = "is blog author";
    constexpr auto is_blog_author_query = "SELECT COUNT(*) FROM users u join microblog m on m.author = u.id_user WHERE id_user = $1 AND m.id_microblog = $2";
    constexpr auto is_post_author = "is post author";
    constexpr auto is_post_author_query = "SELECT COUNT(*) FROM users u join post m on m.author = u.id_user WHERE id_user = $1 AND m.id_post = $2";
    constexpr auto delete_follows = "delete follows";
    constexpr auto delete_follows_query = "DELETE FROM follow WHERE blogid = $1";

}
