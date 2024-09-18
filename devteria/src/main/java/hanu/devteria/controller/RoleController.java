package hanu.devteria.controller;

import hanu.devteria.dto.request.PermissionRequest;
import hanu.devteria.dto.request.RoleRequest;
import hanu.devteria.dto.response.ApiResponse;
import hanu.devteria.dto.response.PermissionResponse;
import hanu.devteria.dto.response.RoleResponse;
import hanu.devteria.service.PermissionService;
import hanu.devteria.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @PostMapping()
    ApiResponse<RoleResponse> create(@RequestBody RoleRequest request){
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.create(request))
                .build();
    }
    @GetMapping()
    ApiResponse<List<RoleResponse>> getAll(){
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }

    @DeleteMapping("/{roleId}")
    ApiResponse<Void> delete(@PathVariable String roleId){
        roleService.delete(roleId);
        return ApiResponse.<Void>builder().build();
    }

}


