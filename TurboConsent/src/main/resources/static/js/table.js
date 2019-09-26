// changing tables for experiments
window.onpageshow = function(event) {
    $('#t-switch').is(':checked') ? showR() : showCP();
};

const showCP = function() {
    $('.consent-pending').removeClass('is-hidden');
    $('.reviewed').addClass('is-hidden');
};

const showR = function() {
    $('.consent-pending').addClass('is-hidden');
    $('.reviewed').removeClass('is-hidden');
};

const updateLabel = function() {
    let tName = $('#t-switch').is(':checked') ? 'Reviewed Experiments' : 'Unreviewed Experiments';
    $('.current-table').text(tName);
};

const updatePNo = function() {
    let numSelected = $('input[class=pending]:visible:checked').length || $('.is-checkradio:checked').length;
    $('#p-selected').text(numSelected);
};

const updateRNo = function() {
    let numSelected = $('input[class=rev]:visible:checked').length || $('.is-checkradio:checked').length;
    $('#r-selected').text(numSelected);
};

const updateVNo = function() {
    let numSelected = $('input[class=visitor]:visible:checked').length || $('.is-checkradio:checked').length;
    $('#v-selected').text(numSelected);
};

$(document).ready(function() {
    updateLabel();
    updatePNo();
    updateRNo();
    updateVNo();

    $('#t-switch').change(function(){
        $(this).is(':checked') ? showR() : showCP();
        updateLabel();
    });

    $('.toggle-p').click(function() {
        if ($(this).attr('disabled')) return;

        let allPending = $('.pending');

        if ($(this).text() === 'Select All') allPending.prop('checked', true);

        if ($(this).text() === 'Unselect All') allPending.prop('checked', false);

        if ($(this).text() === 'Select All') $(this).text('Unselect All');

        else $(this).text('Select All');

        updatePNo();
    });

    $('.toggle-r').click(function() {
        if ($(this).attr('disabled')) return;

        let allRev = $('.rev');

        if ($(this).text() === 'Select All') allRev.prop('checked', true);

        if ($(this).text() === 'Unselect All') allRev.prop('checked', false);

        if ($(this).text() === 'Select All') $(this).text('Unselect All');

        else $(this).text('Select All');

        updateRNo();
    });

    $('.toggle-v').click(function() {
        if ($(this).attr('disabled')) return;

        let allVisitor = $('.visitor');

        if ($(this).text() === 'Select All') allVisitor.prop('checked', true);

        if ($(this).text() === 'Unselect All') allVisitor.prop('checked', false);

        if ($(this).text() === 'Select All') $(this).text('Unselect All');

        else $(this).text('Select All');

        updateVNo();
    });

    $('.pending').change(function() {
        updatePNo();
    });

    $('.rev').change(function() {
        updateRNo();
    });

    $('.visitor').change(function() {
        updateVNo();
    });
});
