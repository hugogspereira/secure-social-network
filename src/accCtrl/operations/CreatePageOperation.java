package accCtrl.operations;

public class CreatePageOperation implements Operation {

    @Override
    public String getOperationId() {
        return OperationValues.CREATE_PAGE.getOperation();
    }

}
