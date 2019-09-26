// Implements apply all for /settings
$(document).ready(function() {
    $('.apply-master').click(function() {
        let masterValue = $('.master-select').prop('selectedIndex');
        $('.child-select').prop('selectedIndex', masterValue);
    })
});