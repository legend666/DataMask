$(function () {
    restoreData();
});
const colname = ['列名'];
const colmodel = [
    //align: "right"定义数据相对单元格的对齐方式.string left
    //editable : true 定义字段是否可编辑 boolean false
    {name: 'column'},
];

function restoreData() {
    jQuery("#restoreData").jqGrid(
        {
            autowidth: true,
            height: 700,
            caption: "表数据预览",
            colNames: colname,
            colModel: colmodel,
            pager: false,
            hidegrid: false,
        });
}