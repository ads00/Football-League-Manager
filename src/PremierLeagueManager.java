import java.util.ArrayList;
import java.util.List;

import java.io.*;
import java.util.*;

public class PremierLeagueManager implements LeagueManager, Serializable {
	private Scanner sc = new Scanner(System.in);
	private int season;
	private List<FootballClub> league; //Used to store the teams
	private List<Match> matches; //Used to store the matches
	
	private final File LEAGUE_FILE = new File("LeagueFile.txt");
	private final File MATCHES_FILE = new File("MatchesFile.txt");
	private final File SEASON_FILE = new File("SeasonFile.txt");
	
	public PremierLeagueManager() {
		season = 0;
		league = new ArrayList<FootballClub>();
		matches = new ArrayList<Match>();
	}
	
	public int getSeason() {
		return season;
	}
	
	public int getLeagueSize() {
		return league.size();
	}
	
	public List<FootballClub> getLeague() {
		return league;
	}
	
	public void setLeague(List<FootballClub> league) {
		this.league = league;
	}
	
	public List<Match> getMatches() {
		return matches;
	}
	
	public void setMatches(List<Match> matches) {
		this.matches = matches;
	}
	
	public void checkIfResumingProgress() {
		//Checks if the file exists
		//If it doesn't, it creates a file
		try {
			if (LEAGUE_FILE.createNewFile()) {
				System.out.println("League file did not exist");
			}
			if (MATCHES_FILE.createNewFile()) {
				System.out.println("Matches file did not exist");
			}
			if (SEASON_FILE.createNewFile()) {
				System.out.println("Seasons file did not exist");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String load;
		while (true) { //To check if the user wants to resume their progress(if there is any) or start a new one
			load = ((validateString("Do you want to resume your progress(y or n): ")).toLowerCase());
			if (load.equals("y")) {
				resumeProgress(); //reads from saved file
				break;
			} else if (load.equals("n")) {
				System.out.println("Your progress was not loaded. If you had a progress, your previous progress will remain until you save a new progress\n\n");
				season = validateInteger("Which season do you want to start: ");
				while (!(season > 0)) {
					System.out.println("Please enter a valid year");
					season = validateInteger("Which season do you want to start: ");
				}
				break;
			} else {
				System.out.println("Please enter y or n !!!");
			}
		}
	}
	
	public boolean addClub() {
		final int MAX_TEAMS = 20; //Number of teams in the premier league(https://en.wikipedia.org/wiki/Premier_League)
		
		if (league.size() <= MAX_TEAMS) { //Checking if the league is full
			String name = validateString("Please enter the name of the club: ");
			
			for (FootballClub club : league) {
				if (club.getName().equalsIgnoreCase(name)) {
					System.out.println(name + " is already a club");
					return false; //Exits the loop because only one club with that can exist and the name already exists
				}
			}
			
			String location = validateString("Please enter the location of the club: ");
			
			league.add(new FootballClub(name, location));
			return true;
		} else {
			System.out.println("Maximum number of teams already present");
			return false;
		}
	}
	
	public boolean relegateClub() {
		String name = validateString("Please enter the name of the club: ");
		for (FootballClub club : league) {
			if (club.getName().equalsIgnoreCase(name)) {
				removeTeam(club); //Removes the team. Matches played are also removed as it may be disadvantageous to teams who didn't play the relegated team
				return true;
			}
		}
		System.out.println("The club was not found");
		return false;
	}
	
	private void removeTeam(FootballClub clubToRemove) {
		//Removing the matches of the team that was removed as it will be unfair in the scores in the premier league table
		// for the team(s) which didn't play the team to be removed
		List<Match> toRemove = new ArrayList<Match>();
		for (Match match : matches) {
			if (clubToRemove.getName().equalsIgnoreCase(match.getHomeTeam()) || clubToRemove.getName().equalsIgnoreCase(match.getAwayTeam())) {
				toRemove.add(match);
			}
		}
		matches.removeAll(toRemove);
		System.out.println(clubToRemove.getName() + " has been relegated");
		league.remove(clubToRemove);
		updateStats();
	}
	
	private void updateStats() { //To reload data to the clubs using the matches left
		for (FootballClub club : league) {
			clearStats(club); //The football related data is cleared
			for (Match match : matches) {
				if (match.getHomeTeam().equalsIgnoreCase(club.getName())) {
					addMatchToClub(club, match.getHomeGoals(), match.getAwayGoals());
				} else if (match.getAwayTeam().equalsIgnoreCase(club.getName())) {
					addMatchToClub(club, match.getAwayGoals(), match.getHomeGoals());
				}
			}
		}
	}
	
	public void clearStats(FootballClub club) {
		club.setGoalsFor(0);
		club.setGoalsAgainst(0);
		club.setGoalDiff(0);
		club.setWins(0);
		club.setLosses(0);
		club.setDraws(0);
		club.setMatchesPlayed(0);
		club.setPoints(0);
	}
	
	public boolean displayStats() {
		String name = validateString("Please enter the name of the club: ");
		for (FootballClub club : league) {
			if (club.getName().equalsIgnoreCase(name)) {
				System.out.println("Name : " + club.getName());
				System.out.println("Location : " + club.getLocation());
				System.out.println("Statistics of " + club.getName() + " for the season " + season + "/" + (season + 1) + ": ");
				System.out.println("Matches played: " + club.getMatchesPlayed());
				System.out.println("Wins : " + club.getWins());
				System.out.println("Losses : " + club.getLosses());
				System.out.println("Draws : " + club.getDraws());
				System.out.println("Points : " + club.getPoints());
				System.out.println("Goals For : " + club.getGoalsFor());
				System.out.println("Goals Against : " + club.getGoalsAgainst());
				System.out.println("Goal Difference : " + club.getGoalDiff());
				return true;
			}
		}
		System.out.println("The club was not found");
		return false;
	}
	
	public boolean displayTable() {
		if (league.size() < 2) { //Need a minimum of two teams
			System.out.println("Not enough teams for a display of table(Minimum of 2 is required)");
			return false;
		} else {
			int highestNameLength = -1;
			int position = 0;
			
			Collections.sort(league); //Sorts the league according to the compareTo method of FootballClub
			
			System.out.print("Position  ");
			
			for (FootballClub club : league) {
				if (club.getName().length() > highestNameLength) {
					highestNameLength = (club.getName().length());
				}
			}
			highestNameLength += 5;
			
			System.out.print("Name");
			for (int i = 0; i < (highestNameLength - 4); i++) {
				System.out.print(" ");
			}
			System.out.print("Games Played  Won  Lost  Draw  Points  Goals Scored  Goals Conceded  Goal Difference\n");
			for (FootballClub club : league) {
				//Position
				position++;
				if (position > 9) {
					System.out.print(position);
				} else {
					System.out.print("0" + position);
				}
				System.out.print("        ");
				
				//Name
				System.out.print(club.getName());
				for (int i = 0; i < (highestNameLength - (club.getName().length())); i++) {
					System.out.print(" ");
				}
				
				//Matches played
				if (club.getMatchesPlayed() < 10) {
					System.out.print("0");
				}
				System.out.print(club.getMatchesPlayed() + "            ");
				
				//Wins
				if (club.getWins() < 10) {
					System.out.print("0");
				}
				System.out.print(club.getWins() + "   ");
				
				//Losses
				if (club.getLosses() < 10) {
					System.out.print("0");
				}
				System.out.print(club.getLosses() + "    ");
				
				//Draws
				if (club.getDraws() < 10) {
					System.out.print("0");
				}
				System.out.print(club.getDraws() + "    ");
				
				//Points
				if (club.getPoints() < 10) {
					System.out.print("00");
				} else if (club.getPoints() < 100) {
					System.out.print("0");
				}
				System.out.print(club.getPoints() + "     ");
				
				//Goals For
				if (club.getGoalsFor() < 10) {
					System.out.print("00");
				} else if (club.getGoalsFor() < 100) {
					System.out.print("0");
				}
				System.out.print(club.getGoalsFor() + "           ");
				
				//Goals Against
				if (club.getGoalsAgainst() < 10) {
					System.out.print("00");
				} else if (club.getGoalsAgainst() < 100) {
					System.out.print("0");
				}
				System.out.print(club.getGoalsAgainst() + "             ");
				
				//Goal Difference
				if (club.getGoalDiff() < 10 && club.getGoalDiff() >= 0) {
					System.out.print("+0" + club.getGoalDiff());
				} else if (club.getGoalDiff() < 100 && club.getGoalDiff() >= 0) {
					System.out.print("+" + club.getGoalDiff());
				} else if (club.getGoalDiff() > -10) {
					int goalDiff = (club.getGoalDiff() + (club.getGoalDiff() * -2));
					System.out.print("-0" + goalDiff);
				} else if (club.getGoalDiff() > -100) {
					int goalDiff = (club.getGoalDiff() + (club.getGoalDiff() * -2));
					System.out.print("-" + goalDiff);
				}
				
				System.out.println();
			}
			return true;
		}
	}
	
	public boolean addMatch() {
		if (league.size() < 2) { //Need a minimum of two teams
			System.out.println("Not enough teams for a match(Minimum of 2 is required)");
			return false;
		} else {
			if (!(matches.size() == (league.size() * (league.size() - 1)))) {
				boolean hTPresent = false;
				boolean aTPresent = false;
				int day = 0;
				int month = 0;
				int year = 0;
				int homeGoals;
				int awayGoals;
				String homeTeam;
				String awayTeam;
				
				homeTeam = validateString("Please enter the home team: ");
				awayTeam = validateString("Please enter the away team: ");
				
				//Checks if the home team and the away team are the same(Same team can't play itself)
				if (homeTeam.equalsIgnoreCase(awayTeam)) {
					System.out.println("The team can't play itself");
					return false;
				}
				
				//Checks if the home team and the away team are valid teams that are already in the league
				//Home Team and Away Team names obtained to maintain consistency in the team names in the matches
				for (FootballClub club : league) {
					if (club.getName().equalsIgnoreCase(homeTeam)) {
						hTPresent = true;
						homeTeam = club.getName();
					} else if (club.getName().equalsIgnoreCase(awayTeam)) {
						aTPresent = true;
						awayTeam = club.getName();
					}
				}
				
				if (!hTPresent) {
					System.out.println("The home team is not a valid team(i.e not in the league)");
					return false;
				}
				if (!aTPresent) {
					System.out.println("The away team is not a valid team(i.e not in the league)");
					return false;
				}
				
				//Checks if the match has already been played(Same match can't happen twice in a league)
				for (Match match : matches) {
					if (match.getHomeTeam().equalsIgnoreCase(homeTeam) && match.getAwayTeam().equalsIgnoreCase(awayTeam)) {
						System.out.println("This match has already been played");
						return false;
					}
				}
				
				//Checks if the date is valid
				boolean validDate = false;
				while (!validDate) {
					day = validateInteger("Please enter the day: ");
					month = validateInteger("Please enter the month: ");
					year = validateInteger("Please enter the year: ");
					validDate = verifyDate(day, month, year);
					if (!validDate) {
						System.out.println("Invalid date! Try again.");
					}
				}
				
				Date date = new Date(day, month, year);
				
				//Checks if either the home team or the away team has a match on the same date which is not possible to happen
				boolean homeTeamHasGame = false, awayTeamHasGame = false;
				for (Match match : matches) {
					if (match.getDate().equals(date)) {
						if (match.getHomeTeam().equalsIgnoreCase(homeTeam) || match.getAwayTeam().equalsIgnoreCase(homeTeam)) {
							homeTeamHasGame = true;
							break;
						}
						if (match.getHomeTeam().equalsIgnoreCase(awayTeam) || match.getAwayTeam().equalsIgnoreCase(awayTeam)) {
							awayTeamHasGame = true;
							break;
						}
					}
				}
				
				if (homeTeamHasGame) {
					System.out.println("Invalid Entry. Home team already has a game on that day");
					return false;
				}
				if (awayTeamHasGame) {
					System.out.println("Invalid Entry. Away team already has a game on that day");
					return false;
				}
				
				//Gets the number of goals scored by both teams
				homeGoals = validateInteger("Enter the number of goals scored by the home team: ");
				if (homeGoals < 0) {
					System.out.println("Home goals can't be negative");
					return false;
				}
				awayGoals = validateInteger("Enter the number of goals scored by the away team: ");
				if (awayGoals < 0) {
					System.out.println("Away goals can't be negative");
					return false;
				}
				
				//Creates a match and adds it to league and updates the teams
				for (FootballClub club : league) {
					if (club.getName().equalsIgnoreCase(homeTeam)) {
						addMatchToClub(club, homeGoals, awayGoals);
					}
					if (club.getName().equalsIgnoreCase(awayTeam)) {
						addMatchToClub(club, awayGoals, homeGoals);
					}
				}
				matches.add(new Match(date, homeTeam, awayTeam, homeGoals, awayGoals));
				return true;
			} else {
				System.out.println("All Matches have been played for now");
				return false;
			}
		}
	}
	
	public void addMatchToClub(FootballClub club, int gF, int gA) {
		final int WIN_POINTS = 3; //Points earned for a win
		final int DRAW_POINTS = 1; //Points earned for a draw
		
		club.setMatchesPlayed(club.getMatchesPlayed() + 1);
		club.setGoalsFor(club.getGoalsFor() + gF);
		club.setGoalsAgainst(club.getGoalsAgainst() + gA);
		club.setGoalDiff(club.getGoalDiff() + (gF - gA));
		if (gF > gA) {
			club.setWins(club.getWins() + 1);
			club.setPoints(club.getPoints() + WIN_POINTS);
		} else if (gF < gA) {
			club.setLosses(club.getLosses() + 1);
		} else {
			club.setDraws(club.getDraws() + 1);
			club.setPoints(club.getPoints() + DRAW_POINTS);
		}
	}
	
	public boolean verifyDate(int day, int month, int year) {
		final List<Integer> MAX_DAYS = new ArrayList<Integer>(Arrays.asList(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31, 29)); //ArrayList with all the number of days in each month entered
		int maxDaysInMonth;
		
		//Season runs from August to May(https://en.wikipedia.org/wiki/Premier_League)
		if (year == season) {
			if (month >= 8 && month <= 12) {
				maxDaysInMonth = MAX_DAYS.get(month - 1);
				return day >= 1 && day <= maxDaysInMonth;
			} else {
				return false;
			}
		} else if (year == (season + 1)) {
			if (month >= 1 && month <= 5) {
				if ((year % 4) == 0 && month == 2) {
					maxDaysInMonth = MAX_DAYS.get(12);
				} else {
					maxDaysInMonth = MAX_DAYS.get(month - 1);
				}
				return day >= 1 && day <= maxDaysInMonth;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public void resumeProgress() {
		//Reading the clubs
		try (FileInputStream fis = new FileInputStream(LEAGUE_FILE);
			 ObjectInputStream ois = new ObjectInputStream(fis)) {
			
			while (true) {
				try {
					FootballClub club = (FootballClub) ois.readObject();
					league.add(club);
				} catch (IOException e) {
					break;
				} catch (Exception e) {
					System.out.println("Exception1 in reading club");
					break;
				}
			}
		} catch (IOException e) {
			System.out.println("Club file does not exist. A new file was created for the progress to be saved.");
		} catch (Exception e) {
			System.out.println("Exception2 in reading club");
		}
		
		//Reading the matches
		try (FileInputStream fis2 = new FileInputStream(MATCHES_FILE);
			 ObjectInputStream ois2 = new ObjectInputStream(fis2)) {
			
			while (true) {
				try {
					Match match = (Match) ois2.readObject();
					matches.add(match);
				} catch (IOException e) {
					break;
				} catch (Exception e) {
					System.out.println("Exception1 in reading match");
					break;
				}
			}
		} catch (IOException e) {
			System.out.println("Matches file does not exist. A new file was created for the progress to be saved.");
		} catch (Exception e) {
			System.out.println("Exception2 in reading match");
		}
		
		//Reading the season
		try (FileReader fileReader = new FileReader(SEASON_FILE);
			 BufferedReader bufferedReader = new BufferedReader(fileReader)) {
			season = Integer.parseInt(bufferedReader.readLine());
		} catch (Exception e) {
			System.out.println("Season file does not exist. A new file was created for the progress to be saved. \n\n"); //File was created in checkIfResumingProgress()
			season = validateInteger("Which season do you want to start: ");
			while (!(season > 0)) {
				System.out.println("Please enter a valid year");
				season = validateInteger("Which season do you want to start: ");
			}
		}
	}
	
	public boolean saveToFile() {
		//Saving the clubs
		try (FileOutputStream fos = new FileOutputStream(LEAGUE_FILE);
			 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			for (FootballClub club : league) {
				oos.writeObject(club);
			}
		} catch (IOException e) {
			System.out.println("IOException in saving club");
			return false;
		} catch (Exception e) {
			System.out.println("Exception in saving club");
			return false;
		}
		
		//Saving the matches
		try (FileOutputStream fos = new FileOutputStream(MATCHES_FILE);
			 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			for (Match match : matches) {
				oos.writeObject(match);
			}
		} catch (IOException e) {
			System.out.println("IOException in saving match");
			return false;
		} catch (Exception e) {
			System.out.println("Exception in saving match");
			return false;
		}
		
		//Saving the season
		try (FileWriter fileWriter = new FileWriter(SEASON_FILE, false);
			 PrintWriter printWriter = new PrintWriter(fileWriter, true)) {
			printWriter.println(season);
		} catch (IOException e) {
			System.out.println("IOException in saving season");
			return false;
		} catch (Exception e) {
			System.out.println("Exception in saving season");
			return false;
		}
		
		return true;
	}
	
	public void quitProgram(boolean saveFlag) {
		if (!saveFlag) {
			System.out.println("Your progress has not been saved.");
			System.out.print("Do you wish to save before you quit(y for yes, any other character for no): ");
			String choice = sc.next();
			System.out.println();
			if (choice.equalsIgnoreCase("y")) { //The user chooses to save the file before quitting
				saveToFile();
			} else {
				System.out.println("You have not saved your progress");
			}
		}
		System.out.println("You have successfully quit the program");
		System.exit(0);
	}
	
	public int validateInteger(String message) {
		final String INTEGER_ERROR = ("Please enter an integer");
		String str;
		String sign = "";
		StringBuilder sb;
		
		System.out.print(message);
		str = sc.nextLine().trim();
		if (!(str.equals(""))) {
			sb = new StringBuilder(str);
			if (((str.charAt(0)) == '+') || ((str.charAt(0)) == '-')) {
				sign = Character.toString(str.charAt(0));
				sb.deleteCharAt(0);
				str = sb.toString();
			}
		}
		while (!(str.matches("[0-9]+"))) {
			System.out.println();
			System.out.println(INTEGER_ERROR);
			System.out.print(message);
			str = sc.nextLine().trim();
			if (!(str.equals(""))) {
				sb = new StringBuilder(str);
				if (((str.charAt(0)) == '+') || ((str.charAt(0)) == '-')) {
					sign = Character.toString(str.charAt(0));
					sb.deleteCharAt(0);
					str = sb.toString();
				}
			}
		}
		
		System.out.println();
		return (Integer.parseInt((sign + str)));
	}
	
	public String validateString(String message) {
		final String STRING_ERROR = ("Please enter text with all characters and no symbols and numbers and no blank entries");
		boolean allChar = false;
		String str;
		String outString = "";
		
		while (!allChar) {
			allChar = true;
			System.out.print(message);
			str = sc.nextLine();
			outString = str.trim();
			if (!outString.equals("")) {
				System.out.println();
				str = ((str.toLowerCase()).trim());
				char[] charArray = str.toCharArray();
				for (char ch : charArray) {
					if (!(ch >= 'a' && ch <= 'z' || ch == ' ')) {
						allChar = false;
						break;
					}
				}
			} else {
				allChar = false;
			}
			
			if (!allChar) {
				System.out.println(STRING_ERROR);
			}
		}
		return outString; //Removes unnecessary spaces in the front and the back
	}
}
