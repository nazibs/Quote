package httppost;

public class test1 {

	
	public static void main(String args[]) throws ClassNotFoundException{
	
		
		
		
		Class.forName("com.mysql.jdbc.Driver");
		
		
StringBuilder ConnectionURL= new StringBuilder();
		
		ConnectionURL.append("jdbc:")
		.append("mysql://$OPENSHIFT_MYSQL_DB_HOST:$OPENSHIFT_MYSQL_DB_PORT/")
		.append("/?user=")
		.append("adminUe73kVG")
		.append("&password=")
		.append("wnzSW2AWpw2Z");
	}
}
