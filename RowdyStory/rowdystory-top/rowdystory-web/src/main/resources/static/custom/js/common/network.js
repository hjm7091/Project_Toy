var network = {
    ajax : function(method, url, data, success, error) {
        return $.ajax({
            type : method,
            url : url,
            data : data,
            dataType : "json",
            contentType : "application/hal+json",
            timeout : 240000
        }).done(success).fail(error);
    }
};