#pragma once

#include <QtWidgets/QWidget>
#include "ui_simulare.h"

class simulare : public QWidget
{
	Q_OBJECT

public:
	simulare(QWidget *parent = Q_NULLPTR);

private:
	Ui::simulareClass ui;
};
