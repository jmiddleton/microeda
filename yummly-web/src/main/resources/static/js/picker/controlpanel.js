$patterns = "";
for(var i=1; i<= 15; i++){
	$img = 	"http://www.jellythemes.com/themes/zenitewp/wp-content/themes/Zenite/js/picker/style-picker/pattern"+i+".jpg";	
	$patterns += '<li>';
	$patterns += '<a id="pattern'+i+'"  href="" title="">';
	$patterns += '<img src="'+ $img +'" alt="pattern'+i+'" title="pattern'+i+'"/>'
	$patterns += '</a>';
	$patterns += '</li>'; 
}

$color = ["blue","gold","green","grey","olivegreen","orange","purple","red","turquoiseblue","violet"];
$colors = "";
for(var i=0; i<$color.length; i++){
	$img = 	"http://www.jellythemes.com/themes/zenitewp/wp-content/themes/Zenite/js/picker/style-picker/"+$color[i]+".jpg";	
	$colors += '<li>';
	$colors += '<a id="'+$color[i]+'" href="" title="">';
	$colors += '<img src="'+ $img +'" alt="color-'+$color[i]+'" title="color-'+$color[i]+'"/>'
	$colors += '</a>';
	$colors += '</li>'; 
}


$str = '<!-- **Ultimate Style Picker Wrapper** -->';
$str += '<div class="ultimate-style-picker-wrapper">';
$str += '	<a href="" title="" class="style-picker-ico"> <img src="http://www.jellythemes.com/themes/zenitewp/wp-content/themes/Zenite/js/picker/style-picker/picker-icon.png" alt="" title="" /> </a>';
$str += '	<div id="ultimate-style-picker">';
$str += '   	<h2> Select Your Style </h2>';
$str += '       <h3> Choose your layout </h3>';
$str += '		<ul class="layout-picker">';
$str += '       	<li> <a id="fullwidth" href="" title="" class="selected"> <img src="http://www.jellythemes.com/themes/zenitewp/wp-content/themes/Zenite/js/picker/style-picker/fullwidth.jpg" alt="" title="" /> </a> </li>';
$str += '       	<li> <a id="boxed" href="" title=""> <img src="http://www.jellythemes.com/themes/zenitewp/wp-content/themes/Zenite/js/picker/style-picker/boxed.jpg" alt="" title="" /> </a> </li>';
$str += '		</ul>';
$str += '		<div class="hr"> </div>';
$str += '		<div id="pattern-holder" style="display:none;">';
$str +='			<h3> Patterns for Boxed Layout </h3>';
$str += '			<ul class="pattern-picker">';
$str += 				$patterns;
$str += '			</ul>';
$str += '			<div class="hr"> </div>';
$str += '		</div>';
$str += '		<h3> Color scheme </h3>';
$str += '		<ul class="color-picker">';
$str += 		$colors;
$str += '		</ul>';
$str += '	</div>';
$str += '</div><!-- **Ultimate Style Picker Wrapper - End** -->';
jQuery(document).ready(function(){
	jQuery("body > div#wrapper").before($str);
	$picker_container = jQuery("div.ultimate-style-picker-wrapper");
	
	//Check Cookies in diffent pages and do the following things //css line 32 and 71
	if($.cookie("mytheme_skin")!= null){
		$href = jQuery("link[id='skin-css']").attr("href");
		$href = $href.substr(0,$href.lastIndexOf("/"))+"/"+$.cookie("mytheme_skin")+".css";
		jQuery("link[id='skin-css']").attr("href",$href);
		jQuery("ul.color-picker a[id='"+$.cookie("mytheme_skin")+"']").addClass("selected");
	}else{
		jQuery("ul.color-picker a:first").addClass("selected");
	}


	if ( $.cookie('control-open') == 1 ) { 
		$picker_container.animate( { left: -230 } );
		jQuery('a.style-picker-ico').addClass('control-open');
	}


	//1. Apply Layout
	if($.cookie("mytheme_layout") == "boxed"){
		jQuery("ul.layout-picker li a").removeAttr("class");
		jQuery("link[id='responsive-css']").after('<link id="boxed-css" href="boxed.css" rel="stylesheet" type="text/css" media="all" />');
		jQuery("ul.layout-picker li a[id='"+$.cookie("mytheme_layout")+"']").addClass("selected");
		jQuery("div#pattern-holder").removeAttr("style");

		$i = ($.cookie("mytheme_pattern")) ? $.cookie("mytheme_pattern")  : 'pattern1';
		$img = 	"css/img/patterns/"+$i+".jpg";
		jQuery('body').css('background-image', 'url('+$img+')');
		jQuery("ul.pattern-picker a[id="+$.cookie("mytheme_pattern")+"]").addClass('selected');
	}	
	
	//End
	
	
	
	
	//Picker On/Off
	jQuery("a.style-picker-ico").click(function(e){
		$this = jQuery(this);	
		if($this.hasClass('control-open')){
			$picker_container.animate({left: 0},function(){$this.removeClass('control-open');});
			$.cookie('control-open', 0);	
		}else{
			$picker_container.animate({left: -230},function(){$this.addClass('control-open');});
			$.cookie('control-open', 1);
		}
		e.preventDefault();
	});//Picker On/Off end
	

	//Color Picker
	jQuery("ul.color-picker a").click(function(e){
/*		$this = jQuery(this);
		jQuery("ul.color-picker a").removeAttr("class");
		$this.addClass("selected");
		$.cookie("mytheme_skin", $this.attr("id"), { path: '/' });
		$href = jQuery("link[id='skin-css']").attr("href");
		//$href = $href.substr(0,$href.lastIndexOf("/"));
		//$href = $href+"/"+$.cookie("mytheme_skin")+".css";
		$href = $href.substr(0,$href.lastIndexOf("/"))+"/"+$this.attr("id")+".css";
		jQuery("link[id='skin-css']").attr("href",$href);*/
		e.preventDefault();
	});
	//Color Picker End
	
	//Layout Picker
	jQuery("ul.layout-picker a").click(function(e){
		$this = jQuery(this);
		jQuery("ul.layout-picker a").removeAttr("class");
		$this.addClass("selected");
		$.cookie("mytheme_layout", $this.attr("id"), { path: '/' });

		if($.cookie("mytheme_layout") == "boxed") {
			jQuery("div#pattern-holder").slideDown();
			jQuery("link[id='responsive-css']").after('<link id="boxed-css" href="boxed.css" rel="stylesheet" type="text/css" media="all" />');
			
			if( $.cookie("mytheme_pattern") == null ){
				jQuery("ul.pattern-picker a:first").addClass('selected');
				$.cookie("mytheme_pattern","pattern1",{ path: '/' });
			}else{
				jQuery("ul.pattern-picker a[id="+$.cookie("mytheme_pattern")+"]").addClass('selected');
				$img = 	"css/img/patterns/"+$.cookie("mytheme_pattern")+".jpg";
				jQuery('body').css('background-image', 'url('+$img+')');
			}

		}else {
			jQuery("div#pattern-holder").slideUp();
			jQuery('body').removeAttr("style");
			if(jQuery("link[id='boxed-css']").length)  jQuery("link[id='boxed-css']").remove();
			jQuery("ul.pattern-picker a").removeAttr("class");
		}
		e.preventDefault();
	});//Layout Picker End
	
	//Pattern Picker
	jQuery("ul.pattern-picker a").click(function(e){
		if($.cookie("mytheme_layout") == "boxed"){
			$this = jQuery(this);
			jQuery("ul.pattern-picker a").removeAttr("class");
			$this.addClass("selected");
			$.cookie("mytheme_pattern", $this.attr("id"), { path: '/' });
			$img = 	"css/img/patterns/"+$.cookie("mytheme_pattern")+".jpg";
			jQuery('body').css('background-image', 'url('+$img+')');
		}
		e.preventDefault();
	});//Pattern Picker End
	
});