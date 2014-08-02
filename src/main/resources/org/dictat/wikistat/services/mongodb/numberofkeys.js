function(obj) {
    var ret = 0;
    for(key in obj) {
        ret += 1;
    }
    return ret;
}