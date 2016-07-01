/*
 * All GTAS code is Copyright 2016, Unisys Corporation.
 * 
 * Please see LICENSE.txt for details.
 */
package gov.gtas.services.security;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.gtas.model.Filter;
import gov.gtas.model.Role;
import gov.gtas.model.User;
import gov.gtas.services.Filter.FilterData;
import gov.gtas.services.Filter.FilterServiceUtil;

@Component
public class UserServiceUtil {

    @Autowired
    private FilterServiceUtil filterServiceUtil;

    public List<UserData> getUserDataListFromEntityCollection(Iterable<User> userEntities) {

        List<UserData> users = StreamSupport.stream(userEntities.spliterator(), false)
                .map(new Function<User, UserData>() {

                    @Override
                    public UserData apply(User user) {

                        Set<RoleData> roles = user.getRoles().stream().map(new Function<Role, RoleData>() {

                            @Override
                            public RoleData apply(Role role) {
                                return new RoleData(role.getRoleId(), role.getRoleDescription());
                            }

                        }).collect(Collectors.toSet());
                        FilterData filterData = null;
                        if (user.getFilter() != null) {

                            filterData = filterServiceUtil.mapFilterDataFromEntity(user.getFilter());

                        }
                        return new UserData(user.getUserId(), user.getPassword(), user.getFirstName(),
                                user.getLastName(), user.getActive(), roles, filterData);
                    }
                }).collect(Collectors.toList());

        return users;
    }

    public UserData mapUserDataFromEntity(User entity) {

        // System.out.println(entity);

        Set<RoleData> roles = entity.getRoles().stream().map(new Function<Role, RoleData>() {
            @Override
            public RoleData apply(Role role) {
                return new RoleData(role.getRoleId(), role.getRoleDescription());
            }
        }).collect(Collectors.toSet());

        FilterData filterData = null;

        if (entity.getFilter() != null) {
            filterData = filterServiceUtil.mapFilterDataFromEntity(entity.getFilter());
        }

        UserData userData = new UserData(entity.getUserId(), entity.getPassword(), entity.getFirstName(),
                entity.getLastName(), entity.getActive(), roles, filterData);

        return userData;
    }

    public User mapUserEntityFromUserData(UserData userData) {

        Set<Role> roles = userData.getRoles().stream().map(new Function<RoleData, Role>() {
            @Override
            public Role apply(RoleData roleData) {
                return new Role(roleData.getRoleId(), roleData.getRoleDescription());
            }

        }).collect(Collectors.toSet());

        Filter filter = null;
        if (userData.getFilter() != null) {

            filter = filterServiceUtil.mapFilterEntityFromFilterData(userData.getFilter());
        }

        User user = new User(userData.getUserId(), userData.getPassword(), userData.getFirstName(),
                userData.getLastName(), userData.getActive(), roles, filter);

        return user;
    }
}
