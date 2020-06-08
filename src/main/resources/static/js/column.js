$(function () {
    column();
});

$(function () {
    $(window).ready(function () {
        $("#column").setGridWidth($(window).width() * 0.5);
    });
});

function column() {
    $("#column").jqGrid({       //Grid设置
        datatype: 'local',
        loadonce: true,
        height: 428,
        autowidth: true,
        hidegrid: false,
        caption: "数据列名",
        colNames: ['序号', '数据列名', '类型', '是否脱敏', '脱敏规则'],
        colModel: [
            //align: "right"定义数据相对单元格的对齐方式.string left
            //editable : true 定义字段是否可编辑 boolean false
            {name: 'id', index: 'id', width: 8, editable: true},
            {name: 'column', index: 'column', width: 10, editable: true},
            {name: 'type', index: 'type', width: 10, editable: true},
            {name: 'masking', index: 'masking', width: 10, editable: true},
            {name: 'maskingRules', index: 'maskingRules', width: 10, editable: true},

        ],
        celledit: true,
        viewrecords: true,//是否在浏览导航栏显示记录总数
        rowNum: 10000,//每页显示记录数
        sortname: 'UserName',//默认的排序列名
        pgbuttons: false,
        pgtext: false,
        loadComplete: function () {
            const ids = $("#column").jqGrid('getDataIDs');
            // 脱敏规则有限，而且脱敏取值直接是str（金额脱敏规则）所以直接写死在页面，就不从数据库取了
            for (let i = 0; i < ids.length; i++) {
                const cid = ids[i];
                eb = "<select  id=\"maskingRule" + cid + "\"  onchange=\"masking('" + cid + "')\">" +
                    "<option value=\"\" selected=\"selected\">请选择脱敏规则</option>" +
                    "<option value=\"1\">身份证号脱敏规则</option>" +
                    "<option value=\"2\">电话号码脱敏规则</option>" +
                    "<option value=\"2\">姓名脱敏规则</option>" +
                    "<option value=\"2\">金额脱敏规则</option>" +
                    "<option value=\"3\">纯文本脱敏规则</option></select>";
                $("#column").jqGrid('setRowData', cid, {maskingRules: eb});
            }
        }
    });
}

function masking(cid) {
    const id = "maskingRule" + cid.toString();
    const obj = document.getElementById(id); //定位id
    const index = obj.selectedIndex; // 选中索引
    const maskingRule = obj.options[index].text; // 选中文本
    // var value = obj.options[index].value; // 选中值
    const maskingColumn = jQuery("#column").jqGrid("getRowData", cid);
    // 吧选择了的变颜色
    $("#column").find('#' + cid).find("td").css("background-color", "royalblue");
    const tableRowId = $("#tables").jqGrid("getGridParam", "selrow");
    const tableRowData = jQuery("#tables").jqGrid("getRowData", tableRowId);
    const maskingListData = {
        maskingColumn: maskingColumn.column,
        maskingRules: maskingRule,
        table: tableRowData.tableName
    };
    // alert(typeof (maskingListData));
    // const maskingdata = JSON.parse(maskingListData);
    // const o = jQuery("#maskingList");
    const count = $("#maskingList").getGridParam("reccount");
    $("#maskingList").jqGrid('addRowData', count + 1, maskingListData, 'first');
    const ids = $("#maskingList").jqGrid('getDataIDs');
    for (let i = 0; i < ids.length; i++) {
        const cid = ids[i];
        db = "<a href='#' onclick = \"deleteEvent('" + cid + "');\">删除</a>";
        $("#maskingList").jqGrid('setRowData', cid, {act: db});
    }

}
function deleteEvent(id) {
    var ret = $("#maskingList").jqGrid('getRowData', id);
    if (confirm("确认删除\"" + ret.maskingColumn + "\"?")) {
        $("#maskingList").jqGrid('delRowData', id);
    }
}
