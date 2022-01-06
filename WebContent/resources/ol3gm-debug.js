/**
 * @author : Sunil Kumar
 * Date : 10.12.2018
 */


var defaults =
{
		"z-index": 9999,
		"width": "100%",
		"height": "100%",
		"position": "fixed",
		"top": 0,
		"left": 0,
		"right": 0,
		"bottom": 0,
		"background": "white",
		"max-width": "100%"
}

var callbackCode = null;

var selector = null;

(function($){

	$.fn.fullscreen = function(options,callback) {
		if( options != undefined && options != null && options != "")
		{
			
			if(options.hasOwnProperty("width"))
				defaults.width = options.width;
			
			if(options.hasOwnProperty("height"))
				defaults.height = options.height;
			
			if(options.hasOwnProperty("top"))
				defaults.top = options.top;
			
			if(options.hasOwnProperty("left"))
				defaults.left = options.left;
			
			if(options.hasOwnProperty("right"))
				defaults.right = options.right;
			
			if(options.hasOwnProperty("bottom"))
				defaults.bottom = options.bottom;
			
			if(options.hasOwnProperty("background"))
				defaults.background = options.background;
			
		}
		selector = $(this);
		//console.log(defaults);
		register();
	};
}(jQuery));


var register = function()
{
	if(selector != null)
	{
		$(selector).click(function()
		{
			var elem = $(this).parent()
			
			if($(elem).parent().hasClass("full_screen_class"))		
			{
				$(elem).parent().removeClass("full_screen_class");
				$(elem).parent().removeAttr("style");
				
			}
			else
			{
				$(elem).parent().addClass("full_screen_class");
				$(elem).parent().css(defaults);
				//setTimeout(function(){callbackCode.call(this);},500);
			}
		});
	}
}

/*$(document).ready(function()
{
	register();
});*/

