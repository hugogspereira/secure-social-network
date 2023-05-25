package accCtrl.resources;

public class ResourceClass implements Resource {

    private String id;

    public ResourceClass(String id) {
        this.id = id;
    }

    @Override
    public String getResourceId() {
        return id;
    }

}
