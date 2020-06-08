// JavaScript Document
$(function(){
		$(".discolor tr:odd").addClass("tr-odd-bg"); //先排除第一行,然后给奇数行添加样式
		$(".discolor tr:even").addClass("tr-even-bg"); //先排除第一行,然后给偶数行添加样式
	})
	
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
//折叠表格
function slidefolde(obj,layer){
				if($(layer).css("display")=="block"){
	//	$("#obj1").attr("src","../themes_blue/images/icons/panel_display.png");		
		}
	else{
		$(layer).css("display")=="none";
	//	$("#obj1").attr("src","../themes_blue/images/icons/panel_hide.png");
		}
		$(layer).slideToggle(500);//窗帘效果的切换,点一下收,点一下开,参数可以无,参数说
			}
