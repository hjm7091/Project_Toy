var index = {
    originEmail : $("#inputEmail").val(),
    init : function() {
        var _this = this;

        $("#inputEmail").on("change keyup paste", function() {
            _this.validateEmail("#inputEmail", "#alertEmail");
            _this.activateButton("#inputEmail", "#modifyEmailCompleteBtn");
        });

        let inputIds = ["#prevPassword", "#newPassword", "#newPasswordCheck"];
        let divIds = ["#alertPrevPassword", "#alertNewPassword", "#alertNewPasswordCheck"];
        $("#prevPassword").on("change keyup paste", function() {
            _this.validatePassword("#prevPassword", "#alertPrevPassword", "#modifyPasswordBtn");
            _this.activatePasswordButton(inputIds, divIds, "#modifyPasswordBtn");
        });
        $("#newPassword").on("change keyup paste", function() {
            _this.validatePassword("#newPassword", "#alertNewPassword", "#modifyPasswordBtn");
            _this.activatePasswordButton(inputIds, divIds, "#modifyPasswordBtn");
        });
        $("#newPasswordCheck").on("change keyup paste", function() {
            _this.validatePassword("#newPasswordCheck", "#alertNewPasswordCheck", "#modifyPasswordBtn");
            _this.activatePasswordButton(inputIds, divIds, "#modifyPasswordBtn");
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
            _this.modifyPassword($("#prevPassword").val(), $("#newPassword").val());
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
    validateEmail : function(inputId, divId) {
        var _this = this;
        const re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        var email = $(inputId).val();
        if(email == _this.originEmail) {
            _this.emptyAction(inputId, divId);
            return;
        }
        if(re.test(email)) {
            _this.checkDuplicate(email);
        } else {
            _this.invalidAction(inputId, divId, "유효한 이메일 주소를 입력해주세요.");
        }
    },
    validatePassword : function(inputId, divId, buttonId) {
        var _this = this;
        var password = $(inputId).val();
        if(password == "") {
            _this.emptyAction(inputId, divId);
            return;
        }
        if(password.length >= 6) {
            _this.validAction(inputId, divId);
        } else {
            _this.invalidAction(inputId, divId, "비밀번호를 6자 이상 입력해주세요.");
            $(buttonId).attr("disabled", true);
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
    activatePasswordButton : function(inputIds, divIds, buttonId) {
        let _this = this;

        let newPassword = $(inputIds[1]).val();
        let newPasswordCheck = $(inputIds[2]).val();

        if (newPassword.length >= 6 && newPasswordCheck.length >= 6) {
            if ( newPassword !== newPasswordCheck ) {
                _this.invalidAction(inputIds[1], divIds[1], "비밀번호가 일치하지 않습니다.");
                _this.invalidAction(inputIds[2], divIds[2], "비밀번호가 일치하지 않습니다.");
            } else {
                _this.validAction(inputIds[1], divIds[1]);
                _this.validAction(inputIds[2], divIds[2]);
            }
        }

        let flag = true;
        inputIds.forEach(inputId => {
            if(!$(inputId).hasClass("is-valid")) {
                flag = false;
            }
        });

        if (flag) {
            $(buttonId).attr("disabled", false);
        } else {
            $(buttonId).attr("disabled", true);
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
        let _this = this;

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

            var error = function(result) {
                var responseText = util.stringToObject(result.responseText);
                alert(responseText.message);
                location.reload();
            }

            account.modifyEmail(JSON.stringify(data), success, error);
        }
    },
    modifyPassword : function(before, after) {
        let _this = this;

        if (confirm("정말로 변경하시겠습니까?")) {
            let data = { "before" : before, "after" : after };

            /*
            * response 로 string 이 반환되면 성공임에도 불구하고 error 콜백이 호출됨. 이유를 모르겠음.
            * response 로 객체가 반환되면 정상적으로 동작함.
            */
            let success = function(result) {
                if (result.responseText !== null) {
                    alert("비밀번호 변경 성공");
                } else {
                    alert("비밀번호 변경 실패");
                }
                location.reload();
            }

            let error = function(result) {
                let responseText = util.stringToObject(result.responseText);
                alert(responseText.message);
                location.reload();
            }

            account.modifyPassword(JSON.stringify(data), success, error);
        }
    }
};

index.init();