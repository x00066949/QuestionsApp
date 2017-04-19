$(document).ready(function() {
  $('[data-toggle=offcanvas]').click(function() {
    $('.row-offcanvas').toggleClass('active');
  });
});

var idx = document.getElementByName('')
var totalsA = new Array();
totalsA['sec1'] = 0;
 
function add(sec,val) {
     totalsA[sec] += new Number(val);
    alert(totalsA[sec]);
}

document.getElementById('total'+idx).value = total;