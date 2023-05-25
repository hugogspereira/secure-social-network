package accCtrl.operations;

public class DeletePageOperation implements Operation {

    @Override
    public String getOperationId() {
        return OperationValues.DELETE_PAGE.getOperation();
    }

}
