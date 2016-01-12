-- Dumping structure for view gtas.daily_apis_counts
DROP VIEW IF EXISTS `daily_apis_counts`;
-- Removing temporary table and create final VIEW structure
DROP TABLE IF EXISTS `daily_apis_counts`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` VIEW `daily_apis_counts` AS SELECT
        id,
        DATE(create_date) AS 'day',
        COUNT(IF(HOUR(create_date)=0,1,NULL)) AS '1am',
        COUNT(IF(HOUR(create_date)=1,1,NULL)) AS '2am',
        COUNT(IF(HOUR(create_date)=2,1,NULL)) AS '3am',
        COUNT(IF(HOUR(create_date)=3,1,NULL)) AS '4am',
        COUNT(IF(HOUR(create_date)=4,1,NULL)) AS '5am',
        COUNT(IF(HOUR(create_date)=5,1,NULL)) AS '6am',
        COUNT(IF(HOUR(create_date)=6,1,NULL)) AS '7am',
        COUNT(IF(HOUR(create_date)=7,1,NULL)) AS '8am',
        COUNT(IF(HOUR(create_date)=8,1,NULL)) AS '9am',
        COUNT(IF(HOUR(create_date)=9,1,NULL)) AS '10am',
        COUNT(IF(HOUR(create_date)=10,1,NULL)) AS '11am',
        COUNT(IF(HOUR(create_date)=11,1,NULL)) AS '12pm',
        COUNT(IF(HOUR(create_date)=12,1,NULL)) AS '1pm',
        COUNT(IF(HOUR(create_date)=13,1,NULL)) AS '2pm',
        COUNT(IF(HOUR(create_date)=14,1,NULL)) AS '3pm',
        COUNT(IF(HOUR(create_date)=15,1,NULL)) AS '4pm',
        COUNT(IF(HOUR(create_date)=16,1,NULL)) AS '5pm',
        COUNT(IF(HOUR(create_date)=17,1,NULL)) AS '6pm',
        COUNT(IF(HOUR(create_date)=18,1,NULL)) AS '7pm',
        COUNT(IF(HOUR(create_date)=19,1,NULL)) AS '8pm',
        COUNT(IF(HOUR(create_date)=20,1,NULL)) AS '9pm',
        COUNT(IF(HOUR(create_date)=21,1,NULL)) AS '10pm',
        COUNT(IF(HOUR(create_date)=22,1,NULL)) AS '11pm',
        COUNT(IF(HOUR(create_date)=23,1,NULL)) AS '12am'
    FROM message
    where create_date >= DATE_FORMAT(CURRENT_DATE(),'%Y-%m-%d') AND CURRENT_DATE()
    AND raw LIKE '%PAXLST%' 
    GROUP BY day ;


-- Dumping structure for view gtas.daily_pnr_counts
DROP VIEW IF EXISTS `daily_pnr_counts`;
-- Removing temporary table and create final VIEW structure
DROP TABLE IF EXISTS `daily_pnr_counts`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` VIEW `daily_pnr_counts` AS SELECT
        id,         
        DATE(create_date) AS 'day',
        COUNT(IF(HOUR(create_date)=0,1,NULL)) AS '1am',
        COUNT(IF(HOUR(create_date)=1,1,NULL)) AS '2am',
        COUNT(IF(HOUR(create_date)=2,1,NULL)) AS '3am',
        COUNT(IF(HOUR(create_date)=3,1,NULL)) AS '4am',
        COUNT(IF(HOUR(create_date)=4,1,NULL)) AS '5am',
        COUNT(IF(HOUR(create_date)=5,1,NULL)) AS '6am',
        COUNT(IF(HOUR(create_date)=6,1,NULL)) AS '7am',
        COUNT(IF(HOUR(create_date)=7,1,NULL)) AS '8am',
        COUNT(IF(HOUR(create_date)=8,1,NULL)) AS '9am',
        COUNT(IF(HOUR(create_date)=9,1,NULL)) AS '10am',
        COUNT(IF(HOUR(create_date)=10,1,NULL)) AS '11am',
        COUNT(IF(HOUR(create_date)=11,1,NULL)) AS '12pm',
        COUNT(IF(HOUR(create_date)=12,1,NULL)) AS '1pm',
        COUNT(IF(HOUR(create_date)=13,1,NULL)) AS '2pm',
        COUNT(IF(HOUR(create_date)=14,1,NULL)) AS '3pm',
        COUNT(IF(HOUR(create_date)=15,1,NULL)) AS '4pm',
        COUNT(IF(HOUR(create_date)=16,1,NULL)) AS '5pm',
        COUNT(IF(HOUR(create_date)=17,1,NULL)) AS '6pm',
        COUNT(IF(HOUR(create_date)=18,1,NULL)) AS '7pm',
        COUNT(IF(HOUR(create_date)=19,1,NULL)) AS '8pm',
        COUNT(IF(HOUR(create_date)=20,1,NULL)) AS '9pm',
        COUNT(IF(HOUR(create_date)=21,1,NULL)) AS '10pm',
        COUNT(IF(HOUR(create_date)=22,1,NULL)) AS '11pm',
        COUNT(IF(HOUR(create_date)=23,1,NULL)) AS '12am'
    FROM message
    where create_date >= DATE_FORMAT(CURRENT_DATE(),'%Y-%m-%d') AND CURRENT_DATE()
    AND raw LIKE '%PNRGOV%' 
    GROUP BY day ;