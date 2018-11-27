#include "RequestHandler.hpp"
#include "ConnectionPool.hpp"
#include "../Consts.hpp"
#include <nlohmann/json.hpp>
#include <type_traits>
#include <algorithm>
#include <utility>

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

void handle_result(const std::optional<pqxx::result>& result, const Rest::Request& rq, Http::ResponseWriter& rw)
{
    if(result.has_value() && result->size() > 0)
    {
        rw.headers().add<Http::Header::ContentType>(MIME(Application, Json));
        try
        {
            json json_array = json::parse(result->at(0).at(0).as<std::string>());
            rw.send(Http::Code::Ok, json_array.dump().c_str());
        } catch(json::exception& e) {
            std::cerr << e.what() << '\n';
            rw.send(Http::Code::Bad_Request, "Failed");        
        }
    }
    else if(result.has_value())
        rw.send(Http::Code::Bad_Request, "Failed");
    else
        rw.send(Http::Code::Service_Unavailable, "No Available Connectiom");
}




void RequestHandler::CreateUser(const Rest::Request& rq, Http::ResponseWriter rw)
{
    json login_data = json::parse(std::move(rq.body()));
    auto dumper = string_dumper_t(login_data);
    std::cout << QueriesConsts::find_user  <<'\n';
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

    json login = json::parse(std::move(rq.body()));
    auto dumper = string_dumper_t(login);
    std::cout << QueriesConsts::login_user  <<'\n';

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
    json microblog_data = json::parse(std::move(rq.body()));
    auto dumper = string_dumper_t(microblog_data);
    auto iddumper = id_dumper_t(microblog_data);
    std::cout << QueriesConsts::create_microblog  <<'\n';

    auto result = pool.executeQuery(QueriesConsts::create_microblog, dumper("title"), 
                                    iddumper("id"),dumper("tags"), dumper("private") != "private" );

    handle_result(result, rq, rw);

}

void RequestHandler::GetMyBlogs(const Rest::Request& rq, Http::ResponseWriter rw)
{
    auto id = json::parse(std::move(rq.body()));
    auto dumper = id_dumper_t(id);
    std::cout << QueriesConsts::get_my_microblogs  <<'\n';
    auto result = pool.executeQuery(QueriesConsts::get_my_microblogs, dumper("id"));
    handle_result(result, rq, rw);

}

void RequestHandler::GetPostsByBlog(const Rest::Request& rq, Http::ResponseWriter rw)
{
    json blog_id = json::parse(std::move(rq.body()));
    auto dumper = id_dumper_t(blog_id);
    std::cout << QueriesConsts::get_posts_by_id  <<'\n';

    auto result = pool.executeQuery(QueriesConsts::get_posts_by_id, dumper("id"));
    handle_result(result, rq, rw);
}


void RequestHandler::AddPost(const pr::Request& rq, ph::ResponseWriter rw)
{
    json post_data = json::parse(std::move(rq.body()));
    auto str_dumper = string_dumper_t(post_data);
    auto iddumper = id_dumper_t(post_data);
    std::cout << QueriesConsts::add_post <<'\n';

    auto result = pool.executeQuery(QueriesConsts::add_post, iddumper("author_id"), str_dumper("title"), 
                                    str_dumper("content"), iddumper("blog_id"), str_dumper("tags"));

    handle_result(result, rq, rw);
    
}

void RequestHandler::AddComment(const pr::Request& rq, ph::ResponseWriter rw)
{
    json comment_data = json::parse(std::move(rq.body()));
    auto str_dumper = string_dumper_t(comment_data);
    auto id_dumper = id_dumper_t(comment_data);
    std::cout << QueriesConsts::add_comment  <<'\n';

    auto result = pool.executeQuery(QueriesConsts::add_comment, id_dumper("post_id"), str_dumper("content"), id_dumper("author"));
    handle_result(result, rq, rw);
    
}

void RequestHandler::Upvote(const pr::Request& rq, ph::ResponseWriter rw)
{
    json upvote = json::parse(std::move(rq.body()));
    auto dumper = id_dumper_t(upvote);
    std::cout << QueriesConsts::upvote  <<'\n';

    auto result = pool.executeQuery(QueriesConsts::upvote, dumper("id_post"));
    handle_result(result, rq, rw);
    
}

void RequestHandler::Follow(const pr::Request& rq, ph::ResponseWriter rw)
{
    json upvote = json::parse(std::move(rq.body()));
    auto dumper = id_dumper_t(upvote);
    std::cout << QueriesConsts::follow  <<'\n';

    auto result = pool.executeQuery(QueriesConsts::follow, dumper("userid"), dumper("blogid"));

    handle_result(result, rq, rw);

}

void RequestHandler::Followed(const pr::Request& rq, ph::ResponseWriter rw)
{
    auto dumper = id_dumper_t(json::parse(std::move(rq.body())));
    std::cout << QueriesConsts::followed  <<'\n';

    auto result = pool.executeQuery(QueriesConsts::followed, dumper("userid"));
    handle_result(result, rq, rw);


}

void RequestHandler::GetFollowers(const pr::Request& rq, ph::ResponseWriter rw)
{
    auto dumper = id_dumper_t(json::parse(rq.body()));
    std::cout << QueriesConsts::get_followers  <<'\n';

    auto result = pool.executeQuery(QueriesConsts::get_followers, dumper("id"));

    handle_result(result, rq, rw);

}

void RequestHandler::Search(const pr::Request& rq, ph::ResponseWriter rw)
{
    json comment_data = json::parse(std::move(rq.body()));
    auto dumper = string_dumper_t(comment_data);
    auto tbl = dumper("tbl");
    tbl.erase(remove(tbl.begin(), tbl.end(), '\"'), tbl.end());
    auto key  = dumper("key");
    key.erase(remove( key.begin(), key.end(), '\"' ), key.end());
    auto query = ""s;
    if(tbl == "blog")
        query = QueriesConsts::search_blogs;
    else
        query = QueriesConsts::search_posts;

    std::cout << query  <<'\n';

    auto result = pool.executeQuery(query, std::move("%"s + key + "%"s));

    handle_result(result, rq, rw);

}

void RequestHandler::GetPost(const pr::Request& rq, ph::ResponseWriter rw)
{
    auto dumper = id_dumper_t(json::parse(std::move(rq.body())));
    std::cout << QueriesConsts::get_post  <<'\n';

    auto result = pool.executeQuery(QueriesConsts::get_post, dumper("id"));
    handle_result(result, rq, rw);


}

void RequestHandler::GetComments(const pr::Request& rq, ph::ResponseWriter rw)
{
    auto dumper = id_dumper_t(json::parse(std::move(rq.body())));
    std::cout << QueriesConsts::get_comments  <<'\n';

    auto result = pool.executeQuery(QueriesConsts::get_comments, dumper("id"));
    handle_result(result, rq, rw);


}

void RequestHandler::DeletePost(const pr::Request& rq, ph::ResponseWriter rw)
{
    json delet = json::parse(std::move(rq.body()));
    auto dumper = id_dumper_t(delet);
    std::cout << QueriesConsts::delete_post << '\n';
    auto r = pool.executeQuery(QueriesConsts::is_post_author, dumper("userid"));

    if(r.has_value() && r->size() > 0)
    {
        auto r1 = pool.executeQuery(QueriesConsts::delete_comments, dumper("postid"));
        auto r2 = pool.executeQuery(QueriesConsts::delete_post, dumper("postid"));
        if(r1.has_value() && r2.has_value() && r1->size() > 0 && r2->size() > 0)
            rw.send(Http::Code::Ok, "Removed");
        else
            rw.send(Http::Code::Bad_Request, "Failed To Remove");

    }
    else if(r.has_value())
        rw.send(Http::Code::Bad_Request, "Failed To Remove");
    else
        rw.send(Http::Code::Service_Unavailable, "No Available Connection");
    
}

void RequestHandler::DeleteBlog(const pr::Request& rq, ph::ResponseWriter rw)
{
    json delet = json::parse(std::move(rq.body()));
    std::cout << delet << '\n';
    auto dumper = id_dumper_t(delet);
    std::cout << QueriesConsts::delete_blog << '\n';
    auto r = pool.executeQuery(QueriesConsts::is_blog_author, dumper("userid"));
    if(r.has_value() && r->size() > 0)
    {
        auto r1 = pool.executeQuery(QueriesConsts::delete_posts_comments, dumper("blogid"));
        auto r2 = pool.executeQuery(QueriesConsts::delete_blogs_posts, dumper("blogid"));
        auto r3 = pool.executeQuery(QueriesConsts::delete_blog, dumper("blogid"));
        auto r4 = pool.executeQuery(QueriesConsts::delete_follows, dumper("blogid"));
        if(r1.has_value() && r2.has_value() && r3.has_value() && r4.has_value())
            rw.send(Http::Code::Ok, "Removed");
        else
            rw.send(Http::Code::Bad_Request, "Failed To Remove");

    }
    else if(r.has_value())
        rw.send(Http::Code::Bad_Request, "Failed To Remove");
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
    Rest::Routes::Post(r, RoutingConsts::follow, Rest::Routes::bind(&RequestHandler::Follow, this));
    Rest::Routes::Post(r, RoutingConsts::get_followed_blogs, Rest::Routes::bind(&RequestHandler::Followed, this));
    Rest::Routes::Post(r, RoutingConsts::get_followers, Rest::Routes::bind(&RequestHandler::GetFollowers, this));
    Rest::Routes::Post(r, RoutingConsts::search, Rest::Routes::bind(&RequestHandler::Search, this));
    Rest::Routes::Post(r, RoutingConsts::get_post, Rest::Routes::bind(&RequestHandler::GetPost, this));
    Rest::Routes::Post(r, RoutingConsts::get_comments, Rest::Routes::bind(&RequestHandler::GetComments, this));
    Rest::Routes::Post(r, RoutingConsts::delete_post, Rest::Routes::bind(&RequestHandler::DeletePost, this));
    Rest::Routes::Post(r, RoutingConsts::delete_blog, Rest::Routes::bind(&RequestHandler::DeleteBlog, this));









    std::cout << "Done\n";

}