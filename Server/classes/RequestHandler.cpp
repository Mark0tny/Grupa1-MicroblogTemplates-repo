#include "RequestHandler.hpp"
#include "ConnectionPool.hpp"
#include "../Consts.hpp"
#include <nlohmann/json.hpp>
#include <type_traits>
#include <algorithm>


using namespace Pistache;
using namespace std::literals;
using json = nlohmann::json;


constexpr auto string_dumper_t = [](const json& m) {

        return [&map = m](const auto& key) -> std::string {
            return map.at(key).dump();
        };
};
constexpr auto id_dumper_t = [](const json& m) {
        return [&map = m](const auto& key) -> unsigned long long {
            return map.at(key).template get<unsigned long long>();
        };
};


void RequestHandler::CreateUser(const Rest::Request& rq, Http::ResponseWriter rw)
{
    json login_data = json::parse(rq.body());
    auto dumper = string_dumper_t(login_data);
    std::cout << "Create user request\n";
    auto result = pool.executeQuery(QueriesConsts::find_user, dumper("email"), dumper("username"));
    if(result.has_value() && result->size() > 0)
        rw.send(Http::Code::Bad_Request, "User Already Exists");
    else if(result.has_value() && result->size() == 0)
    {
        auto result = pool.executeQuery(QueriesConsts::create_user, dumper("email"), dumper("username"), dumper("password"));
        if(result.has_value() && result->size() == 1)
        {
            rw.headers().add<Http::Header::ContentType>(MIME(Application, Json));

            json user = {
                {"id_user" ,result->at(0).at(0).as<unsigned long long>()},
                {"username", result->at(0).at(1).c_str()}
            };
            rw.send(Http::Code::Ok, user.dump());
            std::cout << user.dump() << "\n";
        }
        else
            rw.send(Http::Code::Service_Unavailable, "No Available Connection");
    }
    else
        rw.send(Http::Code::Service_Unavailable, "No Available Connection");
}


void RequestHandler::LoginUser(const Rest::Request& rq, Http::ResponseWriter rw)
{

    json login = json::parse(rq.body());
    auto dumper = string_dumper_t(login);
    auto result = pool.executeQuery(QueriesConsts::login_user, dumper("login"), dumper("password"));
    if(result.has_value() && result->size() == 1)
    {
        json user = {
            {"id_user" ,result->at(0).at(0).c_str()},
            {"username", result->at(0).at(1).c_str()}
            };
        rw.send(Http::Code::Ok, user.dump());        

    }
    else if(result.has_value() && result->size() == 0)
        rw.send(Http::Code::Bad_Request, "Login failed");
    else
        rw.send(Http::Code::Service_Unavailable, "No Available Connection");
}

void RequestHandler::CreateMicroBlog(const Rest::Request& rq, Http::ResponseWriter rw)
{
    json microblog_data = json::parse(rq.body());
    auto dumper = string_dumper_t(microblog_data);
    auto iddumper = id_dumper_t(microblog_data);
    auto result = pool.executeQuery(QueriesConsts::create_microblog, dumper("title"), 
                                    iddumper("id"),dumper("tags"), dumper("private") != "private" );

    if(result.has_value() && result->size() == 1)
    {
        rw.send(Http::Code::Ok, "MicroBlog Created");
    }
    else 
        rw.send(Http::Code::Bad_Request, "Failed To Create MicroBlog");

}

void RequestHandler::GetMyBlogs(const Rest::Request& rq, Http::ResponseWriter rw)
{
    auto id = json::parse(rq.body());
    auto dumper = id_dumper_t(id);
    std::cout << "Fetching microblogs\n";
    auto result = pool.executeQuery(QueriesConsts::get_my_microblogs, dumper("id"));
    if(result.has_value() && result->size() > 0)
    {
        rw.headers().add<Http::Header::ContentType>(MIME(Application, Json));
        auto stream = rw.stream(Http::Code::Ok);

        json json_array = json::parse(result->at(0).at(0).as<std::string>());
        for(int i=0; i < json_array.size(); ++i)
        {
            stream << json_array[i].dump().c_str();
            std::cout << json_array[i].dump() << '\n';
        }
    
        stream.ends();
    }
    else 
        rw.send(Http::Code::Service_Unavailable, "No Available Connection");

}

void RequestHandler::GetPostsByBlog(const Rest::Request& rq, Http::ResponseWriter rw)
{
    json blog_id = json::parse(rq.body());
    auto dumper = id_dumper_t(blog_id);
    std::cout << "Fetching posts\n for id:" << dumper("id") << "\n";

    auto result = pool.executeQuery(QueriesConsts::get_posts_by_id, dumper("id"));
    if(result.has_value() && result->size() > 0)
    {
        rw.headers().add<Http::Header::ContentType>(MIME(Application, Json));
        auto stream = rw.stream(Http::Code::Ok);

        json json_array = json::parse(result->at(0).at(0).as<std::string>());
        for(int i=0; i < json_array.size(); ++i)
        {
            stream << json_array[i].dump().c_str();
            std::cout << json_array[i].dump() << '\n';
        }

        stream.ends();
    }
    else 
        rw.send(Http::Code::Service_Unavailable, "No Available Connection");

}


void RequestHandler::AddPost(const pr::Request& rq, ph::ResponseWriter rw)
{
    json post_data = json::parse(rq.body());
    auto str_dumper = string_dumper_t(post_data);
    auto iddumper = id_dumper_t(post_data);

    auto result = pool.executeQuery(QueriesConsts::add_post,iddumper("author_id"), str_dumper("title"), 
                                    str_dumper("content"), iddumper("blog_id"), str_dumper("tags"));

    if(result.has_value() && result->size() > 0)
        rw.send(Http::Code::Ok, "Post Added");
    else if(result.has_value())
        rw.send(Http::Code::Bad_Request, "Not Able To Add Post");
    else
        rw.send(Http::Code::Service_Unavailable, "No Available Connection");
}

void RequestHandler::AddComment(const pr::Request& rq, ph::ResponseWriter rw)
{
    json comment_data = json::parse(rq.body());
    auto str_dumper = string_dumper_t(comment_data);
    auto id_dumper = id_dumper_t(comment_data);

    auto result = pool.executeQuery(QueriesConsts::add_comment, id_dumper("post_id"), str_dumper("content"), id_dumper("author"));
    if(result.has_value() && result->size() > 0)
        rw.send(Http::Code::Ok, "Comment Added");
    else if(result.has_value())
        rw.send(Http::Code::Bad_Request, "Not Able To Add Comment");
    else
        rw.send(Http::Code::Service_Unavailable, "No Available Connection");
}

void RequestHandler::Upvote(const pr::Request& rq, ph::ResponseWriter rw)
{
    json upvote = json::parse(rq.body());
    auto dumper = id_dumper_t(upvote);

    auto result = pool.executeQuery(QueriesConsts::upvote, dumper("id_post"));
    if(result.has_value() && result->size() > 0)
        rw.send(Http::Code::Ok, "Upvoted");
    else if(result.has_value())
        rw.send(Http::Code::Bad_Request, "Could Not Upvote");
    else
        rw.send(Http::Code::Service_Unavailable, "No Available Connection");
}



void RequestHandler::setRoutes(Rest::Router& r)
{
    std::cout << "Routing setup...";
    Rest::Routes::Post(r, RoutingConsts::create_user_route, Rest::Routes::bind(&RequestHandler::CreateUser, this));
    Rest::Routes::Post(r, RoutingConsts::login_user, Rest::Routes::bind(&RequestHandler::LoginUser, this));
    Rest::Routes::Post(r, RoutingConsts::create_microblog, Rest::Routes::bind(&RequestHandler::CreateMicroBlog, this));
    Rest::Routes::Post(r, RoutingConsts::get_my_blogs, Rest::Routes::bind(&RequestHandler::GetMyBlogs, this));
    Rest::Routes::Post(r, RoutingConsts::add_post, Rest::Routes::bind(&RequestHandler::AddPost, this));
    Rest::Routes::Post(r, RoutingConsts::get_posts_by_id, Rest::Routes::bind(&RequestHandler::GetPostsByBlog, this));
    Rest::Routes::Post(r, RoutingConsts::add_comment, Rest::Routes::bind(&RequestHandler::AddComment, this));
    Rest::Routes::Post(r, RoutingConsts::upvote, Rest::Routes::bind(&RequestHandler::Upvote, this));

    std::cout << "Done\n";

}