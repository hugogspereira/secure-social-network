package accCtrl.operations;

public enum OperationValues {
    ACCESS_POST("access post"),
    CREATE_POST("create post"),
    DELETE_POST("delete post"),
    LIKE_UNLIKE_POST("like post"),
    CREATE_PAGE("create page"),
    AUTHORIZE_FOLLOW("authorize follow"),
    DELETE_PAGE("delete page");

    private final String operation;

    OperationValues(String operation) {
        this.operation = operation;
    }

    public String getOperation() {
        return operation;
    }
}
