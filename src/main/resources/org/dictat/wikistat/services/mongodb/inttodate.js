function(num, type) {
    var ret = new Date( basedate.getTime());
    switch(type) {
        case 'h' :
            ret.setUTCHours( ret.getUTCHours() + num);
        break;
        case 'd' :
            ret.setUTCDate( ret.getUTCDate() + num);
        break;
        case 'm' :
            ret.setUTCMonth( ret.getUTCMonth() + num);
        break;
        case 'y' :
            ret.setUTCFullYear( ret.getUTCFullYear() + num );
        break;
        default:
            throw 'unknown type: ' + type;
    }
    return ret;
}