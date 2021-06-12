public class CustomOutputDataType {
	private int position;
	private String name;
	private int matchesPlayed;
	private int wins;
	private int losses;
	private int draws;
	private int points;
	private int goalsFor;
	private int goalsAgainst;
	private int goalDiff;
	
	public CustomOutputDataType(int position, String name, int matchesPlayed, int wins, int losses, int draws, int points, int goalsFor, int goalsAgainst, int goalDiff) {
		this.position = position;
		this.name = name;
		this.matchesPlayed = matchesPlayed;
		this.wins = wins;
		this.losses = losses;
		this.draws = draws;
		this.points = points;
		this.goalsFor = goalsFor;
		this.goalsAgainst = goalsAgainst;
		this.goalDiff = goalDiff;
	}
	
	public int getPosition() {
		return position;
	}
	
	public void setPosition(int position) {
		this.position = position;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getMatchesPlayed() {
		return matchesPlayed;
	}
	
	public void setMatchesPlayed(int matchesPlayed) {
		this.matchesPlayed = matchesPlayed;
	}
	
	public int getWins() {
		return wins;
	}
	
	public void setWins(int wins) {
		this.wins = wins;
	}
	
	public int getLosses() {
		return losses;
	}
	
	public void setLosses(int losses) {
		this.losses = losses;
	}
	
	public int getDraws() {
		return draws;
	}
	
	public void setDraws(int draws) {
		this.draws = draws;
	}
	
	public int getPoints() {
		return points;
	}
	
	public void setPoints(int points) {
		this.points = points;
	}
	
	public int getGoalsFor() {
		return goalsFor;
	}
	
	public void setGoalsFor(int goalsFor) {
		this.goalsFor = goalsFor;
	}
	
	public int getGoalsAgainst() {
		return goalsAgainst;
	}
	
	public void setGoalsAgainst(int goalsAgainst) {
		this.goalsAgainst = goalsAgainst;
	}
	
	public int getGoalDiff() {
		return goalDiff;
	}
	
	public void setGoalDiff(int goalDiff) {
		this.goalDiff = goalDiff;
	}
}
