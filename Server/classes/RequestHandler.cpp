#include "RequestHandler.hpp"
#include "../Consts.hpp"

using namespace Pistache;

void RequestHandler::CreateUser(const Rest::Request& rq, Http::ResponseWriter rw)
{
    auto username = rq.param(":n").as<std::string>();
    auto email = rq.param(":e").as<std::string>();
    auto passwd = rq.param(":h").as<std::string>();
    auto connection = pqxx::connection("hostaddr=127.0.0.1 port=5433 dbname=MicroBlog user=postgres password=blog");
    connectionSetUp(connection);
    pqxx::work con(connection);
    try{

        auto result = con.exec_prepared(QueriesConsts::find_user, email, username);
        con.commit();
        if(result.size() == 0)
        {
            con.exec_prepared(QueriesConsts::create_user, email, username, passwd);
            rw.send(Http::Code::Created, "User Created");
        }
        else
            rw.send(Http::Code::Not_Acceptable, "User Already Exists");
        con.commit();
    }
    catch(const std::exception &e)
    {
        std::cerr << e.what() << std::endl;
    }
}

void RequestHandler::connectionSetUp(pqxx::connection &con)
{
    using namespace QueriesConsts;
    con.prepare(find_user, find_user_query);
    con.prepare(create_user, create_user_query);
}

void RequestHandler::setRoutes(Rest::Router& r)
{
    Rest::Routes::Get(r, RoutingConsts::create_user_route, Rest::Routes::bind(&RequestHandler::CreateUser, this));
}