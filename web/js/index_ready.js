$(document).ready(function () {
    $('#imageSvg').click(imageClicked);
    drawDots();
    $('[name="r"]').each(function (a) {
        $(this)[0].oninvalid = function (e) {
            e.target.setCustomValidity("");
            if (!e.target.validity.valid) {
                e.target.setCustomValidity('Нужен радиус!');
            }
        };
    });
});