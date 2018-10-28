CREATE TABLE room (
	kdno int,
	kcno int,
	ccno int,
	kdname varchar(100),
	exptime varchar(100),
	papername varchar(100),
	primary key (kdno, kcno, ccno)
);

CREATE TABLE student(
	registno varchar(100),
	name varchar(100),
	kdno int,
	kcno int,
	ccno int,
	seat int,
	primary key(registno, kdno , kcno , ccno)
);