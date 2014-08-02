function(obj) {
    var ret = 0;
    for(key in obj) {
        ret += obj[key];
    }
    return ret;
}