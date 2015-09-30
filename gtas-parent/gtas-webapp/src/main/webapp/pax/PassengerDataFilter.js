app.filter('hitsConditionDisplayFilter', function () {

    return function (ruleDetail) {
        //Could be always one item populate
        var ruleCondition=ruleDetail.ruleConditions;

        var description= ruleCondition.replace (/[$]/g,'');
        return description;
    };
});
