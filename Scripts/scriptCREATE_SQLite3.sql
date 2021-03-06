DROP TABLE IPCore;
DROP TABLE IPManager;
DROP TABLE Author;
DROP TABLE FPGAConfiguration;
DROP TABLE MappedIP;
PRAGMA foreign_keys = ON;
PRAGMA foreign_keys ;

CREATE TABLE IPCore(idIP 			CHAR(30) PRIMARY KEY,	
					name		CHAR(25),
					hdlSourcePath 	CHAR(100),
					description		CHAR(2000),
					LUTs			INTEGER CHECK(LUTs>=0),
					FFs				INTEGER CHECK(FFs>=0),
					latency 		DOUBLE CHECK(latency>=0.0),
					nMemories 		SMALLINT CHECK(nMemories>=0),
					powerConsuption DOUBLE CHECK(powerConsuption>=0.0),
					maxClockFrequency DOUBLE CHECK(maxClockFrequency>=0.0),
					driverPath 		CHAR(100),
					contactPoint	CHAR(30),
					FOREIGN KEY(contactPoint)
									REFERENCES Author(idAuthor)
									ON DELETE SET NULL
									ON UPDATE CASCADE);

CREATE TABLE IPManager(idIP 			CHAR(30) PRIMARY KEY,
					   name				CHAR(25),
					   hdlSourcePath 	CHAR(100),
					   description		CHAR(2000),
					   LUTs		    	INTEGER CHECK(LUTs>=0),
					   FFs				INTEGER CHECK(FFs>=0),
					   latency 			DOUBLE CHECK(latency>=0.0),
					   nMemories 		SMALLINT CHECK(nMemories>=0),
					   powerConsuption 	DOUBLE CHECK(powerConsuption>=0.0),
					   maxClockFrequency 	DOUBLE CHECK(maxClockFrequency>=0.0),
					   contactPoint		CHAR(30),
					   FOREIGN KEY(contactPoint)
									REFERENCES Author(idAuthor)
									ON DELETE SET NULL
									ON UPDATE CASCADE);

CREATE TABLE Author(idAuthor CHAR(30) PRIMARY KEY,
					name	 CHAR(30),
					company  CHAR(30),
					email	 CHAR(30),
					role	 CHAR(20));

CREATE TABLE FPGAConfiguration(idConf			CHAR(30) PRIMARY KEY,
							   name				CHAR(30),
							   bitstreamPath	CHAR(100),
							   LUTs			    INTEGER CHECK(LUTs>=0),
							   FFs				INTEGER CHECK(FFs>=0),
							   latency 			DOUBLE CHECK(latency>=0.0),
							   nMemories 		SMALLINT CHECK(nMemories>=0),
							   powerConsuption 	DOUBLE CHECK(powerConsuption>=0.0),
							   maxClockFrequency 	DOUBLE CHECK(maxClockFrequency>=0.0),
							   contactPoint 	CHAR(30),
							   idIP				CHAR(30),
							   additionalDriverSource CHAR(50),
							   FOREIGN KEY(contactPoint)
									REFERENCES Author(idAuthor)
									ON DELETE SET NULL
									ON UPDATE CASCADE,
							   FOREIGN KEY(idIP)
									REFERENCES IPManager(idIP)
									ON DELETE CASCADE
									ON UPDATE CASCADE);

CREATE TABLE MappedIP(idConf		  CHAR(30),
					  idMappedIP	  CHAR(30),
					  idIP			  CHAR(30),
					  priority 		  INTEGER,
					  physicalAddress CHAR(20),
					  PRIMARY KEY(idConf, idMappedIP),
					  FOREIGN KEY(idConf)
						REFERENCES FPGAConfiguration(idConf)
					  ON DELETE CASCADE
					  ON UPDATE CASCADE,
					  FOREIGN KEY(idIP)
					    REFERENCES IPCore(idIP)
					  ON DELETE CASCADE
					  ON UPDATE CASCADE);

INSERT INTO Author(idAuthor, name, company, email, role)
VALUES('cp01', 'Alessandro Salvato', 'ARM', 'myemail', 'Digital Designer');

INSERT INTO Author(idAuthor, name, company, email, role)
VALUES('cp02', 'Emaneuele Parisi', 'Illumina', 'lasuaemail', 'Chief');

