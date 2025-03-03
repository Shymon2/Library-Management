package Library.Project.controller;

import Library.Project.configuration.Translator;
import Library.Project.dto.Request.RoleRequest;
import Library.Project.dto.Response.PageResponse;
import Library.Project.dto.Response.ResponseData;
import Library.Project.dto.Response.RoleResponse;
import Library.Project.service.implement.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    private final RoleService roleService;

    @PreAuthorize("hasAuthority(@roleMapping.getRoleForApi('role.add.new'))")
    @Operation(summary = "Create new role")
    @PostMapping("/add")
    public ResponseData<RoleResponse> addNew(@RequestBody RoleRequest request){
        return new ResponseData<>(1000, Translator.toLocale("role.create.success"), roleService.createNewRole(request));
    }

    @PreAuthorize("hasAuthority(@roleMapping.getRoleForApi('role.get'))")
    @Operation(summary = "Get all roles")
    @GetMapping("/get-all")
    public ResponseData<PageResponse> getAll(@RequestParam int pageNo, @RequestParam int pageSize){
        return new ResponseData<>(1000, Translator.toLocale("role.get.all"), roleService.showAll(pageNo, pageSize));
    }

    @PreAuthorize("hasAuthority(@roleMapping.getRoleForApi('role.update'))")
    @Operation(summary = "Update role")
    @PostMapping("/update")
    public ResponseData<String> updateRole(@RequestParam Long id ,@RequestBody RoleRequest request){
        roleService.updateRole(id, request);
        return new ResponseData<>(1000, Translator.toLocale("role.update.done"));
    }

    @PreAuthorize("hasAuthority(@roleMapping.getRoleForApi('role.delete'))")
    @Operation(summary = "Delete role")
    @PostMapping("/delete")
    public ResponseData<String> deleteRole(@RequestParam Long id){
        roleService.deleteRole(id);
        return new ResponseData<>(1000, Translator.toLocale("role.delete.done"));
    }
}
