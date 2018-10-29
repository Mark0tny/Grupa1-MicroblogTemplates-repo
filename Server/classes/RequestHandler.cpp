#include "RequestHandler.hpp"
#include "ConnectionPool.hpp"
#include "../Consts.hpp"


using namespace Pistache;

void RequestHandler::CreateUser(const Rest::Request& rq, Http::ResponseWriter rw)
{
    auto username = rq.param(":n").as<std::string>();
    auto email = rq.param(":e").as<std::string>();
    auto passwd = rq.param(":h").as<std::string>();
    std::cout << "Create user request\n";   
    auto result = pool.executeQuery(QueriesConsts::find_user, email, username);
    if(result.has_value() && result->size() > 0)
        rw.send(Http::Code::Not_Acceptable, "User Already Exists");
    else if(result.has_value() && result->size() == 0)
    {
        pool.executeQuery(QueriesConsts::create_user, email, username, passwd);
        rw.send(Http::Code::Created, "User Created");
    }
    else
        rw.send(Http::Code::Service_Unavailable, "No Available Connection");
}


void RequestHandler::LoginUser(const Rest::Request& rq, Http::ResponseWriter rw)
{
    auto login = rq.param(":e").as<std::string>();
    auto passwd = rq.param(":h").as<std::string>();
    auto result = pool.executeQuery(QueriesConsts::login_user, login, passwd);
    if(result.has_value() && result->size() == 1)
        rw.send(Http::Code::Ok, result.value()[0].at(0).c_str());
    else if(result.has_value() && result->size() == 0)
        rw.send(Http::Code::Not_Found, "Login failed");
    else
        rw.send(Http::Code::Service_Unavailable, "No Available Connection");
}


void RequestHandler::setRoutes(Rest::Router& r)
{
    std::cout << "Routing setup\n";
    Rest::Routes::Post(r, RoutingConsts::create_user_route, Rest::Routes::bind(&RequestHandler::CreateUser, this));
    Rest::Routes::Get(r, RoutingConsts::login_user, Rest::Routes::bind(&RequestHandler::LoginUser, this));
}