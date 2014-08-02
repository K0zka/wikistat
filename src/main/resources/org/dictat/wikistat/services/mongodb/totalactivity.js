function(record) {
    var ret = 0;
    if('h' in record) {
        ret += sumofall(record.h);
    }
    if('d' in record) {
            ret += sumofall(record.d);
    }
    if('m' in record) {
                ret += sumofall(record.m);
    }
    if('y' in record) {
                    ret += sumofall(record.y);
    }
    return ret;
}