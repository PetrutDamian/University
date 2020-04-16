#include "Service.h"

const vector<Room>& Service::getAll()const
{
	/*
	Returneaza toate elementele
	out: all 
	post: all - vector cu toate camerele
	*/
	return repo.getAll();
}

Room  Service::searchRoom(int nr)
{
	/*
	Cauta o camera dupa numar
	pre: room nr exists
	in:nr - int
	out: r
	post: r - room, r are numarul nr
	*/
	return repo.search(nr);
}
