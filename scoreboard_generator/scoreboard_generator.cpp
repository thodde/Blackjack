// Needs to be compiled like this: g++ -Wall -I./mysql-connector-cpp/include -L./mysql-connector-cpp/lib -lmysqlcppconn scoreboard_generator.cpp
// Also, the mysql connector libraries MUST be in the same directory as the executable that gets generated

#include <iostream>
#include <vector>
#include <string>
#include <algorithm>

#include "mysql_connection.h"

#include "cppconn/driver.h"
#include "cppconn/exception.h"
#include "cppconn/resultset.h"
#include "cppconn/statement.h"

using namespace std;

// simple user class for storing the info retrieved from the database
class UserInfo {
	string name;
	int wins;
	int losses;
	int id;
public:
	void set_info(string, int, int, int);
	UserInfo get_info();
	int get_wins();
	string get_name();
};

// return the amount of wins the user has
int UserInfo::get_wins() {
	return this->wins;
}

// return the users name
string UserInfo::get_name() {
	return this->name;
}

// define a way to sort which user has the larger number of wins
bool sortByWins(UserInfo &A, UserInfo &B) {
    return (A.get_wins() > B.get_wins());
}

// basically a lazy constructor
void UserInfo::set_info(string name, int id, int wins, int losses) {
	this->name = name;
	this->id = id;
	this->wins = wins;
	this->losses = losses;
}

int main() {
	cout << "";
	cout << "Welcome to the Scoreboard Generator!" << endl;

	// data structure for storing user info
	vector<UserInfo> scoreboard;

	try {
		// configure the database connection
	    sql::Driver *driver;
	    sql::Connection *con;
		sql::Statement *stmt;
	  	sql::ResultSet *res;

		// Create a connection
		driver = get_driver_instance();
		con = driver->connect("tcp://192.168.64.2:3306", "java", "password");
		// Connect to the MySQL test database
		con->setSchema("scoreboard");
		stmt = con->createStatement();
		res = stmt->executeQuery("SELECT * FROM scoreboard");

		while (res->next()) {
			// Access column data by alias or column name
			string name = res->getString("name");
			// Access column data by id, 1 is the first column
			int id = res->getInt(1);
			int wins = res->getInt("wins");
			int losses = res->getInt("losses");

			// create a new user from the database and add them to the vector
			UserInfo current_user;
			current_user.set_info(name, id, wins, losses);
			scoreboard.push_back(current_user);
		}
	}
	catch (sql::SQLException &e) {
		cout << "Database connection error." << endl;
	}

	// sort the scoreboard vector from start to finish using wins as the sort operator
	sort(scoreboard.begin(), scoreboard.end(), sortByWins);

	// display the sorted vector
	for (int i = 0; i < scoreboard.size(); i++) {
    	cout << "Wins: " << scoreboard[i].get_wins() << " Name: " << scoreboard[i].get_name() << endl;
	}

	return 0;
}