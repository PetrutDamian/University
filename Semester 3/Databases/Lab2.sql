/*
1
Selecteaza toti clientii cu numarul total de comenzi mai mare decat 1
-having 
-group by 

select clientId,count(cId) as nrOrders from Cumparaturi group by clientId having count(cId)>1;

2
selecteaza produsele ce au fost cumparate in cantitati mari(>2)
-distinct 
-where 
-join

select distinct P.pId,P.nume,PM.pret,CumparaturiProdus.cantitate as C from
	CumparaturiProdus inner join ProduseMagazin as PM on CumparaturiProdus.pId= PM.pdId 
	inner join Produse as P on PM.pId = P.pId where CumparaturiProdus.cantitate>2


3
calculeaza suma totala a cumparaturilor fiecarui client si afiseaza descrescator
-group by
-order by
-join
	select C.cId, sum(PM.pret*CPR.cantitate) as suma from Clienti C 
		inner join Cumparaturi CP on CP.clientId=C.cId 
		inner join CumparaturiProdus CPR on CPR.cpId = CP.cId 
		inner join ProduseMagazin PM on PM.pdId=CPR.pId
		group by C.cId 
		order by suma desc
4
afiseaza clientii ce au cel putin doua carduri de fidelitate
-having
		select  C.cId as idCLient,count(CF.cId) as nrCarduri from Clienti C 
		inner join CarduriFidelitate  CF on CF.cId = C.cId group by C.cId having count(CF.cId)>1;

5

afiseaza magazinele care au angajati cu salariu mai mic decat 2200 lei
-distinct
-where

select distinct M.mId as id,M.nume as nume, AD.localitate as localitate, AD.strada as strada from Magazine M 
		inner join Angajati A on A.mId=M.mId 
		inner join Adrese AD on AD.adrId = M.adrId  where A.salariu<2200 ;


6
Afiseaza magazinele cu salariu mediu mai mare de 2000 care au nr de angajati > 1
-having
-group by
-join
select M.mId as id, AVG(A.salariu) as salariu, count(A.aId) as nrAngajati from Magazine M 
		inner join Angajati A on A.mId=M.mId group by M.mId 
		having AVG(A.salariu)>2000 and count(A.aId)>1


7 Afiseaza toate produsele din toate magazinele si promotii pentru ele daca exista
-left join

		select M.nume as numeMagazin, P.nume as numeProdus, PM.pret as pretProdus, Pr.dataStart as start,
		Pr.dataSfarsit as sfarsit, Pr.discount as discount from Magazine M 
		inner join ProduseMagazin PM on M.mId=PM.mId 
		inner join Produse P on PM.pId = P.pId 
		left join Promotii Pr on Pr.pId = P.pId and Pr.mId = M.mId

8.

Cumparaturile clientilor dupa data de 2017 2 februarie
-where
-5 inner joins
select C.cId as id, P.nume as numeProdus,Cpr.cantitate as cantitate, M.nume as numeMagazin from Clienti C 
		inner join Cumparaturi CP on C.cId=CP.cId
		inner join CumparaturiProdus Cpr on CPr.cpId = CP.cId
		inner join ProduseMagazin PM on PM.pdId = Cpr.pId
		inner join Produse P on P.pId = PM.pId
		inner join Magazine M on M.mId = PM.mId where CP.dataCump>'2017-02-02'

9.
Clientii care au cheltui mai mult de 50 de lei pe lactate 
-where 
-4 joins
select re.id,re.suma from (select C.cId as id, sum(PM.pret*CPR.cantitate) as suma from Clienti C 
		inner join Cumparaturi CP on CP.clientId = C.cId
		inner join CumparaturiProdus CPR on CPR.cpId = CP.cId
		inner join ProduseMagazin PM on PM.pdId = CPR.pId
		inner join Produse P on PM.pId=P.pId where P.tip='lactate'
		group by C.cId) as re where re.suma >50


10.
Afiseaza produsele de la magazinele care le vand cu pretul mai mic de 9 lei
-where
-join
select M.mId,PM.pret,P.nume,Prod.nume as producator from Magazine M
		inner join ProduseMagazin PM on M.mId=PM.mId
		inner join Produse P on PM.pId = P.pId
		inner join Producatori Prod on P.prodId = Prod.pId
		where PM.pret <9
11.
Pentru produsele care se afla in mai multe magazine afiseaza Magazinele unde se gaseste cel mai ieftin
- group by 
- having

select P.nume as numeProdus,result.id as idProdus,
result.price as pretProdus, result.price - Pr.discount*result.price as pretRedus,
M.mId as idMagazin, M.nume as numeMagazin,Pr.dataStart as startDate, Pr.dataSfarsit as endDate,
Pr.discount as discount
from (select PM.pId as id,min(PM.pret) as price from ProduseMagazin PM
		group by PM.pId
		having count(PM.pId)>1) as result
		inner join Produse P on P.pId = result.id
		inner join ProduseMagazin PM on PM.pId = P.pId
		inner join Magazine M on M.mId = PM.mId
		left join Promotii Pr on Pr.pId = P.pId and Pr.mId = M.mId
		where PM.pret = result.price 

		select * from Cumparaturi;
select * from CumparaturiProdus;
select * from ProduseMagazin;
select * from Produse;
select * from Angajati;
*/


select idProdus = result.id,P.nume as numeProdus,
result.price as pretProdus, M.mId as idMagazin, M.nume as numeMagazin,
Pr.dataStart as startDate, Pr.dataSfarsit as endDate,
Pr.discount as discount,result.price - Pr.discount*result.price/100 as pretRedus
from (select PM.pId as id,min(PM.pret) as price from ProduseMagazin PM
		group by PM.pId
		having count(PM.pId)>1) as result
		inner join Produse P on P.pId = result.id
		inner join ProduseMagazin PM on PM.pId = P.pId
		inner join Magazine M on M.mId = PM.mId
		left join Promotii Pr on Pr.prodId = P.pId and Pr.mId = M.mId
		where PM.pret = result.price 
		order by result.price asc


