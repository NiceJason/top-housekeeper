function centerModals(modalId) {
    $(modalId).each(function() {
        var $clone = $(this).clone().css('display','block').appendTo('body');
        var top = Math.round(($clone.height() - $clone.find('.modal-content').height()) / 2);
        top = top > 0 ? top : 0;
        $clone.remove();
        $(this).find('.modal-content').css("margin-top", top);
    });
};


console.debug("执行了@@#￥@#");
function adjustBody_beforeShow (){
    $('#navbar').css({
        'position' : 'static'});
}

function adjustBody_afterShow (){

        $('#navbar').css({
            'position' : 'fixed'});
}

