package socialNetwork;

public class PostObject {

    private int post_id;
    private int page_id;
    private String post_date;
    private String post_text;
    
    public PostObject(int _post_id, int _page_id, String _post_date, String _post_text) {
	    post_id = _post_id;
	    page_id = _page_id;
	    post_date = _post_date;
	    post_text = _post_text;
    }

    public int getPostId() {
	    return post_id;
    }

    public int getPageId() {
	    return page_id;
    }
    
    public void setPageId(int _page_id) {
	    page_id = _page_id ;
    }
    
    public String getPostDate() {
	    return post_date;
    }
    
    public void setPostDate(String _post_date) {
	    post_date = _post_date ;
    }

    public String getPostText() {
	    return post_text;
    }

    public void setPostText(String _post_text) {
	    post_text = _post_text ;
    }

}
