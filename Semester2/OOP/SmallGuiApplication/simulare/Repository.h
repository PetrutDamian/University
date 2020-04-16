#pragma once
#include "Room.h"
#include <fstream>
#include <vector>
#include <algorithm>
class Repository {
	vector<Room> elems;


public:
	void add(Room& r);
	Room search(int nr);
	Repository(){}
	const vector<Room>& getAll() const;
};

class FileRepository:public Repository {
	string filename;
	void loadFromFile();
public:
	FileRepository(string _filename) :Repository(), filename{ _filename }{
		loadFromFile();
	}

};