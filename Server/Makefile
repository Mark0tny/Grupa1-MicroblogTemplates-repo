CC=g++
CFLAGS = -lpistache -lpthread -std=c++17 -lpqxx -lpq

Server:  user.o requestHandler.o Consts.hpp Server.cpp
	$(CC) Server.cpp -o Server requestHandler.o user.o $(CFLAGS)

requestHandler.o: classes/RequestHandler.cpp classes/RequestHandler.hpp
	$(CC) classes/RequestHandler.cpp -c -std=c++17 -o requestHandler.o

user.o: classes/User.cpp classes/User.hpp
	$(CC) classes/User.cpp -c -std=c++17 -o user.o

.Phony : clean
clean:
		rm *.o




