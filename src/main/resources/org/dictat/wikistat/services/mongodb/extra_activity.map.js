
function() {
	dates = {
	    "9:0" : new Date(123456),
	    "9:1" : new Date(123457)
	}
	baseDate = new Date(2007,0,1)
	//holy constant from DateUtils

    sumOverInterval = 0;

	dates.keys().forEach(function(d) {
        if(typeof this.h[d] != 'Unknown') {
        	sumOverInterval += this.h[d]
        }
	});
    avgOverInterval = sumOverInterval / 7;
}
