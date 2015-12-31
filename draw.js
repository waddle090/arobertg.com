		(function( $ ){
		   $.fn.singularity = function() {
				var s = new Singularity(this);
				return s;			
		   }; 
		})( jQuery );

		(function( $ ){
		   $.fn.sing2 = function() {
				var s = new Singularity2(this);
				return s;			
		   }; 
		})( jQuery );
	//our singularity wiget
	var Singularity = function(element) {	

	    this.size = 365;//default size
		this.setSize = function(size){
			this.size = size;
		};
		this.getSize = function(){
			return this.size;
		};

		this.top = 10;
		this.setTop = function(top){
			this.top = top;
		};
		this.getTop = function(){
			return this.top;
		};

		this.left = 10;
		this.setLeft = function(left){
			this.left = left;
		};
		this.getLeft = function(){
			return this.left;
		};

		this.numberOfColors = 10;
		this.setNumberOfColors = function(numberOfColors){
			this.numberOfColors = numberOfColors;
		};

		this.borderWidth = 1;
		this.setBorderWidth = function(borderWidth){
			this.borderWidth = borderWidth;
		};

		// this.position = "relative";
		// this.setPosition = function(position){
		// 	this.position = position;
		// };

		this.redraw = function(){			
			for(var i = 1; i <= this.numberOfColors; ++i){
				updateColor("b" + i);			
			}
		};

	    this.create = function(size, top, left, numberOfColors, borderWidth){


			var phi = (1+ Math.sqrt(5))/2;

			size = size?size:this.size;	
			// size = a + (a/phi);
			// a = size - a/phi
			// a= phi * size / phi - a/phi

			// a= (pih* size - a)/phi

			// a * phi = phi*size - a

			// 2a * phi = phi*size

			// a = (phi * size)/2phi
			// a = size /phi;



			size = size/phi;
			top = top?top:this.top;
			left = left?left:this.left;
			numberOfColors = numberOfColors?numberOfColors:this.numberOfColors;
			borderWidth = borderWidth?borderWidth:this.borderWidth;
			// position = position?position:this.position;

			// if(position==="relative"){	    		
	  //   		top = $(element).offset().top;	
	  //   		left = $(element).offset().left;	
	  //   	}


			//create classes
			createBlockClass(borderWidth);
			for(var i = 1; i <= numberOfColors; ++i){
				createClass("b" + i);			
			}


			var a = size;
			var stopNumber = 1;
			// var phi = 1.61803398875;
			
			var lastBlock = false;
			var className = "";
			// var top = 10;
			// var left = 10;
			var dim = 99999;

			var x = 0;
			var block = 1;
			while(dim > stopNumber){
				
				
				var b = a/phi;
				var c = b/phi;
				var d = c/phi;
				var e = d/phi;
				
				
				
				// if(x%4==0){
				// 	dim = a;				
				// 	lastBlock = false;				
				// } else if(x%4==1){
				// 	dim = b;
				// //move to the right a and down by c
				// 	top += c;
				// 	left += a;				
				// } else if (x%4==2){
				// 	dim = c;
				// //move to the right d and up c
				// 	top -= c;
				// 	left += d;				
				// } else if(x%4==3) {
				// 	dim = d;
				// //move to the to left d
				// 	left -= d;			
				// 	lastBlock = true;
				// }
				if(x%4==0){
					dim = a;				
					lastBlock = false;				
				} else if(x%4==1){
					dim = b;
				//move to the right					
					left += a;				
				} else if (x%4==2){
					dim = c;
				//move to the right d and down b
					top += b;
					left += d;				
				} else if(x%4==3) {
					dim = d;
				//move to the to left d and down e
					left -= d;
					top += e;			
					lastBlock = true;
				}




				//move down by d
				$(element).append("<div class='block b"+block
					+"' style='z-index:-1;position:absolute;top:"+top+"px;left:"+left+"px;width:"+dim+"px;height:"+dim+"px;'/>");
					
				if(lastBlock){
					top -= e;
					a = e;
				}
					
				++x;	
				block=block===numberOfColors?1:++block;

			}
			
		}

		function createClass(className){		
			$(element).append($("<style />").html('.'+className+' { background-color: '+randomColor()+'; }'));
		}
		// function randomColor(){
		// 	return '#'+Math.floor(Math.random()*16777215).toString(16);
		// }

		function createBlockClass(borderWidth){
			$(element).append($("<style />").html('.block { border:solid '+borderWidth+'px black; }'));
		}
		function updateColor(className){
			$("."+className).css("background-color", randomColor());	
		}

    
	};


	var Singularity2 = function(element) {

		var currentType;
		var doOnce = true;
		var bt = -1;

		this.create = function(){
			createBlockClass(1);
			currentType = 1;
			draw(currentType);

		}
		this.btnClick = function(){
			// if(doOnce){
			// $(".block2").each(function(){
			// 	$(this).remove();
			// });
						
			currentType = currentType==4?0:currentType;
			draw(++currentType);		
			// doOnce = false;	
			// }
		}
		function draw(type){
			var startNumber = 200;
			var stopNumber = 1;
			var phi = 1.618;

			var bottomOrTop = "bottom";
			var bottom = 0;
			var left = 650;

			var a = startNumber;
			var b = a/phi;
			var perviousA;
			
			
			for(var x =0; b > stopNumber; ++x){
				var width = a + b;
				if(x%2==0){
					if(type==1) {
						$(element).append("<div class='block2' style='position:absolute;"+bottomOrTop+":"+bottom+"px;left:"+left+"px;width:"+width+"px;height:"+a+"px;'/>");
						bt = bt < 0 ? Number(a) : bt;
					}

					if(type==2) $(element).append("<div class='block2' style='position:absolute;"+bottomOrTop+":"+bottom+"px;left:"+(left-width)+"px;width:"+width+"px;height:"+a+"px;'/>");

					if(type==3) $(element).append("<div class='block2' style='position:absolute;bottom:"+(bottom + bt)+"px;left:"+(left - width)+"px;width:"+width+"px;height:"+a+"px;'/>");
					if(type==4) $(element).append("<div class='block2' style='position:absolute;bottom:"+(bottom + bt)+"px;left:"+(left)+"px;width:"+width+"px;height:"+a+"px;'/>");
				} else if(x%2==1){
					if(type==1) $(element).append("<div class='block2' style='position:absolute;"+bottomOrTop+":"+bottom+"px;left:"+left+"px;width:"+a+"px;height:"+width+"px;'/>");
					
					if(type==2) $(element).append("<div class='block2' style='position:absolute;"+bottomOrTop+":"+bottom+"px;left:"+(left-a)+"px;width:"+a+"px;height:"+width+"px;'/>");
					if(type==3) $(element).append("<div class='block2' style='position:absolute;bottom:"+(bottom + bt)+"px;left:"+(left - a)+"px;width:"+a+"px;height:"+width+"px;'/>");
					if(type==4) $(element).append("<div class='block2' style='position:absolute;bottom:"+(bottom + bt)+"px;left:"+(left)+"px;width:"+a+"px;height:"+width+"px;'/>");
				}
				
				a = b;
				b = a/phi;			
			}
		}
		function createBlockClass(borderWidth){
			$(element).append($("<style />").html('.block2 { border:solid '+borderWidth+'px black; }'));
		}
	};



	function loadCanvas(element, size, top,  left){
	    // get available screen size
	    var availWidth = $(window).width() - 20;
	    var availHeight = $(window).height() - 20;

	    // instantiate golden ratio constant
	    var PHI = (1+ Math.sqrt(5))/2;

	    // resize canvas based on golden ratio
	    if(availWidth/availHeight > PHI){
	        availWidth = availHeight * PHI;
	    } else if(availWidth/availHeight < PHI){
	        availHeight = availWidth/PHI;
	    }
	    availHeight = availHeight/2;
	    availWidth = availWidth/2;

	    availHeight = size;
	    availWidth = size + (size/PHI);

	    // add canvas HTML tag to body
	    var canvasTag = '<canvas id="canvas" width="'+availWidth+'" height="'+availHeight+'" style="z-index:99;position:absolute;top:'+top+'px;left:'+left+'px;"></canvas>';
	    element.append(canvasTag);

	    // prepare to draw
	    var canvas = $('canvas').get(0);
	    if(canvas.getContext){
	        var ctx = canvas.getContext('2d');
	        ctx.lineWidth = 5;	        
	    }

	    // draw first inner golden rectangle
	    var x1 = availWidth/PHI;
	    ctx.beginPath();
	    ctx.moveTo(x1, 0);
	    ctx.lineTo(x1, availHeight);
	    ctx.stroke();


	    // draw arc
	    ctx.beginPath();
	    ctx.moveTo(x1, availHeight);
	    ctx.arc(x1, availHeight, x1, Math.PI, (3/2*Math.PI));
	    ctx.stroke();

	    // draw second inner rectangle
	    var y1 = availHeight/PHI;
	    ctx.beginPath();
	    ctx.moveTo(x1, y1);
	    ctx.lineTo(availWidth, y1);
	    ctx.stroke();

	    // draw arc
	    ctx.beginPath();
	    ctx.moveTo(x1, y1);
	    ctx.arc(x1, y1, y1, (3/2*Math.PI), 0);
	    ctx.stroke();

	    // draw 3rd
	    var x2 = availWidth - (availWidth-x1)/PHI;
	    ctx.beginPath();
	    ctx.moveTo(x2, y1);
	    ctx.lineTo(x2, availHeight);
	    ctx.stroke();

	    // draw arc
	    ctx.beginPath();
	    ctx.moveTo(availWidth, y1);
	    ctx.arc(x2, y1, (availHeight-y1), 0, (1/2*Math.PI));
	    ctx.stroke();

	    // draw 4th
	    var y2 = availHeight - (availHeight-y1)/PHI;
	    ctx.beginPath();
	    ctx.moveTo(x1, y2);
	    ctx.lineTo(x2, y2);
	    ctx.stroke();

	    // draw arc
	    ctx.beginPath();
	    ctx.moveTo(x2, y2);
	    ctx.arc(x2, y2, (availHeight-y2), (1/2*Math.PI), Math.PI);
	    ctx.stroke();

	    // draw 5th
	    var x3 = x1 + (x2-x1)/PHI;
	    ctx.beginPath();
	    ctx.moveTo(x3, y1);
	    ctx.lineTo(x3, y2);
	    ctx.stroke();

	    // draw arc
	    ctx.beginPath();
	    ctx.moveTo(x3, y2);
	    ctx.arc(x3, y2, (x3-x1), Math.PI, (3/2*Math.PI));
	    ctx.stroke();

	    // draw 6th
	    var y3 = y1 + (y2-y1)/PHI;
	    ctx.beginPath();
	    ctx.moveTo(x3, y3);
	    ctx.lineTo(x2, y3);
	    ctx.stroke();

	    // draw arc
	    ctx.beginPath();
	    ctx.moveTo(x3, y3);
	    ctx.arc(x3, y3, (x2-x3), (3/2*Math.PI), 0);
	    ctx.stroke();

	    // draw 7th
	    var x4 = x2 - (x2-x3)/PHI;
	    ctx.beginPath();
	    ctx.moveTo(x4, y3);
	    ctx.lineTo(x4, y2);
	    ctx.stroke();

	    // draw arc
	    ctx.beginPath();
	    ctx.moveTo(x4, y3);
	    ctx.arc(x4, y3, (x2-x4), 0, (1/2*Math.PI));
	    ctx.stroke();

	    // draw 8th
	    var y4 = y2 - (y2-y3)/PHI;
	    ctx.beginPath();
	    ctx.moveTo(x3, y4);
	    ctx.lineTo(x4, y4);
	    ctx.stroke();

	    // draw arc
	    ctx.beginPath();
	    ctx.moveTo(x4, y4);
	    ctx.arc(x4, y4, (x4-x3), (1/2*Math.PI), Math.PI);
	    ctx.stroke();

	    // draw 9th
	    var x5 = x3 + (x4-x3)/PHI;
	    ctx.beginPath();
	    ctx.moveTo(x5, y3);
	    ctx.lineTo(x5, y4);
	    ctx.stroke();

	    // draw arc
	    ctx.beginPath();
	    ctx.moveTo(x5, y4);
	    ctx.arc(x5, y4, (x5-x3), Math.PI, (3/2*Math.PI));
	    ctx.stroke();

	    // draw 10th
	    var y5 = y3 + (y4-y3)/PHI;
	    ctx.beginPath();
	    ctx.moveTo(x4, y5);
	    ctx.lineTo(x5, y5);
	    ctx.stroke();

	    // draw arc
	    ctx.beginPath();
	    ctx.moveTo(x5, y5);
	    ctx.arc(x5, y5, (x4-x5), (3/2*Math.PI), 0);
	    ctx.stroke();

	    // draw 11th
	    var x6 = x4 - (x4-x5)/PHI;
	    ctx.beginPath();
	    ctx.moveTo(x6, y4);
	    ctx.lineTo(x6, y5);
	    ctx.stroke();

	    // draw arc
	    ctx.beginPath();
	    ctx.moveTo(x6, y5);
	    ctx.arc(x6, y5, (x4-x6), 0, (1/2*Math.PI));
	    ctx.stroke();

	    // draw 12th
	    var y6 = y4 - (y4-y5)/PHI;
	    ctx.beginPath();
	    ctx.moveTo(x5, y6);
	    ctx.lineTo(x6, y6);
	    ctx.stroke();

	    // draw arc
	    ctx.beginPath();
	    ctx.moveTo(x6, y6);
	    ctx.arc(x6, y6, (x6-x5), (1/2*Math.PI), Math.PI);
	    ctx.stroke();	  

	}




