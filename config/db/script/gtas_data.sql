-- ----------------------------
-- Records of gtas_roles
-- ----------------------------
INSERT INTO `gtas_roles` VALUES ('1', 'ROLE_ADMIN');
INSERT INTO `gtas_roles` VALUES ('2', 'ROLE_CUST');
INSERT INTO `gtas_roles` VALUES ('3', 'VIEW_FLIGHT_PASSENGERS');
INSERT INTO `gtas_roles` VALUES ('4', 'MANAGE_QUERIES');
INSERT INTO `gtas_roles` VALUES ('5', 'MANAGE_RULES');
INSERT INTO `gtas_roles` VALUES ('6', 'MANAGE_WATCHLIST');
INSERT INTO `gtas_roles` VALUES ('7', 'ADMIN');

-- ----------------------------
-- Records of gtas_users
-- ----------------------------
-- password is 'password'
INSERT INTO `gtas_users` VALUES ('svempati', 'Srinivas', 'Vempati', '$2a$10$T5xL/wIet8ev/RhqM5iDTOrqRwIbO7IkQ/DWkDe592zaBaZy8RAxu', '1');
INSERT INTO `gtas_users` VALUES ('abandopadhay', 'Amit', 'Bandopadhay', '$2a$10$T5xL/wIet8ev/RhqM5iDTOrqRwIbO7IkQ/DWkDe592zaBaZy8RAxu', '1');
INSERT INTO `gtas_users` VALUES ('jtang', 'Jeen', 'Tang', '$2a$10$T5xL/wIet8ev/RhqM5iDTOrqRwIbO7IkQ/DWkDe592zaBaZy8RAxu', '1');
INSERT INTO `gtas_users` VALUES ('mcopenhafer', 'Mike', 'Copenhafer', '$2a$10$T5xL/wIet8ev/RhqM5iDTOrqRwIbO7IkQ/DWkDe592zaBaZy8RAxu', '1');
INSERT INTO `gtas_users` VALUES ('bstygar', 'Brian', 'Styger', '$2a$10$T5xL/wIet8ev/RhqM5iDTOrqRwIbO7IkQ/DWkDe592zaBaZy8RAxu', '1');
INSERT INTO `gtas_users` VALUES ('ladebiyi', 'Lola', 'Adebiyi', '$2a$10$T5xL/wIet8ev/RhqM5iDTOrqRwIbO7IkQ/DWkDe592zaBaZy8RAxu', '1');
INSERT INTO `gtas_users` VALUES ('adelorie', 'Anthony', 'Delorie', '$2a$10$T5xL/wIet8ev/RhqM5iDTOrqRwIbO7IkQ/DWkDe592zaBaZy8RAxu', '1');


INSERT INTO `authorities` VALUES('1','jtang','2','jtang');
INSERT INTO `authorities` VALUES('2','jtang','4','jtang');
INSERT INTO `authorities` VALUES('3','jtang','3','jtang');
INSERT INTO `authorities` VALUES('4','bstygar','7','bstygar');
INSERT INTO `authorities` VALUES('5','mcopenhafer','3','mcopenhafer');
INSERT INTO `authorities` VALUES('6','mcopenhafer','4','mcopenhafer');
INSERT INTO `authorities` VALUES('7','mcopenhafer','5','mcopenhafer');
