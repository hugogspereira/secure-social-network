package accCtrl.operations;

public class LikePostOperation implements Operation {

    @Override
    public String getOperationId() {
        return OperationValues.LIKE_POST.getOperation();
    }

}
