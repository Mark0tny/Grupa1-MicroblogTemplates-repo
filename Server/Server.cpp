#include <pistache/endpoint.h>
#include <pistache/router.h>
#include <pistache/http.h>
#include <pistache/net.h>
#include <iostream>
#include <memory>
#include <unordered_map>
#include "RoutingConsts.hpp"
#include "classes/User.hpp"
#include <pqxx/pqxx>

using namespace Pistache;
    
namespace rs = RoutingConsts;

void connectionSetUp(pqxx::connection &con)
{
    con.prepare("find user", "SELECT id_user FROM users WHERE email = $1 AND username = $2;");
    con.prepare("create user", "INSERT INTO users (email, username, password) VALUES($1, $2, $3)");
}




struct Handler
{
    Rest::Router r;

    void CreateUser(const Rest::Request& rq, Http::ResponseWriter rw)
    {
        std::cout << "Create account request\n";
        auto username = rq.param(":n").as<std::string>();
        auto email = rq.param(":e").as<std::string>();
        auto passwd = rq.param(":h").as<std::string>();

        auto connection = pqxx::connection("hostaddr=127.0.0.1 port=5433 dbname=MicroBlog user=postgres password=blog");
        connectionSetUp(connection);
        pqxx::work con(connection);

        std::cout << username + " " << email << " " << passwd << std::endl;
        try{

    
            auto result = con.exec_prepared("find user", email, username);
            con.commit();
            if(result.size() == 0)
            {
                con.exec_prepared("create user", email, username, passwd);
                rw.send(Http::Code::Created, "User Created");
                std::cout << "CREATED\n";
            }
            else
                rw.send(Http::Code::Not_Acceptable, "User Already Exists");
                con.commit();
        }
        catch(const std::exception &e)
        {
            std::cerr << e.what() << std::endl;
            exit(1);
        }
    }

    void setRoutes(Rest::Router& r)
    {
        Rest::Routes::Get(r, rs::create_user_route, Rest::Routes::bind(&Handler::CreateUser, this));
    }
};


int main()
{

    auto addr = Address(Ipv4(127,0,0,1), Port(9080));
    Rest::Router router;
    auto hh = std::shared_ptr<Handler>();
    hh->setRoutes(router);

    Http::Endpoint server(addr);
    server.init();
    server.setHandler(router.handler());
    server.serve();
    return 0;
}
