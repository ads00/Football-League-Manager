import java.io.Serializable;
import java.util.Objects;

public abstract class SportsClub implements Serializable {
	private final String name;
	private final String location;
	private int matchesPlayed; //Number of matches played by the team
	private int wins; //Total number of wins earned by the team in the season
	private int losses; //Total number of losses faced by the team in the season
	private int points; //Total points earned by the team
	
	public SportsClub(String name, String location) {
		this.name = name;
		this.location = location;
		this.matchesPlayed = 0;
		this.wins = 0;
		this.losses = 0;
		this.points = 0;
	}
	
	public String getName() {
		return name;
	}
	
	public String getLocation() {
		return location;
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
	
	public int getPoints() {
		return points;
	}
	
	public void setPoints(int points) {
		this.points = points;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SportsClub club = (SportsClub) o;
		return this.name.equalsIgnoreCase(club.name); //Each name is unique so comparison of name is enough
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name); //Each name is unique
	}
	
	@Override
	public String toString() {
		return (name + "/" + location);
	}
}
