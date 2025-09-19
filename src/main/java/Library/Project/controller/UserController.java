package Library.Project.controller;

import Library.Project.configuration.Translator;
import Library.Project.dto.response.ApiResponse.PageResponse;
import Library.Project.entity.User;
import Library.Project.dto.request.User.UpdateUserDTO;
import Library.Project.dto.request.User.UserDTO;
import Library.Project.dto.response.UserResponse.UserInforResponse;
import Library.Project.service.interfaces.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "User Controller")
@RequestMapping("/user")
public class UserController {
    private final IUserService userService;

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Get user by Id", description = "Id must be positive")
    @GetMapping("/getUser")
    public ResponseData<User> getUserById(HttpServletRequest httpServletRequest, @RequestParam Long userId){
        return new ResponseData<>(1000, Translator.toLocale("user.get.success"), userService.getUserById(userId));
    }

    @Operation(summary = "Sign up", description = "Create new user")
    @SecurityRequirements
    @PostMapping("/newUser")
    public ResponseData<UserInforResponse> signUp(HttpServletRequest httpServletRequest, @RequestBody UserDTO request){
        log.info("URL: {}", httpServletRequest.getRequestURI().toString());
        return new ResponseData<>(1000, Translator.toLocale("user.create.success"), userService.createUser(request));
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Update user information")
    @PutMapping("/updateUserInfo/user")
    public ResponseData<UserInforResponse> updateUserInfo(HttpServletRequest httpServletRequest,
                                                          @RequestBody UpdateUserDTO request,
                                                          @RequestParam Long userId){
        return new ResponseData<>(1000, Translator.toLocale("user.upd.success"), userService.updateUser(request, userId));
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Delete user by user id")
    @DeleteMapping("/delete/user")
    public ResponseData<String> deleteUserById(HttpServletRequest httpServletRequest, @RequestParam Long userId){
        userService.deleteUser(userId);
        return new ResponseData<>(1000, Translator.toLocale("user.del.done"));
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Add role for user")
    @PatchMapping("/upgradeRole/user")
    public ResponseData<String> updateRole(HttpServletRequest httpServletRequest,
                                           @RequestParam Long userId,
                                           @RequestParam String role){
        userService.upgradeRole(role, userId);
        return new ResponseData<>(1000, Translator.toLocale("user.upgrade.role.done"));
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Get all user")
    @GetMapping("/getAll")
    public ResponseData<PageResponse> getAllUser(HttpServletRequest httpServletRequest,
                                                 @RequestParam int pageNo,
                                                 @RequestParam int pageSize){
        return new ResponseData<>(1000, Translator.toLocale("user.show.all.success"), userService.getAllUser(pageNo, pageSize));
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Get current user info")
    @GetMapping("/getCurrentUser")
    public ResponseData<UserInforResponse> getCurrentUser(HttpServletRequest httpServletRequest){
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        User userFound = userService.findUserByUserName(user.getName());

        log.info("Username: {}", user.getName());
        user.getAuthorities().forEach(a -> log.info(a.getAuthority()));
        return new ResponseData<>(1000, Translator.toLocale("user.info.show"), userService.convertToInforResponse(userFound));
    }
}
