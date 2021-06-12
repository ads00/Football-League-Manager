import java.io.Serializable;
import java.util.Objects;

public class Match implements Serializable, Comparable<Match> {
	private Date date; //dd-mm-yyyy
	private String homeTeam;
	private String awayTeam;
	private int homeGoals;
	private int awayGoals;
	
	public Match(Date date, String homeTeam, String awayTeam, int homeGoals, int awayGoals) {
		this.date = date;
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.homeGoals = homeGoals;
		this.awayGoals = awayGoals;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getHomeTeam() {
		return homeTeam;
	}
	
	public void setHomeTeam(String homeTeam) {
		this.homeTeam = homeTeam;
	}
	
	public String getAwayTeam() {
		return awayTeam;
	}
	
	public void setAwayTeam(String awayTeam) {
		this.awayTeam = awayTeam;
	}
	
	public int getHomeGoals() {
		return homeGoals;
	}
	
	public void setHomeGoals(int homeGoals) {
		this.homeGoals = homeGoals;
	}
	
	public int getAwayGoals() {
		return awayGoals;
	}
	
	public void setAwayGoals(int awayGoals) {
		this.awayGoals = awayGoals;
	}
	
	@Override
	public String toString() {
		return (date + "/" + homeTeam + "/" + awayTeam + "/" + homeGoals + "/" + awayGoals);
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Match match = (Match) o;
		return this.homeTeam.toLowerCase().equals(match.getHomeTeam().toLowerCase()) &&
				this.awayTeam.toLowerCase().equals(match.getAwayTeam().toLowerCase());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(date, homeTeam, awayTeam, homeGoals, awayGoals);
	}
	
	
	@Override
	public int compareTo(Match match) {
		return this.date.compareTo(match.getDate());
	}
}
