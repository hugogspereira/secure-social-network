package accCtrl.operations;

public class OperationClass implements Operation {

    private String id;

    public OperationClass(String id) {
        this.id = id;
    }

    @Override
    public String getOperationId() {
        return id;
    }

}