#include "Gui.h"

void Gui::createButtons()
{
	QVBoxLayout* main_lay = new QVBoxLayout;
	this->setLayout(main_lay);

	QHBoxLayout* part1 = new QHBoxLayout;
	main_lay->addLayout(part1);
	part1->addWidget(rooms);

	QVBoxLayout* col = new QVBoxLayout;
	part1->addLayout(col);
	QFormLayout* form = new QFormLayout;
	col->addLayout(form);

	form->addRow(new QLabel{ "status" }, status);
	form->addRow(new QLabel{ "nota" }, nota);
	col->addStretch();

	main_lay->addWidget(exit);
	main_lay->addStretch();
}

void Gui::initSignals()
{
	QObject::connect(rooms, &QListWidget::itemSelectionChanged, [&]() {
		if (rooms->selectedItems().size() == 0)
		{
			status->setText("");
			nota->setText(" ");
		}
		else
		{
			Room r = serv.searchRoom(rooms->selectedItems().at(0)->data(Qt::UserRole).toInt());
			status->setText(QString::fromStdString(r.getStatus()));
			nota->setText(QString::fromStdString(std::to_string(r.getNota())));
		}
	});
	QObject::connect(exit, &QPushButton::pressed, [&]() {
		QMessageBox::information(nullptr, "exit", "Bye");
		this->setVisible(false);
	});
}

void Gui::loadAll(const vector<Room>& all)
{
	rooms->clear();
	for (auto& i : all) {
		QListWidgetItem* item = new QListWidgetItem{ QString::fromStdString(std::to_string(i.getNr()) + " - " + i.getStatus()) };
		if (i.getNota() < 8 && i.getNota() > 0)
			item->setBackgroundColor(Qt::red);
		else
			item->setBackgroundColor(Qt::blue);
		rooms->addItem(item);
		item->setData(Qt::UserRole, i.getNr());
	}
}
