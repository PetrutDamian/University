
--crearea tabelelor noi
--Tabela cu o cheie primara si fara chei straine
create table Producers
(id int primary key,
nume varchar(50) not null,
tara varchar(50)
)
--Tabela cu o cheie primara si o cheie straina - Medicamentes
create table Medicamente
(id int primary key,
p_id int foreign key references Producers(id),
nume varchar(50),
tip varchar(50),
descriere varchar(100),
lista_ingrediente varchar(100)
)
--tabela cu o cheie primara din 2 campuri
create table Farmacii
(localitate varchar(50),
strada varchar(50),
nume varchar(50),
rating int not null,
primary key(localitate,strada)
)



--View-uri :
-- un view ce contine o comanda select pe o tabela;
go
create view View_1
as
select * from Farmacii


--un view ce contine o comanda select pe 2 tabele
go 
create view View_2
as
select M.nume, M.lista_ingrediente, P.nume as [nume producator] from Medicamente M
	inner join Producers P on M.p_id = P.id


--un view ce contine o comanda select pe 2 tabele si avand o clauza group by
go 
create view View_3
as 
select M.nume, M.tip, count (M.id) as [numar de medicamente] from Medicamente M inner join Producers P on M.p_id = P.id
	group by M.nume,M.tip

--Adaugare de date in tabela Producers+Medicamente
go
create procedure test1
as begin
	declare @id int;
	declare @id_p int;
	declare @name varchar(50);
	declare @tip varchar(50);
	declare @lista_ingrediente varchar(100);
	declare @descriere varchar(100);
	declare @start datetime;
	declare @finish datetime;
	declare @tara varchar(50);

	set @id = 1;
	set @start = GETDATE();
	while @id<=1000
	begin
		set @name = 'name_'+cast(@id as varchar(8))
		set @tara = 'tara_' + cast (@id as varchar(8))
		insert into Producers values (@id,@name,@tara);
		set @id = @id + 1;
	end

	set @id = 1;
	while @id<=10000
	begin
		set @name = 'name_' + cast (@id as varchar(8));
		set @descriere = 'descriere_' + cast (@id as varchar (8));
		set @lista_ingrediente = 'lista_ingrediente_' + cast (@id as varchar(8));
		set @id_p = @id%100+1;
		set @tip = 'tip_' + cast (@id_p as varchar(8));
		set @id_p = @id%1000+1;
		insert into Medicamente values (@id,@id_p,@name,@tip,@descriere,@lista_ingrediente);
		set @id = @id + 1;
	end

	set @finish = GETDATE();
	declare @nr int;
	select @nr= count(TestRunid) from TestRuns;
	set @nr = @nr +1;
	INSERT INTO Tests(Name) values ('insert_Producers_Medicamente');
	INSERT INTO TestRuns(Description, StartAt, EndAt) VALUES ('insert_Producers_Medicamente',@START, @finish);
	INSERT INTO TestTables (TestID, TableID, NoOfRows, Position) values (@nr, 2, 10000, 2 ),(@nr, 1, 1000,1);
	INSERT INTO TestRunTables (TestRunID, TableID, StartAt, EndAt) values (@nr, 2, @start, @finish),(@nr, 1	, @start, @finish);

	select * from TestRunTables;

	PRINT 'Test1 executed in';
	PRINT DATEDIFF(second, @start,@finish);

end

--Adaugare de data in tabela Farmacii
go
create procedure test2
as begin
	declare @start DATETIME;
	declare @finish DATETIME;
	declare @id int;
	declare @nr int;
	
	select @id = 1;
	SET @start = GETDATE();
	
	while  @id <= 10000
	begin
		declare @nume varchar(50);
		declare @localitate varchar(50);
		declare @strada varchar(50);
		declare @aux int;
		declare @rating int;
		
		set @aux = @id%100;
		set @nume = 'nume_' + cast(@id as varchar(10));
		set @localitate = 'localitate_' + cast(@id as varchar(8));
		set @strada = 'strada_' + cast (@aux as varchar(8));
		set @rating = @id%10;
		insert into Farmacii values (@localitate,@strada,@nume,@rating);
		set @id = @id + 1 ;
	end

	SET @finish = GETDATE();

	select @nr = count(TestRunid) from TestRuns;
	set @nr = @nr +1;

	insert into tests(Name) values ('insert_Farmacii');
	insert into TestRuns(Description,StartAt,EndAt) values ('insert_Farmacii', @start, @finish);
	insert into TestTables(TestID, TableID,NoOfRows,Position) values (@nr, 3, 10000, 1);
	insert into TestRunTables(TestRunID, TableID, StartAt,EndAt) values (@nr, 3, @start,@finish);

	PRINT 'Test2 executed in';
	PRINT DATEDIFF(second, @finish, @start);

end;

go--Stergerea datelor din tabelele Medicamente si Producers
create procedure test_3
as
begin
	declare @start DATETIME;
	declare @finish DATETIME;
	SET @start = GETDATE();
	DELETE FROM Medicamente;
	DELETE FROM Producers;
	
	SET @finish = GETDATE();

	INSERT INTO TestRuns(Description, StartAt, EndAt) VALUES ('delete_Producers_Medicamente',@start,@finish );
	INSERT INTO TESTS(Name) values ('delete_Producers_Medicamente');
	declare @nr int;
	select @nr = count(testID) from tests;

	INSERT INTO TestRunTables(TestRunID, TableID, StartAt,EndAt) VALUES
						(@nr, 1, @start, @finish),(@nr, 2, @start, @finish);
	insert into TestTables values (@nr,2, 10000, 2), (@nr, 1, 1000, 1);

	PRINT 'Test3 executed in';
	PRINT DATEDIFF(second, @finish, @start);
end

go
create procedure test_4
as begin
	declare @start datetime;	
	declare @finish datetime;
	set @start = GETDATE();

	delete from Farmacii;
	set @finish = GETDATE();

	insert into Tests values ('delete_Farmacii');
	insert into TestRuns values ('delete_Farmacii', @start, @finish);

	declare @nr int;
	select @nr = count(testid) from tests;

	insert into TestRunTables(testrunid, TableID,StartAt,EndAt) values (@nr,3,@start,@finish);
	insert into TestTables(testid,TableID,NoOfRows,Position) values (@nr, 3, 10000, 1);

	PRINT 'Test4 executed in';
	PRINT DATEDIFF(second, @finish, @start);

end;

--test view pe tabela Producers

go
create procedure test_view_1
as
begin
declare @start datetime;
	declare @finish datetime;
	set @start = GETDATE();
	select * from View_1;
	set @finish = GETDATE();

	PRINT 'Test_view_1 executed in';
	PRINT DATEDIFF(second, @finish, @start);

	declare @nr int;
	insert into Tests values ('test_view_1');
	insert into TestRuns(Description,StartAt,EndAt) values ('TestView_1',@start,@finish);

	select @nr = count(testid) from Tests;
	insert into TestViews values (@nr,1);
	insert into TestRunViews values (@nr,1,@start,@finish);

end

go
create procedure test_view_2
as
begin
declare @start datetime;
	declare @finish datetime;
	set @start = GETDATE();
	select * from View_2;
	set @finish = GETDATE();

	PRINT 'Test_view_1 executed in';
	PRINT DATEDIFF(second, @finish, @start);

	declare @nr int;
	insert into Tests values ('test_view_2');
	insert into TestRuns(Description,StartAt,EndAt) values ('TestView_2',@start,@finish);

	select @nr = count(testid) from Tests;
	insert into TestViews values (@nr,2);
	insert into TestRunViews values (@nr,2,@start,@finish);

end

go
create procedure test_view_3
as
begin
declare @start datetime;
	declare @finish datetime;
	set @start = GETDATE();
	select * from View_3;
	set @finish = GETDATE();

	PRINT 'Test_view_3 executed in';
	PRINT DATEDIFF(second, @finish, @start);

	declare @nr int;
	insert into Tests values ('test_view_3');
	insert into TestRuns(Description,StartAt,EndAt) values ('TestView_3',@start,@finish);

	select @nr = count(testid) from Tests;
	insert into TestViews values (@nr,3);
	insert into TestRunViews values (@nr,3,@start,@finish);

end

--Popularea tabelelor
exec test1;
exec test2;

--testarea view-urilor
exec test_view_1;
exec test_view_2;
exec test_view_3;
--stergerea din tabele
exec test_3;
exec test_4;


insert into tables values('Producers'),('Medicamente'),('Farmacii');
insert into Views values ('View_1'),('View_2'),('View_3');

select * from tests;
select * from TestRuns;
select * from TestRunTables;
select * from TestTables;
select * from TestRunViews;
select * from TestViews;

select * from Medicamente ;
select * from Farmacii;
select * from Producers;

