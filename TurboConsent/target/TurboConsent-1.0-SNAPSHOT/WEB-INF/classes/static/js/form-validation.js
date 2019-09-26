// prevents illegal submit
$(document).ready(function() {
    $('.bulk-submit').click(function(e) {
        let checked1 = $('input[type=checkbox]:visible:checked').not('#t-switch').length || $('.is-checkradio:checked').length;
        let checked2 = $('input[type=radio]:visible:checked').length;

        if(!checked1 || !checked2) {
            alert("You must select at least one experiment and exactly one consent option.");
            e.preventDefault();
        }
    });

    $('.visitor-submit').click(function(e) {
        let checked1 = $('input[type=checkbox]:visible:checked').length || $('.is-checkradio:checked').length;
        let checked2 = $('input[type=radio]:visible:checked').length;

        if(!checked1 || !checked2) {
            alert("You must select at least one visitor and exactly one consent option.");
            e.preventDefault();
        }
    });

    $('.single-submit').click(function(e) {
        let checked = $('input[type=radio]:checked').length;

        if(!checked) {
            alert("You must select a consent option to submit.");
            e.preventDefault();
        }
    });
});
