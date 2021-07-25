var index = {
    originEmail : $("#inputEmail").val(),
    init : function() {
        var _this = this;
        $("#inputEmail").on("change keyup paste", function() {
            _this.validateEmail();
            _this.activateButton("#inputEmail", "#modifyEmailCompleteBtn");
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
        $("#modifyEmailForm").bind("submit", function(e) {
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
                    location.reload();
                } else {
                    alert("이메일 변경 실패");
                }
            }

            account.modifyEmail(JSON.stringify(data), success, util.error);
        }
    }
};

index.init();