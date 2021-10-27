function removeURLParameter(url, parameter) {
    var urlparts = url.split('?');
    if (urlparts.length >= 2) {

        var prefix = encodeURIComponent(parameter) + '=';
        var pars = urlparts[1].split(/[&;]/g);

        //reverse iteration as may be destructive
        for (var i = pars.length; i-- > 0;) {
            //idiom for string.startsWith
            if (pars[i].lastIndexOf(prefix, 0) !== -1) {
                pars.splice(i, 1);
            }
        }

        return urlparts[0] + (pars.length > 0 ? '?' + pars.join('&') : '');
    }
    return url;
}

jQuery(document).ready(function($) {
    $(".clickable-row").click(function() {
        window.location = $(this).attr("href");
    });
});

$(document).ready(function(){
    $('#sortingType').change(function(){
        window.location.href = removeURLParameter(window.location.href, "sortingType")+ '?sortingType=' + $(this).val();
    });
});

$(document).ready(function(){
    $('#aggregationType').change(function(){
        window.location.href = removeURLParameter(window.location.href, "aggregationType")+ '?aggregationType=' + $(this).val();
    });
});

