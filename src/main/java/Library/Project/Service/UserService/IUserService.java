package Library.Project.Service.UserService;

import Library.Project.Model.User;
import Library.Project.dto.Request.UpdateUserDTO;
import Library.Project.dto.Request.UserDTO;
import Library.Project.dto.Response.UserCartResponse;
import Library.Project.dto.Response.UserInforResponse;

import java.util.List;

public interface IUserService {
    User getUserById(Long userId);

    List<User> getAllUser();

    UserInforResponse createUser(UserDTO request);

    UserInforResponse updateUser(UpdateUserDTO request, Long id);

    void deleteUser(Long userId);

    UserCartResponse convertToCartResponse(User user);

    UserInforResponse convertToInforResponse(User user);

    User findUserByUserName(String username);

    void upgradeRole(String role, Long userid);
}
