package accCtrl.operations;

public class DeletePostOperation implements Operation {

    @Override
    public String getOperationId() {
        return OperationValues.DELETE_POST.getOperation();
    }

}
