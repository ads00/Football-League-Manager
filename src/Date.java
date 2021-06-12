import java.io.Serializable;
import java.util.Objects;

public class Date implements Serializable, Comparable<Date> { //Object can be serialized
	private int day;
	private int month;
	private int year;
	
	public Date(int day, int month, int year) {
		this.day = day;
		this.month = month;
		this.year = year;
	}
	
	public int getDay() {
		return day;
	}
	
	public void setDay(int day) {
		this.day = day;
	}
	
	public int getMonth() {
		return month;
	}
	
	public void setMonth(int month) {
		this.month = month;
	}
	
	public int getYear() {
		return year;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	@Override
	public boolean equals(Object o) { //Checks if the day, month and year are eqaual
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Date date = (Date) o;
		return day == date.day &&
				month == date.month &&
				year == date.year;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(day, month, year);
	}
	
	@Override
	public String toString() {
		String delimiter = "/";
		return (day + delimiter + month + delimiter + year);
	}
	
	@Override
	public int compareTo(Date o) {
		if (this.getYear() > o.getYear()) {
			return 1;
		} else if (this.getYear() < o.getYear()) {
			return -1;
		} else {
			if (this.getMonth() > o.getMonth()) {
				return 1;
			} else if (this.getMonth() < o.getMonth()) {
				return -1;
			} else {
				if (this.getDay() > o.getDay()) {
					return 1;
				} else if (this.getDay() < o.getDay()) {
					return -1;
				} else {
					return 0;
				}
			}
		}
	}
}
