-- ----------------------------
-- Records of gtas_roles
-- ----------------------------
INSERT INTO `role` VALUES ('1', 'ROLE_ADMIN');
INSERT INTO `role` VALUES ('2', 'ROLE_CUST');
INSERT INTO `role` VALUES ('3', 'VIEW_FLIGHT_PASSENGERS');
INSERT INTO `role` VALUES ('4', 'MANAGE_QUERIES');
INSERT INTO `role` VALUES ('5', 'MANAGE_RULES');
INSERT INTO `role` VALUES ('6', 'MANAGE_WATCHLIST');
INSERT INTO `role` VALUES ('7', 'ADMIN');

-- ----------------------------
-- Records of gtas_users
-- ----------------------------
-- password is 'password'
INSERT INTO `user` VALUES ('svempati', 'Srinivas', 'Vempati', '$2a$10$T5xL/wIet8ev/RhqM5iDTOrqRwIbO7IkQ/DWkDe592zaBaZy8RAxu', '1');
INSERT INTO `user` VALUES ('abandopadhay', 'Amit', 'Bandopadhay', '$2a$10$T5xL/wIet8ev/RhqM5iDTOrqRwIbO7IkQ/DWkDe592zaBaZy8RAxu', '1');
INSERT INTO `user` VALUES ('jtang', 'Jeen', 'Tang', '$2a$10$T5xL/wIet8ev/RhqM5iDTOrqRwIbO7IkQ/DWkDe592zaBaZy8RAxu', '1');
INSERT INTO `user` VALUES ('mcopenhafer', 'Mike', 'Copenhafer', '$2a$10$T5xL/wIet8ev/RhqM5iDTOrqRwIbO7IkQ/DWkDe592zaBaZy8RAxu', '1');
INSERT INTO `user` VALUES ('bstygar', 'Brian', 'Styger', '$2a$10$T5xL/wIet8ev/RhqM5iDTOrqRwIbO7IkQ/DWkDe592zaBaZy8RAxu', '1');
INSERT INTO `user` VALUES ('ladebiyi', 'Lola', 'Adebiyi', '$2a$10$T5xL/wIet8ev/RhqM5iDTOrqRwIbO7IkQ/DWkDe592zaBaZy8RAxu', '1');
INSERT INTO `user` VALUES ('adelorie', 'Anthony', 'Delorie', '$2a$10$T5xL/wIet8ev/RhqM5iDTOrqRwIbO7IkQ/DWkDe592zaBaZy8RAxu', '1');


INSERT INTO `user_role` (`user_id`, `role_id`) VALUES ('bstygar', 7);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES ('svempati', 7);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES ('abandopadhay', 7);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES ('jtang', 7);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES ('mcopenhafer', 7);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES ('ladebiyi', 7);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES ('adelorie', 7);
