package Library.Project.service.interfaces;

import Library.Project.dto.Response.PageResponse;
import Library.Project.entity.User;
import Library.Project.dto.Request.UpdateUserDTO;
import Library.Project.dto.Request.UserDTO;
import Library.Project.dto.Response.UserCartResponse;
import Library.Project.dto.Response.UserInforResponse;

import java.util.List;

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
