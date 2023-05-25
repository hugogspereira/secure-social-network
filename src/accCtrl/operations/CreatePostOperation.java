package accCtrl.operations;

public class CreatePostOperation implements Operation {

    @Override
    public String getOperationId() {
        return OperationValues.CREATE_POST.getOperation();
    }

}