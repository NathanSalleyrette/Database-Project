DROP TABLE PisteContientFichiers;
DROP TABLE FilmContientFichiers;
DROP TABLE LogicielAPourCodecTexte;
DROP TABLE LogicielAPourCodecVideo;
DROP TABLE LogicielAPourCodecAudio;
DROP TABLE FilmACategoriesFilm;
DROP TABLE AlbumACategoriesMusique;
DROP TABLE PisteMusicalACategoriesMusique;
DROP TABLE Acteurs;
DROP TABLE MUSICIENS;
DROP TABLE Utilisateur;


DROP TABLE PhotoFilm ;
DROP TABLE CategorieFilm ;
DROP TABLE  Film ;
DROP TABLE Logiciel;

DROP TABLE FluxVideo;
DROP TABLE FluxAudio;
DROP TABLE FLuxTexte;
DROP TABLE CodecVideo;
DROP TABLE CodecTexte;
DROP TABLE CodecAudio;
DROP TABLE Flux;
DROP TABLE Langues;
DROP TABLE Instruments;
DROP TABLE PisteMusicale;
DROP TABLE Biographie;
DROP TABLE DateNaissanceArtiste;
DROP TABLE Artiste;
DROP TABLE Fichier;
DROP TABLE Album;
DROP TABLE CategorieMusique;

CREATE TABLE CategorieMusique(
    NomCategorie VARCHAR(21) PRIMARY KEY
);


CREATE TABLE Album(
    IDAlbum INT PRIMARY KEY,
    Titre VARCHAR(30) NOT NULL,
    NomArtiste VARCHAR(25) NOT NULL,
    DateSortie DATE NOT NULL,
    URLImage VARCHAR(21) NOT NULL
        );


CREATE TABLE Fichier(
    IdFichier INT PRIMARY KEY,
    Taille INT NOT NULL,
    DateFichier DATE NOT NULL,
    CONSTRAINT TailleValide CHECK(Taille > 0)
);



CREATE TABLE Artiste (
    IDArtiste INT PRIMARY KEY,
    NomArtiste VARCHAR(25) NOT NULL,
    URLPhoto  VARCHAR(256) NOT NULL,
    SpecialiteGlobale VARCHAR(256) NOT NULL,
    CONSTRAINT IDInvalide
        CHECK ( IDArtiste >= 0)
);



CREATE TABLE DateNaissanceArtiste(
    IDArtiste INT PRIMARY KEY ,
    DateNaissance DATE NOT NULL,
    CONSTRAINT fk_DateNaissancesArtiste_Artiste
        FOREIGN KEY (IDArtiste)
        REFERENCES Artiste (IDArtiste)
        ON DELETE Cascade
);


CREATE TABLE Biographie(
    IDArtiste INT PRIMARY KEY,
    Bio VARCHAR(2000) NOT NULL,
      CONSTRAINT fk_Biographie_Artiste
        FOREIGN KEY (IDArtiste)
        REFERENCES Artiste (IDArtiste)
        ON DELETE Cascade
);




CREATE TABLE PisteMusicale(
NumPiste INT NOT NULL,
Titre VARCHAR(256) NOT NULL,
  Duree INT NOT NULL,
IDAlbum INT NOT NULL,

	PRIMARY KEY(IDAlbum,NumPiste),

      CONSTRAINT IDInvalidePisteMusicale
        CHECK (NumPiste >= 1) ,
CONSTRAINT fk_piste_album
FOREIGN KEY (IDAlbum)
REFERENCES Album (IDAlbum)
ON DELETE Cascade

);


CREATE TABLE Instruments(
    Instrument VARCHAR(256) PRIMARY KEY
);

CREATE TABLE Langues(
  Langue VARCHAR(25) PRIMARY KEY
);

CREATE TABLE CodecAudio (
  CodecA VARCHAR(20) PRIMARY KEY

);


CREATE TABLE CodecTexte (
  CodecT VARCHAR(20) PRIMARY KEY

);


CREATE TABLE CodecVideo (
  CodecV VARCHAR(20) PRIMARY KEY

);


CREATE TABLE Flux (
  NumFlux INT NOT NULL,
  IdFichier INT NOT NULL,
  Debit INT NOT NULL,
  PRIMARY KEY (IdFichier, NumFlux),
  CONSTRAINT fk_Flux_Fichier
    FOREIGN KEY (IdFichier)
    REFERENCES Fichier (IdFichier)
    ON DELETE Cascade
);


CREATE TABLE FluxTexte (
  NumFlux INT NOT NULL,
  IdFichier INT NOT NULL,
  Langue VARCHAR(25) NOT NULL,
CodecT VARCHAR(20) NOT NULL,
  PRIMARY KEY (NumFlux, IdFichier),
  CONSTRAINT fk_Flux_Texte
    FOREIGN KEY (NumFlux, IdFichier)
    REFERENCES Flux (NumFLux, IdFichier)
    ON DELETE Cascade,
CONSTRAINT fk_Flux_Texte_Langue
	FOREIGN KEY (Langue)
	REFERENCES Langues(Langue)
ON DELETE Cascade,
CONSTRAINT Flux_Codec_T
FOREIGN KEY (CodecT)
REFERENCES CodecTexte (CodecT)
ON DELETE Cascade

	);



CREATE TABLE FluxAudio (
  NumFlux INT NOT NULL,
  IdFichier INT NOT NULL,
  Langue VARCHAR(25) NOT NULL,
  Echantillonnage INT NOT NULL,
CodecA VARCHAR(20) NOT NULL,
  PRIMARY KEY (NumFlux, IdFichier),
CONSTRAINT echantillon_valide CHECK (Echantillonnage = 16 OR Echantillonnage = 24 OR Echantillonnage = 32),
  CONSTRAINT fk_Flux_Audio
    FOREIGN KEY (NumFlux, IdFichier)
    REFERENCES Flux (NumFLux, IdFichier)
    ON DELETE Cascade,
  CONSTRAINT fk_Flux_Audio_Langue
    FOREIGN KEY (Langue)
    REFERENCES Langues(Langue)
    ON DELETE Cascade,
CONSTRAINT Flux_Codec_A
FOREIGN KEY (CodecA)
REFERENCES CodecAudio (CodecA)
ON DELETE Cascade


);







CREATE TABLE FluxVideo (
  NumFlux INT NOT NULL,
  IdFichier INT NOT NULL,
  LargeurImage INT NOT NULL,
  LongueurImage INT NOT NULL,
CodecV VARCHAR(20) NOT NULL,
  PRIMARY KEY (NumFlux, IdFichier),
  CONSTRAINT pst_Largeur CHECK (LargeurImage > 0),
  CONSTRAINT pst_Longueur CHECK (LongueurImage > 0),
  CONSTRAINT fk_Flux_Video
    FOREIGN KEY (NumFlux, IdFichier)
    REFERENCES Flux (NumFLux, IdFichier)
    ON DELETE Cascade,
CONSTRAINT Flux_Codec_V
FOREIGN KEY (CodecV)
REFERENCES CodecVideo (CodecV)
ON DELETE Cascade
);



CREATE TABLE Logiciel (
  Modele VARCHAR(20) NOT NULL,
  Marque VARCHAR(20) NOT NULL,
  LogLargeur INT NOT NULL,
  LogHauteur INT NOT NULL,
  CONSTRAINT pst_Log_Largeur CHECK (LogLargeur > 0),
  CONSTRAINT pst_Log_Hauteur CHECK (LogHauteur > 0),
  PRIMARY KEY (Modele, Marque)
);







CREATE TABLE Film (
  Titre VARCHAR(30)  NOT NULL,
  AnneeSortie INT NOT NULL,
  PRIMARY KEY (Titre, AnneeSortie),

  Resume VARCHAR(4000) NOT NULL,
  AgeMinimum INT NOT NULL,
  URLAfficheFilm VARCHAR(100),
  CONSTRAINT pst_Annee CHECK (1890 < AnneeSortie AND AnneeSortie < 2021)
 );




CREATE TABLE CategorieFilm (
  NomCategorie VARCHAR(30)PRIMARY KEY
);




CREATE TABLE PhotoFilm (
Titre VARCHAR(30),
AnneeSortie INT,
URLPhotoFilm VARCHAR(100),
PRIMARY KEY(Titre, AnneeSortie, URLPhotoFilm),
CONSTRAINT fk_Photo_Film
FOREIGN KEY (Titre, AnneeSortie)
REFERENCES Film(Titre, AnneeSortie)
ON DELETE Cascade
);



CREATE TABLE Utilisateur(
    Email VARCHAR(25) PRIMARY KEY,
    Age INT NOT NULL,
    Nom VARCHAR(25),
    Prenom VARCHAR(25),
    Langue VARCHAR(25),
    Code INT,
    CONSTRAINT AgeValide
    check (Age > 0),
    CONSTRAINT CodeValide
    check(Code > 999 AND Code < 10000),
    CONSTRAINT Fk_langueValide_utilisateur
    FOREIGN KEY(Langue)
    REFERENCES Langues(Langue)
    ON DELETE cascade
);




CREATE TABLE MUSICIENS(
    Instru VARCHAR(256),
    Artiste INT NOT NULL,
   NumPiste INT NOT NULL,
    IDAlbum INT NOT NULL,

    PRIMARY KEY (IDAlbum, NumPiste, Artiste),
    CONSTRAINT fk_musiciens_instru
        FOREIGN KEY (Instru)
        REFERENCES Instruments (Instrument)
        ON DELETE Cascade,
    CONSTRAINT fk_musiciens_artiste
        FOREIGN KEY (Artiste)
        REFERENCES Artiste (IDArtiste)
        ON DELETE Cascade,
    CONSTRAINT fk_musiciens_piste
        FOREIGN KEY (IDAlbum, NumPiste)
        REFERENCES PisteMusicale (IDAlbum, NumPiste)
        ON DELETE Cascade

);




CREATE TABLE Acteurs(
    Artiste INT,
    Role VARCHAR(256),
    Titre VARCHAR(20),
    AnneeSortie INT,
    PRIMARY KEY (Artiste, Role, Titre, AnneeSortie),
    CONSTRAINT fk_acteurs_artiste
        FOREIGN KEY (Artiste)
        REFERENCES Artiste (IDArtiste)
        ON DELETE Cascade,
      CONSTRAINT fk_acteurs_film
        FOREIGN KEY (Titre, AnneeSortie)
        REFERENCES Film (Titre, AnneeSortie)
        ON DELETE Cascade
);



CREATE TABLE PisteMusicalACategoriesMusique (
	NumPiste INT NOT NULL,
NomCategorie VARCHAR(30) NOT NULL,
IDAlbum INT NOT NULL,
  		PRIMARY KEY (IDAlbum, NumPiste,  NomCategorie),
CONSTRAINT fk_PM_A_PM_Cat
	FOREIGN KEY (IDAlbum, NumPiste)
	REFERENCES PisteMusicale (IDAlbum, NumPiste)
	ON DELETE Cascade,
CONSTRAINT fk_PM_A_CategorieM
	FOREIGN KEY (NomCategorie)
	REFERENCES CategorieMusique (NomCategorie)
	ON DELETE Cascade

);


CREATE TABLE AlbumACategoriesMusique (
	IDAlbum INT NOT NULL,
  	NomCategorie VARCHAR(30) NOT NULL,
	PRIMARY KEY (IDAlbum, NomCategorie),
CONSTRAINT fk_Album_A_Album_Cat
	FOREIGN KEY (IDAlbum)
	REFERENCES Album (IDAlbum)
	ON DELETE Cascade,
CONSTRAINT fk_Album_A_CategorieM
	FOREIGN KEY (NomCategorie)
	REFERENCES CategorieMusique (NomCategorie)
	ON DELETE Cascade

);

CREATE TABLE FilmACategoriesFilm (
	Titre VARCHAR(30) NOT NULL,
 	 AnneeSortie INT NOT NULL,
  	NomCategorie VARCHAR(30) NOT NULL,
	PRIMARY KEY (Titre, AnneeSortie, NomCategorie),
CONSTRAINT fk_Film_A_Film_Cat
	FOREIGN KEY (Titre, AnneeSortie)
	REFERENCES Film (Titre, AnneeSortie)
	ON DELETE Cascade,
CONSTRAINT fk_Film_A_CategorieF
	FOREIGN KEY (NomCategorie)
	REFERENCES CategorieFilm (NomCategorie)
	ON DELETE Cascade

);


CREATE TABLE LogicielAPourCodecAudio (
	Modele VARCHAR(20) NOT NULL,
  	Marque VARCHAR(20) NOT NULL,
	CodecA VARCHAR(20) NOT NULL,
	PRIMARY KEY (Modele, Marque, CodecA),

   CONSTRAINT fk_LogicielALogicielCA
	FOREIGN KEY (Modele, Marque)
	REFERENCES Logiciel (Modele, Marque)
	ON DELETE Cascade,
    CONSTRAINT fk_LogicielACodecA
	FOREIGN KEY (CodecA)
	REFERENCES CodecAudio (CodecA)
	ON DELETE Cascade
);


CREATE TABLE LogicielAPourCodecVideo (
Modele VARCHAR(20) NOT NULL,
  	Marque VARCHAR(20) NOT NULL,
	CodecV VARCHAR(20) NOT NULL,
	PRIMARY KEY (Modele, Marque, CodecV),

   CONSTRAINT fk_LogicielALogicielCV
	FOREIGN KEY (Modele, Marque)
	REFERENCES Logiciel (Modele, Marque)
	ON DELETE Cascade,
    CONSTRAINT fk_LogicielACodecV
	FOREIGN KEY (CodecV)
	REFERENCES CodecVideo (CodecV)
	ON DELETE Cascade
);

CREATE TABLE LogicielAPourCodecTexte (
Modele VARCHAR(20) NOT NULL,
  	Marque VARCHAR(20) NOT NULL,
	CodecT VARCHAR(20) NOT NULL,
	PRIMARY KEY (Modele, Marque, CodecT),

   CONSTRAINT fk_LogicielALogicielCT
	FOREIGN KEY (Modele, Marque)
	REFERENCES Logiciel (Modele, Marque)
	ON DELETE Cascade,
    CONSTRAINT fk_LogicielACodecT
	FOREIGN KEY (CodecT)
	REFERENCES CodecTexte (CodecT)
	ON DELETE Cascade
);


CREATE TABLE FilmContientFichiers (
	Titre VARCHAR(30) NOT NULL,
 	 AnneeSortie INT NOT NULL,
	IDFichier INT NOT NULL,
	PRIMARY KEY (Titre, AnneeSortie, IDFichier),
CONSTRAINT fk_Film_Fichier
	FOREIGN KEY (IDFichier)
	REFERENCES Fichier (IDFichier)
	ON DELETE Cascade,
CONSTRAINT fk_Film_A_Film_Fichier
	FOREIGN KEY (Titre, AnneeSortie)
	REFERENCES Film (Titre, AnneeSortie)
	ON DELETE Cascade
);

CREATE TABLE PisteContientFichiers (
	IDFichier INT NOT NULL,
IDAlbum INT NOT NULL,
NumPiste INT NOT NULL,
	PRIMARY KEY (NumPiste, IDFichier),
CONSTRAINT fk_Piste_Fichier
	FOREIGN KEY (IDFichier)
	REFERENCES Fichier (IDFichier)
	ON DELETE Cascade,
CONSTRAINT fk_Piste_Piste
	FOREIGN KEY (IDAlbum, NumPiste)
	REFERENCES PisteMusicale (IDAlbum, NumPiste)
	ON DELETE Cascade
);
