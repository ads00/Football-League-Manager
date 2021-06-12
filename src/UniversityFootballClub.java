import java.util.Objects;

public class UniversityFootballClub extends FootballClub {
	private final int MAX_AGE = 22;
	
	public UniversityFootballClub(String name, String location) {
		super(name, location);
	}
	
	public int getMAX_AGE() {
		return MAX_AGE;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		UniversityFootballClub club = (UniversityFootballClub) o;
		return this.getName().equalsIgnoreCase(club.getName()) && this.getMAX_AGE() == club.getMAX_AGE();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), MAX_AGE);
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
}
