package Library.Project.service.interfaces;

import Library.Project.dto.response.ApiResponse.PageResponse;
import Library.Project.entity.User;
import Library.Project.dto.request.User.UpdateUserDTO;
import Library.Project.dto.request.User.UserDTO;
import Library.Project.dto.response.UserResponse.UserInforResponse;

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
