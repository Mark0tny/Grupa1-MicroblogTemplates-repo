#include "ConnectionPool.hpp"
#include <iostream>
#include <utility>
#include "../Consts.hpp"


ConnectionPool::ConnectionPool()
{
    mapSetup();
    std::cout << "Preparing connections...";
    for(int i = 0; i < size; ++i)
        pool.emplace_back(std::make_shared<pqxx::lazyconnection>("hostaddr=127.0.0.1 port=5432 dbname=MicroBlog user=postgres password=blog"));
    for(auto & c : pool)
        for(const auto& [k, v] : queries)
            c->prepare(k, v);
    std::cout << "Done\n";

}

std::optional<con_ptr> ConnectionPool::getConnection()
{
    std::cout << "Connection taken\n";
    std::optional<con_ptr> retval;
    std::scoped_lock lock(mutex);
    if(!pool.empty())
    {
        retval.emplace(pool.front());
        pool.pop_front();  
    }
    return retval;
}

void ConnectionPool::freeConnection(con_ptr con)
{   
    std::cout << "Connection freed\n";
    std::scoped_lock lock(mutex);
    pool.push_back(con);
}

void ConnectionPool::mapSetup()
{
    std::cout << "Queries map setup...";
    using namespace QueriesConsts;
    queries.clear();
    queries = {
        {find_user, find_user_query},
        {create_user, create_user_query},
        {login_user, login_user_query},
        {create_microblog, create_microblog_query},
        {get_my_microblogs, get_my_microblogs_query},
        {add_post, add_post_query},
        {get_posts_by_id, get_posts_by_id_query},
        {add_comment, add_comment_query},
        {upvote, upvote_query},
        {follow, follow_query},
        {followed, followed_query},
        {get_followers, get_followers_query},
        {search_blogs, search_blogs_query},
        {search_posts, search_posts_query},
        {get_post, get_post_query},
        {get_comments, get_comments_query},
        {delete_post, delete_post_query},
        {delete_blog, delete_blog_query},
        {delete_blogs_posts, delete_blogs_posts_query},
        {delete_posts_comments, delete_posts_comments_query},
        {delete_comments, delete_comments_query},
        {is_blog_author, is_blog_author_query},
        {is_post_author, is_post_author_query},
        {delete_follows, delete_follows_query}

    };

    std::cout << "Done\n";
}


