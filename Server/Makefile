CC=g++-8
CFLAGS = -lpistache -lpthread
PFLAGS = -lpqxx -lpq

Server: server.o pool.o user.o requestHandler.o
	$(CC) server.o pool.o user.o requestHandler.o $(PFLAGS) $(CFLAGS) -std=c++17 -o Server


server.o:  user.o requestHandler.o pool.o Consts.hpp Server.cpp 
	$(CC) Server.cpp -c -o server.o -std=c++17

requestHandler.o: classes/RequestHandler.cpp classes/RequestHandler.hpp pool.o classes/ConnectionPool.hpp Consts.hpp
	$(CC) classes/RequestHandler.cpp -c -std=c++17 -o requestHandler.o 

user.o: classes/User.cpp classes/User.hpp
	$(CC) classes/User.cpp -c -std=c++17 -o user.o

pool.o: classes/ConnectionPool.cpp classes/ConnectionPool.hpp Consts.hpp
	$(CC) classes/ConnectionPool.cpp -c -o pool.o -std=c++17

.Phony : clean
clean:
		rm *.o




