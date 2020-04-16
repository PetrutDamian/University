#pragma once
#include <string>
using namespace std;
class Room {
	int nr;
	string status;
	int nota;
public:
	Room(int _nr,string _status,int _nota):nr{_nr},status{_status},nota{_nota}{}
	int getNr() const;
	string getStatus() const ;
	int getNota() const;
};