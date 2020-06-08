$(function () {
    tableData();
});

$(function(){
    $(window).ready(function(){
        $("#tableData").setGridWidth($(window).width()*0.5);
    });
});
const colname = ['列名'];
const colmodel =  [
    //align: "right"定义数据相对单元格的对齐方式.string left
    //editable : true 定义字段是否可编辑 boolean false
    {name: 'column'},
];

function tableData() {
    jQuery("#tableData").jqGrid(
        {
            height: 300,
            caption:"表数据预览",
            colNames: colname,
            colModel:colmodel,
            pager: false,
            hidegrid:false,
        });

}