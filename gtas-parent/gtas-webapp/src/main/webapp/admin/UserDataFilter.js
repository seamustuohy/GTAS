

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

        return function (values) {
            var roles="";
            var len=values.length;
            values.forEach(function (value, index) {
                roles+=value.roleDescription;

                if(index<len-1)
                    roles+=', '
            });
            return roles;
        };
    });