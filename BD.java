import java.sql.*;
import java.util.*;

public class BD {
	static final String CONN_URL = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1";
	static final String USER = "peyrichn";
	static final String PASSWD = "annivdeloic";
	// static final String PRE_STMT ="SELECT * FROM CATEGORIEMUSIQUE";
	// static final Hashtable<String,Savepoint> savepoints = new
	// Hashtable<String,Savepoint>();

	public BD() {
		try {
			// Enregistrement du driver Oracle
			System.out.print("Loading Oracle driver... ");
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			System.out.println("loaded");
			// Etablissement de la connection
			System.out.print("Connecting to the database... ");
			Connection conn = DriverManager.getConnection(CONN_URL, USER, PASSWD);
			System.out.println("connected");
			// disabling autocommit
			System.out.print("Disabling autocommit...");
			conn.setAutoCommit(false);
			System.out.println("done");
			// variables utiles
			boolean activity = true;
			Scanner sc = new Scanner(System.in);
			String state = "main";
			String entree = "valeur initiale quelconque";
			System.out.println("-------------BIENVENUE-------------");
			LinkedList<String> list = new LinkedList();
			;
			// debut de la boucle principale
			while (activity) {

				if (entree.equals("0")) {
					System.out.println("Exiting program,good bye");
					activity = false;
				} else if (state.equals("main")) {
					System.out.println("Qu'est ce que vous voulez faire?");
					System.out.println("tapez 1 pour inserer un nouveau fichier");
					System.out.println("tapez 2 pour selectionner un contenu");
					System.out.println("tapez 3 pour supprimer un contenu");
					System.out.println("0 si vous voulez quittez");
					entree = sc.nextLine();
					if (entree.equals("1")) {
						state = "insert";
					} else if (entree.equals("2")) {
						state = "select";
					} else if (entree.equals("3")) {
						state = "delete";
					}
				}

				else if (state.equals("select")) {
					try {
						System.out.println("vous etes dans l'etat select");
						// inserer le code
						System.out.println("Veuilez entrer votre email: ");
						String username = sc.nextLine();
						System.out.println("Votre mot de passe: ");
						String password = sc.nextLine();
						String request = "SELECT * FROM Utilisateur WHERE Email = '" + username + "' AND Code = "
								+ password;
						PreparedStatement stmt = conn.prepareStatement(request);
						ResultSet rset = stmt.executeQuery();
						if (rset.next()) {
							String langue = rset.getString("LANGUE");
							int age = rset.getInt("AGE");
							System.out.println("Quel est le nom de votre logiciel?");
							String modele = sc.nextLine();
							System.out.println("Quelle est la marque de votre logciel?");
							String marque = sc.nextLine();
							// On eclate cette requete en 3 une par type audio, video, texte
							request = "SELECT *  FROM  Logiciel L WHERE L.Modele LIKE '" + modele
									+ "' AND L.Marque LIKE '" + marque + "'";
							String requestaudio = "SELECT *  FROM  LogicielAPourCodecAudio L WHERE L.Modele LIKE '"
									+ modele + "' AND L.Marque LIKE '" + marque + "'";
							String requestvideo = "SELECT *  FROM  LogicielAPourCodecVideo L WHERE L.Modele LIKE '"
									+ modele + "' AND L.Marque LIKE '" + marque + "'";
							String requesttexte = "SELECT *  FROM  LogicielAPourCodecTexte L WHERE L.Modele LIKE '"
									+ modele + "' AND L.Marque LIKE '" + marque + "'";
							stmt = conn.prepareStatement(request);
							rset = stmt.executeQuery();
							stmt = conn.prepareStatement(requestaudio);
							ResultSet rsetaudio = stmt.executeQuery();
							stmt = conn.prepareStatement(requestvideo);
							ResultSet rsetvideo = stmt.executeQuery();
							stmt = conn.prepareStatement(requesttexte);
							ResultSet rsettexte = stmt.executeQuery();
							/*
							 * dumpResultSet(rsetaudio); dumpResultSet(rsettexte); dumpResultSet(rsetvideo);
							 */
							if (rset.next()) {
								LinkedList<String> codecUAudio = new LinkedList();
								LinkedList<String> codecUTexte = new LinkedList();
								LinkedList<String> codecUVideo = new LinkedList();
								if (rsetaudio.getFetchSize() > 0) {
									while (rsetaudio.next()) {
										codecUAudio.add(rsetaudio.getString("CODECA"));
									}
								}
								if (rsettexte.getFetchSize() > 0) {
									while (rsettexte.next()) {
										codecUTexte.add(rsettexte.getString("CODECT"));
									}
								}
								if (rsetvideo.getFetchSize() > 0) {
									while (rsetvideo.next()) {
										codecUVideo.add(rsetvideo.getString("CODECV"));
									}
								}
								int connect = 1;
								while (connect == 1) {
									request = new String();
									String requestnoA = new String();
									String requestnoT = new String();
									String requestnono = new String();
									String rnono = new String();
									String rnoA = new String();
									String rnoT = new String();
									String requestnono2 = "SELECT COUNT(*) FROM ";
									String requestnoA2 = "SELECT COUNT(*) FROM FLUXTEXTE FT, ";
									String requestnoT2 = "SELECT COUNT(*) FROM FLUXAUDIO FA, ";
									String request2 = "SELECT COUNT(*) FROM FLUXAUDIO FA, FLUXTEXTE FT, ";
									String r = new String();
									ResultSet catF = stmt.executeQuery("SELECT NomCategorie FROM CategorieFilm");

									System.out.println("Voici les catégories de films disponibles  :");
									
									dumpResultSet(catF);
									System.out.println();

									System.out.println("Voici les catégories de pistes musicales disponibles  :");

									ResultSet catM = stmt.executeQuery("SELECT NomCategorie FROM CategorieMusique");

									dumpResultSet(catM);
									System.out.println();
									System.out.println("Veuilez indiquer la catégorie qui vous interesse: ");
									
									String categorie = sc.nextLine();
									System.out.println("Vous voulez ecouter une musique (2) ou bien voir un film(1)");
									entree = sc.nextLine();
									if (entree.equals("1")) {
										// La aussi
										request = "SELECT F.TITRE, F.ANNEESORTIE, F.AGEMINIMUM FROM  FLUXTEXTE FT, FLUXAUDIO FA, ";
										r += "FLUXVIDEO FV, FILM F, FILMACATEGORIESFILM FC,FILMCONTIENTFICHIERS FCF WHERE F.TITRE LIKE FC.TITRE AND F.ANNEESORTIE = FC.ANNEESORTIE AND FC.NOMCATEGORIE LIKE '"
												+ categorie + "' AND F.AGEMINIMUM <" + age
												+ " AND F.TITRE LIKE FCF.TITRE AND F.ANNEESORTIE = FCF.ANNEESORTIE AND FCF.IDFICHIER = FA.IDFICHIER AND FT.IDFICHIER = FCF.IDFICHIER AND FV.IDFICHIER = FCF.IDFICHIER AND (";
										requestnoA = "SELECT F.TITRE, F.ANNEESORTIE, F.AGEMINIMUM FROM  FLUXTEXTE FT, ";
										rnoA += "FLUXVIDEO FV, FILM F, FILMACATEGORIESFILM FC,FILMCONTIENTFICHIERS FCF WHERE F.TITRE LIKE FC.TITRE AND F.ANNEESORTIE = FC.ANNEESORTIE AND FC.NOMCATEGORIE LIKE '"
												+ categorie + "' AND F.AGEMINIMUM <" + age
												+ " AND F.TITRE LIKE FCF.TITRE AND F.ANNEESORTIE = FCF.ANNEESORTIE AND FT.IDFICHIER = FCF.IDFICHIER AND FV.IDFICHIER = FCF.IDFICHIER AND (";
										rnoT += "FLUXVIDEO FV, FILM F, FILMACATEGORIESFILM FC,FILMCONTIENTFICHIERS FCF WHERE F.TITRE LIKE FC.TITRE AND F.ANNEESORTIE = FC.ANNEESORTIE AND FC.NOMCATEGORIE LIKE '"
												+ categorie + "' AND F.AGEMINIMUM <" + age
												+ " AND F.TITRE LIKE FCF.TITRE AND F.ANNEESORTIE = FCF.ANNEESORTIE AND FCF.IDFICHIER = FA.IDFICHIER AND FV.IDFICHIER = FCF.IDFICHIER AND (";
										requestnoT = "SELECT F.TITRE, F.ANNEESORTIE, F.AGEMINIMUM FROM FLUXAUDIO FA, ";
										rnono += "FLUXVIDEO FV, FILM F, FILMACATEGORIESFILM FC,FILMCONTIENTFICHIERS FCF WHERE F.TITRE LIKE FC.TITRE AND F.ANNEESORTIE = FC.ANNEESORTIE AND FC.NOMCATEGORIE LIKE '"
												+ categorie + "' AND F.AGEMINIMUM <" + age
												+ " AND F.TITRE LIKE FCF.TITRE AND F.ANNEESORTIE = FCF.ANNEESORTIE AND FV.IDFICHIER = FCF.IDFICHIER AND (";
										requestnono = "SELECT F.TITRE, F.ANNEESORTIE, F.AGEMINIMUM FROM ";
										for (String codec : codecUAudio) {
											r += " FA.CODECA = '" + codec + "' ";
											rnoT += " FA.CODECA = '" + codec + "' ";
											if (codec != codecUAudio.getLast()) {
												r += "OR ";
												rnoT += "OR ";
											}
										}
										r += " ) AND FA.LANGUE LIKE '" + langue + "' AND (";
										rnoT += " ) AND FA.LANGUE LIKE '" + langue + "' AND (";
										for (String codec : codecUTexte) {
											r += " FT.CODECT = '" + codec + "' ";
											rnoA += " FT.CODECT = '" + codec + "' ";
											if (codec != codecUTexte.getLast()) {
												r += "OR ";
												rnoA += "OR ";
											}
										}
										r += " ) AND FT.LANGUE LIKE '" + langue + "'AND (";
										rnoA += " ) AND FT.LANGUE LIKE '" + langue + "'AND (";
										for (String codec : codecUVideo) {
											rnono += " FV.CODECV = '" + codec + "' ";
											r += " FV.CODECV = '" + codec + "' ";
											rnoA += " FV.CODECV = '" + codec + "' ";
											rnoT += " FV.CODECV = '" + codec + "' ";
											if (codec != codecUVideo.getLast()) {
												rnono += " OR ";
												r += "OR ";
												rnoA += "OR ";
												rnoT += "OR ";
											}
										}
										r += ")";
										rnoA += ") AND FCF.IDFICHIER NOT IN(SELECT IDFICHIER FROM FLUXAUDIO) ";
										rnoT += ") AND FCF.IDFICHIER NOT IN(SELECT IDFICHIER FROM FLUXTEXTE) ";
										rnono += ") AND FCF.IDFICHIER NOT IN(SELECT IDFICHIER FROM FLUXAUDIO) AND FCF.IDFICHIER NOT IN(SELECT IDFICHIER FROM FLUXTEXTE) ";
									} else {
										request = "SELECT P.NUMPISTE, A.IDALBUM, P.TITRE, P.DUREE , A.TITRE, A.DATESORTIE, A.URLIMAGE FROM FLUXTEXTE FT, FLUXAUDIO FA, ";
										r += " PISTEMUSICALE P, PISTEMUSICALACATEGORIESMUSIQUE PC, PISTECONTIENTFICHIERS PCF, ALBUM A WHERE A.IDALBUM = P.IDALBUM AND P.IDALBUM = PC.IDALBUM AND P.NUMPISTE = PC.NUMPISTE AND PC.NOMCATEGORIE = '"
												+ categorie
												+ "' AND P.IDALBUM = PCF.IDALBUM AND P.NUMPISTE = PCF.NUMPISTE AND  PCF.IDFICHIER = FA.IDFICHIER AND FT.IDFICHIER = PCF.IDFICHIER AND (";
										requestnoT = "SELECT P.NUMPISTE, A.IDALBUM, P.TITRE, P.DUREE , A.TITRE, A.DATESORTIE, A.URLIMAGE FROM FLUXAUDIO FA, ";
										rnoT += " PISTEMUSICALE P, PISTEMUSICALACATEGORIESMUSIQUE PC, PISTECONTIENTFICHIERS PCF, ALBUM A WHERE A.IDALBUM = P.IDALBUM AND P.IDALBUM = PC.IDALBUM AND P.NUMPISTE = PC.NUMPISTE AND PC.NOMCATEGORIE = '"
												+ categorie
												+ "' AND P.IDALBUM = PCF.IDALBUM AND P.NUMPISTE = PCF.NUMPISTE AND  PCF.IDFICHIER = FA.IDFICHIER AND (";
										requestnoA = "SELECT * FROM ALBUM";
										for (String codec : codecUAudio) {
											r += " FA.CODECA = '" + codec + "' ";
											rnoT += " FA.CODECA = '" + codec + "' ";
											if (codec != codecUAudio.getLast()) {
												r += "OR ";
												rnoT += "OR ";
											}
										}
										r += " ) AND ( ";
										rnoT += " ) AND PCF.IDFICHIER NOT IN(SELECT IDFICHIER FROM FLUXTEXTE) ";
										for (String codec : codecUTexte) {
											r += " FT.CODECT = '" + codec + "' ";
											if (codec != codecUTexte.getLast()) {
												r += "OR ";
											}
										}
										r += ")";
										rnoA += "FLUXVIDEO WHERE FT.IDFICHIER = 0";
										rnono += "FLUXVIDEO FA WHERE FA.IDFICHIER = 0";
									}
									requestnono += rnono;
									requestnono2 += rnono;
									request += r;
									request2 += r;
									requestnoA += rnoA;
									requestnoA2 += rnoA;
									requestnoT += rnoT;
									requestnoT2 += rnoT;
									stmt = conn.prepareStatement(request);
									rset = stmt.executeQuery();
									stmt = conn.prepareStatement(request2);
									ResultSet rset2 = stmt.executeQuery();
									rset2.next();
									stmt = conn.prepareStatement(requestnoA2);
									ResultSet rsetnoA2 = stmt.executeQuery();
									rsetnoA2.next();
									stmt = conn.prepareStatement(requestnoT);
									ResultSet rsetnoT = stmt.executeQuery();
									stmt = conn.prepareStatement(requestnoT2);
									ResultSet rsetnoT2 = stmt.executeQuery();
									rsetnoT2.next();
									stmt = conn.prepareStatement(requestnono2);
									ResultSet rsetnono2 = stmt.executeQuery();
									rsetnono2.next();
									if (rsetnono2.getInt(1) > 0 || rset2.getInt(1) > 0 || rsetnoA2.getInt(1) > 0
											|| rsetnoT2.getInt(1) > 0) {
										System.out.println("Voici ce que nous vous conseillons");
										dumpResultSet(rset);
										dumpResultSet(rsetnoT);
										if (entree.equals("1")) {
											stmt = conn.prepareStatement(requestnoA);
											ResultSet rsetnoA = stmt.executeQuery();
											dumpResultSet(rsetnoA);
											stmt = conn.prepareStatement(requestnono);
											ResultSet rsetnono = stmt.executeQuery();
											dumpResultSet(rsetnono);
										}
										System.out.println("Voulez-vous faire une recherche approfondie ? (oui/non)");
										String answer = sc.nextLine();
										if (answer.equals("oui")) {
											requestnono2 = "SELECT COUNT(*) FROM ";
											requestnoA2 = "SELECT COUNT(*) FROM FLUXTEXTE FT, ";
											requestnoT2 = "SELECT COUNT(*) FROM FLUXAUDIO FA, ";
											request2 = "SELECT COUNT(*) FROM FLUXAUDIO FA, FLUXTEXTE FT, ";
											if (entree.equals("1")) {
												requestnono = "SELECT AR.NOMARTISTE, AR.IDARTISTE, AC.ROLE FROM ";
												request = "SELECT AR.NOMARTISTE, AR.IDARTISTE, AC.ROLE FROM FLUXAUDIO FA, FLUXTEXTE FT, ";
												requestnoA = "SELECT AR.NOMARTISTE, AR.IDARTISTE, AC.ROLE FROM FLUXTEXTE FT, ";
												requestnoT = "SELECT AR.NOMARTISTE, AR.IDARTISTE, AC.ROLE FROM FLUXAUDIO FA, ";
												System.out.println(
														"Veuillez entrer le titre du film dont vous souhaitez avoir plus d'information:");
												String titre = sc.nextLine();
												System.out.println("Son annee de sortie:");
												String annee = sc.nextLine();
												r = " ARTISTE AR, ACTEURS AC, " + r + " AND F.TITRE = '" + titre
														+ "' AND F.ANNEESORTIE = " + annee
														+ " AND AC.TITRE = F.TITRE AND AC.ANNEESORTIE = F.ANNEESORTIE AND AR.IDARTISTE = AC.ARTISTE";
												rnoA = " ARTISTE AR, ACTEURS AC, " + rnoA + " AND F.TITRE = '" + titre
														+ "' AND F.ANNEESORTIE = " + annee
														+ " AND AC.TITRE = F.TITRE AND AC.ANNEESORTIE = F.ANNEESORTIE AND AR.IDARTISTE = AC.ARTISTE";
												rnoT = " ARTISTE AR, ACTEURS AC, " + rnoT + " AND F.TITRE = '" + titre
														+ "' AND F.ANNEESORTIE = " + annee
														+ " AND AC.TITRE = F.TITRE AND AC.ANNEESORTIE = F.ANNEESORTIE AND AR.IDARTISTE = AC.ARTISTE";
												rnono = " ARTISTE AR, ACTEURS AC, " + rnono + " AND F.TITRE = '" + titre
														+ "' AND F.ANNEESORTIE = " + annee
														+ " AND AC.TITRE = F.TITRE AND AC.ANNEESORTIE = F.ANNEESORTIE AND AR.IDARTISTE = AC.ARTISTE";
											} else {
												request = "SELECT AR.NOMARTISTE, AR.IDARTISTE, M.INSTRU FROM FLUXAUDIO FA, FLUXTEXTE FT, ";
												requestnoT = "SELECT AR.NOMARTISTE, AR.IDARTISTE, M.INSTRU FROM FLUXAUDIO FA, ";
												System.out.println(
														"Veuillez entrez le numéro de l'album qui vous intéresse:");
												String idalbum = sc.nextLine();
												System.out.println(
														"Veuillez entrer le numéro de la piste dont vous souhaitez avoir plus d'information:");
												String numPiste = sc.nextLine();
												r = "ARTISTE AR, MUSICIENS M, " + r
														+ " AND M.NUMPISTE = P.NUMPISTE AND M.IDALBUM = P.IDALBUM AND P.IDALBUM = "
														+ idalbum + " AND P.NUMPISTE = " + numPiste
														+ " AND M.ARTISTE = AR.IDARTISTE";
												rnoT = "ARTISTE AR, MUSICIENS M, " + rnoT
														+ " AND M.NUMPISTE = P.NUMPISTE AND M.IDALBUM = P.IDALBUM AND P.IDALBUM = "
														+ idalbum + " AND P.NUMPISTE = " + numPiste
														+ " AND M.ARTISTE = AR.IDARTISTE";
												rnoA = "FLUXVIDEO WHERE FT.IDFICHIER = 0";
												rnono = "FLUXVIDEO FA WHERE FA.IDFICHIER = 0";
											}
											requestnono += rnono;
											requestnono2 += rnono;
											request += r;
											request2 += r;
											requestnoA += rnoA;
											requestnoA2 += rnoA;
											requestnoT += rnoT;
											requestnoT2 += rnoT;
											stmt = conn.prepareStatement(request);
											rset = stmt.executeQuery();
											stmt = conn.prepareStatement(request2);
											rset2 = stmt.executeQuery();
											rset2.next();
											stmt = conn.prepareStatement(requestnoA2);
											rsetnoA2 = stmt.executeQuery();
											rsetnoA2.next();
											stmt = conn.prepareStatement(requestnoT);
											rsetnoT = stmt.executeQuery();
											stmt = conn.prepareStatement(requestnoT2);
											rsetnoT2 = stmt.executeQuery();
											rsetnoT2.next();
											stmt = conn.prepareStatement(requestnono2);
											rsetnono2 = stmt.executeQuery();
											rsetnono2.next();
											if (rsetnono2.getInt(1) > 0 || rset2.getInt(1) > 0 || rsetnoA2.getInt(1) > 0
													|| rsetnoT2.getInt(1) > 0) {
												System.out.println("Voici les résultats de votre recherche:");
												dumpResultSet(rset);
												dumpResultSet(rsetnoT);
												if (entree.equals("1")) {
													stmt = conn.prepareStatement(requestnoA);
													ResultSet rsetnoA = stmt.executeQuery();
													dumpResultSet(rsetnoA);
													stmt = conn.prepareStatement(requestnono);
													ResultSet rsetnono = stmt.executeQuery();
													dumpResultSet(rsetnono);
												}
												System.out.println(
														"Souhaitez-vous acceder à la biographie d'un des artistes ici présent? (oui/non)");
												answer = sc.nextLine();
												if (answer.equals("oui")) {
													requestnono2 = "SELECT COUNT(*) FROM ";
													request2 = "SELECT COUNT(*) FROM FLUXAUDIO FA, FLUXTEXTE FT, ";
													requestnoA2 = "SELECT COUNT(*) FROM FLUXTEXTE FT, ";
													requestnoT2 = "SELECT COUNT(*) FROM FLUXAUDIO FA, ";
													requestnono = "SELECT B.BIO, AR.IDARTISTE, AR.SPECIALITEGLOBALE, AR.URLPHOTO, AR.NOMARTISTE FROM ";
													request = "SELECT B.BIO, AR.IDARTISTE, AR.SPECIALITEGLOBALE, AR.URLPHOTO, AR.NOMARTISTE FROM FLUXAUDIO FA, FLUXTEXTE FT, ";
													requestnoA = "SELECT B.BIO, AR.IDARTISTE, AR.SPECIALITEGLOBALE, AR.URLPHOTO, AR.NOMARTISTE FROM FLUXTEXTE FT, ";
													requestnoT = "SELECT B.BIO, AR.IDARTISTE, AR.SPECIALITEGLOBALE, AR.URLPHOTO, AR.NOMARTISTE FROM FLUXAUDIO FA, ";
													System.out.println("Id de l'artiste souhaité:");
													String idartiste = sc.nextLine();
													r = " BIOGRAPHIE B, " + r
															+ " AND B.IDARTISTE = AR.IDARTISTE AND B.IDARTISTE = "
															+ idartiste;
													rnoT = " BIOGRAPHIE B, " + rnoT
															+ " AND B.IDARTISTE = AR.IDARTISTE AND B.IDARTISTE = "
															+ idartiste;
													if (entree.equals("1")) {
														rnoA = " BIOGRAPHIE B, " + rnoA
																+ " AND B.IDARTISTE = AR.IDARTISTE AND B.IDARTISTE = "
																+ idartiste;
														rnono = " BIOGRAPHIE B, " + rnono
																+ " AND B.IDARTISTE = AR.IDARTISTE AND B.IDARTISTE = "
																+ idartiste;
													} else {
														rnoA = "FLUXVIDEO WHERE FT.IDFICHIER = 0";
														rnono = "FLUXVIDEO FA WHERE FA.IDFICHIER = 0";
													}
													requestnono += rnono;
													requestnono2 += rnono;
													request += r;
													request2 += r;
													requestnoA += rnoA;
													requestnoA2 += rnoA;
													requestnoT += rnoT;
													requestnoT2 += rnoT;
													stmt = conn.prepareStatement(request);
													rset = stmt.executeQuery();
													stmt = conn.prepareStatement(request2);
													rset2 = stmt.executeQuery();
													rset2.next();
													stmt = conn.prepareStatement(requestnoA2);
													rsetnoA2 = stmt.executeQuery();
													rsetnoA2.next();
													stmt = conn.prepareStatement(requestnoT);
													rsetnoT = stmt.executeQuery();
													stmt = conn.prepareStatement(requestnoT2);
													rsetnoT2 = stmt.executeQuery();
													rsetnoT2.next();
													stmt = conn.prepareStatement(requestnono2);
													rsetnono2 = stmt.executeQuery();
													rsetnono2.next();
													if (rsetnono2.getInt(1) > 0 || rset2.getInt(1) > 0
															|| rsetnoA2.getInt(1) > 0 || rsetnoT2.getInt(1) > 0) {
														System.out.println("Voici la biographie de votre artiste:");
														dumpResultSet(rset);
														dumpResultSet(rsetnoT);
														if (entree.equals("1")) {
															stmt = conn.prepareStatement(requestnoA);
															ResultSet rsetnoA = stmt.executeQuery();
															dumpResultSet(rsetnoA);
															stmt = conn.prepareStatement(requestnono);
															ResultSet rsetnono = stmt.executeQuery();
															dumpResultSet(rsetnono);
														}
													} else {
														System.out.println(
																"L'artiste voulu n'existe pas ou ne possède pas de biographie.");
													}
												} else {
													System.out.println("Pas de soucis");
												}

											} else {
												System.out.println(
														"Votre recherche approfondie n'a hélas pas pu aboutir.");
											}
										} else {
											System.out.println("D'accord");
										}
									} else {
										System.out.println("Rien à proposer");
									}
									System.out.println("Voulez-vous refaire une recherche (1) pour oui");
									String answer = sc.nextLine();
									if (!answer.equals("1")) {
										connect = 0;
										System.out.println("Déconnexion ...");
									}
								}
							} else {
								System.out.println(
										"Desole mais nous ne sommes pas encore disponible sur votre appareil ! ");
							}
						} else {
							System.out.println("Voulez-vous creer un compte? Tapez 1 pour dire oui");
							entree = sc.nextLine();
							if (entree.equals("1")) {
								System.out.println("Veuilez entrer votre email: ");
								username = sc.nextLine();
								System.out.println("Votre mot de passe: ");
								password = sc.nextLine();
								System.out.println("Votre langue");
								String langue = sc.nextLine();
								System.out.println("Votre age");
								String age = sc.nextLine();
								System.out.println("Votre nom");
								String nom = sc.nextLine();
								System.out.println("Votre prenom");
								String prenom = sc.nextLine();
								request = "INSERT INTO Utilisateur VALUES('" + username + "', " + age + ", '" + nom
										+ "', '" + prenom + "', '" + langue + "', " + password + ")";
								stmt = conn.prepareStatement(request);
								stmt.executeQuery();
							} else {
								System.out.println("Comme vous voulez, au revoir");
							}
						}
						// a la fin on va a l'etat confirm
						state = "confirm";
					} catch (Exception e) {
						System.out.println("Failed \n");
						e.printStackTrace(System.err);
					}
				}

				else if (state.equals("insert")) {

					try {
						System.out.println("vous etes dans l'etat insert");
						// inserer le code
						System.out.println("Creation d'un fichier");
						// on va chercher l'ID max
						PreparedStatement stmt = conn.prepareStatement("SELECT MAX(IDFICHIER) FROM Fichier");
						ResultSet rset = stmt.executeQuery();
						rset.next();
						int IDMAX = rset.getInt(1);
						// System.out.println("IDMAX:"+IDMAX);
						rset.close();
						stmt.close();
						// TAILLE
						System.out.println("Quelle est la taille de votre fichier?:");
						String taille = sc.nextLine();
						// on creer le Fichier
						int id = IDMAX + 1;
						// System.out.println("FLAG");
						String pre_stmt = "INSERT INTO FICHIER VALUES (" + id + "," + taille + ",CURRENT_DATE )";
						stmt = conn.prepareStatement(pre_stmt);
						rset = stmt.executeQuery();
						System.out.println("Fichier crée");
						System.out.println("Combien de Flux ajouter?(il faut ajouter au moins 1 flux)");
						String a = sc.nextLine();
						int nbFlux = Integer.parseInt(a);
						while (!(nbFlux >= 1)) {
							System.out.println("doit etre superieur a 1!,reessayez:");
							a = sc.nextLine();
							nbFlux = Integer.parseInt(a);
						}
						int numFlux = 1;
						boolean echec = false;
						for (int i = 1; i < nbFlux + 1; i++) {
							echec = false;
							System.out.println("pour le flux numero " + i);
							// DEBIT
							System.out.println("Quelle est le debit de ce flux?:");
							String debit = sc.nextLine();
							// CREATION FLUX
							try {
								stmt = conn.prepareStatement(
										"INSERT INTO FLUX VALUES (" + numFlux + "," + id + "," + debit + ")");
								rset = stmt.executeQuery();
								System.out.println("GOOD");
							} catch (SQLException e) {
								System.out.println("probleme avec la requete numero " + i
										+ "(creation FLUX),retour au menu principal");
								state = "main";
								echec = true;
								break;
							}

							// TYPE DU FLUX
							System.out.println("Quel est le type de ce flux?(audio/texte/video)");
							String type = sc.nextLine();
							if (type.equals("video")) {
								// CODEC DU FLUX
								System.out.println("Quelle est le CODEC de ce flux?(exemple:DivX,H264,MPEG2,MPEG4):");
								String codec = sc.nextLine();
								// LONGUEUR ET LARGEUR DE L'IMAGE
								System.out.println("Quelle est la largeur de l'image?:");
								String largeurImage = sc.nextLine();
								System.out.println("Quelle est la longueur de l'image?:");
								String longueurImage = sc.nextLine();
								try {
									stmt = conn.prepareStatement("INSERT INTO FLUXVIDEO VALUES (" + numFlux + "," + id
											+ "," + largeurImage + "," + longueurImage + ",'" + codec + "')");
									rset = stmt.executeQuery();
									numFlux++;
								} catch (SQLException e) {
									System.out.println("probleme avec la requete numero " + i
											+ "(creation FLUXVIDEO),retour au menu principal");
									echec = true;
									break;
								}

							} else if (type.equals("audio")) {
								// LANGUE DU FlUX
								boolean langueVraie = false;
								String langue = "";
								System.out.println("Quelle est la langue de ce flux? Voici les langues disponibles");
								printLangue(conn);
								while (!langueVraie) {
									langue = sc.nextLine();
									if (checkLangueVraie(langue, conn))
										langueVraie = true;
									else
										System.out.println("La langue voulue n'est pas disponible. Recommencez");

								}
								// CODEC DU FLUX
								System.out.println("Quelle est le CODEC de ce flux?(exemple:AC3,ACC,MP3,MPEG4):");
								String codec = sc.nextLine();
								// ECHANTILLONAGE
								System.out.println("Quelle est l'echantillonage?:");
								String echantillonage = sc.nextLine();
								try {
									stmt = conn.prepareStatement("INSERT INTO FLUXAUDIO VALUES (" + numFlux + "," + id
											+ ",'" + langue + "'," + echantillonage + ",'" + codec + "')");
									rset = stmt.executeQuery();
									numFlux++;
								} catch (SQLException e) {
									System.out.println("probleme avec la requete numero " + i
											+ " (creation FLUXAUDIO),retour au menu principal");
									echec = true;
									break;
								}
							} else if (type.equals("texte")) {
								// LANGUE DU FlUX
								String langue = "";
								boolean langueVraie = false;
								System.out.println("Quelle est la langue de ce flux?:");
								printLangue(conn);
								while (!langueVraie) {
									langue = sc.nextLine();
									if (checkLangueVraie(langue, conn))
										langueVraie = true;
									else
										System.out.println("La langue voulue n'est pas disponible. Recommencez");

								}
								// CODEC DU FLUX
								System.out.println("Quelle est le CODEC de ce flux?(exemple:AQTitle,DKS,Kate):");
								String codec = sc.nextLine();
								try {
									stmt = conn.prepareStatement("INSERT INTO FLUXTEXTE VALUES (" + numFlux + "," + id
											+ ",'" + langue + "','" + codec + "')");
									rset = stmt.executeQuery();
									numFlux++;
								} catch (SQLException e) {
									System.out.println("probleme avec la requete numero " + i
											+ " (creation FLUXTEXTE),retour au menu principal");
									echec = true;
									break;
								}

							} else {
								System.out.println("Veuillez rentrer soit audio, texte ou video");
								echec = true;
							}
						}

						if (echec) {
							System.out.println("FLAG ECHEC");
							rset.close();
							stmt.close();
							state = "main";
						} else {
							rset.close();
							stmt.close();
							// ON PASSE A LA CREATION DE FILM/PM
							System.out.println(
									"a quel type de contenu voulez vous lier votre fichier?(film/piste musicale):");
							entree = sc.nextLine();
							boolean signal = false;
							while (!signal) {
								if (entree.equals("film")) {
									signal = true;
									System.out.println("Vous avez choisi film");
									// lui montrer les films existant
									System.out.println("Voici les films existants");
									ArrayList<String[]> films = new ArrayList<String[]>();
									stmt = conn.prepareStatement("SELECT TITRE,ANNEESORTIE FROM FILM");
									rset = stmt.executeQuery();
									// ResultSetMetaData rsetmd = rset.getMetaData();
									// int i = rsetmd.getColumnCount();
									while (rset.next()) {
										String titre = rset.getString(1);
										String date = rset.getString(2);
										System.out.println(titre + "\t" + date);
										String[] s = { titre, date };
										films.add(s);
									}
									// lui proposer d'ajouter un film (rentrer le titre est la date de sortie)
									System.out.println(
											"Vous pouvez choisir parmis les anciens films ou entrer un nouveau");
									System.out.println("Entrez le titre du film que vous voulez");
									String titreEntre = sc.nextLine();
									System.out.println("Entrez sa date de sortie");
									String dateEntree = sc.nextLine();
									// checker si le film existe deja
									boolean existe = false;
									for (String[] couple : films) {
										if (couple[0].equals(titreEntre) && couple[1].equals(dateEntree)) {
											existe = true;
											break;
										}
									}
									// si oui on update uniquement la table film<->fichier
									if (existe) {
										System.out.println("ce film existe");
										stmt = conn.prepareStatement("INSERT INTO FILMCONTIENTFICHIERS VALUES ('"
												+ titreEntre + "'," + dateEntree + "," + id + ")");
										rset = stmt.executeQuery();
									}
									// sinon on creer le film
									else {
										System.out.println("ce film n'existe pas, on va l'ajouter");
										// AGE MIN
										System.out.println("Quel est l'age minimum pour voir ce film?");
										String ageMin = sc.nextLine();
										// RESUME
										System.out.println("Entrez un resume pour le film");
										String resume = sc.nextLine();
										resume = "'" + resume + "'";

										System.out.println(
												"Entrer une URL de l'affiche si vous en avez,laissez vide est cliquez sur entrer");
										String url = sc.nextLine();
										url = "'" + url + "'";

										stmt = conn.prepareStatement("INSERT INTO FILM VALUES ('" + titreEntre + "',"
												+ dateEntree + "," + resume + "," + ageMin + "," + url + ")");
										// stmt = conn.prepareStatement("INSERT INTO FILMCONTIENTFICHIERS VALUES ('"
										// + titreEntre + "'," + dateEntree + "," + id + ")");
										rset = stmt.executeQuery();

										list.add("INSERT INTO FILMCONTIENTFICHIERS VALUES ('" + titreEntre + "',"
												+ dateEntree + "," + id + ")");

										// on l'interroge sur les categories
										System.out.println("Voici les categories de film existantes");
										stmt = conn.prepareStatement("SELECT * FROM CATEGORIEFILM");
										rset = stmt.executeQuery();
										dumpResultSet(rset);
										System.out.println("Choisissez une catégorie");

										boolean chooseCat = true;

										String reponse;
										while (chooseCat) {

											String categorieFilm = sc.nextLine();
											if (!checkCatFilm(categorieFilm, conn)) {
												System.out.println(
														"Cette catégorie n'existe pas, voulez vous l'ajouter ? (oui) (non)");
												reponse = sc.nextLine();
												if (reponse.equals("oui")) {
													stmt = conn.prepareStatement("INSERT INTO CATEGORIEFILM VALUES ('"
															+ categorieFilm + "')");
													rset = stmt.executeQuery();
													list.add("INSERT INTO FILMACATEGORIESFILM VALUES('" + titreEntre
															+ "'," + dateEntree + ",'" + categorieFilm + "')");

												} else
													System.out.println("Ecrivez votre catégorie");

											} else
												list.add("INSERT INTO FILMACATEGORIESFILM VALUES ('" + titreEntre + "',"
														+ dateEntree + ",'" + categorieFilm + "')");

											System.out.println("Voulez-vous ajouter une autre catégorie ? (oui) (non)");
											reponse = sc.nextLine();
											if (!reponse.equals("oui"))
												chooseCat = false;

										}

										// On choisit l'url des photos;
										boolean choosePic = false;
										System.out.println("Voulez-vous choisir des photos à mettre ? (oui) (non)");
										reponse = sc.nextLine();
										if (reponse.equals("oui"))
											choosePic = true;
										while (choosePic) {
											System.out.println("Ecrivez l'url de la photo");
											String urlPhoto = sc.nextLine();
											list.add("INSERT INTO PhotoFilm VALUES('" + titreEntre + "', " + dateEntree
													+ ", '" + urlPhoto + "')");
											System.out.println("Voulez-vous ajouter un autre url ? (oui) (non)");
											reponse = sc.nextLine();
											if (!reponse.equals("oui"))
												choosePic = false;
										}

										// on l'interroge sur les acteurs
										boolean entrerActeur = true;
										System.out.println(
												"voulez vous entrer un acteur en rapport avec ce film?(oui/non)");
										String ouinon = sc.nextLine();
										while (entrerActeur) {

											if (ouinon.equals("non")) {
												entrerActeur = false;
											} else if (ouinon.equals("oui")) {
												// Aller checher l'id pour prendre la maximale
												stmt = conn.prepareStatement("SELECT MAX(IDARTISTE) FROM ARTISTE");
												rset = stmt.executeQuery();
												rset.next();
												IDMAX = rset.getInt(1);
												int idArtiste = IDMAX + 1;
												rset.close();
												stmt.close();
												// NOM
												System.out.println("entrez le nom de l'artiste:");
												String nomArtiste = sc.nextLine();
												// URL Photo
												System.out.println("entrez une URL de son image:");
												String urlArtiste = sc.nextLine();
												// SpecialitéGlobale
												System.out.println("Quelle est sa specialité globale?:");
												String speGlobaleArtiste = sc.nextLine();
												stmt = conn.prepareStatement("INSERT INTO ARTISTE VALUES (" + idArtiste
														+ ",'" + nomArtiste + "','" + urlArtiste + "','"
														+ speGlobaleArtiste + "')");
												rset = stmt.executeQuery();
												// est ce que c'est un acteur

												System.out.println("Quel est son role dans le film ?:");
												String role = sc.nextLine();
												// stmt = conn.prepareStatement("INSERT INTO ACTEURS VALUES
												// ("+idArtiste+",'"+role+"','"+titreEntre+"',"+dateEntree+")");
												// rset = stmt.executeQuery();
												list.add("INSERT INTO ACTEURS VALUES(" + idArtiste + ",'" + role + "','"
														+ titreEntre + "'," + dateEntree + ")");

												// DATE NAISSANCE
												System.out.println(
														"voulez vous entrer une date de naissance pour cet artiste?(oui/non):");
												ouinon = sc.nextLine();
												if (ouinon.equals("oui")) {
													System.out
															.println("entrez la date de naissance(format JJ-MM-AAAA):");
													String DNArtiste = sc.nextLine();
													// stmt = conn.prepareStatement(
													// "INSERT INTO DATENAISSANCEARTISTE VALUES (" + idArtiste
													// + ",TO_DATE('" + DNArtiste + "'))");
													// rset = stmt.executeQuery();
													list.add("INSERT INTO DATENAISSANCEARTISTE VALUES (" + idArtiste
															+ ",TO_DATE('" + DNArtiste + "'))");
												}
												// BIO
												System.out.println(
														"voulez vous entrer une biographie pour cet artiste?(oui/non):");
												ouinon = sc.nextLine();
												if (ouinon.equals("oui")) {
													System.out.println("entrez la biographie:");
													String BioArtiste = sc.nextLine();
													// stmt = conn.prepareStatement("INSERT INTO BIOGRAPHIE VALUES ("
													// + idArtiste + ",'" + BioArtiste + "')");
													// rset = stmt.executeQuery();
													list.add("INSERT INTO BIOGRAPHIE VALUES (" + idArtiste + ",'"
															+ BioArtiste + "')");
												}
												System.out.println("Voulez vous entrer un autre artiste?:");
												ouinon = sc.nextLine();
												if (ouinon.equals("oui")) {
													entrerActeur = true;
													ouinon = "oui";
												} else {
													entrerActeur = false;
												}

											} else {
												System.out.println("entrée incomprise,ressayez:");
												reponse = sc.nextLine();
											}
										}

									}
								} else if (entree.equals("piste musicale")) {

									signal = true;
									int IDAlbum = 1;
									int NumPiste = 1;
									boolean existePiste = true;
									boolean existeAlbum = true;

									System.out.println("Vous avez choisi piste musicale");
									// lui montrer les pistes existantes
									System.out.println("Voici les pistes musicales existantes");
									stmt = conn.prepareStatement(
											"SELECT a.NomArtiste, a.Titre ,p.Titre,  p.IDAlbum, NumPiste FROM PisteMusicale p JOIN Album a ON p.IDAlbum = a.IDAlbum ORDER BY p.IDAlbum");
									rset = stmt.executeQuery();
									dumpResultSet(rset);

									System.out.println(
											"Voulez-vous créer un nouvel album et l'utiliser pour votre piste ? (oui) (non)");
									String reponse = sc.nextLine();
									// lui proposer d'ajouter une piste musicale
									if (reponse.equals("oui")) {
										existePiste = false;
										existeAlbum = false;
										System.out.println("Création d'un nouvel album");

										System.out.println("Indiquez le titre de votre album");
										String titreAlbum = sc.nextLine();
										System.out.println("Qui a composé l'album ?");
										String nomArtiste = sc.nextLine();
										System.out.println("Quand est sorti l'album ? (format : JJ-MM-AAAA)");
										String dateDeSortie = sc.nextLine();
										System.out.println("Indiquez l'url de l'image de couverture");
										String urlImage = sc.nextLine();
										IDAlbum = findIDAlbum(conn);
										stmt.executeQuery("INSERT INTO Album VALUES(" + IDAlbum + ", '" + titreAlbum
												+ "','" + nomArtiste + "', TO_DATE('" + dateDeSortie + "'),'" + urlImage
												+ "')");

										System.out.println("Voici les categories d'albums existantes");
										stmt = conn.prepareStatement("SELECT * FROM CATEGORIEMUSIQUE");
										rset = stmt.executeQuery();
										dumpResultSet(rset);
										System.out.println(
												"Voulez-vous donner une ou plusieurs catégories à cet album ? (oui) (non)");
										reponse = sc.nextLine();
										boolean chooseCat = false;
										if (reponse.equals("oui"))
											chooseCat = true;

										while (chooseCat) {
											System.out.println("Ecrivez votre catégorie");
											String categorieM = sc.nextLine();
											if (!checkCatM(categorieM, conn)) {
												System.out.println(
														"Cette catégorie n'existe pas, voulez vous l'ajouter ? (oui) (non)");
												reponse = sc.nextLine();
												if (reponse.equals("oui")) {
													stmt = conn
															.prepareStatement("INSERT INTO CATEGORIEMUSIQUE VALUES ('"
																	+ categorieM + "')");
													rset = stmt.executeQuery();
													list.add("INSERT INTO ALBUMACATEGORIESMUSIQUE VALUES(" + IDAlbum
															+ ",'" + categorieM + "')");

												}

											} else
												list.add("INSERT INTO ALBUMACATEGORIESMUSIQUE VALUES(" + IDAlbum + ",'"
														+ categorieM + "')");
											System.out.println("Voulez-vous ajouter une autre catégorie ? (oui) (non)");
											reponse = sc.nextLine();
											if (!reponse.equals("oui"))
												chooseCat = false;

										}

									} else {
										boolean IDValide = false;
										while (!IDValide) {
											System.out.println(
													"Ecrivez l'ID de l'album dans lequel vous voulez ajouter votre fichier");

											IDAlbum = Integer.parseInt(sc.nextLine());
											if (checkIDAlbum(IDAlbum, conn)) {
												IDValide = true;
											}
										}
										System.out.println("Voulez-vous créer une nouvelle piste ? (oui) (non)");
										reponse = sc.nextLine();
										if (reponse.equals("oui")) {
											existePiste = false;
										} else {
											boolean IDPisteValide = false;
											while (!IDPisteValide) {
												System.out.println(
														"Ecrivez le numéro de la piste dans laquelle vous voulez ajouter votre fichier");

												NumPiste = Integer.parseInt(sc.nextLine());
												if (checkPiste(IDAlbum, NumPiste, conn)) {
													IDPisteValide = true;
												}
											}
										}

									}

									// si oui on update uniquement la table piste<->fichier
									if (existePiste) {
										System.out.println("cette piste  existe");
										stmt = conn.prepareStatement("INSERT INTO PISTECONTIENTFICHIERS VALUES (" + id
												+ "," + IDAlbum + "," + NumPiste + ")");
										rset = stmt.executeQuery();
										System.out.println("PISTECONTIENTFICHIERS updated");
									}
									// sinon on creer la piste
									else {
										System.out.println("Ajoutons la piste");
										// DUREE
										System.out.println("Quel est la duree de cette piste musicale?");
										String duree = sc.nextLine();
										// Titre
										String titre;
										System.out.println("Indiquez le titre de votre piste");
										titre = sc.nextLine();

										if (existeAlbum)
											NumPiste = findID(IDAlbum, conn);

										list.add("INSERT INTO PISTEMUSICALE VALUES (" + NumPiste + ",'" + titre + "',"
												+ duree + "," + IDAlbum + ")");

										list.add("INSERT INTO PISTECONTIENTFICHIERS VALUES (" + id + "," + IDAlbum + ","
												+ NumPiste + ")");

										System.out.println("Voici les categories de piste musicales existantes");
										stmt = conn.prepareStatement("SELECT * FROM CATEGORIEMUSIQUE");
										rset = stmt.executeQuery();
										dumpResultSet(rset);

										boolean chooseCat = true;

										while (chooseCat) {
											System.out.println("Ecrivez votre catégorie");
											String categorieM = sc.nextLine();
											if (!checkCatM(categorieM, conn)) {
												System.out.println(
														"Cette catégorie n'existe pas, voulez vous l'ajouter ? (oui) (non)");
												reponse = sc.nextLine();
												if (reponse.equals("oui")) {
													stmt = conn
															.prepareStatement("INSERT INTO CATEGORIEMUSIQUE VALUES ('"
																	+ categorieM + "')");
													rset = stmt.executeQuery();
													list.add("INSERT INTO PISTEMUSICALACATEGORIESMUSIQUE VALUES("
															+ NumPiste + ",'" + categorieM + "'," + IDAlbum + ")");

												}

											} else
												list.add("INSERT INTO PISTEMUSICALACATEGORIESMUSIQUE VALUES(" + NumPiste
														+ ",'" + categorieM + "'," + IDAlbum + ")");
											System.out.println("Voulez-vous ajouter une autre catégorie ? (oui) (non)");
											reponse = sc.nextLine();
											if (!reponse.equals("oui"))
												chooseCat = false;

										}

										// on l'interroge sur les acteurs
										boolean entrerActeur = true;
										System.out.println(
												"Voulez vous entrer un.e musicien.ne en rapport avec cette piste ?(oui/non)");
										String ouinon = sc.nextLine();
										while (entrerActeur) {

											if (ouinon.equals("non")) {
												entrerActeur = false;
											} else if (ouinon.equals("oui")) {
												// Aller checher l'id pour prendre la maximale
												stmt = conn.prepareStatement("SELECT MAX(IDARTISTE) FROM ARTISTE");
												rset = stmt.executeQuery();
												rset.next();
												IDMAX = rset.getInt(1);
												int idArtiste = IDMAX + 1;
												rset.close();
												stmt.close();
												// NOM
												System.out.println("entrez le nom de l'artiste:");
												String nomArtiste = sc.nextLine();
												// URL Photo
												System.out.println("entrez une URL de son image:");
												String urlArtiste = sc.nextLine();
												// SpecialitéGlobale
												System.out.println("Quelle est sa specialité globale?:");
												String speGlobaleArtiste = sc.nextLine();
												stmt = conn.prepareStatement("INSERT INTO ARTISTE VALUES (" + idArtiste
														+ ",'" + nomArtiste + "','" + urlArtiste + "','"
														+ speGlobaleArtiste + "')");
												rset = stmt.executeQuery();

												boolean instruAjoute = false;
												String instrument = "piano";
												while (!instruAjoute) {
													System.out.println("Quel est l'instrument qu'il joue ?:");
													instrument = sc.nextLine();
													if (!checkInstru(instrument, conn)) {
														System.out.println(
																"L'instrument n'est pas dans la base, voulez-vous l'ajouter ? (oui) (non)");
														reponse = sc.nextLine();
														if (reponse.equals("oui")) {
															stmt.executeQuery("INSERT INTO Instruments VALUES('"
																	+ instrument + "')");
															instruAjoute = true;
														}
													} else {
														instruAjoute = true;
													}
												}

												list.add("INSERT INTO MUSICIENS VALUES('" + instrument + "',"
														+ idArtiste + "," + NumPiste + "," + IDAlbum + ")");

												// DATE NAISSANCE
												System.out.println(
														"voulez vous entrer une date de naissance pour cet artiste?(oui/non):");
												ouinon = sc.nextLine();
												if (ouinon.equals("oui")) {
													System.out
															.println("entrez la date de naissance(format JJ-MM-AAAA):");
													String DNArtiste = sc.nextLine();
													// stmt = conn.prepareStatement(
													// "INSERT INTO DATENAISSANCEARTISTE VALUES (" + idArtiste
													// + ",TO_DATE('" + DNArtiste + "'))");
													// rset = stmt.executeQuery();
													list.add("INSERT INTO DATENAISSANCEARTISTE VALUES (" + idArtiste
															+ ",TO_DATE('" + DNArtiste + "'))");
												}
												// BIO
												System.out.println(
														"voulez vous entrer une biographie pour cet artiste?(oui/non):");
												ouinon = sc.nextLine();
												if (ouinon.equals("oui")) {
													System.out.println("entrez la biographie:");
													String BioArtiste = sc.nextLine();
													// stmt = conn.prepareStatement("INSERT INTO BIOGRAPHIE VALUES ("
													// + idArtiste + ",'" + BioArtiste + "')");
													// rset = stmt.executeQuery();
													list.add("INSERT INTO BIOGRAPHIE VALUES (" + idArtiste + ",'"
															+ BioArtiste + "')");
												}
												System.out.println("Voulez vous entrer un autre artiste?:");
												ouinon = sc.nextLine();
												if (ouinon.equals("oui")) {
													entrerActeur = true;
													ouinon = "oui";
												} else {
													entrerActeur = false;
												}

											} else {
												System.out.println("entrée incomprise,ressayez:");
												reponse = sc.nextLine();
											}
										}

									}
									signal = true;

								}
							}

							state = "confirm";
						}

					} catch (Exception e) {
						System.out.println("Failed \n");
					}
				}

				else if (state.equals("delete")) {
					String entree_d;
					System.out.println("vous etes dans l'etat delete");
					System.out.println("Voulez vous supprimer un film (f) ou une piste musicale (m)?");
					entree_d = sc.nextLine();

					if (entree_d.equals("f")) {
						// On supprime le film

						Statement stmt = conn.createStatement();
						// Execution de la requete

						ResultSet rset = stmt.executeQuery("SELECT Titre, AnneeSortie FROM Film");

						System.out.println(
								"Ecrivez le titre et l'année de sortie du film que vous voulez supprimez en les espacant \n");

						dumpResultSet(rset);
						ResultSet nombreFilmAvant = stmt.executeQuery("SELECT COUNT(*) FROM Film");
						nombreFilmAvant.next();
						String filmAvant = nombreFilmAvant.getString(1);

						String titre;
						String AS;
						String s;
						// stmt = conn.createStatement();
						// Execution de la requete
						ResultSet nombreFilmApres;
						String filmApres;

						boolean FilmNotExist = true;
						while (FilmNotExist) {
							boolean malecrit = true;

							while (malecrit) {
								entree_d = sc.nextLine();
								try {

									int index = entree_d.lastIndexOf(" ");
									titre = entree_d.substring(0, index);
									AS = entree_d.substring(index + 1);
									s = "DELETE Film WHERE Titre = '" + titre + "' AND AnneeSortie = " + AS;
									// stmt = conn.createStatement();
									// Execution de la requete
									rset = stmt.executeQuery(s);
									nombreFilmApres = stmt.executeQuery("SELECT COUNT(*) FROM Film");
									nombreFilmApres.next();
									filmApres = nombreFilmApres.getString(1);
									if (filmAvant.equals(filmApres)) {
										System.out.println(
												"Le film choisi n'existe pas, voulez vous recommencer ? (oui) (non)");
										entree_d = sc.nextLine();
										if (entree_d.equals("non"))
											FilmNotExist = false;
										else
											System.out.println(
													"Ecrivez le titre et l'année de sortie du film que vous voulez supprimez en les espacant \n");
									} else
										FilmNotExist = false;

									malecrit = false;
								} catch (Exception re) {
									System.out.println(
											" Votre syntaxe est mauvaise. Ecrivez le titre espace l'année de sortie");
								}
							}
						}

						// On regarde si les artistes ou les fichiers servent encore.
						checkArtistes(conn);
						checkFichier(conn);

					} else {
						String IDA = "";
						String NUM = "";
						Statement stmt = conn.createStatement();
						ResultSet piste = stmt.executeQuery("SELECT COUNT(*) FROM PisteMusicale");
						ResultSet nvpiste;
						String nbnvpiste;
						piste.next();
						String nbPiste = piste.getString(1);

						ResultSet rset = stmt.executeQuery(
								"SELECT a.NomArtiste, a.Titre ,p.Titre,  p.IDAlbum, NumPiste FROM PisteMusicale p JOIN Album a ON p.IDAlbum = a.IDAlbum ORDER BY p.IDAlbum");
						System.out.println(
								"Ecrivez l'ID de l'album et le numéro de la piste que vous voulez effacer : ID1 Piste1 \n");
						dumpResultSet(rset);

						boolean pisteNotExist = true;
						while (pisteNotExist) {
							boolean malecrit = true;

							while (malecrit) {
								entree_d = sc.nextLine();
								try {

									int index = entree_d.lastIndexOf(" ");
									IDA = entree_d.substring(0, index);
									NUM = entree_d.substring(index + 1);
									String s = "DELETE PisteMusicale WHERE NumPiste = " + NUM + " AND IDAlbum = " + IDA;
									// stmt = conn.createStatement();
									// Execution de la requete
									rset = stmt.executeQuery(s);
									nvpiste = stmt.executeQuery("SELECT COUNT(*) FROM PisteMusicale");
									nvpiste.next();
									nbnvpiste = nvpiste.getString(1);
									if (nbPiste.equals(nbnvpiste)) {
										System.out.println(
												"La piste choisie n'existe pas, voulez vous recommencer ? (oui) (non)");
										entree_d = sc.nextLine();
										if (entree_d.equals("non"))
											pisteNotExist = false;
										else
											System.out.println(
													"Ecrivez l'ID de l'album et le numéro de la piste que vous voulez effacer : ID1 Piste1 \n");
									} else
										pisteNotExist = false;

									malecrit = false;
								} catch (ArrayIndexOutOfBoundsException | SQLSyntaxErrorException re) {
									System.out.println(
											" Votre syntaxe est mauvaise. Ecrivez l'ID de l'album espace le Numero de Piste");
								}
							}
						}

						checkFichier(conn);
						checkAlbums(conn);

					}

					state = "confirm";
				} else if (state.equals("confirm")) {
					System.out.println("Vous confirmez votre action?(oui/non)");
					boolean compris = false;
					while (!compris) {
						entree = sc.nextLine();
						if (entree.equals("oui")) {
							compris = true;
							state = "main";
							conn.commit();
							System.out.println("action confirmée, retour a l'etat main");
							if (!list.isEmpty()) {
								Statement stmt = conn.createStatement();
								for (String cmd : list) {
									stmt.executeQuery(cmd);
								}
								list.clear();
								conn.commit();
							}

						} else if (entree.equals("non")) {
							compris = true;
							state = "main";
							conn.rollback();
							System.out.println("action abondonnée, retour a l'etat main");
						} else {
							System.out.println("entrée incomprise , reessayez:");
						}
					}
				}
			}

			// stmt.close();
			conn.close();

		} catch (SQLException e) {
			System.err.println("failed");
			e.printStackTrace(System.err);
		}

	}




	private void dumpResultSet(ResultSet rset) throws SQLException {
		int espace = 20;
		ResultSetMetaData rsetmd = rset.getMetaData();
		int i = rsetmd.getColumnCount();
		if (rset.next()) {
			for (int j = 1; j <= i; j++) {
				System.out.print(rsetmd.getColumnLabel(j));
				for (int k = rsetmd.getColumnLabel(j).length(); k < 20; k++)
					System.out.print(" ");
			}
			System.out.println("\n");

			for (int j = 1; j <= i; j++) {
				System.out.print(rset.getString(j));
				for (int k = rset.getString(j).length(); k < 20; k++)
					System.out.print(" ");
			}
			System.out.println();

			while (rset.next()) {
				for (int j = 1; j <= i; j++) {
					System.out.print(rset.getString(j));
					for (int k = rset.getString(j).length(); k < 20; k++)
						System.out.print(" ");
				}
				System.out.println();
			}
		}
	}

	private void checkArtistes(Connection c) throws SQLException {
		
		
		LinkedList<String> artistes = new LinkedList<String>();
		LinkedList<String> musiciens = new LinkedList<String>();
		LinkedList<String> acteurs = new LinkedList<String>();
		
		Statement stmt = c.createStatement();
		ResultSet rset = stmt.executeQuery("SELECT IDArtiste FROM Artiste");

		
		while(rset.next()) {
			artistes.add(rset.getString(1));
		}
		
		stmt = c.createStatement();
		rset = stmt.executeQuery("SELECT Artiste FROM Musiciens");
		while(rset.next()) {
			musiciens.add(rset.getString(1));
		}
		
		stmt = c.createStatement();
		rset = stmt.executeQuery("SELECT Artiste FROM Acteurs");
		while(rset.next()) {
			acteurs.add(rset.getString(1));
		}


		for (String s : artistes) {
			boolean fichierValide = false;
			
			for (String p : musiciens) {
				if (p.equals(s)) {
					fichierValide = true;
				}
			}
			
			for (String f : acteurs) {
				if (f.equals(s)) {
					fichierValide = true;
				}
			}
			
			if (!fichierValide) {
				stmt = c.createStatement();
				rset = stmt.executeQuery("DELETE FROM Artiste WHERE IDArtiste = " + s);
			}
		}
	}

	private void checkFichier(Connection c) throws SQLException {
		LinkedList<String> fichiers = new LinkedList<String>();
		LinkedList<String> pistes = new LinkedList<String>();
		LinkedList<String> films = new LinkedList<String>();
		
		Statement stmt = c.createStatement();
		ResultSet rset = stmt.executeQuery("SELECT IDFichier FROM Fichier");

		
		while(rset.next()) {
			fichiers.add(rset.getString(1));
		}
		
		stmt = c.createStatement();
		rset = stmt.executeQuery("SELECT IDFichier FROM PisteContientFichiers");
		while(rset.next()) {
			pistes.add(rset.getString(1));
		}
		
		stmt = c.createStatement();
		rset = stmt.executeQuery("SELECT IDFichier FROM FilmContientFichiers");
		while(rset.next()) {
			pistes.add(rset.getString(1));
		}


		for (String s : fichiers) {
			boolean fichierValide = false;
			
			for (String p : pistes) {
				if (p.equals(s)) {
					fichierValide = true;
				}
			}
			
			for (String f : films) {
				if (f.equals(s)) {
					fichierValide = true;
				}
			}
			
			if (!fichierValide) {
				stmt = c.createStatement();
				rset = stmt.executeQuery("DELETE FROM Fichier WHERE IDFichier = " + s);
			}
		}
	}

	private void checkAlbums(Connection c) throws SQLException {

		
		LinkedList<String> albums = new LinkedList<String>();
		LinkedList<String> pistes = new LinkedList<String>();
	
		
		Statement stmt = c.createStatement();
		ResultSet rset = stmt.executeQuery("SELECT IDAlbum FROM Album");

		
		while(rset.next()) {
			albums.add(rset.getString(1));
		}
		
		stmt = c.createStatement();
		rset = stmt.executeQuery("SELECT IDAlbum FROM PisteMusicale");
		while(rset.next()) {
			pistes.add(rset.getString(1));
		}
		

		for (String s : albums) {
			boolean fichierValide = false;
			
			for (String p : pistes) {
				if (p.equals(s)) {
					fichierValide = true;
				}
			}
			
			if (!fichierValide) {
				stmt = c.createStatement();
				rset = stmt.executeQuery("DELETE FROM Album WHERE IDAlbum = " + s);
			}
		}
	}

	private void printLangue(Connection c) throws SQLException {
		Statement stmt = c.createStatement();
		ResultSet rset = stmt.executeQuery("SELECT Langue FROM Langues");
		dumpResultSet(rset);
	}

	private boolean checkLangueVraie(String l, Connection c) throws SQLException {
		Statement stmt = c.createStatement();
		ResultSet rset = stmt.executeQuery("SELECT Langue FROM Langues");
		String lToCompare;

		while (rset.next()) {
			lToCompare = rset.getString(1);
			if (l.equals(lToCompare))
				return true;

		}
		return false;
	}

	private boolean checkCatFilm(String cat, Connection c) throws SQLException {
		Statement stmt = c.createStatement();
		ResultSet rset = stmt.executeQuery("SELECT NomCategorie FROM CategorieFilm");
		String catToCompare;

		while (rset.next()) {
			catToCompare = rset.getString(1);
			if (cat.equals(catToCompare))
				return true;

		}
		return false;
	}

	private boolean checkIDAlbum(int id, Connection c) throws SQLException {
		Statement stmt = c.createStatement();
		ResultSet rset = stmt.executeQuery("SELECT IDAlbum FROM Album");
		int idToCompare;

		while (rset.next()) {
			idToCompare = Integer.parseInt(rset.getString(1));
			if (id == idToCompare)
				return true;

		}
		return false;
	}

	private boolean checkPiste(int ida, int np, Connection c) throws SQLException {
		Statement stmt = c.createStatement();
		ResultSet rset = stmt.executeQuery("SELECT IDAlbum, NumPiste FROM PisteMusicale");
		int idaTC;
		int npTC;

		while (rset.next()) {
			idaTC = Integer.parseInt(rset.getString(1));
			npTC = Integer.parseInt(rset.getString(2));
			if (ida == idaTC && np == npTC)
				return true;

		}
		return false;
	}

	private boolean checkCatM(String cat, Connection c) throws SQLException {
		Statement stmt = c.createStatement();
		ResultSet rset = stmt.executeQuery("SELECT NomCategorie FROM CategorieMusique");
		String catToCompare;

		while (rset.next()) {
			catToCompare = rset.getString(1);
			if (cat.equals(catToCompare))
				return true;

		}
		return false;
	}

	private boolean checkInstru(String i, Connection c) throws SQLException {
		Statement stmt = c.createStatement();
		ResultSet rset = stmt.executeQuery("SELECT Instrument FROM Instruments");
		String instruToCompare;

		while (rset.next()) {
			instruToCompare = rset.getString(1);
			if (i.equals(instruToCompare))
				return true;

		}
		return false;
	}

	private int findID(int ida, Connection c) throws SQLException {
		Statement stmt = c.createStatement();
		ResultSet rset = stmt.executeQuery("SELECT MAX(NumPiste) FROM PisteMusicale WHERE IDAlbum = " + ida);
		rset.next();
		return Integer.parseInt(rset.getString(1)) + 1;
	}

	private int findIDAlbum(Connection c) throws SQLException {
		Statement stmt = c.createStatement();
		ResultSet rset = stmt.executeQuery("SELECT MAX(IDAlbum) FROM Album");
		rset.next();
		return Integer.parseInt(rset.getString(1)) + 1;
	}

	public static void main(String[] args) {
		new BD();
	}

}
