package hanu.devteria.mapper;

import hanu.devteria.dto.request.PermissionRequest;
import hanu.devteria.dto.response.PermissionResponse;
import hanu.devteria.model.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    @Mapping(target = "description", source = "description")
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);

}
