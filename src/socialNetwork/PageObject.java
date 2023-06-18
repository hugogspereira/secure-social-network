package socialNetwork;

public class PageObject {

    private final int page_id;
    private String user_id;
    private String email;
    private String page_title;
    private String page_pic;
    
    public PageObject(int _page_id, String _user_id, String _email, String _page_title, String _page_pic) {
	    page_id = _page_id;
	    user_id = _user_id;
	    email = _email;
	    page_title = _page_title;
	    page_pic = _page_pic;
    }

    public int getPageId() {
    	return page_id;
    }

    public String getUserId() {
    	return user_id;
    }
    
    public void setUserId(String _user_id) {
	    user_id = _user_id ;
    }

    public String getEmail() {
    	return email;
    }

    public void setEmail(String _email) {
    	email = _email ;
    }

    public String getPageTitle() {
    	return page_title;
    }

    public void setPageTitle(String _page_title) {
        page_title = _page_title;
    }

    public String getPagePic() {
    	return page_pic;
    }

    public void setPagePic(String _page_pic) {
	    page_pic = _page_pic ;
    }

}
