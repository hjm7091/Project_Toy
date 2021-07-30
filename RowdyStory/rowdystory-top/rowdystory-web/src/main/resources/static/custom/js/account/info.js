var index = {
    originEmail : $("#inputEmail").val(),
    init : function() {
        var _this = this;
        $("#inputEmail").on("change keyup paste", function() {
            _this.validateEmail();
            _this.activateButton("#inputEmail", "#modifyEmailCompleteBtn");
        });
        $("#inputPassword").on("change keyup paste", function() {
            _this.validatePassword();
            _this.activateButton("#inputPassword", "#modifyPasswordBtn");
        });
        $("#modifyEmailBtn").on("click", function() {
            _this.toggleButton("#modifyEmailBtn");
            _this.toggleButton("#modifyEmailCancelBtn");
            _this.toggleButton("#modifyEmailCompleteBtn");
            _this.toggleInput("#inputEmail");
        });
        $("#modifyEmailCancelBtn").on("click", function() {
            _this.toggleButton("#modifyEmailBtn");
            _this.toggleButton("#modifyEmailCancelBtn");
            _this.toggleButton("#modifyEmailCompleteBtn");
            _this.toggleInput("#inputEmail");
        });
        $("#modifyEmailCompleteBtn").on("click", function() {
            _this.modifyEmail($("#inputEmail").val());
        });
        $("#modifyPasswordBtn").on("click", function() {
            _this.modifyPassword($("#inputPassword").val());
        });
        $("#modifyEmailForm").bind("submit", function(e) {
            util.preventEvent(e);
        });
        $("#modifyPasswordForm").bind("submit", function(e) {
            util.preventEvent(e);
        });
    },
    toggleButton : function(buttonId) {
        if( $(buttonId).hasClass("d-block") ) {
            $(buttonId).removeClass("d-block"); $(buttonId).addClass("d-none");
        } else {
            $(buttonId).removeClass("d-none"); $(buttonId).addClass("d-block");
        }
    },
    toggleInput : function(inputId) {
        var _this = this;
        if( $(inputId).attr("readonly") ) {
            $(inputId).removeAttr("readonly");
        } else {
            $(inputId).attr("readonly", true);
            $(inputId).val(_this.originEmail);
            $("#modifyEmailCompleteBtn").attr("disabled", true);
            _this.emptyAction("#inputEmail", "#alertEmail");
        }
    },
    validateEmail : function() {
        var _this = this;
        const re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        var email = $("#inputEmail").val();
        if(email == _this.originEmail) {
            _this.emptyAction("#inputEmail", "#alertEmail");
            return;
        }
        if(re.test(email)) {
            _this.checkDuplicate(email);
        } else {
            _this.invalidAction("#inputEmail", "#alertEmail", "유효한 이메일 주소를 입력해주세요.");
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
    activateButton : function(inputId, buttonId) {
        if($(inputId).hasClass("is-valid")) {
            $(buttonId).attr("disabled", false);
            return;
        }
        if($(inputId).hasClass("is-invalid")) {
            $(buttonId).attr("disabled", true);
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
            _this.activateButton("#inputEmail", "#modifyEmailCompleteBtn");
        }

        account.getByEmail(data, success, util.error);
    },
    modifyEmail : function(email) {
        var _this = this;

        if (confirm("정말로 변경하시겠습니까?")) {
            var data = { "email" : email };

            var success = function(userDTO) {
                if (userDTO.email != _this.originEmail) {
                    alert("이메일 변경 성공");
                } else {
                    alert("이메일 변경 실패");
                }
                location.reload();
            }

            account.modifyEmail(JSON.stringify(data), success, util.error);
        }
    },
    modifyPassword : function(password) {
        var _this = this;

        if (confirm("정말로 변경하시겠습니까?")) {
            var data = { "password" : password };

            var success = function(userDTO) {
                if (userDTO != null) {
                    alert("비밀번호 변경 성공");
                } else {
                    alert("비밀번호 변경 실패");
                }
                location.reload();
            }

            account.modify(JSON.stringify(data), success, util.error);
        }
    }
};

index.init();