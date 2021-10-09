const index = {
    init : function() {
        const _this = this;
        $("#inputEmail").on("change keyup paste", function() {
            _this.validateEmail();
            _this.activateButton();
        });
        $("#inputPassword").on("change keyup paste", function() {
            _this.validatePassword();
            _this.activateButton();
        });
        $('.btn_social').on("click", function() {
            let socialType = $(this).data('social');
            console.log(socialType);
            location.href = "/login/oauth2/authorization/" + socialType;
        });
    },
    resetForm : function() {
        $("#inputEmail").val('');
        $("#inputPassword").val('');
    },
    validateEmail : function() {
        const _this = this;
        const re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        let email = $("#inputEmail").val();
        if(email === "") {
            _this.emptyAction("#inputEmail", "#alertEmail");
            return;
        }
        if(re.test(email) || email === "admin") {
            _this.validAction("#inputEmail", "#alertEmail");
        } else {
            _this.invalidAction("#inputEmail", "#alertEmail", "유효한 이메일 주소를 입력해주세요.");
        }
    },
    validatePassword : function() {
        const _this = this;
        let password = $("#inputPassword").val();
        if(password === "") {
            _this.emptyAction("#inputPassword", "#alertPassword");
            return;
        }
        if(password.length >= 6) {
            _this.validAction("#inputPassword", "#alertPassword");
        } else {
            _this.invalidAction("#inputPassword", "#alertPassword", "비밀번호를 6자 이상 입력해주세요.");
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
        if($("#inputEmail").hasClass("is-valid") && $("#inputPassword").hasClass("is-valid")) {
            $("#loginBtn").attr("disabled", false);
            return;
        }
        if($("#inputEmail").hasClass("is-invalid") || $("#inputPassword").hasClass("is-invalid")) {
            $("#loginBtn").attr("disabled", true);
            return;
        }
    }
};

index.init();
index.resetForm();