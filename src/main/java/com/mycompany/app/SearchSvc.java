package rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

//import org.apache.commons.lang.*;

import java.sql.*;  
import java.util.*;

import dto.Search;
import dto.Response;

@Path("/search")
public class SearchSvc {
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response<List<Search>> getSearches() { 
		String sql = "SELECT search_term, COUNT(*) as count FROM search_terms GROUP BY search_term ORDER BY count DESC";
		Connection conn = new Database().connect();
		List<Search> searches = new ArrayList<Search>();
		Response<List<Search>> response = new Response<List<Search>>();
		response.setReturnData(searches);
		if( conn == null ){
			return response;
		}
		try{
			Statement stmt=conn.createStatement();  
			ResultSet rs=stmt.executeQuery(sql);  
			while(rs.next()) { 
				Search search = new Search();
				search.setTerm(rs.getString(1));
				search.setCount(Integer.parseInt(rs.getString(2))); 
				searches.add(search); 
			}
		}catch(Exception e){ 
			System.out.println(e);
		}  
		return response;
	}
	
	@POST
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response<String> postSearch(Search search) {
		System.out.println("Output json server .... \n");
		System.out.println(search);
		String sql = "INSERT INTO search_terms (search_term) VALUES (?)";
		Connection conn = new Database().connect();
		if( conn == null ){
			return new Response<String>("Could not connect to Database");
		}
		List<Search> searches = new ArrayList<Search>();
		try{
			PreparedStatement ps =conn.prepareStatement(sql);  
			ps.setString(1, search.getTerm());
			ps.executeUpdate();  
		}catch(Exception e){ 
			System.out.println(e);
			return new Response<String>("Internal Database Error");
		}  
		return new Response<String>("Succesfully inserted term: " + search.getTerm());
	} 

	class Database{  
		public Connection connect(){
			String sql_user = System.getenv("EPIFINDER_MYSQL_USER");
			String sql_password = System.getenv("EPIFINDER_MYSQL_PASSWORD");
			String sql_db_host = System.getenv("EPIFINDER_MSYQL_SERVER");
			try{  
				Class.forName("com.mysql.jdbc.Driver");  
				Connection con=DriverManager.getConnection(  
						sql_db_host,sql_user,sql_password);  
				return con;
			}catch(Exception e){ 
				System.out.println(e);
			}  
			return null;
		}
	}  
} 

