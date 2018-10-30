#include "ConnectionPool.hpp"
#include <iostream>
#include <utility>
#include "../Consts.hpp"


ConnectionPool::ConnectionPool()
{
    mapSetup();
    std::cout << "Preparing connections\n";
    for(int i = 0; i < size; ++i)
        pool.emplace_back(std::make_shared<pqxx::lazyconnection>("hostaddr=127.0.0.1 port=5433 dbname=MicroBlog user=postgres password=blog"));
    for(auto & c : pool)
        for(const auto& [k, v] : queries)
            c->prepare(k, v);
    std::cout << "Connections prepared\n";

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
    std::cout << "Queries map setup\n";
    using namespace QueriesConsts;
    queries.clear();
    queries.emplace(std::make_pair(find_user, find_user_query));
    queries.emplace(std::make_pair(create_user, create_user_query));
    queries.emplace(std::make_pair(login_user, login_user_query));
    queries.emplace(std::make_pair(create_microblog, create_microblog_query));
    queries.emplace(std::make_pair(get_my_microblogs, get_my_microblogs_query));
    queries.emplace(std::make_pair(add_post, add_post_query));
    queries.emplace(std::make_pair(get_posts_by_id, get_posts_by_id_query));
}


