package Library.Project.Controller;

import Library.Project.Configuration.Translator;
import Library.Project.Model.User;
import Library.Project.Service.UserService.UserService;
import Library.Project.dto.Request.UpdateUserDTO;
import Library.Project.dto.Request.UserDTO;
import Library.Project.dto.Response.ResponseData;
import Library.Project.dto.Response.UserInforResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "User Controller")
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PreAuthorize(value = "hasRole('ADMIN') || hasAuthority(@roleService.getRoleForApi('library.user.getUserById'))")
    @Operation(summary = "Get user by Id", description = "Id must be positive")
    @GetMapping("/getUser")
    public ResponseData<User> getUserById(@RequestParam Long userId){
        return new ResponseData<>(1000, Translator.toLocale("user.get.success"), userService.getUserById(userId));
    }

    @Operation(summary = "Sign up", description = "Create new user")
    @PostMapping("/newUser")
    public ResponseData<UserInforResponse> signUp(@RequestBody UserDTO request){
        return new ResponseData<>(1000, Translator.toLocale("user.create.success"), userService.createUser(request));
    }

    @PreAuthorize(value = "hasRole('ADMIN') || hasAuthority(@roleService.getRoleForApi('library.user.updateUserInfor'))")
    @Operation(summary = "Update user information")
    @PutMapping("/updateUserInfor/user")
    public ResponseData<UserInforResponse> updateUserInfor(@RequestBody UpdateUserDTO request,
                                                      @RequestParam Long userId){
        return new ResponseData<>(1000, Translator.toLocale("user.upd.success"), userService.updateUser(request, userId));
    }

    @PreAuthorize(value = "hasRole('ADMIN') || hasAuthority(@roleService.getRoleForApi('library.user.deleteUserById'))")
    @Operation(summary = "Delete user by user id")
    @DeleteMapping("/delete/user")
    public ResponseData<String> deleteUserById(@RequestParam Long userId){
        userService.deleteUser(userId);
        return new ResponseData<>(1000, Translator.toLocale("user.del.done"));
    }

    @PreAuthorize(value = "hasRole('ADMIN') || hasAuthority(@roleService.getRoleForApi('library.user.upgradeRole'))")
    @Operation(summary = "Add role for user")
    @PatchMapping("/upgradeRole/user")
    public ResponseData<String> updateRole(@RequestParam Long userId, @RequestParam String role){
        userService.upgradeRole(role, userId);
        return new ResponseData<>(1000, Translator.toLocale("user.upgrade.role.done"));
    }

    @PreAuthorize(value = "hasRole('ADMIN') || hasAuthority(@roleService.getRoleForApi('library.user.getAll'))")
    @Operation(summary = "Get all user")
    @GetMapping("/getAll")
    public ResponseData<List<User>> getAllUser(){
        return new ResponseData<>(1000, Translator.toLocale("user.show.all.success"), userService.getAllUser());
    }

    @PreAuthorize(value = "hasRole('ADMIN') || hasAuthority(@roleService.getRoleForApi('library.user.getCurrentUserInfor'))")
    @Operation(summary = "Get current user info")
    @GetMapping("/getCurrentUser")
    public ResponseData<UserInforResponse> getCurrentUser(){
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        User userFound = userService.findUserByUserName(user.getName());

        log.info("Username: {}", user.getName());
        user.getAuthorities().forEach(a -> log.info(a.getAuthority()));
        return new ResponseData<>(1000, Translator.toLocale("user.infor.show"), userService.convertToInforResponse(userFound));
    }
}
