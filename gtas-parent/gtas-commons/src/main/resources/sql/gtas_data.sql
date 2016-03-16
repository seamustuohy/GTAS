-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', 'Admin');
INSERT INTO `role` VALUES ('2', 'Manage Queries');
INSERT INTO `role` VALUES ('3', 'View Flight And Passenger');
INSERT INTO `role` VALUES ('4', 'Manage Watch List');
INSERT INTO `role` VALUES ('5', 'Manage Rules');

-- ----------------------------
-- Records of user
-- ----------------------------
-- password is 'password'
INSERT INTO `user` VALUES ('nsamha', 1,'Nael', 'Samha', '$2a$10$0rGc.QzA0MH7MM7OXqynJ.2Cnbdf9PiNk4ffi4ih6LSW3y21OkspG');
INSERT INTO `user` VALUES ('bstygar', 1,'Brian', 'Stygar', '$2a$10$0rGc.QzA0MH7MM7OXqynJ.2Cnbdf9PiNk4ffi4ih6LSW3y21OkspG');
INSERT INTO `user` VALUES ('svempati', 1,'Srinivas', 'Vempati', '$2a$10$0rGc.QzA0MH7MM7OXqynJ.2Cnbdf9PiNk4ffi4ih6LSW3y21OkspG');
INSERT INTO `user` VALUES ('abandopadhay',1, 'Amit', 'Bandopadhay', '$2a$10$0rGc.QzA0MH7MM7OXqynJ.2Cnbdf9PiNk4ffi4ih6LSW3y21OkspG');
INSERT INTO `user` VALUES ('jtang', 1,'Jeen', 'Tang', '$2a$10$0rGc.QzA0MH7MM7OXqynJ.2Cnbdf9PiNk4ffi4ih6LSW3y21OkspG');
INSERT INTO `user` VALUES ('mcopenhafer',1, 'Mike', 'Copenhafer', '$2a$10$0rGc.QzA0MH7MM7OXqynJ.2Cnbdf9PiNk4ffi4ih6LSW3y21OkspG');
INSERT INTO `user` VALUES ('ladebiyi',1, 'Lola', 'Adebiyi', '$2a$10$0rGc.QzA0MH7MM7OXqynJ.2Cnbdf9PiNk4ffi4ih6LSW3y21OkspG');
INSERT INTO `user` VALUES ('adelorie',1, 'Anthony', 'Delorie', '$2a$10$0rGc.QzA0MH7MM7OXqynJ.2Cnbdf9PiNk4ffi4ih6LSW3y21OkspG');
INSERT INTO `user` VALUES ('gtas',1, 'GTAS', 'Application User', '$2a$10$0rGc.QzA0MH7MM7OXqynJ.2Cnbdf9PiNk4ffi4ih6LSW3y21OkspG');
INSERT INTO `user` VALUES ('kwillard',1, 'Kevin', 'Willard', '$2a$10$0rGc.QzA0MH7MM7OXqynJ.2Cnbdf9PiNk4ffi4ih6LSW3y21OkspG');
INSERT INTO `user` VALUES ('admin',1, 'Admin', 'Admin', '$2a$10$0rGc.QzA0MH7MM7OXqynJ.2Cnbdf9PiNk4ffi4ih6LSW3y21OkspG');

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES ('nsamha', 1);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES ('bstygar', 1);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES ('svempati', 1);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES ('abandopadhay', 1);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES ('jtang', 1);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES ('mcopenhafer', 1);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES ('ladebiyi', 1);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES ('adelorie', 1);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES ('kwillard', 1);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES ('admin', 1);

-- ----------------------------
-- Records of flight_direction
-- ----------------------------

INSERT INTO `flight_direction` VALUES (1,'I', 'Inbound');
INSERT INTO `flight_direction` VALUES (2,'O', 'Outbound');
INSERT INTO `flight_direction` VALUES (3,'A', 'Any');

-- ----------------------------
-- Records of app_configuration
-- ----------------------------
insert into app_configuration (opt, val, description) values('HOME_COUNTRY', 'USA', 'home country for the loader to determine incoming/outgoing flights');
insert into app_configuration (opt, val, description) values('QUEUE', 'gtasQ', 'queue name for storing incoming messages');
insert into app_configuration (opt, val, description) values('Dashboard Time Adjustment', 'HOURLY_ADJ', '-5');
