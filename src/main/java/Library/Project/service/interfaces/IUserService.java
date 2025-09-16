package Library.Project.service.interfaces;

import Library.Project.dto.Response.ApiResponse.PageResponse;
import Library.Project.entity.User;
import Library.Project.dto.Request.User.UpdateUserDTO;
import Library.Project.dto.Request.User.UserDTO;
import Library.Project.dto.Response.UserResponse.UserInforResponse;

public interface IUserService {
    User getUserById(Long userId);

    PageResponse getAllUser(int pageNo, int pageSize);

    UserInforResponse createUser(UserDTO request);

    UserInforResponse updateUser(UpdateUserDTO request, Long id);

    void deleteUser(Long userId);

    UserInforResponse convertToInforResponse(User user);

    User findUserByUserName(String username);

    void upgradeRole(String role, Long userid);
}
