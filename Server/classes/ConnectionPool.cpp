#include "ConnectionPool.hpp"
#include <iostream>
#include <utility>
#include "../Consts.hpp"


ConnectionPool::ConnectionPool()
{
    mapSetup();
    for(int i = 0; i < size; ++i)
        pool.emplace_back(std::make_shared<pqxx::lazyconnection>("hostaddr=127.0.0.1 port=5433 dbname=MicroBlog user=postgres password=blog"));
    std::cout << pool.size() << "\n";
    for(auto & c : pool)
        for(const auto& [k, v] : queries)
        {
            c->prepare(k, v);
        }

}

con_ptr ConnectionPool::getConnection()
{
    std::scoped_lock lock(mutex);
    while(pool.empty()){};
    auto con = pool.front();
    pool.pop_front();
    return con;
}

void ConnectionPool::freeConnection(con_ptr con)
{
    std::scoped_lock lock(mutex);
    pool.push_back(con);
}

void ConnectionPool::mapSetup()
{
    using namespace QueriesConsts;
    queries.clear();
    queries.emplace(std::make_pair(find_user, find_user_query));
    queries.emplace(std::make_pair(create_user, create_user_query));
}


