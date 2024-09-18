package hanu.devteria.mapper;


import hanu.devteria.dto.request.RoleRequest;
import hanu.devteria.dto.response.RoleResponse;
import hanu.devteria.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);

}
