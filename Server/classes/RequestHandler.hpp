#pragma once
#include <pistache/endpoint.h>
#include <pistache/router.h>
#include <pistache/http.h>
#include <pistache/net.h>
#include <iostream>
#include <pqxx/pqxx>
#include <string>


namespace pr = Pistache::Rest;
namespace ph = Pistache::Http;


struct RequestHandler
{
    void connectionSetUp(pqxx::connection &con);
    void CreateUser(const pr::Request& rq, ph::ResponseWriter rw);
    void setRoutes(pr::Router& r);
};

