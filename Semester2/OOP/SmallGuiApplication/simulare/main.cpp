#include "simulare.h"
#include <QtWidgets/QApplication>
#include "Gui.h"
void testGetAll() {

	FileRepository repo{ "TestFile.csv" };
	Service serv{ repo };
	const vector<Room>& all{ repo.getAll() };
	assert(all.size() == 4);
	assert(all.at(0).getNr() == 100);
	assert(all.at(0).getNota() == 1);
	assert(all.at(0).getStatus() == "ocupat");

	assert(all.at(3).getNr() == 400);
	assert(all.at(3).getNota() == 10);
	assert(all.at(3).getStatus() == "liber");
}
void testSearch() {

	FileRepository repo{ "TestFile.csv" };
	Service serv{ repo };
	Room r = serv.searchRoom(100);
	assert(r.getNr() == 100);
	assert(r.getStatus() == "ocupat");
	assert(r.getNota() == 1);
	r = serv.searchRoom(200);
	assert(r.getNr() == 200);
	assert(r.getStatus() == "liber");
	assert(r.getNota() == 8);
}
void testAll() {

	testSearch();
	testGetAll();
}
int main(int argc, char *argv[])
{
	QApplication a(argc, argv);
	testAll();
	FileRepository repo{ "FileRepo.csv" };
	Service serv{ repo };
	Gui console{ serv };
	console.show();
	return a.exec();
}
