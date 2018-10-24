CC=g++
CFLAGS = -lpistache -lpthread -std=c++17 -lpqxx -lpq

Server: user.o RoutingConsts.hpp Server.cpp
	$(CC) Server.cpp -o Server $(CFLAGS)

user.o: classes/User.cpp classes/User.hpp
	$(CC) classes/User.cpp -c -std=c++17
	





