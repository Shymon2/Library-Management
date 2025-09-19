package Library.Project.controller;

import Library.Project.configuration.Translator;
import Library.Project.dto.request.User.PermissionRequest;
import Library.Project.dto.response.ApiResponse.PageResponse;
import Library.Project.dto.response.UserResponse.PermissionResponse;
import Library.Project.service.interfaces.IPermissionService;
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
@RequestMapping("/permission")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Permission Controller")
public class PermissionController {
    private final IPermissionService permissionService;

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Add new permission")
    @PostMapping("/add")
    public ResponseData<PermissionResponse> addNew(HttpServletRequest httpServletRequest, @RequestBody PermissionRequest request){
        return new ResponseData<>(1000, Translator.toLocale("permission.add.success"), permissionService.create(request));
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Get all permission")
    @GetMapping("/get-all")
    public ResponseData<PageResponse> getAll(HttpServletRequest httpServletRequest, @RequestParam int pageNo, @RequestParam int pageSize){
        return new ResponseData<>(1000, Translator.toLocale("permission.get.all"), permissionService.getAll(pageNo, pageSize));
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Update permission")
    @PutMapping("/update")
    public ResponseData<String> updatePermission(HttpServletRequest httpServletRequest, @RequestParam Long permissionId, @RequestBody PermissionRequest request){
        permissionService.update(permissionId, request);
        return new ResponseData<>(1000, Translator.toLocale("permission.update.done"));
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Delete permission")
    @DeleteMapping("/delete")
    public ResponseData<String> deletePermission(HttpServletRequest httpServletRequest, @RequestParam Long id){
        permissionService.delete(id);
        return new ResponseData<>(1000, Translator.toLocale("permission.delete.done"));
    }
}
