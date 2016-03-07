-- Dumping structure for view gtas.daily_apis_counts
DROP VIEW IF EXISTS `daily_apis_counts`;
-- Removing temporary table and create final VIEW structure
DROP TABLE IF EXISTS `daily_apis_counts`;
CREATE VIEW `daily_apis_counts` AS SELECT
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
CREATE VIEW `daily_pnr_counts` AS SELECT
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


DROP VIEW IF EXISTS YTD_RULE_HIT_COUNTS;
DROP TABLE IF EXISTS YTD_RULE_HIT_COUNTS;
DROP VIEW IF EXISTS YTD_RULES;
DROP TABLE IF EXISTS YTD_RULES;

    CREATE VIEW YTD_RULE_HIT_COUNTS AS
    Select
      r.id as 'ruleid',
      r.UDR_RULE_REF as 'ruleref',
      COUNT(hd.id) as 'hits'
      FROM rule as r JOIN hit_detail as hd ON hd.rule_id = r.id GROUP BY r.id;


    CREATE VIEW YTD_RULES AS
    SELECT
    	  udr.id as 'id',
        rm.TITLE as 'RuleName',
        r.hits as 'RuleHits',
        udr.author as 'CreatedBy',
        DATE_FORMAT(rm.START_DT,'%d %b %Y') as 'CreatedOn',
        udr.edited_by as 'LastUpdatedBy',
        DATE_FORMAT(udr.edit_dt,'%d %b %Y')  as 'LastEditedOn'

        FROM UDR_RULE as udr, rule_meta rm,
        YTD_RULE_HIT_COUNTS as r
        WHERE udr.id = r.ruleref AND rm.ID = r.ruleid;

DROP VIEW IF EXISTS YTD_AIRPORT_STATS;
DROP TABLE IF EXISTS YTD_AIRPORT_STATS;

    CREATE VIEW YTD_AIRPORT_STATS AS
    SELECT a.id as 'ID', a.iata 'AIRPORT', COUNT(*) 'FLIGHTS', SUM(f.rule_hit_count) 'RULEHITS', SUM(f.list_hit_count) 'WATCHLISTHITS'
        FROM Flight f, Airport a
        WHERE a.country = 'USA'
        AND (TRIM(a.iata) = TRIM( f.origin )
        OR TRIM(a.iata) = TRIM(f.destination))
        GROUP BY a.iata
        ORDER BY COUNT(*) DESC
        LIMIT 5;
