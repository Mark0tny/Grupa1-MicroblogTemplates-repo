#pragma once
#include<pqxx/pqxx>
#include<mutex>
#include<unordered_map>
#include<deque>
#include<optional>
#include<memory>
#include<iostream>
#include<string>
using con_ptr = std::shared_ptr<pqxx::lazyconnection>;
using query_map = std::unordered_map<std::string, std::string>;
struct ConnectionPool{

    std::mutex mutex;
    std::deque<con_ptr> pool;
    query_map queries;
    int size = 100;


    ConnectionPool();
    con_ptr getConnection();
    void freeConnection(con_ptr con);
    void mapSetup();
    ~ConnectionPool() =default;
    ConnectionPool(const ConnectionPool &) =delete;
    ConnectionPool(ConnectionPool &&) =delete;


    template<typename... Args>
    std::optional<pqxx::result> executeQuery(std::string query, Args&&... args)
    {
        std::optional<pqxx::result> retval;
        auto con = getConnection();
        pqxx::work w(*con);
        try{
            retval.emplace(w.exec_prepared(query, std::forward<Args>(args)...));
            w.commit();
            freeConnection(con);
        }
        catch(std::exception & e){
            std::cerr << e.what() << std::endl;
        }
        return retval;
    }


};