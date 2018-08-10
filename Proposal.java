package manageProject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Proposal{

	/*Variables: owner, title, the file
	Functions: place proposal(student), view proposal file(lecturer), approve proposal(lecturer), view proposal status(student)*/

	ConnectorToDb con = new ConnectorToDb();
	
	
	

	//InputStream inpStream = new FileInputStream(new File(filePath));

	//Check which students have submitted their proposals
	public ArrayList<String> checkSubmittedProposalsIds() throws SQLException {
		
		
		String query = "select owner from proposal";
		ArrayList<String> owner = new ArrayList<String>();
		
		ResultSet results = con.statement.executeQuery(query);
		while(results.next()){
			owner.add(results.getString("owner"));
		}
		return owner;
	}
	
	//Does Proposal Exist
	public  static Boolean doesProposalExist(String proposalId) throws Exception  {
		
		Proposal proposal = new Proposal();
		ArrayList<String> testaProposalId=proposal.checkSubmittedProposalsIds();
		int i=0;Boolean proposalAvailable = false;
		while(i<testaProposalId.size() & proposalAvailable != true) {
			if(!proposalId.equalsIgnoreCase(testaProposalId.get(i))) {proposalAvailable = false;}else {proposalAvailable = true;}i++;
		}
		
		return proposalAvailable;
	}
	
	//Check if proposal has been approved
	public String viewProposalStatus(String owner) throws Exception{
		
		//First check if the project exists
		
		String state;
		if(doesProposalExist(owner)) {
		PreparedStatement prep = con.connection.prepareStatement("select state from proposal where owner=?");
		prep.setString(1, owner);

		ResultSet results=prep.executeQuery();
		results.next();
		state=results.getString("state");}else {state="Project does not exist";}

		return state;
	}

	//Forward your proposal
	public String placeProposal(String filePath, String owner) throws Exception{

		//First check if the proposal does not exist

		InputStream inp = new FileInputStream(new File(filePath));
		if(!doesProposalExist(owner)) {
		
		PreparedStatement prep = con.connection.prepareStatement("insert into proposal (owner, proposalfile, state) values (?, ?, ?)");
		prep.setString(1, owner);
		
		prep.setBlob(2, inp);
		prep.setString(3, "Not Approved");

		prep.executeUpdate();}else /*if proposal already exists*/{
			
			String defaultState= "Not Approved";
			PreparedStatement prep = con.connection.prepareStatement("update proposal set proposalfile=? where owner ='"+owner+"' and state='"+defaultState+"'");
			prep.setBlob(1, inp);
			prep.executeUpdate();
		}
		
		return "Successfully Added Your Proposal";

	}

	//View Proposal returns the path to the saved file
	public String viewProposal(String owner) throws Exception{
		
		//First check if the proposal exists
		String savedOrNot;
		int BUFFER_SIZE = 100000;
		String filePathSave = "C:\\Users\\Ouma\\Documents\\workspace-sts-3.9.5.RELEASE\\projectManger\\javaFiles\\myproposal.txt";
	
	    if(doesProposalExist(owner)) {
		PreparedStatement prep = con.connection.prepareStatement("select proposalfile from proposal where owner=?");
		prep.setString(1, owner);

		ResultSet resultSet = prep.executeQuery(); 
		resultSet.next();
		Blob blob = resultSet.getBlob("proposalFile");

		InputStream input = blob.getBinaryStream();
		
		OutputStream output = new FileOutputStream(new File(filePathSave ));
		int bytesRead = -1;
		byte[] buffer = new byte[BUFFER_SIZE];
		while((bytesRead=input.read(buffer))!=-1){
			output.write(buffer, 0, bytesRead);
		}
		input.close();
		output.close(); savedOrNot="Successfully Saved Your Proposal to "+ filePathSave;}else {savedOrNot="Proposal Does not Exist";}

		return savedOrNot;
	}
	
		//Approve proposal
	public String approveProposal(String owner) throws Exception {
		
		//First check if the proposal exists

		String done;

		if(doesProposalExist(owner)) {
			
			String defaultState= "Not Approved";
			String newtState= "Approved";
			con.statement.executeUpdate("update proposal set state='"+newtState+"' where owner ='"+owner+"' and state='"+defaultState+"'");
			done="Proposal Approved";
		}else {done="Proposal Does not Exist";}
		return done;

	}
}