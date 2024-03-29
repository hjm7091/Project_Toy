const util = {
    preventEvent : function(e) {
        if (e.preventDefault) {
            e.preventDefault();
        } else {
            e.returnValue = false;
        }
    },
    stopPropagation : function(e) {
        if (e.stopPropagation) {
            e.stopPropagation();
        } else {
            e.cancelBubble = true;
        }
    },
    stringToObject : function(str) {
        let properties = str.split(',');
        let obj = {};
        properties.forEach(function(property) {
            let tup = property.trim().split(':');
            obj[tup[0].trim()] = tup[1].trim();
        });
        return obj;
    },
    error : function(error) {
        alert(JSON.stringify(error));
    }
};