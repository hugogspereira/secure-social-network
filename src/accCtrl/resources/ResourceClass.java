package accCtrl.resources;

public class ResourceClass implements Resource {

    private String type;
    private String id;

    public ResourceClass(String type, String id) {
        this.type = type;
        this.id = id;
    }

    @Override
    public String getResourceType() {return type;}
    @Override
    public String getResourceId() {return id;}


}
