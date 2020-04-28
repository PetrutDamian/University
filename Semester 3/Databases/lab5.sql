---PROCEDURA DE STERGERE 
--Parametrii de intrare: cheia si tabela
--Parametrii de iesire: cod de incheiere, -1 daca nu a functionat corect, 0 altfel

alter procedure usp_crud_delete
(@key int,@table varchar(30))
as
begin
declare @id_name varchar(30);
set @id_name = 'default';
IF @table='Producatori' or @table='Produse'
begin
	set @id_name = 'pId'
	if not exists (select pId from Producatori where pId =@key) and not exists (select pId from Produse where pId=@key) 
		throw 50003,'Nu exista o entitate cu id-ul dat',1;
end
IF @table='Clienti'
begin
	if not exists (select cId from Clienti where cId=@key) 
		throw 50003,'Nu exista o entitate cu id-ul dat',1;
	set @id_name = 'cId'
end
IF @table='ProduseMagazin'
begin
	set @id_name = 'pdId'
	if not exists (select pdId from ProduseMagazin where pId=@key)
		throw 50003,'Nu exista o entitate cu id-ul dat',1;
end
if @table='Magazine'
begin
	set @id_name ='mId'
	if not exists (select mId from Magazine where mid=@key)
		throw 50003,'Nu exista o entitate cu id-ul dat',1;
end
IF @id_name = 'default'
	throw 50003, 'Ati ales o tabela invalida!',1;
ELSE
if @table='Magazin'
	begin
	delete from ProduseMagazin where ProduseMagazin.mId = @key
	delete from Angajati where Angajati.mId = @key
	end
if @table = 'Produse'
	begin
	delete from ProduseMagazin where ProduseMagazin.pId=@key
	end
if @table = 'Producatori'
	begin
	delete from Produse where Produse.prodId = @key
	end
EXEC('delete from ' + @table +' where '+@id_name+'='+ @key );
exec('select * from ' + @table);
end

--PROCEDURA DE CITIRE
--Parametrii de intrare: cheia si tabela
--Parametrii de iesire: cod de incheiere, -1 daca nu a functionat corect, 0 altfel
go
alter procedure usp_crud_read
(@key int,@table varchar(30))
as
begin
declare @id_name varchar(30);
set @id_name = 'default';
IF @table='Producatori' or @table = 'Produse'
set @id_name = 'pId'
IF @table='Clienti'
set @id_name = 'cId'
IF @table='ProduseMagazin'
set @id_name = 'pdId'
if @table = 'Magazine'
set @id_name = 'mId'
IF @id_name = 'default'
	throw 50003,'Ati ales o tabela invalida',1;
else
EXEC('select * from ' + @table +' where '+@id_name+'='+ @key );
end

--PROCEDURA DE ADAUGARE
--Parametrii de intrare: tabela, campurile(string separat prin , )

go 
alter procedure usp_validate
(@input varchar(100),@type varchar(100))
as
begin
declare @count int;
declare @arg varchar(30);
declare @expected_count int;
declare @aux int ;
set @expected_count=-1;
select @count = count(*) from string_split(@input,',');
if @count=0
	throw 50002,'Numar de argumente necorespunzator!',1;
else
declare cursor_fields CURSOR FOR SELECT value from string_split(@input,',');
open cursor_fields;
fetch next from cursor_fields into @arg;
if @arg='Producatori' 
set @expected_count = 3;
if @arg = 'Produse'
set @expected_count = 5;
IF @arg='Clienti'
set @expected_count = 5;
IF @arg='ProduseMagazin'
set @expected_count = 4;
if @arg='Magazine'
set @expected_count = 3;
declare @table varchar(30);
set @table = @arg;
if @count != ( 1 + @expected_count)
	throw 50002,'Numar de argumente necorespunzator!',1;

while @expected_count>0
begin
	fetch next from cursor_fields into @arg;
	if @arg =''
		throw 50002,'Argument invalid!String vid!',1;
	if @table = 'Producatori' and (@count-@expected_count)=1
	begin 
		begin try
			set @aux = cast(@arg as int);
		end try
		begin catch
			throw 50002,'Id-ul trebuie sa fie numeric',1;
		end catch
		if @aux<1
			throw 50002,'Id-ul trebuie sa fie pozitiv',1;
		if @type ='add' and exists(select * from Producatori where pId=@aux)
			throw 50002,'Deja exista o inregistrare  cu acest id!',1;
		if @type = 'update' and not exists(select * from Producatori where pId=@aux)
			throw 50002,'Nu exista o inregistrare cu id-ul specificat!',1;
	end
	if @table = 'Produse'
		begin
			if (@count-@expected_count)=1
				begin
				begin try
				set @aux = cast(@arg as int);
				end try
				begin catch
				throw 50002,'Id-ul trebuie sa fie numeric',1;
				end catch
				if @aux<1
					throw 50002,'Id-ul trebuie sa fie pozitiv',1;
				if @type ='add' and exists(select * from Produse where pId=@aux)
					throw 50002,'Deja exista o inregistrare  cu acest id!',1;
				if @type = 'update' and not exists(select * from Produse where pId=@aux)
					throw 50002,'Nu exista o inregistrare cu id-ul specificat!',1;
				end
			if(@count - @expected_count)=2
					begin
					begin try
					set @aux = cast(@arg as int);
					end try
					begin catch
					throw 50002,'Id-ul producatorului trebuie sa fie numeric',1;
					end catch
					if not exists (select * from Producatori where pId=@aux)
						throw 50002,'Nu exista un producator cu id-ul dat',1;
					end
		end
	if @table = 'Clienti'
		begin
		if (@count - @expected_count)=1
			begin
			begin try
			set @aux = cast(@arg as int);
			end try
			begin catch
			throw 50002,'Id-ul clientului trebuie sa fie numeric',1;
			end catch
			if @aux<1
				throw 50002,'Id-ul trebuie sa fie pozitiv!',1;
			if @type ='add' and exists(select * from Clienti where cId=@aux)
				throw 50002,'Deja exista o inregistrare  cu acest id!',1;
			if @type = 'update' and not exists(select * from Clienti where cId=@aux)
				throw 50002,'Nu exista o inregistrare cu id-ul specificat!',1;
			end
		if(@count-@expected_count)=5
			begin
			begin try
			declare @aux_data date;
			set @aux_data = cast(@arg as date);
			end try
			begin catch
				throw 50002,'Data nasterii are un format invalid!',1;
			end catch
			end
		end
	if @table = 'ProduseMagazin'
		begin
		if (@count-@expected_count)=1
			begin
			begin try
			set @aux  = cast(@arg as int);
			end try
			begin catch
			throw 50002,'Id-ul trebuie sa fie numeric!',1;
			end catch
			if (@aux<1)
				throw 50002,'Id-ul trebuie sa fie strict pozitiv!',1;
			if @type ='add' and exists(select * from ProduseMagazin where pdId=@aux)
				throw 50002,'Deja exista o inregistrare  cu acest id!',1;
			if @type = 'update' and not exists(select * from ProduseMagazin where pdId=@aux)
				throw 50002,'Nu exista o inregistrare cu id-ul specificat!',1;
			end
		if(@count-@expected_count=2)
			begin
			begin try
			set @aux = cast(@arg as int);
			end try
			begin catch
			throw 50002,'Id-ul produsului trebuie sa fie numeric!',1;
			end catch
			if not exists (select * from Produse where pId = @aux)
				throw 50002,'Nu exista un produs cu id-ul dat!',1;
			end
		if(@count-@expected_count=3)
			begin
			begin try
			set @aux  = cast(@arg as int);
			end try
			begin catch
			throw 50002,'Id-ul magazinului trebuie sa fie numeric!',1;
			end catch
			if not exists (select * from Magazine where mId = @aux)
				throw 50002,'Nu exista un magazin cu id-ul dat!',1;
			end
		if(@count-@expected_count=4)
			begin
			begin try
			declare @aux_float float;
			set @aux_float  = cast(@arg as float);
			end try
			begin catch
				throw 50002,'Pretul produsului trebuie sa fie numeric!',1;
			end catch
			if @aux_float <=0
				throw 50002,'Pretul trebuie sa fie strict pozitiv!',1;
			end
		end
	if (@table = 'Magazine')
		begin
		if (@count - @expected_count) =1
			begin
			begin try
				set @aux  = cast(@arg as int);
			end try
			begin catch
				throw 50002,'Id-ul magazinului trebuie sa fie numeric',1;
			end catch
			if @type ='add' and exists(select * from Magazine where mId=@aux)
				throw 50002,'Deja exista o inregistrare  cu acest id!',1;
			if @type = 'update' and not exists(select * from Magazine where mId=@aux)
				throw 50002,'Nu exista o inregistrare cu id-ul specificat!',1;
			end
		end
set @expected_count = @expected_count - 1;
end
close cursor_fields;
deallocate cursor_fields;
end


go
alter procedure usp_crud_add
(@input varchar(100),@exit int output)
as
begin
declare @count int;
declare @poz int;
declare @sql varchar(150);
declare @table varchar(50);
set @poz = 1;
set @exit = 0;
set @sql = 'insert into ';
begin try
	exec usp_validate @input,'add';
	select @count=count(*) from string_split(@input,',');
	declare cursor_fields cursor for select value from string_split(@input,',');
	open cursor_fields;
	fetch next from cursor_fields into @table;
	set @sql = concat(@sql,@table,' values (');
	while @poz<@count
	begin
		declare @aux varchar(50);
		fetch next from cursor_fields into @aux;
		if ((@table = 'Magazine' and @poz=2) or (@table = 'Producatori' and @poz!=1) or (@table ='Produse' and @poz>2) or (@table = 'Clienti' and @poz!=1))
			set @aux = concat('''',@aux,'''');
		set @sql = concat(@sql,@aux);
		if (@poz+1 < @count)
			set @sql = concat(@sql,',');
		set @poz = @poz + 1;
	end
	close cursor_fields;
	deallocate cursor_fields;
	set @sql = concat(@sql,')');
	exec (@sql);
	print 'Adaugarea a fost incheiata'
	exec('select * from ' + @table);
	set @exit = 0;

end try
begin catch
	print error_message();
	set @exit = -1;
end catch
end




go
alter procedure usp_crud_update
(@input varchar(100),@exit int output)
as
begin
declare @count int;
declare @poz int;
declare @sql varchar(150);
declare @table varchar(50);
set @poz = 1;
set @exit = 0;
set @sql = 'update ';
begin try
	exec usp_validate @input,'update';
	select @count=count(*) from string_split(@input,',');
	declare cursor_fields cursor for select value from string_split(@input,',');
	open cursor_fields;
	fetch next from cursor_fields into @table;
	set @sql = concat(@sql,@table,' set ');
	declare @id varchar(50);
	fetch next from cursor_fields into @id;
	set @poz = 2;
	while @poz<@count
	begin
		declare @column varchar(50);
		if(@table = 'Producatori')
			begin
			if(@poz=2)
				set @column = 'nume'
			if (@poz=3)
				set @column = 'reputatie'
			end
		if(@table = 'Produse')
			begin
			if(@poz=2)
				set @column = 'prodId'
			if(@poz=3)
				set @column = 'nume'
			if (@poz=4)
				set @column = 'tip'
			if (@poz=5)
				set @column = 'listaIngrediente'
			end
		if(@table = 'Clienti')
			begin
			if(@poz=2)
				set @column = 'nume'
			if(@poz=3)
				set @column = 'prenume'
			if (@poz=4)
				set @column = 'telefon'
			if (@poz=5)
				set @column = 'data_nasterii'
			end
		if(@table = 'ProduseMagazin')
			begin
			if(@poz=2)
				set @column = 'pId'
			if(@poz=3)
				set @column = 'mId'
			if(@poz=4)
				set @column = 'pret'
			end
		if(@table = 'Magazine')
			begin
			if(@poz=2)
				set @column = 'mId'
			if(@poz=3)
				set @column = 'nume'
			if(@poz=4)
				set @column = 'adrId'
			end
		declare @aux varchar(50);
		fetch next from cursor_fields into @aux;
		if ((@table = 'Magazine' and @poz=1)or (@table = 'Producatori' and @poz!=1) or (@table ='Produse' and @poz>2) or (@table = 'Clienti' and @poz!=1))
			set @aux = concat('''',@aux,'''');
		set @sql = concat(@sql,@column,'=',@aux);
		if (@poz+1 < @count)
			set @sql = concat(@sql,',');
		set @poz = @poz + 1;
	end
	close cursor_fields;
	deallocate cursor_fields;
	set @sql = concat(@sql,' where ');
	if(@table='Produse' or @table = 'Producatori')
		set @sql = concat(@sql,'pId=',@id);
	if(@table='Clienti')
		set @sql = concat(@sql,'cId=',@id);
	if(@table='ProduseMagazin')
		set @sql = concat(@sql,'pdId=',@id);
	if(@table = 'Magazine')
		set @sql = concat(@sql,'mId=',@id);
	exec (@sql);
	print 'Modificarea a fost incheiata'
	exec('select * from ' + @table);
	set @exit = 0;

end try
begin catch
	print error_message();
	set @exit = -1;
end catch
end


select * from Clienti
select * from Produse
select * from Producatori
select * from Magazine
select * from ProduseMagazin


--Adaugare
begin
declare @exit as int;
set @exit=1
exec usp_crud_add 'Clienti,99,nume,prenume,030214,2008-10-10',@exit=@exit output
print 'cod de exit:' +cast(@exit as varchar(2))
end	
select * from Produse
--Update
begin
declare @exit as int;
set @exit=1
exec usp_crud_update 'Clienti,99,dsadsqd,rwqrwq,fewf,2009-10-02',@exit=@exit output
print 'cod de exit:' +cast(@exit as varchar(2))
end
--Delete
begin
exec usp_crud_delete 99,'Clienti'
end

--Read
begin
exec usp_crud_read 99,'Clienti'
end


--CREARE DE VIEW-URI
go
alter view view_index_1 as select P.nume as produs,P.tip as tip,P.listaIngrediente as ingrediente,Pr.nume
	from Produse P inner join Producatori Pr on P.prodId=Pr.pId 

go
alter view view_index_2 as select Pr.nume as produs,M.nume as magazin, PM.pret as pret from
		Produse Pr inner join ProduseMagazin PM on Pr.pId=PM.pId
					inner join Magazine M on M.mId=PM.mId
go 
alter view view_index_3 as select top 7 nume,prenume,telefon,data_nasterii from Clienti order by data_nasterii


select * from view_index_1
select * from view_index_2
select * from view_index_3

drop index index_Producatori_1 on Producatori
drop index index_Produse_1 on Produse

create nonclustered index index_Producatori_1 on Producatori(pId,nume) 
create nonclustered index index_Produse_1 on Produse(nume,tip) include (pId,prodId,listaIngrediente)
create nonclustered index index_Produse_Magazin on ProduseMagazin(pId,mId) include (pret)
create nonclustered index index_magazine on Magazine(mId,nume)
create nonclustered index index_Clienti on Clienti(data_nasterii asc) include (nume,prenume,telefon)
