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
        rw.send(Http::Code::Bad_Request, "User Already Exists");
    else if(result.has_value() && result->size() == 0)
    {
        auto result = pool.executeQuery(QueriesConsts::create_user, email, username, passwd);
        if(result.has_value() && result->size() == 1)
            for(auto&& r : result.value())
                rw.send(Http::Code::Ok, std::string(r.at(0).c_str()) + ":" + std::string(r.at(1).c_str()));
        else
            rw.send(Http::Code::Service_Unavailable, "No Available Connection");
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
        for(auto&& r : result.value())
                rw.send(Http::Code::Ok, std::string(r.at(0).c_str()) + ":" + std::string(r.at(1).c_str()));        
    else if(result.has_value() && result->size() == 0)
        rw.send(Http::Code::Bad_Request, "Login failed");
    else
        rw.send(Http::Code::Service_Unavailable, "No Available Connection");
}

void RequestHandler::CreateMicroBlog(const Rest::Request& rq, Http::ResponseWriter rw)
{
    auto name = rq.param(":name").as<std::string>();
    auto id = rq.param(":id").as<unsigned long>();
    auto p = rq.param(":p").as<std::string>();
    bool priv = p == std::string("private") ? true : false;

    auto result = pool.executeQuery(QueriesConsts::create_microblog, name, id, priv);

    if(result.has_value() && result->size() == 1)
        rw.send(Http::Code::Ok, result.value()[0].at(0).c_str());
    else 
        rw.send(Http::Code::Bad_Request, "Failed To Create MicroBlog");

}

void RequestHandler::GetMyBlogs(const Rest::Request& rq, Http::ResponseWriter rw)
{
    using namespace std::literals;
    auto author = rq.param(":id").as<unsigned long>();

    std::cout << "Fetching microblogs\n";
    auto result = pool.executeQuery(QueriesConsts::get_my_microblogs, author);
    if(result.has_value() && result->size() > 0)
    {
        using namespace std::literals;
        rw.headers().add<Pistache::Http::Header::ContentType>(MIME(Application, Json));
        auto stream = rw.stream(Http::Code::Ok);

        std::string jsons = result.value().at(0).at(0).as<std::string>();
        jsons = jsons.substr(1, jsons.size() - 2);

        std::istringstream iss(jsons);
        for(std::string line; std::getline(iss, line);)
            stream << std::move(std::string(line + "\n"s).c_str());

        stream.ends();
    }
    else 
        rw.send(Http::Code::Service_Unavailable, "No Available Connection");

}

void RequestHandler::GetPostsByBlog(const Rest::Request& rq, Http::ResponseWriter rw)
{
    auto id = rq.param(":blogid").as<unsigned long>();
    std::cout << "Fetching posts\n";

    using namespace std::literals;
    auto result = pool.executeQuery(QueriesConsts::get_posts_by_id, id);
    if(result.has_value() && result->size() > 0)
    {
        rw.headers().add<Pistache::Http::Header::ContentType>(MIME(Application, Json));
        auto stream = rw.stream(Http::Code::Ok);
        
        std::string jsons = result.value().at(0).at(0).as<std::string>();
        jsons = jsons.substr(1, jsons.size() - 2);

        std::istringstream iss(jsons);
        for(std::string line; std::getline(iss, line);)
            stream << std::move(std::string(line + "\n"s).c_str());
        stream.ends();
    }
    else 
        rw.send(Http::Code::Service_Unavailable, "No Available Connection");

}


void RequestHandler::AddPost(const pr::Request& rq, ph::ResponseWriter rw)
{
    auto id = rq.param(":id").as<unsigned long>();
    auto author = rq.param(":a").as<unsigned long>();
    auto title = rq.param(":t").as<std::string>();
    auto content = rq.param(":c").as<std::string>();

    auto result = pool.executeQuery(QueriesConsts::add_post, author, title, content, id);

    if(result.has_value() && result->size() > 0)
        rw.send(Http::Code::Ok, "Post Added");
    else if(result.has_value())
        rw.send(Http::Code::Bad_Request, "Not Able To Add Post");
    else
        rw.send(Http::Code::Service_Unavailable, "No Available Connection");


}

void RequestHandler::setRoutes(Rest::Router& r)
{
    std::cout << "Routing setup\n";
    Rest::Routes::Post(r, RoutingConsts::create_user_route, Rest::Routes::bind(&RequestHandler::CreateUser, this));
    Rest::Routes::Get(r, RoutingConsts::login_user, Rest::Routes::bind(&RequestHandler::LoginUser, this));
    Rest::Routes::Post(r, RoutingConsts::create_microblog, Rest::Routes::bind(&RequestHandler::CreateMicroBlog, this));
    Rest::Routes::Get(r, RoutingConsts::get_my_blogs, Rest::Routes::bind(&RequestHandler::GetMyBlogs, this));
    Rest::Routes::Post(r, RoutingConsts::add_post, Rest::Routes::bind(&RequestHandler::AddPost, this));
    Rest::Routes::Get(r, RoutingConsts::get_posts_by_id, Rest::Routes::bind(&RequestHandler::GetPostsByBlog, this));
    

}