function(str) {
    var ret = 0;
    for(var i = 0; i < str.length; i++) {
        ret = (ret * digits.length) + chartoint(str[i]);
    }
    return ret;
}