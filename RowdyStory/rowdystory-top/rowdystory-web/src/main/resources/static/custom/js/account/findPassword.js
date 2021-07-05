var index = {
    init : function() {
        var _this = this;
        $("#inputEmail").on("change keyup paste", function() {
            _this.validateEmail();
            _this.activateButton();
        });
        $("#sendEmailBtn").on("click", function() {
            _this.loadButton();
        });
    },
    validateEmail : function() {
        var _this = this;
        const re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        var email = $("#inputEmail").val();
        if(email == "") {
            _this.emptyAction("#inputEmail", "#alertEmail");
            return;
        }
        if(re.test(email)) {
            _this.validAction("#inputEmail", "#alertEmail");
        } else {
            _this.invalidAction("#inputEmail", "#alertEmail", "유효한 이메일 주소를 입력해주세요.");
        }
    },
    emptyAction : function(inputId, divId) {
        $(inputId).removeClass("is-valid").removeClass("is-invalid");
        $(divId).html("");
    },
    validAction : function(inputId, divId) {
        $(inputId).addClass("is-valid").removeClass("is-invalid");
        $(divId).addClass("valid-feedback").removeClass("invalid-feedback");
        $(divId).html("");
    },
    invalidAction : function(inputId, divId, message) {
        $(inputId).addClass("is-invalid").removeClass("is-valid");
        $(divId).addClass("invalid-feedback").removeClass("valid-feedback");
        $(divId).html(`<strong>${message}</strong>`);
    },
    activateButton : function() {
        if($("#inputEmail").hasClass("is-valid")) {
            $("#sendEmailBtn").attr("disabled", false);
            return;
        }
        if($("#inputEmail").hasClass("is-invalid")) {
            $("#sendEmailBtn").attr("disabled", true);
            return;
        }
    },
    loadButton : function() {
        $("#sendEmailBtn").html("<span class='spinner-border spinner-border-sm' role='status'></span> 인증 이메일을 보내는중...");
    }
};

index.init();