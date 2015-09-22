

app.filter('userStatusFilter', function () {
        return function (value) {
            if (value == 1) {
                return "true";
            } else {
                return "false";
            }
        };
    })
    .filter('roleDescriptionFilter', function () {
        var roles="";
        return function (values) {

            var len=values.length;
            values.forEach(function (value, index) {
                roles+=value.roleDescription;
                if(index<len-1)
                    roles+=','
            });
            return roles;
        };
    });