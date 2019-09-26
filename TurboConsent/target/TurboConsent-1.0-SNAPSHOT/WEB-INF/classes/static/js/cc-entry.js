$(document).ready(function() {
    $('.add-cc-entry').click(function() {
        $('.cc-entry:lt(2)').clone().find('input, textarea').val('').end().appendTo('.add-exp');
    });
});