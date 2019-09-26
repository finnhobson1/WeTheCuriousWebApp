// The following code is based off a toggle menu by @Bradcomp
// source: https://gist.github.com/Bradcomp/a9ef2ef322a8e8017443b626208999c1
$(document).ready(function() {
    $('.navbar-burger').click(function() {
        $(this).toggleClass('is-active');
        $('.navbar-menu').toggleClass('is-active');
    });

    $('.close-modal').click(function() {
        let modal = $('.modal');
        modal.removeClass('is-active');
    });

    $('.open-modal').click(function() {
        let modal = $('.modal');
        modal.addClass('is-active');
    });

    $('.back').click(function() {
        window.history.back()
    });

    $('.close-success').click(function() {
        $('.success-flag').addClass('is-hidden');
    });

    $('.close-error').click(function() {
        $('.error-flag').addClass('is-hidden');
    });

    $('.delete-entry').click(function(e) {
        e.preventDefault();
        let choice = confirm($(this).attr('data-confirm'));
        let section = $(this).attr('data-type');
        let url = '/admin/' + section + '/delete';

        if (choice) $.post(url, { deleteId: $(this).attr('id') }, function() {
            location.href='/admin/' + section + '?updateSuccess=true';
        });
    });
});
