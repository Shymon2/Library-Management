package Library.Project.controller;

import Library.Project.configuration.Translator;
import Library.Project.dto.Request.User.RoleRequest;
import Library.Project.dto.Response.ApiResponse.PageResponse;
import Library.Project.dto.Response.UserResponse.RoleResponse;
import Library.Project.service.interfaces.IRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/role")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Role Controller")
public class RoleController {
    private final IRoleService roleService;

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Create new role")
    @PostMapping("/add")
    public ResponseData<RoleResponse> addNew(HttpServletRequest httpServletRequest, @RequestBody RoleRequest request){
        return new ResponseData<>(1000, Translator.toLocale("role.create.success"), roleService.createNewRole(request));
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Get all roles")
    @GetMapping("/get-all")
    public ResponseData<PageResponse> getAll(HttpServletRequest httpServletRequest, @RequestParam int pageNo, @RequestParam int pageSize){
        return new ResponseData<>(1000, Translator.toLocale("role.get.all"), roleService.showAll(pageNo, pageSize));
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Update role")
    @PostMapping("/update")
    public ResponseData<String> updateRole(HttpServletRequest httpServletRequest, @RequestParam Long id ,@RequestBody RoleRequest request){
        roleService.updateRole(id, request);
        return new ResponseData<>(1000, Translator.toLocale("role.update.done"));
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Delete role")
    @DeleteMapping("/delete")
    public ResponseData<String> deleteRole(HttpServletRequest httpServletRequest, @RequestParam Long id){
        roleService.deleteRole(id);
        return new ResponseData<>(1000, Translator.toLocale("role.delete.done"));
    }
}
