$('.btn_social').click(function () {
    var socialType = $(this).data('social');
    console.log(socialType);
    location.href="/login/oauth2/authorization/"+socialType;
});