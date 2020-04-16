#pragma once
#include <qlabel.h>
#include <qformlayout.h>
#include <qstring.h>
#include <qmessagebox.h>
#include <QString>
#include <QTableWidget>
#include <qpushbutton.h>
#include <qlineedit.h>
#include <QVBoxLayout>
#include <qlistwidget.h>
#include <qwidget.h>
#include "Service.h"

class Gui :public QWidget {
	Service& serv;

	void createButtons();
	void initSignals();
	QListWidget* rooms = new QListWidget;
	QLineEdit* status = new QLineEdit;
	QLineEdit* nota = new QLineEdit;
	QPushButton* exit = new QPushButton{ "exit" };
	void loadAll( const vector<Room>& all);
public:
	Gui(Service& _serv):serv{ _serv } {
		createButtons();
		initSignals();
		loadAll(serv.getAll());
	}
};