import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Random;

public class Main extends Application {
	private static final String OPTION_ERROR = ("Please enter an integer between 1 and 8"); //To be displayed when wrong option is entered
	
	private static List<CustomOutputDataType> leagueOutList;
	private static List<Match> matchOut;
	private static List<FootballClub> leagueOut;
	private static PremierLeagueManager plm = new PremierLeagueManager();
	
	public static void main(String[] args) {
		final String SUCCESS_MESSAGE = ("Task Completed Successfully");
		final String FAILURE_MESSAGE = ("Task Failed");
		int option;
		boolean defaultFlag, status = false;
		boolean saveFlag = true;
		
		plm.checkIfResumingProgress();
		System.out.println("\n\n\n");
		
		while (true) {
			defaultFlag = false;
			System.out.println("Welcome to Premier League Manager - " + plm.getSeason() + "/" + (plm.getSeason() + 1));
			System.out.println("Your options are as follows:\n");
			displayOptions();
			option = plm.validateInteger("So what is the option you are choosing: ");
			
			if ((plm.getLeagueSize() == 0) && (option == 2 || option == 3 || option == 4 || option == 5 || option == 7)) {
				System.out.println("No clubs present in the league so cannot perform this operation \n");
			} else {
				switch (option) {
					case 1: //To add club
						status = plm.addClub();
						saveFlag = false;
						break;
					case 2: //To delete club
						status = plm.relegateClub();
						saveFlag = false;
						break;
					case 3: //To display the statistics of a club
						status = plm.displayStats();
						break;
					case 4: //To display the premier league table
						status = plm.displayTable();
						break;
					case 5: //To add a match
						status = plm.addMatch();
						saveFlag = false;
						break;
					case 6: //To save the current progress
						status = plm.saveToFile();
						saveFlag = true;
						break;
					case 7: //To open the premier league table in GUI
						try {
							leagueOut = plm.getLeague();
							matchOut = plm.getMatches();
							
							if (leagueOut.size() < 2) { //Need a minimum of two teams
								System.out.println("Not enough teams for a display of table(Minimum of 2 is required)");
							} else {
								Collections.sort(leagueOut);
								Collections.sort(matchOut);
								int count = 0;
								
								List<CustomOutputDataType> leagueOutList2 = new ArrayList<>();
								
								for (FootballClub club : leagueOut) {
									count++;
									leagueOutList2.add(new CustomOutputDataType(count, club.getName(), club.getMatchesPlayed(), club.getWins(), club.getLosses(), club.getDraws(), club.getPoints(), club.getGoalsFor(), club.getGoalsAgainst(), club.getGoalDiff()));
								}
								leagueOutList = leagueOutList2;
								
								launch(args);
								
								plm.setLeague(leagueOut);
								plm.setMatches(matchOut);
							}
							status = true;
						} catch (IllegalStateException e) {
							System.out.println("Application can only be launched once");
							status = false;
						} catch (Exception e) {
							System.out.println("Exception in displaying GUI");
							status = false;
						}
						saveFlag = false;
						break;
					case 8: //To quit the program
						plm.quitProgram(saveFlag);
						break;
					default:
						defaultFlag = true;
				}
				if (!defaultFlag) {
					System.out.println();
					if (status) {
						System.out.println(SUCCESS_MESSAGE);
					} else {
						System.out.println(FAILURE_MESSAGE);
					}
				} else {
					System.out.println(OPTION_ERROR);
				}
				System.out.print("\n\n");
			}
		}
	}
	
	private static void displayOptions() {
		System.out.println("1 - Create a new football club and add it in the premier league");
		System.out.println("2 - Delete (relegate) an existing club from the premier league");
		System.out.println("3 - Display the various statistics for a selected club");
		System.out.println("4 - Display the Premier League table");
		System.out.println("5 - Add a played match");
		System.out.println("6 - Save the current progress");
		System.out.println("7 - Output table in GUI");
		System.out.println("8 - Quit");
	}
	
	@Override
	public void start(Stage primaryStage) {
		
		//To view premier league table
		TableView<CustomOutputDataType> leagueTV = new TableView<CustomOutputDataType>();
		
		TableColumn<CustomOutputDataType, Integer> column1 = new TableColumn<>("Position");
		column1.setCellValueFactory(new PropertyValueFactory<>("position"));
		
		TableColumn<CustomOutputDataType, String> column2 = new TableColumn<>("Name");
		column2.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		TableColumn<CustomOutputDataType, Integer> column3 = new TableColumn<>("Matches Played");
		column3.setCellValueFactory(new PropertyValueFactory<>("matchesPlayed"));
		
		TableColumn<CustomOutputDataType, Integer> column4 = new TableColumn<>("Wins");
		column4.setCellValueFactory(new PropertyValueFactory<>("wins"));
		
		TableColumn<CustomOutputDataType, Integer> column5 = new TableColumn<>("Losses");
		column5.setCellValueFactory(new PropertyValueFactory<>("losses"));
		
		TableColumn<CustomOutputDataType, Integer> column6 = new TableColumn<>("Draws");
		column6.setCellValueFactory(new PropertyValueFactory<>("draws"));
		
		TableColumn<CustomOutputDataType, Integer> column7 = new TableColumn<>("Points");
		column7.setCellValueFactory(new PropertyValueFactory<>("points"));
		
		TableColumn<CustomOutputDataType, Integer> column8 = new TableColumn<>("Goals For");
		column8.setCellValueFactory(new PropertyValueFactory<>("goalsFor"));
		
		TableColumn<CustomOutputDataType, Integer> column9 = new TableColumn<>("Goals Against");
		column9.setCellValueFactory(new PropertyValueFactory<>("goalsAgainst"));
		
		TableColumn<CustomOutputDataType, Integer> column10 = new TableColumn<>("Goal Difference");
		column10.setCellValueFactory(new PropertyValueFactory<>("goalDiff"));
		
		leagueTV.getColumns().addAll(column1, column2, column3, column4, column5, column6, column7, column8, column9, column10);
		
		leagueTV.getItems().addAll(leagueOutList);
		
		//Button to generate a match
		Button genMatch = new Button("Generate Match");
		genMatch.setOnAction(event -> {
			if (!(matchOut.size() == (leagueOut.size() * (leagueOut.size() - 1)))) {
				Match match = null;
				while (match == null) {
					match = generateMatch();
				}
				Label genMatchScoreLabel = new Label(matchDetails(match));
				genMatchScoreLabel.setPadding(new Insets(20));
				genMatchScoreLabel.setAlignment(Pos.CENTER);
				Scene genMatchScoreScene = new Scene(genMatchScoreLabel);
				Stage genMatchScoreStage = new Stage();
				genMatchScoreStage.setWidth(350);
				genMatchScoreStage.setTitle("Match");
				genMatchScoreStage.setScene(genMatchScoreScene);
				genMatchScoreStage.setAlwaysOnTop(true);
				genMatchScoreStage.show();
			} else {
				showError("All matches have been played for now");
			}
			leagueTV.getItems().remove(0, leagueOutList.size());
			
			leagueTV.getItems().addAll(leagueOutList);
		});
		
		//To view matches played
		Button matchView = new Button("Matches Played");
		matchView.setOnAction(value -> {
			Collections.sort(matchOut);
			showMatchOutput(matchOut, "Matches Played");
		});
		
		//Button to find match
		Button findMatch = new Button("Find Match(es)");
		findMatch.setOnAction(value -> {
			Label integerError = new Label("Please enter integers !!!");
			
			//Getting the date to check
			Label dayLabel = new Label("Day: ");
			TextField dayTF = new TextField();
			
			Label monthLabel = new Label("Month: ");
			TextField monthTF = new TextField();
			
			Label yearLabel = new Label("Year: ");
			TextField yearTF = new TextField();
			
			Button resetButton = new Button("Reset");
			resetButton.setOnAction(event -> {
				dayTF.clear();
				monthTF.clear();
				yearTF.clear();
				integerError.setVisible(false);
			});
			
			Button submitDateButton = new Button("Submit");
			integerError.setPadding(new Insets(10));
			integerError.setVisible(false);
			integerError.setTextFill(Color.web("#cc0000"));
			submitDateButton.setOnAction(event -> {
				List<Match> matchOutList = new ArrayList<>(); //New List to store the matches played on the date
				int day = 0, month = 0, year = 0;
				boolean dateInInt = false;
				try {
					day = Integer.parseInt(dayTF.getText());
					month = Integer.parseInt(monthTF.getText());
					year = Integer.parseInt(yearTF.getText());
					dateInInt = true;
				} catch (Exception e) {
					integerError.setVisible(true);
				}
				if (dateInInt) {
					integerError.setVisible(false);
					for (Match match : matchOut) {
						if (match.getDate().equals(new Date(day, month, year))) {
							matchOutList.add(match);
						}
					}
					
					if (!(matchOutList.isEmpty())) {
						showMatchOutput(matchOutList, "Matches Played on " + day + "/" + month + "/" + year);
					} else {
						showError("No matches found on that day");
					}
				}
			});
			
			HBox hBox = new HBox(); //Horizontal box for buttons
			hBox.setSpacing(20);
			hBox.setAlignment(Pos.CENTER_RIGHT);
			
			GridPane grid = new GridPane();
			grid.addRow(0, dayLabel, dayTF);
			grid.addRow(1, monthLabel, monthTF);
			grid.addRow(2, yearLabel, yearTF);
			grid.addRow(3, resetButton, submitDateButton, integerError);
			grid.setHgap(20);
			grid.setVgap(15);
			
			VBox getMatchDateVBox = new VBox(grid);
			getMatchDateVBox.setPadding(new Insets(10));
			getMatchDateVBox.setSpacing(20);
			Scene findMatchScene = new Scene(getMatchDateVBox);
			Stage findMatchStage = new Stage();
			findMatchStage.setScene(findMatchScene);
			findMatchStage.setTitle("Find Match");
			findMatchStage.setAlwaysOnTop(true);
			findMatchStage.show();
		});
		
		VBox buttonVBox = new VBox(genMatch, matchView, findMatch);
		buttonVBox.setSpacing(20);
		buttonVBox.setPadding(new Insets(20));
		buttonVBox.setAlignment(Pos.TOP_CENTER);
		
		HBox leagueHBox = new HBox(leagueTV, buttonVBox);
		leagueHBox.setSpacing(10);
		
		Scene mainScene = new Scene(leagueHBox);
		primaryStage.setScene(mainScene);
		primaryStage.setTitle("Premier League Table");
		primaryStage.setAlwaysOnTop(true);
		primaryStage.show();
	}
	
	public void showMatchOutput(List<Match> list, String title) {
		TableView<Match> matchTableView = new TableView<Match>();
		
		TableColumn<Match, Date> matchColumn1 = new TableColumn<>("Date");
		matchColumn1.setCellValueFactory(new PropertyValueFactory<>("date"));
		
		TableColumn<Match, String> matchColumn2 = new TableColumn<>("Home Team");
		matchColumn2.setCellValueFactory(new PropertyValueFactory<>("homeTeam"));
		
		TableColumn<Match, Integer> matchColumn3 = new TableColumn<>("Home Goals");
		matchColumn3.setCellValueFactory(new PropertyValueFactory<>("homeGoals"));
		
		TableColumn<Match, Integer> matchColumn4 = new TableColumn<>("Away Goals");
		matchColumn4.setCellValueFactory(new PropertyValueFactory<>("awayGoals"));
		
		TableColumn<Match, String> matchColumn5 = new TableColumn<>("Away Team");
		matchColumn5.setCellValueFactory(new PropertyValueFactory<>("awayTeam"));
		
		matchTableView.getColumns().addAll(matchColumn1, matchColumn2, matchColumn3, matchColumn4, matchColumn5);
		
		matchTableView.getItems().addAll(list);
		
		VBox matchVBox = new VBox(matchTableView);
		
		Scene matchScene = new Scene(matchVBox);
		Stage matchStage = new Stage();
		matchStage.setWidth(500);
		matchStage.setScene(matchScene);
		matchStage.setTitle(title);
		matchStage.setAlwaysOnTop(true);
		matchStage.show();
	}
	
	public void showError(String errorMessage) {
		Label errorLabel = new Label(errorMessage);
		errorLabel.setPadding(new Insets(25));
		Scene errorScene = new Scene(errorLabel);
		Stage errorStage = new Stage();
		errorStage.setTitle("Error");
		errorStage.setScene(errorScene);
		errorStage.setAlwaysOnTop(true);
		errorStage.show();
	}
	
	public static int getRandomNum(int min, int max) {
		Random random = new Random();
		return random.nextInt(max - min) + min;
	}
	
	public static Match generateMatch() {
		int day = 0, month = 0, year = 0, homeGoals, awayGoals;
		String homeTeam, awayTeam;
		
		homeTeam = leagueOut.get(getRandomNum(0, leagueOut.size())).getName();
		awayTeam = leagueOut.get(getRandomNum(0, leagueOut.size())).getName();
		
		//Checks if the home team and the away team are the same(Same team can't play itself)
		if (homeTeam.equalsIgnoreCase(awayTeam)) {
			return null;
		}
		
		//Checks if the match has already been played(Same match can't happen twice in a league)
		for (Match match : matchOut) {
			if (match.getHomeTeam().equalsIgnoreCase(homeTeam) && match.getAwayTeam().equalsIgnoreCase(awayTeam)) {
				return null;
			}
		}
		
		//Checks if the date is valid
		boolean validDate = false;
		while (!validDate) {
			day = getRandomNum(1, 32);
			month = getRandomNum(1, 13);
			year = getRandomNum(plm.getSeason(), (plm.getSeason() + 2));
			validDate = plm.verifyDate(day, month, year);
		}
		
		Date date = new Date(day, month, year);
		
		//Checks if either the home team or the away team has a match on the same date which is not possible to happen
		boolean homeTeamHasGame = false, awayTeamHasGame = false;
		for (Match match : matchOut) {
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
			return null;
		}
		if (awayTeamHasGame) {
			return null;
		}
		
		//Gets the number of goals scored by both teams
		homeGoals = getRandomNum(0, 10);
		awayGoals = getRandomNum(0, 10);
		
		//Creates a match and adds it to league and updates the teams
		Match match = new Match(date, homeTeam, awayTeam, homeGoals, awayGoals);
		for (FootballClub club : leagueOut) {
			if (club.getName().equalsIgnoreCase(homeTeam)) {
				plm.addMatchToClub(club, homeGoals, awayGoals);
			}
			if (club.getName().equalsIgnoreCase(awayTeam)) {
				plm.addMatchToClub(club, awayGoals, homeGoals);
			}
		}
		matchOut.add(match);
		
		leagueOutList.clear();
		int count = 0;
		Collections.sort(leagueOut);
		for (FootballClub club : leagueOut) {
			count++;
			leagueOutList.add(new CustomOutputDataType(count, club.getName(), club.getMatchesPlayed(), club.getWins(), club.getLosses(), club.getDraws(), club.getPoints(), club.getGoalsFor(), club.getGoalsAgainst(), club.getGoalDiff()));
		}
		return match;
	}
	
	public String matchDetails(Match match) {
		return (match.getDate() + "  " + match.getHomeTeam() + " " + match.getHomeGoals() + " - " + match.getAwayGoals() + " " + match.getAwayTeam());
	}
}
