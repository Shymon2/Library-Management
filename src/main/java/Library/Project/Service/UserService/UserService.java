package Library.Project.Service.UserService;

import Library.Project.Configuration.Translator;
import Library.Project.Enums.Role;
import Library.Project.Exception.AlreadyExistsException;
import Library.Project.Exception.ResourcesNotFoundException;
import Library.Project.Model.Cart;
import Library.Project.Model.User;
import Library.Project.Repository.UserRepository;
import Library.Project.Service.CartService.CartService;
import Library.Project.dto.Request.UpdateUserDTO;
import Library.Project.dto.Request.UserDTO;
import Library.Project.dto.Response.UserCartResponse;
import Library.Project.dto.Response.UserInforResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final CartService cartService;
    private PasswordEncoder encoder;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new ResourcesNotFoundException(Translator.toLocale("user.not.found")));
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
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
                            .role(Role.USER)
                            .build();

                    Cart newCart = cartService.initializeNewCart();
                    user.setCart(newCart);
                    userRepository.save(user);
                    newCart.setUser(user);
                    return convertToInforResponse(user);
                }).orElseThrow(() -> new AlreadyExistsException(Translator.toLocale("username.existed")));
    }

    @Override
    public UserInforResponse updateUser(UpdateUserDTO request, Long id) {
        return userRepository.findById(id).map(existingUser ->{
            existingUser.setFullname(request.getFullname());
            existingUser.setPhoneNumber(request.getPhoneNUmber());
            existingUser.setDateOfBirth(request.getDateOfBirth());
            existingUser.setAddress(request.getAddress());
            return convertToInforResponse(userRepository.save(existingUser));
        }).orElseThrow(() -> new ResourcesNotFoundException(Translator.toLocale("user.not.found")));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).ifPresentOrElse(userRepository::delete,
                () -> {
                    throw new ResourcesNotFoundException(Translator.toLocale("delete.fail"));
                }
        );
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
                .cart(user.getCart())
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
                .role(user.getRole().name())
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
        Role roleFound = Role.valueOf(toUpper);
        user.setRole(roleFound);

        userRepository.save(user);
    }

    public String getUsernameFromRequest() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
