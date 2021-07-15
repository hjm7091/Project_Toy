var account = {
    getByEmail : function(data, success, error) {
        return network.ajax("GET", ACCOUNT_API, data, success, error);
    },
    register : function(data, success, error) {
        return network.ajax("POST", ACCOUNT_API, data, success, error);
    }
};