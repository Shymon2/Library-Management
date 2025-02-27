package Library.Project.constant;

public interface RolePermissions {
    String[] admin_permission = {"EVERYTHING"};
    String[] manager_permission = {"GET_USER_BY_ID", "UPDATE_USER_INFOR", "DELETE_USER_BY_ID", "GET_ALL_USER",
            "GET_CART_BY_ID", "APPROVE_ORDER", "SET_BORROWED", "SET_RETURN", "SET_OVER_DUE", "GET_USER_BY_USER", "GET_ORDER_BY_STATUS"};
    String[] librarian_permission = {"UPDATE_USER_INFOR", "GET_CURRENT_USER_INFOR", "GET_BOOK_BY_ID", "ADD_NEW_BOOK",
            "DELETE_BOOK_BY_ID", "UPDATE_BOOK_BY_ID", "CREATE_NEW_CATEGORY", "GET_CATEGORY_BY_ID", "UPDATE_CATEGORY",
            "DELETE_CATEGORY"};
    String[] user_permission = {"UPDATE_USER_INFOR", "GET_CURRENT_USER_INFOR"};
}
