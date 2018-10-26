#include "RequestHandler.hpp"
#include "ConnectionPool.hpp"
#include "../Consts.hpp"


using namespace Pistache;

void RequestHandler::CreateUser(const Rest::Request& rq, Http::ResponseWriter rw)
{
    auto username = rq.param(":n").as<std::string>();
    auto email = rq.param(":e").as<std::string>();
    auto passwd = rq.param(":h").as<std::string>();
    
    auto result = pool.executeQuery(QueriesConsts::find_user, email, username);
    if(result->size() > 0)
        rw.send(Http::Code::Not_Acceptable, "User Already Exists");
    else
    {
        pool.executeQuery(QueriesConsts::create_user, email, username, passwd);
        rw.send(Http::Code::Ok, "User Created");
    }
}


void RequestHandler::setRoutes(Rest::Router& r)
{
    Rest::Routes::Post(r, RoutingConsts::create_user_route, Rest::Routes::bind(&RequestHandler::CreateUser, this));
}