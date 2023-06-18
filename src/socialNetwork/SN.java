package socialNetwork;

import java.sql.*;
import java.util.*;

public class SN {

    private static Connection theconnection = null;

    Statement stmt;

	private static SN instance;

	public static SN getInstance() throws Exception {
		if(instance == null) {
			instance = new SN();
		}
		return instance;
	}

	void connect() throws Exception {
		if(theconnection==null) {
			String sqliteConn = "jdbc:sqlite:../webapps/seg-soft/SQLite.db";
			theconnection = DriverManager.getConnection(sqliteConn);
		}
    }

    public SN() throws Exception {
		DBBuild();
		Class.forName("org.sqlite.JDBC");
		connect();
		stmt=theconnection.createStatement();
		theconnection.setAutoCommit(true);
    }

    // Utils

    String q(String s) {
		return "\""+s+"\"";
    }
        
    // Constructor

    public void doSQL(Statement stmt, String sqlcmd) throws SQLException {
		try {
			stmt.execute(sqlcmd);
			// System.out.println(sqlcmd + " done.");
		} catch (Exception _ex) {}
    }

    public void DBDrop() throws SQLException {
		doSQL(stmt,"drop table pagekey");
		doSQL(stmt,"drop table postkey");
		doSQL(stmt,"drop table page;");
		doSQL(stmt,"drop table follower;");
		doSQL(stmt,"drop table posts;");
		doSQL(stmt,"drop table post;");
		doSQL(stmt,"drop table likes;");
    }

    public void DBBuild() throws SQLException {
		String sqlcmd;
		ResultSet rs   ;

		doSQL(stmt,"CREATE TABLE IF NOT EXISTS pagekey(page_id integer);");
		//System.out.println("pagekey created.");

		doSQL(stmt,"CREATE TABLE IF NOT EXISTS postkey(post_id integer);");
		//System.out.println("postkey created.");

		doSQL(stmt,"CREATE TABLE IF NOT EXISTS page("+
				"page_id integer," +
				"user_id text," +
				"email text," +
				"page_title text," +
				"page_pic text);");
		doSQL(stmt,"CREATE UNIQUE INDEX IF NOT EXISTS idx_page_page_id on page(page_id)");
		//System.out.println("page created.");

		doSQL(stmt,"CREATE TABLE IF NOT EXISTS follower ("+
				"page_ids integer," +
				"page_idd integer," +
				"status text)");
		doSQL(stmt,"CREATE UNIQUE INDEX IF NOT EXISTS idx_follower on follower(page_ids,page_idd)");
		//System.out.println("follower created.");

		doSQL(stmt,"CREATE TABLE IF NOT EXISTS post(" +
				"post_id integer," +
				"page_id integer," +
				"post_date text," +
				"post_text text)");
		doSQL(stmt,"CREATE UNIQUE INDEX IF NOT EXISTS idx_post_post_id on post(post_id)");
		//System.out.println("post created.");

		doSQL(stmt,"CREATE TABLE IF NOT EXISTS likes(" +
				"post_id integer," +
				"page_id integer)");
		doSQL(stmt,"CREATE UNIQUE INDEX IF NOT EXISTS idx_likes_id on likes(post_id, page_id)");
		//System.out.println("likes created.");
     }

    int alloc_page() throws SQLException {
		ResultSet rs   ;
		int key;
		synchronized(this) {
			rs    = stmt.executeQuery("SELECT page_id FROM pagekey");
			key = rs.getInt("page_id");
			doSQL(stmt,"UPDATE pagekey SET page_id = "+(key+1));
		}
		return key;
	}

    int alloc_post() throws SQLException {
        ResultSet rs   ;
		int key;
		synchronized(this) {
			rs    = stmt.executeQuery("SELECT post_id FROM postkey");
			key = rs.getInt("post_id");
			doSQL(stmt,"UPDATE postkey SET post_id = "+(key+1));
		}
		return key;
    }

    // Posts

    public PostObject newPost(int page_id, String post_date, String post_text) throws SQLException {
		int post_id = alloc_post();
		stmt.execute("insert into post (post_id,page_id,post_date,post_text) values ("+
				post_id+","+page_id+","+ q(post_date)+","+ q(post_text)+")");
		return new PostObject(post_id,page_id,post_date,post_text);
    }

    public PostObject getPost(int post_id) throws SQLException{
		int _page_id;
		String _post_date;
		String _post_text;
		String sql ="select * from post where post_id="+post_id;
		ResultSet rs   ;
		rs    = stmt.executeQuery(sql);
		if (rs.next()) {
			_page_id = rs.getInt("page_id");
			_post_date = rs.getString("post_date");
			_post_text = rs.getString("post_text");
			return new PostObject(post_id, _page_id,_post_date,_post_text);
		} else throw new SQLException();
    }
    
    public void updatePost(PostObject p) throws SQLException{
		stmt.execute("update post set "+
				"page_id = "+p.getPageId()+","+
				"post_date = "+q(p.getPostDate())+","+
				"post_text = "+q(p.getPostText())+
				" where post_id="+p.getPostId());
    }

    public void deletePost(PostObject p) throws SQLException {
		stmt.execute("delete from post where post_id="+p.getPostId());
		stmt.execute("delete from likes where post_id="+p.getPostId());
    }

    public List<PostObject> getAllPosts() throws SQLException {
		List<PostObject> lpages = new ArrayList<PostObject>();
		Statement stmtl = theconnection.createStatement();
		ResultSet rs    = stmtl.executeQuery("select post_id from post");
		while (rs.next()) {
			PostObject p = getPost(rs.getInt("post_id"));
			lpages.add(p);
		}
		return lpages;
	}

     public List<PostObject> getPagePosts(int page_id) throws SQLException {
		 List<PostObject> lpages = new ArrayList<PostObject>();
		 Statement stmtl = theconnection.createStatement();
		 ResultSet rs    = stmtl.executeQuery("select post_id from post where page_id="+page_id);
		 while (rs.next()) {
			 PostObject p = getPost(rs.getInt("post_id"));
			 lpages.add(p);
		 }
		 return lpages;
     }

    // Pages

    public PageObject newPage(String user_id, String email, String page_title, String page_pic) throws SQLException{
		int page_id = alloc_page();
		stmt.execute("insert into page (page_id,user_id,email,page_title,page_pic) values ("+
				page_id+","+ q(user_id)+","+
				q(email)+","+ q(page_title)+","+ q(page_pic)+")");
		return new PageObject(page_id, user_id, email, page_title, page_pic);
    }

    public PageObject getPage(int page_id) throws SQLException{
		String _user_id;
		String _email;
		String _page_title;
		String _page_pic;
		String sql ="select * from page where page_id="+page_id;
		ResultSet rs   ;
		rs    = stmt.executeQuery(sql);
		if (rs.next()) {
			_user_id = rs.getString("user_id");
			_email = rs.getString("email");
			_page_title = rs.getString("page_title");
			_page_pic = rs.getString("page_pic");
			return new PageObject(page_id, _user_id,_email,_page_title,_page_pic);
		} else throw new SQLException();
    }

    public void updatePage(PageObject p) throws SQLException{
		stmt.execute("update page set "+
				"user_id = "+q(p.getUserId())+","+
				"email = "+q(p.getEmail())+","+
				"page_title = "+q(p.getPageTitle())+","+
				"page_pic = "+q(p.getPagePic())+" where page_id="+p.getPageId());
    }

    public void deletePage(PageObject p) throws SQLException {
		stmt.execute("delete from page where page_id="+p.getPageId());
		stmt.execute("delete from post where page_id="+p.getPageId());
		stmt.execute("delete from likes where page_id="+p.getPageId());
	}

     public List<PageObject> getAllPages() throws SQLException {
		 List<PageObject> lpages = new ArrayList<PageObject>();
		 Statement stmtl = theconnection.createStatement();
		 ResultSet rs    = stmtl.executeQuery("select page_id from page order by page_id ASC");
		 while (rs.next()) {
			 PageObject p = getPage(rs.getInt("page_id"));
			 lpages.add(p);
		 }
		 return lpages;
     }

    // Like
    
    public void like(int post_id, int page_id) throws SQLException {
		String sql ="select * from page where page_id="+page_id;
		ResultSet rs;
		rs    = stmt.executeQuery(sql);
		if (rs.next()) {
			stmt.execute("insert into likes (post_id,page_id) values ("+post_id+","+ page_id+")");
		}
		else {
			throw new SQLException();
		}
    }

    public void unlike(int post_id, int page_id) throws SQLException{
		String sql ="select * from page where page_id="+page_id;
		ResultSet rs;
		rs    = stmt.executeQuery(sql);
		if (rs.next()) {
			stmt.execute("delete from likes where post_id=" + post_id + " and page_id=" + page_id);
		}
		else {
			throw new SQLException();
		}
    }

	public List<PageObject> getLikes(int post_id) throws SQLException {
		List<PageObject> lpages = new ArrayList<PageObject>();
		Statement stmtl = theconnection.createStatement();
		ResultSet rs    = stmtl.executeQuery("select page_id from likes where post_id="+post_id);
		while (rs.next()) {
			PageObject p = getPage(rs.getInt("page_id"));
			lpages.add(p);
		}
		return lpages;
     }

	public boolean isLiked(int post_id, int visitor_pag_id) throws SQLException {
		Statement stmtl = theconnection.createStatement();
		ResultSet rs    = stmtl.executeQuery("select post_id from likes where post_id="+post_id+" and page_id ="+visitor_pag_id);
		return (rs.next());
	}

    // Follow
    
    public void follows(int follower_page_id, int followed_page_id, FState status) throws SQLException {
		if(follower_page_id!=followed_page_id)
			stmt.execute("insert into follower (page_ids,page_idd,status) values ("+
					follower_page_id+","+ followed_page_id+","+q(status.name())+")");
    }

	public void updatefollowsstatus(int follower_page_id, int followed_page_id, FState status) throws SQLException {
		stmt.execute("update follower set status="+q(status.name())+
				" where page_ids="+follower_page_id+" and "+
				"page_idd="+followed_page_id);
    }
    
    public FState  getfollow(int follower_page_id, int followed_page_id) throws SQLException {
		Statement stmtl = theconnection.createStatement();
		ResultSet rs    = stmtl.executeQuery(
				"select status from follower"+
						" where page_ids="+follower_page_id+" and status=\"OK\""+
						" and page_idd="+followed_page_id);
		if (rs.next()) {
			return FState.valueOf(rs.getString("status"));
		}
		return FState.NONE;
     }

	public FState getfollowState(int follower_page_id, int followed_page_id) throws SQLException {
		Statement stmtl = theconnection.createStatement();
		ResultSet rs    = stmtl.executeQuery(
				"select status from follower"+
						" where page_ids="+follower_page_id+
						" and page_idd="+followed_page_id);
		if (rs.next()) {
			return FState.valueOf(rs.getString("status"));
		}
		return null;
	}
    
    public List<PageObject> getfollowers(int page_id) throws SQLException {
		List<PageObject> lpages = new ArrayList<PageObject>();
		Statement stmtl = theconnection.createStatement();
		ResultSet rs    = stmtl.executeQuery("select page_ids from follower where status=\"OK\""+" and page_idd="+page_id);
		while (rs.next()) {
			PageObject p = getPage(rs.getInt("page_ids"));
			lpages.add(p);
		}
		return lpages;
     }

	public List<PageObject> getPages(String username) throws SQLException {
		List<PageObject> lpages = new ArrayList<PageObject>();

		String query = "SELECT page_id FROM page WHERE user_id = ?";
		PreparedStatement pstmt = theconnection.prepareStatement(query);
		pstmt.setString(1, username);

		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			PageObject p = getPage(rs.getInt("page_id"));
			lpages.add(p);
		}

		return lpages;
	}

    public List<PageObject> getfollowed(int page_id) throws SQLException {
		List<PageObject> lpages = new ArrayList<PageObject>();
		Statement stmtl = theconnection.createStatement();
		ResultSet rs    = stmtl.executeQuery("select page_idd from follower where status=\"OK\""+" and page_ids="+page_id);
		while (rs.next()) {
			PageObject p = getPage(rs.getInt("page_idd"));
			lpages.add(p);
		}
		return lpages;
     }

	public List<PageObject> getrequests(int page_id) throws SQLException {
		List<PageObject> lpages = new ArrayList<PageObject>();
		Statement stmtl = theconnection.createStatement();
		ResultSet rs    = stmtl.executeQuery("select page_ids from follower where status=\"PENDING\" "+" and page_idd="+page_id);
		while (rs.next()) {
			PageObject p = getPage(rs.getInt("page_ids"));
			lpages.add(p);
		}
		return lpages;
	}

	public void closeConn() throws SQLException {
		instance = null;
		if(stmt!= null) {
			stmt.close();
		}
		if(theconnection != null) {
			theconnection.close();
			theconnection = null;
		}
	}
}

