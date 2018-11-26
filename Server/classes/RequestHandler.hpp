#pragma once
#include <pistache/endpoint.h>
#include <pistache/router.h>
#include <pistache/http.h>
#include <pistache/net.h>
#include <iostream>
#include <pqxx/pqxx>
#include <string>
#include "ConnectionPool.hpp"

namespace pr = Pistache::Rest;
namespace ph = Pistache::Http;


struct RequestHandler
{
    ConnectionPool pool;
    void connectionSetUp(pqxx::lazyconnection &con);
    void CreateUser(const pr::Request& rq, ph::ResponseWriter rw);
    void LoginUser(const pr::Request& rq, ph::ResponseWriter rw);
    void CreateMicroBlog(const pr::Request& rq, ph::ResponseWriter rw);
    void GetMyBlogs(const pr::Request& rq, ph::ResponseWriter rw);
    void GetPostsByBlog(const pr::Request& rq, ph::ResponseWriter rw);
    void AddPost(const pr::Request& rq, ph::ResponseWriter rw);
    void AddComment(const pr::Request& rq, ph::ResponseWriter rw);
    void Upvote(const pr::Request& rq, ph::ResponseWriter rw);
    void Follow(const pr::Request& rq, ph::ResponseWriter rw);
    void Followed(const pr::Request& rq, ph::ResponseWriter rw);
    void GetFollowers(const pr::Request& rq, ph::ResponseWriter rw);
    void Search(const pr::Request& rq, ph::ResponseWriter rw);
    void GetPost(const pr::Request& rq, ph::ResponseWriter rw);
    void GetComments(const pr::Request& rq, ph::ResponseWriter rw);
    void DeletePost(const pr::Request& rq, ph::ResponseWriter rw);
    void DeleteBlog(const pr::Request& rq, ph::ResponseWriter rw);




    void setRoutes(pr::Router& r);
};

