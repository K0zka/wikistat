function(record) {
    var ret = null;

    var timeSlices = ['h','d','m','y'];
    for(var i = 0; i<timeSlices.length; i++) {
        var timeSlice = timeSlices[i];
        log('checking time slice ' + timeSlice);
        if(timeSlice in record) {
            log('have activity');
            for(key in record[timeSlice]) {
                var d = strtodate(key, timeSlice);
                log(key + ' == ' + d);
                if(ret != null || d > ret) {
                    log('new last activity');
                    ret = d;
                }
            }
        }
    }

    return ret;
}