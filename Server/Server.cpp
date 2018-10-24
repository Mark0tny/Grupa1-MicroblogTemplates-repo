#include <pistache/endpoint.h>
#include <pistache/router.h>
#include <pistache/http.h>
#include <pistache/net.h>
#include <iostream>
#include "classes/User.hpp"
#include "classes/RequestHandler.hpp"

    

int main()
{
    
    auto addr = Pistache::Address(Pistache::Ipv4(127,0,0,1), Pistache::Port(9080));
    Pistache::Rest::Router router;
    RequestHandler rh;
    rh.setRoutes(router);
    Pistache::Http::Endpoint server(addr);
    server.init();
    server.setHandler(router.handler());
    server.serve();
    return 0;
}
