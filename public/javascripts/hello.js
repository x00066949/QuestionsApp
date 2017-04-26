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

function sum(){
	var rate = document.querySelector('input[type = "radio"]:checked').value;
}
function getCheckedValue( ratingRadio ) {
    var radios = document.getElementsByName( ratingRadio );
    for( i = 0; i < radios.length; i++ ) {
        if( radios[i].checked ) {
			document.getElementById('sum').innerHTML=radios[i].value;
            return radios[i].value;
        }
    }
    return null;
}