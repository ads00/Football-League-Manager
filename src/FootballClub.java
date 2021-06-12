import java.io.Serializable;

public class FootballClub extends SportsClub implements Serializable, Comparable<FootballClub> {
	private int draws; //Total number of matches with the same amount of goals scored by both teams
	private int goalsFor; //Total number of goals scored by the team for the entire season
	private int goalsAgainst; //Total number of goals conceded by the team for the entire season
	private int goalDiff; //The goal difference (goals scored - goals conceded) of their season
	
	public FootballClub(String name, String location) {
		super(name, location);
		this.draws = 0;
		this.goalsFor = 0;
		this.goalsAgainst = 0;
		this.goalDiff = 0;
	}
	
	public int getDraws() {
		return draws;
	}
	
	public void setDraws(int draws) {
		this.draws = draws;
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
	
	@Override
	public String toString() {
		String delimiter = "/";
		return (super.toString() + delimiter + this.getMatchesPlayed() + delimiter + this.getWins() + delimiter + this.getLosses() + delimiter + this.draws + delimiter + this.getPoints() + delimiter + this.goalsFor + delimiter + this.goalsAgainst + delimiter + this.goalDiff);
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		FootballClub club = (FootballClub) o;
		return this.getName().equals(club.getName());
	}
	
	@Override
	public int hashCode() {
		return super.hashCode(); //Each name is unique
	}
	
	@Override
	public int compareTo(FootballClub club) {
		if (this.getPoints() < club.getPoints()) {
			return 1;
		} else if (this.getPoints() > club.getPoints()) {
			return -1;
		} else {
			if (this.getGoalDiff() < club.getGoalDiff()) {
				return 1;
			} else if (this.getGoalDiff() > club.getGoalDiff()) {
				return -1;
			} else {
				if (this.getGoalsFor() < club.getGoalsFor()) {
					return 1;
				} else if (this.getGoalsFor() > club.getGoalsFor()) {
					return -1;
				} else {
					return (this.getName().compareToIgnoreCase(club.getName())); //Compares the name
				}
			}
		}
	}
}
