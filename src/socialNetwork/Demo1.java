package socialNetwork;

import java.util.*;

public class Demo1 {
    
public static void main(String[] args) throws Exception
    {
	SN app = new SN();
	
	app.DBDrop();
	app.DBBuild();

	System.out.println("\nnewPage: luis");
	PageObject p = app.newPage("luis", "lc@goo.pt", "Luis Caires", "foo.jpg");
	System.out.println("\nnewPage: mary");
	PageObject q = app.newPage("mary", "mary@goo.com", "Mary Jones", "glo.jpg");
	System.out.println("\nnewPage: joe");
	PageObject r = app.newPage("joe", "joe@com", "Joe Dalton", "joe.jpg");
	
	app.follows(p.getPageId(),q.getPageId(),FState.PENDING); // luis follows mary (PENDING)
	FState f = app.getfollow(p.getPageId(),q.getPageId());
	
	app.updatefollowsstatus(p.getPageId(),q.getPageId(),FState.OK); // luis follows mary (OK)
        f = app.getfollow(p.getPageId(),q.getPageId());
	
        f = app.getfollow(p.getPageId(),p.getPageId());

	app.follows(p.getPageId(),r.getPageId(),FState.PENDING); // luis follows joe (PENDING)
	
	System.out.println("\ngetfollowed:");
	List<PageObject> pl = app.getfollowed(p.getPageId()) ;
	for (PageObject  x : pl) { System.out.println(p.getUserId()+" follows "+x.getUserId()); }

	app.updatefollowsstatus(p.getPageId(),r.getPageId(),FState.OK);
	
	System.out.println("\ngetfollowed:");
	pl = app.getfollowed(p.getPageId()) ;
	for (PageObject  x : pl) { System.out.println(p.getUserId()+" follows "+x.getUserId()); }

	
	PostObject po1 = app.newPost(p.getPageId(), "11.2.2023", "hello there");
	PostObject po2 = app.newPost(p.getPageId(), "15.2.2023", "funny");
	PostObject po3 = app.newPost(r.getPageId(), "15.3.2023", "lets play music");

	System.out.println("\ngetAllPages:");

	List<PageObject> lpages = app.getAllPages();
	for (PageObject  x : lpages) {
	    System.out.println(x.getUserId()+" : "+x.getEmail());
	}

	System.out.println("\nupdatePage");

	PageObject w = app.getPage(r.getPageId());
	w.setEmail("chris@fct.unl.pt");
	app.updatePage(w);
	
	System.out.println("\ngetAllPages:");

	lpages = app.getAllPages();
	for (PageObject  x : lpages) {
	    System.out.println(x.getUserId()+" : "+x.getEmail());
	}
	
	System.out.println("\ngetPosts()");

	List<PostObject> lposts = app.getAllPosts();
	for (PostObject  x : lposts) {
	    System.out.println(x.getPostText() + " " + x.getPostDate());
	}

	po2.setPostText("post text changed!");
	app.updatePost(po2);
	
	System.out.println("\ngetPosts(page_id)");

	lposts = app.getPagePosts(p.getPageId());
	for (PostObject  x : lposts) {
	    System.out.println(x.getPostText() + " " + x.getPostDate());
	}

	app.like(po1.getPostId(),r.getPageId());
	app.like(po3.getPostId(),p.getPageId());
	app.like(po3.getPostId(),q.getPageId());

	System.out.println("\ngetLikess(post_id)");
	lpages = app.getLikes(po3.getPostId());
	for (PageObject  x : lpages) {
	    System.out.println(po3.getPostText()+" liked by " + x.getUserId());
	}

	System.out.println("\ngetLikess(post_id)");
	app.unlike(po3.getPostId(),p.getPageId());
	lpages = app.getLikes(po3.getPostId());
	for (PageObject  x : lpages) {
	    System.out.println(po3.getPostText()+" liked by " + x.getUserId());
	}
	
	//app.deletePage(p);
	//app.deletePage(q);
	//app.deletePage(r);
    }

}
