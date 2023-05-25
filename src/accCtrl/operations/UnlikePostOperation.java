package accCtrl.operations;

public class UnlikePostOperation implements Operation {

    @Override
    public String getOperationId() {
        return OperationValues.UNLIKE_POST.getOperation();
    }

}
