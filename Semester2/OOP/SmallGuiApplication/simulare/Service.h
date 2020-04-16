#pragma once
#include "Repository.h"
class Service {
	Repository& repo;
public:
	Service(Repository& _repo):repo{_repo}{}
	const vector<Room>& getAll() const;
	Room searchRoom(int nr);
};