var account = {
    getByEmail : function(data, success, error) {
        return network.ajax("GET", ACCOUNT_EMAIL_API, data, success, error);
    },
    register : function(data, success, error) {
        return network.ajax("POST", ACCOUNT_API, data, success, error);
    },
    modify : function(data, success, error) {
        return network.ajax("PUT", ACCOUNT_API, data, success, error);
    },
    modifyEmail : function(data, success, error) {
        return network.ajax("PUT", ACCOUNT_EMAIL_API, data, success, error);
    }
};