"use strict";

$(document).ready(function(){
	
/* VEGAS Home Slider */
	function loadVegas(){
		$.vegas('slideshow', {
			  backgrounds:[
				{ src:'img/slider/01_b.jpg', fade:1000 },
				{ src:'img/slider/02_b.jpg', fade:1000 }
			  ]
			})('overlay', {
			  src:'css/vegas/overlays/05.png'
			});
			$( "#vegas-next" ).click(function() {
			  $.vegas('next');
			});
			$( "#vegas-prev" ).click(function() {
			  $.vegas('previous');
		});
	}
	
	loadVegas();
});