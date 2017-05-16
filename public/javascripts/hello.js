$(document).ready(function() {
  $('[data-toggle=offcanvas]').click(function() {
    $('.row-offcanvas').toggleClass('active');
  });
});

/* https://www.w3schools.com/howto/howto_css_modals.asp */

$("#myModal").css("z-index", "15000");

// Get the modal
var modals = document.getElementsByClassName('myModal');

// Get the <span> element that closes the modal
var span = document.getElementsByClassName("close");

// When the user clicks on the button, open the modal
function displayModal(i){

    modals[i-1].style.display = "block";
	
	// When the user clicks on <span> (x), close the modal
	span[i-1].onclick = function() {

		modals[i-1].style.display = "none";

	}
}
function displayLast(){
	
    modals[modals.length -1].style.display = "block";

	// When the user clicks on <span> (x), close the modal
	span[modals.length-1].onclick = function() {

		modals[modals.length-1].style.display = "none";

	}
}
$('#myModal').appendTo("body")


// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
	
	var i;
	for (i = 0; i < modals.length; i++) {
	    if (event.target == modals[i]) {

			modals[i].style.display = "none";
		}
	} 
    
}