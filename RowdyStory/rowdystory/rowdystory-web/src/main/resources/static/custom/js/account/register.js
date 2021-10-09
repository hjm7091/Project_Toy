const index = {
    init : function() {
        const _this = this;
        $("#inputEmail").on("change keyup paste", function() {
            _this.validateEmail();
            _this.activateButton();
        });

        $("#inputUserName").on("change keyup paste", function() {
            _this.validateName();
            _this.activateButton();
        });

        let inputIds = ["#inputPassword", "#inputPasswordConfirm"];
        let divIds = ["#alertPassword", "#alertPasswordConfirm"];
        $("#inputPassword").on("change keyup paste", function() {
            _this.validatePassword(inputIds[0], divIds[0]);
            _this.comparePassword(inputIds, divIds);
            _this.activateButton();
        });
        $("#inputPasswordConfirm").on("change keyup paste", function() {
            _this.validatePassword(inputIds[1], divIds[1]);
            _this.comparePassword(inputIds, divIds);
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
        const _this = this;
        const re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        let email = $("#inputEmail").val();
        if(email === "") {
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
        const _this = this;
        let name = $("#inputUserName").val();
        if(name === "") {
            _this.emptyAction("#inputUserName", "#alertName");
            return;
        }
        if(name.length < 3) {
            _this.invalidAction("#inputUserName", "#alertName", "닉네임을 3자 이상 입력해주세요.");
        } else {
            _this.validAction("#inputUserName", "#alertName");
        }
    },
    validatePassword : function(inputId, divId) {
        const _this = this;
        let password = $(inputId).val();

        if(password === "") {
            _this.emptyAction(inputId, divId);
            return;
        }

        if(password.length >= 6) {
             _this.validAction(inputId, divId);
        } else {
            _this.invalidAction(inputId, divId, "비밀번호를 6자 이상 입력해주세요.");
        }
    },
    comparePassword : function(inputIds, divIds) {
        const _this = this;
        let password = $(inputIds[0]).val();
        let passwordConfirm = $(inputIds[1]).val();

        if (password.length < 6 || passwordConfirm.length < 6) {
            return;
        }

        if(_this.isSame(inputIds[0], inputIds[1])) {
            _this.validAction(inputIds[0], divIds[0]);
            _this.validAction(inputIds[1], divIds[1]);
        } else {
            _this.invalidAction(inputIds[0], divIds[0], "비밀번호가 일치하지 않습니다.");
            _this.invalidAction(inputIds[1], divIds[1], "비밀번호가 일치하지 않습니다.");
        }
    },
    isSame : function(passwordId, passwordConfirmId) {
        let pw = $(passwordId).val();
        let pwc = $(passwordConfirmId).val();
        return pw === pwc;
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
        const _this = this;

        const data = { "email" : email };

        const success = function(duplicate) {
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
        const data = {
            "email" : $("#inputEmail").val(),
            "password" : $("#inputPassword").val(),
            "userName" : $("#inputUserName").val(),
        };

        const result = account.register(JSON.stringify(data), function(user) {
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
