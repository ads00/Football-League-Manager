public interface LeagueManager {
	boolean addClub(); //Add a club to the league
	
	boolean relegateClub(); //Relegate(Delete) a club from the league
	
	boolean displayStats(); //Display statistics of a club
	
	boolean addMatch(); //Add a played match
	
	boolean displayTable(); //Displays the standings of the teams in the league
}
