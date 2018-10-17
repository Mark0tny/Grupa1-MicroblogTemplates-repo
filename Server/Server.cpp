#include <pistache/endpoint.h>
#include <pistache/router.h>
#include <pistache/http.h>
#include <pistache/net.h>
#include <iostream>
#include <memory>
#include "RoutingConsts.hpp"
using namespace Pistache;

namespace rs = RoutingConsts;

struct Handler
{
    Rest::Router r;

    void CreateUser(const Rest::Request& rq, Http::ResponseWriter rw)
    {
        std::cout << "Create account request\n";
        auto email = rq.param(":e").as<std::string>();
        auto passwd = rq.param(":h").as<std::string>();
        std::cout << email << " " << passwd << "\n";
        rw.send(Http::Code::Ok, "Done\n");
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
