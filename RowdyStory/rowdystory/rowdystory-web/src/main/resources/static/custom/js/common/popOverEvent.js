$('[data-toggle="popover"]').popover({ trigger: "manual" , html: true, animation:false})
    .on("mouseenter", function () {
        const _this = this;
        $(this).popover("show");
        $(".popover").on("mouseleave", function () {
            $(_this).popover('hide');
        });
    }).on("mouseleave", function () {
        const _this = this;
        setTimeout(function () {
            if (!$(".popover:hover").length) {
                $(_this).popover("hide");
            }
        }, 100);
});