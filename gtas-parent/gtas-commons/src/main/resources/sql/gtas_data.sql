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
INSERT INTO `user` VALUES ('nsamha', 1,'Nael', 'Samha', 'cGFzc3dvcmQ=');
INSERT INTO `user` VALUES ('bstygar', 1,'Brian', 'Stygar', 'cGFzc3dvcmQ=');
INSERT INTO `user` VALUES ('svempati', 1,'Srinivas', 'Vempati', 'cGFzc3dvcmQ=');
INSERT INTO `user` VALUES ('abandopadhay',1, 'Amit', 'Bandopadhay', 'cGFzc3dvcmQ=');
INSERT INTO `user` VALUES ('jtang', 1,'Jeen', 'Tang', 'cGFzc3dvcmQ=');
INSERT INTO `user` VALUES ('mcopenhafer',1, 'Mike', 'Copenhafer', 'cGFzc3dvcmQ=');
INSERT INTO `user` VALUES ('ladebiyi',1, 'Lola', 'Adebiyi', 'cGFzc3dvcmQ=');
INSERT INTO `user` VALUES ('adelorie',1, 'Anthony', 'Delorie', 'cGFzc3dvcmQ=');

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
