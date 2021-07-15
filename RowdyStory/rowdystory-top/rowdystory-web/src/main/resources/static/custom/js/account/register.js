var index = {
    init : function() {
        var _this = this;
        $("#inputEmail").on("change keyup paste", function() {
            _this.validateEmail();
            _this.activateButton();
        });
        $("#inputUserName").on("change keyup paste", function() {
            _this.validateName();
            _this.activateButton();
        });
        $("#inputPassword").on("change keyup paste", function() {
            _this.validatePassword();
            _this.activateButton();
        });
        $("#inputPasswordConfirm").on("change keyup paste", function() {
            _this.validatePasswordConfirm();
            _this.activateButton();
        });
        $("#registerBtn").on("click", function() {
//            _this.loadButton();
            _this.register();
        });
        $("#registerForm").bind("submit", function(e) {
            util.preventEvent(e);
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
            _this.checkDuplicate(email);
        } else {
            _this.invalidAction("#inputEmail", "#alertEmail", "유효한 이메일 주소를 입력해주세요.");
        }
    },
    validateName : function() {
        var _this = this;
        var name = $("#inputUserName").val();
        if(name == "") {
            _this.emptyAction("#inputUserName", "#alertName");
            return;
        }
        if(name.length < 3) {
            _this.invalidAction("#inputUserName", "#alertName", "닉네임을 3자 이상 입력해주세요.");
        } else {
            _this.validAction("#inputUserName", "#alertName");
        }
    },
    validatePassword : function() {
        var _this = this;
        var password = $("#inputPassword").val();
        if(password == "") {
            _this.emptyAction("#inputPassword", "#alertPassword");
            return;
        }
        if(password.length >= 6) {
            var passwordConfirm = $("#inputPasswordConfirm").val();
            if(passwordConfirm.length > 0) {
                if(_this.compare("#inputPassword", "#inputPasswordConfirm")) {
                    _this.validAction("#inputPassword", "#alertPassword");
                    _this.validAction("#inputPasswordConfirm", "#alertPasswordConfirm");
                } else {
                    _this.invalidAction("#inputPassword", "#alertPassword", "비밀번호가 일치하지 않습니다.");
                }
            } else {
                _this.validAction("#inputPassword", "#alertPassword");
            }
        } else {
            _this.invalidAction("#inputPassword", "#alertPassword", "비밀번호를 6자 이상 입력해주세요.");
        }
    },
    validatePasswordConfirm : function() {
        var _this = this;
        var passwordConfirm = $("#inputPasswordConfirm").val();
        if(passwordConfirm == "") {
            _this.emptyAction("#inputPasswordConfirm", "#alertPasswordConfirm");
            return;
        }
        if(passwordConfirm.length >= 6) {
            var password = $("#inputPassword").val();
            if(password.length > 0) {
                if(_this.compare("#inputPassword", "#inputPasswordConfirm")) {
                    _this.validAction("#inputPasswordConfirm", "#alertPasswordConfirm");
                    _this.validAction("#inputPassword", "#alertPassword");
                } else {
                    _this.invalidAction("#inputPasswordConfirm", "#alertPasswordConfirm" , "비밀번호가 일치하지 않습니다.");
                }
            } else {
                _this.validAction("#inputPasswordConfirm", "#alertPasswordConfirm");
            }
        } else {
            _this.invalidAction("#inputPasswordConfirm", "#alertPasswordConfirm" , "비밀번호를 6자 이상 입력해주세요.");
        }
    },
    compare : function(passwordId, passwordConfirmId) {
        var pw = $(passwordId).val();
        var pwc = $(passwordConfirmId).val();
        return (pw == pwc);
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
        if($("#inputEmail").hasClass("is-valid") && $("#inputUserName").hasClass("is-valid")
            && $("#inputPassword").hasClass("is-valid") && $("#inputPasswordConfirm").hasClass("is-valid")) {
            $("#registerBtn").attr("disabled", false);
            return;
        }
        if($("#inputEmail").hasClass("is-invalid") || $("#inputUserName").hasClass("is-invalid")
            || $("#inputPassword").hasClass("is-invalid") || $("#inputPasswordConfirm").hasClass("is-invalid")) {
            $("#registerBtn").attr("disabled", true);
            return;
        }
    },
    checkDuplicate : function(email) {
        var _this = this;

        var data = { email : email };

        var success = function(duplicate) {
            if(!duplicate) {
                _this.validAction("#inputEmail", "#alertEmail");
            } else {
                _this.invalidAction("#inputEmail", "#alertEmail", "중복된 이메일이 존재합니다.");
            }
            _this.activateButton();
        }

        account.getByEmail(data, success, util.error);
    },
    loadButton : function() {
        $("#registerBtn").html("<span class='spinner-border spinner-border-sm' role='status'></span> 인증 이메일을 보내는중...");
    },
    register : function() {
        var data = {
            "email" : $("#inputEmail").val(),
            "password" : $("#inputPassword").val(),
            "userName" : $("#inputUserName").val(),
        };

        var result = account.register(JSON.stringify(data), function(user) {
            if(user) {
                alert("회원 가입 성공");
                window.location = result.getResponseHeader("Location");
            } else {
                alert("회원 가입 실패");
                location.reload();
            }
        }, util.error);
    }
};

index.init();
