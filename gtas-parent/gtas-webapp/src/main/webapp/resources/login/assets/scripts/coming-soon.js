var ComingSoon = function () {

    return {
        //main function to initiate the module
        init: function () {

            $.backstretch([
    		        "resources/login/assets/img/bg/1.jpg",
    		        "resources/login/assets/img/bg/2.jpg",
    		        "resources/login/assets/img/bg/3.jpg",
    		        "resources/login/assets/img/bg/5.jpg",
    		        "resources/login/assets/img/bg/6.jpg",
    		        "resources/login/assets/img/bg/4.jpg"
    		        ], {
    		          fade: 100,
    		          duration: 1000
    		    });

            var austDay = new Date();
            austDay = new Date(austDay.getFullYear() + 1, 1 - 1, 26);
            $('#defaultCountdown').countdown({until: austDay});
            $('#year').text(austDay.getFullYear());
        }

    };

}();