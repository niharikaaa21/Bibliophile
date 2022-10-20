package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import DBConnection.DBHandler;

public class SceneController {
	
	
	// variables for adding in a new entry
	@FXML public TextArea ne_title;
	@FXML public TextArea ne_date;
	@FXML public TextArea ne_author;
	@FXML public TextArea ne_quote;
	@FXML public TextArea ne_rating;
	@FXML public TextArea ne_genre;
	
	// variables for displaying an existing entry
	@FXML public Label showAuthor;
	@FXML public Label showTitle;
	@FXML public Label showDate;
	@FXML public Label showRating;
	@FXML public Label showQuote;
	@FXML public Label showGenre;
	
	// variables for searching an existing entry
	@FXML public TextArea s_author;
	@FXML public TextArea s_title;
	
	//get a connection to the database
	public Connection database = new DBHandler().getConnection();
	
	public TextField textField;
	public Label textLabel;
	
	private Stage stage;
	private Scene scene;
	private Parent root;
 
	 public void switchToWelcomePage(ActionEvent event) throws IOException {
	  root = FXMLLoader.load(getClass().getResource("WelcomePage.fxml"));
	  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	  scene = new Scene(root);
	  stage.setScene(scene);
	  stage.show();
	 }
 
 
	 @SuppressWarnings("null")
	public void switchToCreateRecord(ActionEvent event) throws IOException {
	  Parent root = FXMLLoader.load(getClass().getResource("CreateRecord.fxml"));
	  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	  scene = new Scene(root);
	  stage.setScene(scene);
	  stage.show();
	 
	 }
	  
     public void switchToSearchRecord(ActionEvent event) throws IOException {
	  root = FXMLLoader.load(getClass().getResource("SearchRecord.fxml"));
	  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	  scene = new Scene(root);
	  stage.setScene(scene);
	  stage.show();
      }
  
     public void createEntry(ActionEvent event) throws IOException {
	 Statement stmt = null;
	  
	  String query = "INSERT INTO `bibliophiledb`.`entries` (`title`, `author`, `date`, `genre`, `memorable_quote`, `rating`) VALUES ('"+ne_title.getText()+"', '"+ne_author.getText()+"', '"+ne_date.getText()+"', '"+ne_genre.getText()+"', '"+ne_quote.getText()+"', '"+ne_rating.getText()+"');";
	  
	  try {
		    stmt = database.createStatement();
		    stmt.executeUpdate(query);
		    }
		catch (SQLException ex){
		    // handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
	  
	  Parent root = FXMLLoader.load(getClass().getResource("RecordCreated.fxml"));
	  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	  scene = new Scene(root);
	  stage.setScene(scene);
	  stage.show();
	  
	 }

  public void queryEntry(ActionEvent event) throws IOException {
	  Statement stmt = null;
	  ResultSet rs = null;
		 
	  String where_query = "";
	  if(!s_title.getText().isEmpty()) {where_query = "WHERE `title`=\""+s_title.getText()+"\"";}
	  else if(!s_author.getText().isEmpty()) {where_query = "WHERE `author`=\""+s_author.getText()+"\"";}
	  
	  String query = "SELECT * FROM bibliophiledb.entries "+where_query+";";
	  
	  root = FXMLLoader.load(getClass().getResource("ViewRecord.fxml"));
	  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	  scene = new Scene(root);
	  stage.setScene(scene);	  
	  stage.show();
	 
	  // fetch label nodes from the scene to display entry values later
	  showTitle = (Label) scene.lookup("#showTitle");
	  showAuthor = (Label) scene.lookup("#showAuthor");
	  showDate = (Label) scene.lookup("#showDate");
	  showGenre = (Label) scene.lookup("#showGenre");
	  showQuote = (Label) scene.lookup("#showQuote");
	  showRating = (Label) scene.lookup("#showRating");
	  
	  
	  try {
		    // lines to send an sql query to the database
		    stmt = database.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		    rs = stmt.executeQuery(query);{
		    if (rs.first()) {                                                       //.first() means that we'll pick the first element in the result set
		    	showTitle.setText(rs.getString(2));									// indexes start at 1, keys start at 0, ex - arrays start at 0
		    	showAuthor.setText(rs.getString(3));
		    	showDate.setText(rs.getString(4));
		    	showGenre.setText(rs.getString(5));
		    	showQuote.setText(rs.getString(6));
		    	showRating.setText(rs.getString(7));
		    };
		}}
		catch (SQLException ex){
		    // display errors in the console
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
 }
  
  // display latest entry
  public void queryNewEntry(ActionEvent event) throws IOException {
	  Statement stmt = null;
	  ResultSet rs = null;
	  
	  String query = "SELECT * FROM bibliophiledb.entries ORDER BY entry_id DESC;";

	  root = FXMLLoader.load(getClass().getResource("ViewRecord.fxml"));
	  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	  scene = new Scene(root);
	  stage.setScene(scene);	  
	  stage.show();
	 
	  showTitle = (Label) scene.lookup("#showTitle");
	  showAuthor = (Label) scene.lookup("#showAuthor");
	  showDate = (Label) scene.lookup("#showDate");
	  showGenre = (Label) scene.lookup("#showGenre");
	  showQuote = (Label) scene.lookup("#showQuote");
	  showRating = (Label) scene.lookup("#showRating");
	  
	  
	  try {
		    stmt = database.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);     //createStatement : prepare the statement
		    rs = stmt.executeQuery(query);{                                                                     // executeQuery : send the query
		    if (rs.first()) {																					
		    	showTitle.setText(rs.getString(2));
		    	showAuthor.setText(rs.getString(3));
		    	showDate.setText(rs.getString(4));
		    	showGenre.setText(rs.getString(5));
		    	showQuote.setText(rs.getString(6));
		    	showRating.setText(rs.getString(7));
		    };

		    
		}}
		catch (SQLException ex){
		    // handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
 }
}
