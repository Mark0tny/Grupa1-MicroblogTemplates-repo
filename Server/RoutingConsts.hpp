#include <string>

namespace RoutingConsts
{
    auto create_user_route = "/email/:e/nick/:n/haslo/:h";
    auto login_user = "/email/:e/haslo/:h";
    auto update_user = "/email/:e/nick/:n/haslo/:h";
    auto follow_blog = "/userid/:uid/blogid/:bid";
    auto follow_user = "/follower/:followerid/followed/:followedid";
    auto search = "/searchfor/:sf/searchby/:sb/keyword/:k/orderby/:ob";
}