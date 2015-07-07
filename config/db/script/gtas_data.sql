-- ----------------------------
-- Records of gtas_roles
-- ----------------------------
INSERT INTO `gtas_roles` VALUES ('1', 'ROLE_ADMIN');
INSERT INTO `gtas_roles` VALUES ('2', 'ROLE_CUST');

-- ----------------------------
-- Records of gtas_users
-- ----------------------------
-- password is 'password'
INSERT INTO `gtas_users` VALUES ('svempati', 'Srinivas', 'Vempati', '$10$49p6sGMHZhEzVQ4j0zLoUOpK7NmrLJjOaQTCQsnf0wqqa.IJjaeUy', '1');
INSERT INTO `gtas_users` VALUES ('abandopadhay', 'Amit', 'Bandopadhay', '$10$49p6sGMHZhEzVQ4j0zLoUOpK7NmrLJjOaQTCQsnf0wqqa.IJjaeUy', '1');
INSERT INTO `gtas_users` VALUES ('jtang', 'Jeen', 'Tang', '$10$49p6sGMHZhEzVQ4j0zLoUOpK7NmrLJjOaQTCQsnf0wqqa.IJjaeUy', '1');
INSERT INTO `gtas_users` VALUES ('mcopenhafer', 'Mike', 'Copenhafer', '$10$49p6sGMHZhEzVQ4j0zLoUOpK7NmrLJjOaQTCQsnf0wqqa.IJjaeUy', '1');
INSERT INTO `gtas_users` VALUES ('bstygar', 'Brian', 'Styger', '$10$49p6sGMHZhEzVQ4j0zLoUOpK7NmrLJjOaQTCQsnf0wqqa.IJjaeUy', '1');
INSERT INTO `gtas_users` VALUES ('ladebiyi', 'Lola', 'Adebiyi', '$10$49p6sGMHZhEzVQ4j0zLoUOpK7NmrLJjOaQTCQsnf0wqqa.IJjaeUy', '1');

-- ------------------------------
-- UI Table Mappings
-- ------------------------------
INSERT INTO `address_ui_mapping` (`id`, `columnName`, `displayName`, `type`) VALUES
	(1, 'city', 'City', 'string'),
	(2, 'country', 'Country', 'string'),
	(3, 'line1', 'Line 1', 'string'),
	(4, 'line2', 'Line 2', 'string'),
	(5, 'line3', 'Line 3', 'string'),
	(6, 'postal_code', 'Postal Code', 'string'),
	(7, 'state', 'State/Province', 'string');
	
INSERT INTO `api_ui_mapping` (`id`, `columnName`, `displayName`, `type`) VALUES
	(1, 'flight_direction', 'Flight Direction', 'string');
	
INSERT INTO `credit_card_ui_mapping` (`id`, `columnName`, `displayName`, `type`) VALUES
	(1, 'cc_number', 'Number', 'string');
	
INSERT INTO `document_ui_mapping` (`id`, `columnName`, `displayName`, `type`) VALUES
	(1, 'citizenship', 'Citizenship OR Issuance Country', 'string'),
	(2, 'exp_date', 'Expiration Date', 'string'),
	(3, 'issuance_country', 'Issuance Country', 'string'),
	(4, 'doc_number', 'Number', 'string'),
	(5, 'doc_type', 'Type', 'string');
	
INSERT INTO `email_ui_mapping` (`id`, `columnName`, `displayName`, `type`) VALUES
	(1, 'email_address', 'Address', 'string'),
	(2, 'domain', 'Domain', 'string');
	
INSERT INTO `flight_ui_mapping` (`id`, `columnName`, `displayName`, `type`) VALUES
	(1, 'airport_destination', 'Airport - Destination', 'string'),
	(2, 'airport_origin', 'Airport - Origin', 'string'),
	(3, 'carrier', 'Carrier', 'string'),
	(4, 'dest_country', 'Country - Destination', 'string'),
	(5, 'origin_country', 'Country - Origin', 'string'),
	(6, 'inbound', 'Direction - Inbound', 'string'),
	(7, 'outbound', 'Direction - Outbound', 'string'),
	(8, 'eta', 'ETA', 'datetime'),
	(9, 'etd', 'ETD', 'datetime'),
	(10, 'flightNumber', 'Number', 'string'),
	(11, 'thru', 'Thru', 'string');
	
INSERT INTO `frequent_flyer_ui_mapping` (`id`, `columnName`, `displayName`, `type`) VALUES
	(1, 'ff_airline', 'Airline', 'string'),
	(2, 'ff_number', 'Number', 'string');
	
INSERT INTO `hits_ui_mapping` (`id`, `columnName`, `displayName`, `type`) VALUES
	(1, 'has_hits', 'Has Hits', 'string'),
	(2, 'has_list_rule_hit', 'Has List Rule Hit', 'string'),
	(3, 'has_rule_hit', 'Has Rule Hit', 'string'),
	(4, 'master_list_id', 'List Rules - Master List Id', 'string'),
	(5, 'sub_list_id', 'Sub List Id', 'string'),
	(6, 'rule_id', 'Rules - Rule Id', 'string');
	
INSERT INTO `name_origin_ui_mapping` (`id`, `columnName`, `displayName`, `type`) VALUES
	(1, 'first_name', 'First Name', 'string'),
	(2, 'first_or_last_name', 'First or Last Name', 'string'),
	(3, 'last_name', 'Last Name', 'string');
	
INSERT INTO `passenger_ui_mapping` (`id`, `columnName`, `displayName`, `type`) VALUES
	(1, 'age', 'Age', 'integer'),
	(2, 'citizenship_country', 'Citizenship Country', 'string'),
	(3, 'debarkation', 'Debarkation', 'string'),
	(4, 'debarkation_country', 'Debarkation Country', 'string'),
	(5, 'dob', 'DOB', 'string'),
	(6, 'embarkation', 'Embarkation', 'string'),
	(7, 'embarkation_country', 'Embarkation Country', 'string'),
	(8, 'gender', 'Gender', 'string'),
	(9, 'first_name', 'Name - First', 'string'),
	(10, 'last_name', 'Name - Last', 'string'),
	(11, 'middle_name', 'Name - Middle', 'string'),
	(12, 'residency_country', 'Residency Country', 'string'),
	(13, 'seat', 'Seat', 'string'),
	(14, 'type', 'Type', 'string');
	
INSERT INTO `phone_ui_mapping` (`id`, `columnName`, `displayName`, `type`) VALUES
	(1, 'phone_number', 'Number', 'string');
	
INSERT INTO `pnr_ui_mapping` (`id`, `columnName`, `displayName`, `type`) VALUES
	(1, 'bag_count', 'Bag - Count', 'integer'),
	(2, 'booking_date', 'Booking Date', 'string'),
	(3, 'carrier_code', 'Carrier Code', 'string'),
	(4, 'days_booked_before_trvl', 'Days Booked Before Travel', 'integer'),
	(5, 'dwell_airport', 'Dwell - Airport', 'string'),
	(6, 'dwell_country', 'Dwell - Country', 'string'),
	(7, 'dwell_duration', 'Dwell - Duration', 'integer'),
	(8, 'dwell_total_duration', 'Dwell - Total Duration', 'integer'),
	(9, 'form_of_payment', 'Form of Payment', 'string'),
	(10, 'first_name', 'Name - First', 'string'),
	(11, 'last_name', 'Name - Last', 'string'),
	(12, 'middle_name', 'Name - Middle', 'string'),
	(13, 'origin_airport', 'Origin - Airport', 'string'),
	(14, 'origin_country', 'Origin - Country', 'string'),
	(15, 'passenger_count', 'Passenger Count', 'integer'),
	(16, 'record_locator', 'Record Locator', 'string'),
	(17, 'route', 'Route', 'string');
	
INSERT INTO `travel_agency_ui_mapping` (`id`, `columnName`, `displayName`, `type`) VALUES
	(1, 'city', 'City', 'string'),
	(2, 'name', 'Name', 'string'),
	(3, 'phone', 'Phone', 'string');
