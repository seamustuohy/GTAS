var Lock = function () {

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
		          fade: 1000,
		          duration: 3000
		      });
        }

    };

}();