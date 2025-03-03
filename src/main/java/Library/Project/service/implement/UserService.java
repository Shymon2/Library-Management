package Library.Project.service.implement;

import Library.Project.dto.Response.PageResponse;
import Library.Project.enums.ErrorCode;
import Library.Project.enums.Role;
import Library.Project.entity.User;
import Library.Project.exception.AppException;
import Library.Project.repository.UserRepository;
import Library.Project.dto.Request.UpdateUserDTO;
import Library.Project.dto.Request.UserDTO;
import Library.Project.dto.Response.UserCartResponse;
import Library.Project.dto.Response.UserInforResponse;
import Library.Project.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private PasswordEncoder encoder;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new AppException(ErrorCode.NOT_FOUND));
    }

    @Override
    public PageResponse getAllUser(int pageNo, int pageSize) {
        Page<User> userFound = userRepository.findUserByIsDelete(PageRequest.of(pageNo - 1, pageSize), false);

        List<UserInforResponse> userList = new ArrayList<>();
        userFound.getContent().forEach(a -> userList.add(UserInforResponse.builder()
                        .username(a.getUsername())
                        .fullname(a.getFullname())
                        .phoneNumber(a.getPhoneNumber())
                        .address(a.getAddress())
                        .identityNum(a.getIdentityNum())
                        .dateOfBirth(a.getDateOfBirth())
                        .role(a.getRole())
                .build()));

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(userFound.getTotalPages())
                .items(userList)
                .build();
    }

    @Override
    public UserInforResponse createUser(UserDTO request) {
        return Optional.of(request)
                .filter(user -> !userRepository.existsByUsername(request.getUsername()))
                .map(req -> {
                    encoder = new BCryptPasswordEncoder(10);
                    User user = User.builder()
                            .username(request.getUsername())
                            .password(encoder.encode(request.getPassword()))
                            .fullname(request.getFullname())
                            .phoneNumber(request.getPhoneNUmber())
                            .identityNum(request.getIdentityNum())
                            .dateOfBirth(request.getDateOfBirth())
                            .address(request.getAddress())
                            .role(Role.USER.getName())
                            .build();

                    userRepository.save(user);
                    return convertToInforResponse(user);
                }).orElseThrow(() -> new AppException(ErrorCode.ALREADY_EXISTED));
    }

    @Override
    public UserInforResponse updateUser(UpdateUserDTO request, Long id) {
        return userRepository.findById(id).map(existingUser ->{
            existingUser.setFullname(request.getFullname());
            existingUser.setPhoneNumber(request.getPhoneNUmber());
            existingUser.setDateOfBirth(request.getDateOfBirth());
            existingUser.setAddress(request.getAddress());
            return convertToInforResponse(userRepository.save(existingUser));
        }).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
    }

    @Override
    public void deleteUser(Long userId) {
        User user = getUserById(userId);
        user.setDelete(true);
    }

    @Override
    public UserCartResponse convertToCartResponse(User user) {
        return UserCartResponse.builder()
                .username(user.getUsername())
                .fullname(user.getFullname())
                .identityNum(user.getIdentityNum())
                .phoneNumber(user.getPhoneNumber())
                .dateOfBirth(user.getDateOfBirth())
                .address(user.getAddress())
                .build();
    }

    @Override
    public UserInforResponse convertToInforResponse(User user) {
        return UserInforResponse.builder()
                .username(user.getUsername())
                .fullname(user.getFullname())
                .identityNum(user.getIdentityNum())
                .phoneNumber(user.getPhoneNumber())
                .dateOfBirth(user.getDateOfBirth())
                .address(user.getAddress())
                .role(user.getRole())
                .build();
    }

    @Override
    public User findUserByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void upgradeRole(String role, Long userId) {
        User user = getUserById(userId);

        String toUpper = role.toUpperCase();
        user.setRole(toUpper);

        userRepository.save(user);
    }

    public String getUsernameFromRequest() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
