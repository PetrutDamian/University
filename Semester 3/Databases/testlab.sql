create table Categorii
(id int primary key,
 nume varchar(50),
 descriere varchar(50)
 )

 create table Campionate
 (
	id int primary key,
	id_categorie int foreign key references Categorii(id)
 )
 create table Organizatii
 (
  id int primary key,
  nume varchar(50),
  infiintare date,
  descriere varchar(50)

 )

 create table Jucatori
 (
 id int primary key,
 nume varchar(50),
 prenume varchar(50),
 data_nasterii date,
 id_org int foreign key references Organizatii(id),
 )

create table JucatoriCampionat
 (
  id int primary key identity,
  id_campionat int foreign key references Campionate(id),
  id_jucator int foreign key references Jucatori(id),
  nr_medalii int
 )

 --Procedura punctul 2
 go
alter procedure usp_add
 (
 @id_jucator int,
 @id_campionat int,
 @nr_medalii int)
 as 
 begin
 if not exists (select * from JucatoriCampionat JC where JC.id_campionat=@id_campionat and JC.id_jucator=@id_jucator)
 insert into JucatoriCampionat values (@id_campionat,@id_jucator,@nr_medalii)
 else
 update JucatoriCampionat set nr_medalii=@nr_medalii where id_campionat=@id_campionat and id_jucator=@id_jucator
 select * from JucatoriCampionat
 end

 exec usp_add 7,4,1


 --View punctul 3


 go
 create view vw_fara_participare as
 select nume,prenume from Jucatori J where J.id not in (select distinct JC.id_jucator from JucatoriCampionat JC)


 select * from vw_fara_participare
  select * from Jucatori

 -- Functie punctul 4 ------------------------
 go
alter function fun4(@medalii int, @campionate int)
 returns @Jucatori Table (nume varchar(50),prenume varchar(50))
 as
 begin
  insert into @Jucatori (nume,prenume) select J.nume,J.prenume from Jucatori J 
  inner join (select JC.id_jucator as id_jucator from JucatoriCampionat JC
				group by JC.id_jucator
				having count(JC.id_jucator)<@campionate and sum(JC.nr_medalii)>=@medalii) X on X.id_jucator=J.id
 return;
 end;

select * from fun4(4,6)

 select J.nume,J.prenume,medalii,campionate from Jucatori J inner join (select JC.id_jucator as id_jucator, sum(JC.nr_medalii) as medalii,count(JC.id_jucator) as campionate
 from JucatoriCampionat JC
 group by JC.id_jucator
 having count(JC.id_jucator)<100 and sum(JC.nr_medalii)>=1) X on X.id_jucator=J.id


select * from Categorii
select * from Organizatii
select * from Jucatori
select * from Campionate
select * from JucatoriCampionat


	  /*
 insert into Categorii values (1,'clasa grea','doar jucatori cu masa corporala >=100kg')
 insert into Categorii values (2,'clasa medie','doar jucatori cu masa corporala >=90kg si <100kg')
 insert into Categorii values (3,'clasa mica','doar jucatori cu masa corporala >=80kg si <90kg')
  insert into Organizatii values (1,'The Hotkey Sports Team','1/10/2015','doar jucatori de elita')
  insert into Organizatii values (2,'The Hotkey Sports Amateur Team','1/10/2016','jucatori de toate nivelurile')
  insert into Jucatori values (1,'Menihard','Cristi','1/1/1999',1)
  insert into Jucatori values (2,'Haja','Robert','1/1/1998',2)
 insert into Jucatori values (3,'Pintea','Darius','1/2/1998',2)
  insert into Jucatori values (4,'Silvia','Maria','10/2/2000',1)
  insert into Jucatori values (5,'Prendea','George','11/11/1999',1)
  insert into Jucatori values (6,'Petrut','Damian','3/3/1997',2)
  insert into Jucatori values (7,'Matean','Daniel','1/5/1999',1)
   insert into Campionate values (1,1),(2,2),(3,3),(4,1),(5,1),(6,2)
 */
 
 

