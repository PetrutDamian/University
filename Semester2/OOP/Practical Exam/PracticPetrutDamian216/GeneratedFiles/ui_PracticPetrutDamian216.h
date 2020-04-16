/********************************************************************************
** Form generated from reading UI file 'PracticPetrutDamian216.ui'
**
** Created by: Qt User Interface Compiler version 5.12.3
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_PRACTICPETRUTDAMIAN216_H
#define UI_PRACTICPETRUTDAMIAN216_H

#include <QtCore/QVariant>
#include <QtWidgets/QApplication>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QMenuBar>
#include <QtWidgets/QStatusBar>
#include <QtWidgets/QToolBar>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_PracticPetrutDamian216Class
{
public:
    QMenuBar *menuBar;
    QToolBar *mainToolBar;
    QWidget *centralWidget;
    QStatusBar *statusBar;

    void setupUi(QMainWindow *PracticPetrutDamian216Class)
    {
        if (PracticPetrutDamian216Class->objectName().isEmpty())
            PracticPetrutDamian216Class->setObjectName(QString::fromUtf8("PracticPetrutDamian216Class"));
        PracticPetrutDamian216Class->resize(600, 400);
        menuBar = new QMenuBar(PracticPetrutDamian216Class);
        menuBar->setObjectName(QString::fromUtf8("menuBar"));
        PracticPetrutDamian216Class->setMenuBar(menuBar);
        mainToolBar = new QToolBar(PracticPetrutDamian216Class);
        mainToolBar->setObjectName(QString::fromUtf8("mainToolBar"));
        PracticPetrutDamian216Class->addToolBar(mainToolBar);
        centralWidget = new QWidget(PracticPetrutDamian216Class);
        centralWidget->setObjectName(QString::fromUtf8("centralWidget"));
        PracticPetrutDamian216Class->setCentralWidget(centralWidget);
        statusBar = new QStatusBar(PracticPetrutDamian216Class);
        statusBar->setObjectName(QString::fromUtf8("statusBar"));
        PracticPetrutDamian216Class->setStatusBar(statusBar);

        retranslateUi(PracticPetrutDamian216Class);

        QMetaObject::connectSlotsByName(PracticPetrutDamian216Class);
    } // setupUi

    void retranslateUi(QMainWindow *PracticPetrutDamian216Class)
    {
        PracticPetrutDamian216Class->setWindowTitle(QApplication::translate("PracticPetrutDamian216Class", "PracticPetrutDamian216", nullptr));
    } // retranslateUi

};

namespace Ui {
    class PracticPetrutDamian216Class: public Ui_PracticPetrutDamian216Class {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_PRACTICPETRUTDAMIAN216_H
