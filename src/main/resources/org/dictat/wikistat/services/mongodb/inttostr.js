function(num) {
    log('inttostr: '+num);
    var cntr = num;
    var ret = '';
    do {
        log('dividing '+cntr);
        var remain = cntr % digits.length;
        log('remainder '+remain);
        cntr -= remain;
        ret = digits[remain] + ret;
        cntr = cntr / digits.length;
    } while(cntr != 0)
    return ret;
}