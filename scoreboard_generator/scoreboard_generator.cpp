// Needs to be compiled like this: g++ -Wall -I./mysql-connector-cpp/include -L./mysql-connector-cpp/lib -lmysqlcppconn scoreboard_generator.cpp
// Also, the mysql connector libraries MUST be in the same directory as the executable that gets generated

#include <iostream>

#include "mysql_connection.h"

#include "cppconn/driver.h"
#include "cppconn/exception.h"
#include "cppconn/resultset.h"
#include "cppconn/statement.h"

using namespace std;

int main() {
	cout << "";
	cout << "Welcome to the Scoreboard Generator!" << endl;

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
			cout << res->getString("name") << endl;
			// Access column data by id, 1 is the first column
			cout << res->getString(1) << endl;
		}
	}
	catch (sql::SQLException &e) {
		cout << "Database connection error." << endl;
	}

	return 0;
}