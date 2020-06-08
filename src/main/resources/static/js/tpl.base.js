// JavaScript Document
$(function(){
		$(".discolor tr:odd").addClass("tr-odd-bg"); //先排除第一行,然后给奇数行添加样式
		$(".discolor tr:even").addClass("tr-even-bg"); //先排除第一行,然后给偶数行添加样式
	}) 
$(function(){
		$(".discolor-form tr:odd").addClass("tr-odd-bg"); //先排除第一行,然后给奇数行添加样式
		$(".discolor-form tr:even").addClass("tr-even-bg"); //先排除第一行,然后给偶数行添加样式
	})
	 

function slideUp(div){
$(div).slideUp(1000);//窗帘效果展开
}

function slideToggle(div1){
	   var icon=div1+"-icon";
	   var classhide="hide"+div1;
	   var classshow="show"+div1;
	   var divobj="#"+div1;
	   var iconobj="#"+icon;
      if($(divobj).css("display")=="block")
	 {
      $(iconobj).removeClass().addClass(classshow);
	 }else{
      $(iconobj).removeClass().addClass(classhide);

    }
  $(divobj).slideToggle(500);
  //窗帘效果的切换,点一下收,点一下开,参数可以无,参数说明同上 
}



function slideFlod(obj2){
	if($(obj2).css("display")=="block"){
	//	$("#obj1").attr("src","../themes_blue/images/icons/panel_display.png");		
		}
	else{
		$(obj2).css("display")=="none";
	//	$("#obj1").attr("src","../themes_blue/images/icons/panel_hide.png");
		}
		$(obj2).slideToggle(500);//窗帘效果的切换,点一下收,点一下开,参数可以无,参数说
	}

function slideFlodgroup(obj1,obj2){
	if($(obj2).css("display")=="block"){
	//	$("#obj1").attr("src","../themes_blue/images/icons/panel_display.png");		
		}
	else{
		$(obj2).css("display")=="none";
	//	$("#obj1").attr("src","../themes_blue/images/icons/panel_hide.png");
		}
		$(obj2).slideToggle(500);//窗帘效果的切换,点一下收,点一下开,参数可以无,参数说
	}

	
//定义右侧固定栏
function tools(){ 
var top=$(document).scrollTop(); 
if(($.browser.msie==true)&&($.browser.version==6.0)){ 
if(top>2)$("#subbox-main").css({position:"absolute",top:top-2}); 
}else{ 
if(top>2)$("#subbox-main").css({position:"fixed",top:"-"&top+"px"}); 
} 
if(top<=2)$("#subbox-main").css({position:"static",top:0}); 
} 
$(function(){ 
window.onscroll=tools; 
window.onresize=tools; 
}); 

//定义吸顶和吸底栏
$(document).ready(function(){

	$(window).scroll(function(){
		if($.browser.msie && $.browser.version=="6.0")$(".bottomshort").css("top",$(window).height()-$(".bottomshort").height()+$(document).scrollTop());
	});
	
});
  
//选卡
function showCard(cardid, infoid, clsName){
    var cardList = cardid.parentNode.getElementsByTagName("li");
    for (i = 0; i < cardList.length; i++) {
        if (cardid == cardList[i]) {
            cardList[i].className = clsName + "-on";
            document.getElementById(infoid + i).style.display = "block";
        }
        else {
            cardList[i].className = clsName + "-off";
            document.getElementById(infoid + i).style.display = "none";
        }
    }
}