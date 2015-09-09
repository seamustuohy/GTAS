-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', 'ROLE_ADMIN');
INSERT INTO `role` VALUES ('2', 'ROLE_CUST');
INSERT INTO `role` VALUES ('3', 'VIEW_FLIGHT_PASSENGERS');
INSERT INTO `role` VALUES ('4', 'MANAGE_QUERIES');
INSERT INTO `role` VALUES ('5', 'MANAGE_RULES');
INSERT INTO `role` VALUES ('6', 'MANAGE_WATCHLIST');
INSERT INTO `role` VALUES ('7', 'ADMIN');

-- ----------------------------
-- Records of user
-- ----------------------------
-- password is 'password'
INSERT INTO `user` VALUES ('svempati', 1,'Srinivas', 'Vempati', '$2a$10$T5xL/wIet8ev/RhqM5iDTOrqRwIbO7IkQ/DWkDe592zaBaZy8RAxu');
INSERT INTO `user` VALUES ('abandopadhay',1, 'Amit', 'Bandopadhay', '$2a$10$T5xL/wIet8ev/RhqM5iDTOrqRwIbO7IkQ/DWkDe592zaBaZy8RAxu');
INSERT INTO `user` VALUES ('jtang', 1,'Jeen', 'Tang', '$2a$10$T5xL/wIet8ev/RhqM5iDTOrqRwIbO7IkQ/DWkDe592zaBaZy8RAxu');
INSERT INTO `user` VALUES ('mcopenhafer',1, 'Mike', 'Copenhafer', '$2a$10$T5xL/wIet8ev/RhqM5iDTOrqRwIbO7IkQ/DWkDe592zaBaZy8RAxu');
INSERT INTO `user` VALUES ('bstygar', 1,'Brian', 'Styger', '$2a$10$T5xL/wIet8ev/RhqM5iDTOrqRwIbO7IkQ/DWkDe592zaBaZy8RAxu');
INSERT INTO `user` VALUES ('ladebiyi',1, 'Lola', 'Adebiyi', '$2a$10$T5xL/wIet8ev/RhqM5iDTOrqRwIbO7IkQ/DWkDe592zaBaZy8RAxu');
INSERT INTO `user` VALUES ('adelorie',1, 'Anthony', 'Delorie', '$2a$10$T5xL/wIet8ev/RhqM5iDTOrqRwIbO7IkQ/DWkDe592zaBaZy8RAxu');

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES ('bstygar', 7);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES ('svempati', 7);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES ('abandopadhay', 7);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES ('jtang', 7);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES ('mcopenhafer', 7);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES ('ladebiyi', 7);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES ('adelorie', 7);
