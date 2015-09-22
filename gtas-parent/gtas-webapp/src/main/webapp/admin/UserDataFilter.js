

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
            console.log(len);
            values.forEach(function (value, index) {
                roles+=value.roleDescription;

                if(index<len-1)
                    roles+=', '
                console.log(roles);
            });
            return roles;
        };
    });