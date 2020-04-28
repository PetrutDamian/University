use MagazineProduseEcologice;


--Creeaza tabela de versiuni
go
create procedure usp_createVersions
as begin
create table Versiuni(vId int);
end

--Schimba tipul coloane
go
create procedure usp_prod1
AS
BEGIN
alter table Adrese
alter column codPostal varchar(30)
print 'tipul coloanei este acum varchar'
END

--Undo schimba tipul coloanei
go
create procedure usp_reverse_prod1
as
begin
alter table Adrese
alter column codPostal int
print 'tipul coloanei este acum int'
end


--constrangere de valoare implicita
go
create procedure usp_prod2
as
if exists (select* from sys.default_constraints where name ='noCardDefault')
	print 'Constrangerea exista deja'
else
begin
alter table Promotii
add constraint noCardDefault default 0 for cardFidelitate
print 'Constrangerea a fost adaugata'
end


--undo constrangere de valoare implicita
go
create procedure usp_reverse_prod2
as
if not exists (select* from sys.default_constraints where name ='noCardDefault')
	print ' Constrangerea nu exista'
else
begin
alter table Promotii
drop constraint noCardDefault
print 'Constrangerea a fost stearsa'
end


--creeaza o tabela
go 
create procedure usp_prod3
as
IF EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES
           WHERE TABLE_NAME = 'Reclame')
print 'Tabela e deja creata'
else
begin
create table Reclame
(id int primary key,
titlu varchar(30),
idP int foreign key references Produse(pId)
)
print 'Tabela a fost creata'
end


--sterge tabela 
go
create procedure usp_reverse_prod3
as
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES
           WHERE TABLE_NAME = 'Reclame')
print 'Tabela nu exista'
else
begin
drop table Reclame
print 'Tabela a fost stearsa'
end


--adaugare camp nou
go
create procedure usp_prod4
as
IF EXISTS (SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS 
           WHERE COLUMN_NAME = 'cnp')
		   print 'Coloana exista deja'
else
begin
alter table Clienti add cnp varchar(13)
print 'Coloana adaugata'
end


--stergere camp
go
create procedure usp_reverse_prod4
as
if not exists(select COLUMN_NAME from INFORMATION_SCHEMA.COLUMNS WHERE COLUMN_NAME ='cnp')
	print 'Coloana nu exista'
else
begin
alter table Clienti drop column cnp
print 'Coloana a fost stearsa'
end


--adauga o constrangere de cheie straina
go
create procedure usp_prod5
as
if exists(select name from sys.foreign_keys where name='fk_mId')
	print 'Constrangerea exista deja'
else
begin
alter table Promotii
add constraint fk_mId foreign key(mId)references Magazine(mId)
print 'Constrangerea foreign key a fost adaugata'
end

--sterge o constrangere de cheie straina
go
create procedure usp_reverse_prod5
as
if not exists(select name from sys.foreign_keys where name='fk_mId')
print 'Constrangerea nu exista'
else
begin
alter table Promotii
drop constraint fk_mId
print ' Constrangerea a fost stearsa'
end


go
create procedure usp_go_to
@ver int
as
begin
	if @ver<0 or @ver>5 
	throw 50001,'Versiune invalida!',1
	declare @crt int;
	declare @exec varchar(100)
	select top 1 @crt=vId from Versiuni;

	while @crt<@ver
	begin
		set @crt = @crt+1
		set @exec = cast(@crt as varchar(1))
		set @exec = concat('usp_prod',@exec)
		exec @exec 
		update Versiuni set vId=@crt 
	end

	while @crt >@ver
	begin
		set @exec = CAST(@crt as varchar(1))
		set @exec = CONCAT('usp_reverse_prod',@exec)
		exec @exec
		set @crt = @crt -1
		update Versiuni set vId=@crt
	end	
end

exec usp_go_to 0