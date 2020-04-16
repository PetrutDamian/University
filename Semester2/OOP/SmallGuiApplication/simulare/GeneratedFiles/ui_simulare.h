/********************************************************************************
** Form generated from reading UI file 'simulare.ui'
**
** Created by: Qt User Interface Compiler version 5.12.3
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_SIMULARE_H
#define UI_SIMULARE_H

#include <QtCore/QVariant>
#include <QtWidgets/QApplication>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_simulareClass
{
public:

    void setupUi(QWidget *simulareClass)
    {
        if (simulareClass->objectName().isEmpty())
            simulareClass->setObjectName(QString::fromUtf8("simulareClass"));
        simulareClass->resize(600, 400);

        retranslateUi(simulareClass);

        QMetaObject::connectSlotsByName(simulareClass);
    } // setupUi

    void retranslateUi(QWidget *simulareClass)
    {
        simulareClass->setWindowTitle(QApplication::translate("simulareClass", "simulare", nullptr));
    } // retranslateUi

};

namespace Ui {
    class simulareClass: public Ui_simulareClass {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_SIMULARE_H
