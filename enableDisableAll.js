
//disable all input and button, mark with was disabled if already disabled
function disableAll() {
    $("body").css("cursor", "progress");
    $("input, button").each(function () {
        if ($(this).prop('disabled'))
            if (!$(this).hasClass("wasDisabled"))
                $(this).addClass("wasDisabled");
        $(this).prop("disabled", true);
    });
}
//enable all input and button called when finished with disableAll(), disable the elements who were originally disabled
function enableAll() {
    $("body").css("cursor", "default");
    $("input, button").each(function () {
        if ($(this).hasClass("wasDisabled")) {
            $(this).removeClass("wasDisabled");
            $(this).prop("disabled", true);
        } else {
            $(this).prop("disabled", false);
        }
    });
}
