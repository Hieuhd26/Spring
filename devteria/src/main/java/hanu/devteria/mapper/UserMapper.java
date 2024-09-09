package hanu.devteria.mapper;

import hanu.devteria.dto.request.UserCreationRequest;
import hanu.devteria.dto.request.UserUpdateRequest;
import hanu.devteria.dto.response.UserResponse;
import hanu.devteria.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);
    UserResponse toUserResponse(User user);
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
