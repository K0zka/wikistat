function(msg) {
    db.jslog.save({m: msg, t: new Date()});
}