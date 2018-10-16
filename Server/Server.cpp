#include <pistache/endpoint.h>

using namespace Pistache;


struct HelloHandler : public Http::Handler
{
    HTTP_PROTOTYPE(HelloHandler)

    void onRequest(const Http::Request& rq, Http::ResponseWriter rw)
    {
        std::string resp(rq.resource());
        rw.send(Http::Code::Ok, "Hello world " + resp);

    }
};


int main()
{
    Http::listenAndServe<HelloHandler>("*:9080");
}
