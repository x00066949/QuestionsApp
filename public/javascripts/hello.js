$(document).ready(function() {
  $('[data-toggle=offcanvas]').click(function() {
    $('.row-offcanvas').toggleClass('active');
  });
});

function calcscore(){
    var score = 0;
    $(".calc:checked").each(function(){
        score+=parseInt($(this).val(),10);
    });
    $("input[type=radio]").val(score)
}
$(document).ready(function(){
    $(".calc").change(function(){
        calcscore()
    });
});