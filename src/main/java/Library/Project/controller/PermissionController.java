package Library.Project.controller;

import Library.Project.configuration.Translator;
import Library.Project.dto.Request.PermissionRequest;
import Library.Project.dto.Response.PageResponse;
import Library.Project.dto.Response.PermissionResponse;
import Library.Project.dto.Response.ResponseData;
import Library.Project.service.implement.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    private final PermissionService permissionService;

    @PreAuthorize(value = "hasAuthority(@roleMapping.getRoleForApi('permission.add.new'))")
    @Operation(summary = "Add new permission")
    @PostMapping("/add")
    public ResponseData<PermissionResponse> addNew(@RequestBody PermissionRequest request){
        return new ResponseData<>(1000, Translator.toLocale("permission.add.success"), permissionService.create(request));
    }

    @PreAuthorize(value = "hasAuthority(@roleMapping.getRoleForApi('permission.get'))")
    @Operation(summary = "Get all permission")
    @GetMapping("/get-all")
    public ResponseData<PageResponse> getAll(@RequestParam int pageNo, @RequestParam int pageSize){
        return new ResponseData<>(1000, Translator.toLocale("permission.get.all"), permissionService.getAll(pageNo, pageSize));
    }

    @PreAuthorize(value = "hasAuthority(@roleMapping.getRoleForApi('permission.update'))")
    @Operation(summary = "Update permission")
    @PutMapping("/update")
    public ResponseData<String> updatePermission(@RequestParam Long permissionId, @RequestBody PermissionRequest request){
        permissionService.update(permissionId, request);
        return new ResponseData<>(1000, Translator.toLocale("permission.update.done"));
    }

    @PreAuthorize(value = "hasAuthority(@roleMapping.getRoleForApi('permission.delete'))")
    @Operation(summary = "Delete permission")
    @DeleteMapping("/delete")
    public ResponseData<String> deletePermission(@RequestParam Long id){
        permissionService.delete(id);
        return new ResponseData<>(1000, Translator.toLocale("permission.delete.done"));
    }
}
