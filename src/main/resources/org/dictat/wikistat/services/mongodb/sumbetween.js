function(lower, upper, obj, type) {
    var sum = 0;
    for(key in obj) {
       var keyDate = strtodate(key)
       if(keyDate.getTime() <= upper.getTime() && keyDate.getTime() >= lower.getTime()) {
           sum += obj[key];
       }
    }
    return sum;
}