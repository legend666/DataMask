$(function () {
    maskingList();
});

$(function(){
    $(window).ready(function(){
        $("#maskingList").setGridWidth($(window).width()*0.25);
    });
});
function maskingList() {
    jQuery("#maskingList").jqGrid(
        {
            // url : ctx+'/JSONData',
            height: 780,
            hidegrid:false,
            datatype: "json",
            caption:"脱敏列管理",
            colNames: ['操作','脱敏列名','脱敏规则','所属数据表'],
            colModel: [
                //align: "right"定义数据相对单元格的对齐方式.string left
                //editable : true 定义字段是否可编辑 boolean false
                {name: 'act', index: 'act', width: 5, editable: true},
                {name: 'maskingColumn', index: 'maskingColumn', width: 10, editable: true},
                {name: 'maskingRules', index: 'maskingRules', width: 10, editable: true},
                {name: 'table', index: 'table', width: 10, editable: true},
            ],
            pager: false,
        });

}