package accCtrl.operations;

public class OperationClass implements Operation {

    private final String id;

    public OperationClass(String id) {
        this.id = id;
    }

    public OperationClass(OperationValues operationValue) {
        this.id = operationValue.getOperation();
    }

    @Override
    public String getOperationId() {
        return id;
    }

}