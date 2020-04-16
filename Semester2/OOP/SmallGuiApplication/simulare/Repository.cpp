#include "Repository.h"

void FileRepository::loadFromFile()
{
	/*
	Incarca toate elementele din fisier
	post: elementele din fisier sunt incarcate in memorie
	*/
	string nr;
	string status;
	string nota;
	ifstream fin(filename);
	while (getline(fin, nr, ','))
	{
		getline(fin, status, ',');
		getline(fin, nota);
		int number = stoi(nr);
		int nota_nr = stoi(nota);
		Room r{ number,status,nota_nr };
		Repository::add(r);
	}
	fin.close();
}

void Repository::add(Room & r)
{
	/*
	Adauga camera
	pre: r - room
	post: r se afla in lista
	*/
	elems.push_back(r);
}

Room  Repository::search(int nr)
{
	/*
	Cauta o camera dupa numar
	pre: room nr exists
	in:nr - int 
	out: r 
	post: r - room, r are numarul nr
	*/
	auto it = find_if(elems.begin(), elems.end(), [&nr](Room& r) {return r.getNr() == nr; });
	return *it;
}

const vector<Room>& Repository::getAll() const
{
	/*
	Returneaza toate elementele
	out: elems, contine toate elementele curente
	*/
	return elems;
}
