const account = {
    getByEmail : function(data, success, error) {
        return network.ajax("GET", ACCOUNT_EMAIL_API, data, success, error);
    },
    register : function(data, success, error) {
        return network.ajax("POST", ACCOUNT_REGISTER_API, data, success, error);
    },
    modify : function(data, success, error) {
        return network.ajax("PUT", ACCOUNT_API, data, success, error);
    },
    modifyEmail : function(data, success, error) {
        return network.ajax("PUT", ACCOUNT_EMAIL_API, data, success, error);
    },
    modifyPassword : function(data, success, error) {
        return network.ajax("PUT", ACCOUNT_PASSWORD_API, data, success, error);
    }
};