app.factory("sharedPaxData", function() {
	 var items = [];
	    var itemsService = {};
	    
	    itemsService.add = function(item, index) {
	        items[index] = item;
	    };
	    itemsService.list = function(index) {
	        return items[index];
	    };
	    
	    itemsService.getAll = function() {
	        return items;
	    };
	    
	    
	    return itemsService;
});