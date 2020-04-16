#include "Room.h"

int Room::getNr()const
{
	/*
	Returneaza numarul camerei
	out: nr - int
	post: nr e numarul camerei
	*/
	return nr;
}

string Room::getStatus()const
{
	/*
	Returneaza statusul
	out: status - string
	post: status e liber/ocupat in functie de statusul camerei
	*/
	return status;
}

int Room::getNota() const
{
	/*
	Returneaza nota camerei
	out: nota
	post: nota - int, nota e nota camerei
	*/
	return nota;
}
