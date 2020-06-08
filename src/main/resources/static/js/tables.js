$(function () {
    tables();
});

$(function () {
    $(window).ready(function () {
        $("#tables").setGridWidth($(window).width() * 0.25);
    });
});

function tables() {
    $("#tables").jqGrid({       //Grid设置
        datatype: 'local',
        hidegrid:false,
        caption: "表名",
        loadonce: true,
        height: 756,
        autowidth: true,
        colNames: ['表名', '脱敏列', '记录数'],
        colModel: [
            {name: 'tableName', index: 'tableName', editable: true},
            {name: 'masking', index: 'masking', editable: true},
            {name: 'rowCount', index: 'rowCount', editable: true},
        ],
        celledit: true,
        viewrecords: true,//是否在浏览导航栏显示记录总数
        rowNum: 10000,//每页显示记录数
        pager: '#pageuser',//分页、按钮所在的浏览导航栏
        sortname: 'UserName',//默认的排序列名
        pgbuttons: false,
        pgtext: false,
        onSelectRow: function () {
            // 取到当前选择的列
            const rowId = $("#tables").jqGrid("getGridParam", "selrow");
            const rowData = jQuery("#tables").jqGrid("getRowData", rowId);
            // 获取到当前选择的库名
            const ConnectionName = jQuery("#connect  option:selected").text();
            let tableData = "";
            $.ajax({
                url: "/display/getTableData",
                type: "POST",
                timeout: "3000",
                async: false,
                traditional: true,
                // 会导致后台取值为空
                // contentType: "application/json",
                data: {ConName: ConnectionName.toString(), tableName: rowData.tableName.toString()},
                // ConnectionName + JSON.stringify(rowData),
                dataType: "json",
                success: function (data) {
                    const dataList = JSON.parse(data.tableMeta);
                    tableData = data.tableData;
                    const localData = {page: 1, total: 0, records: "0", rows: dataList};
                    localData.rows = dataList;
                    localData.records = dataList.length;
                    localData.total = (dataList.length % 5 === 0) ? (dataList.length / 5) : (Math.floor(dataList.length / 5) + 1);
                    const reader = {
                        root: function () {
                            return localData.rows;
                        },
                        page: function () {
                            return localData.page;
                        },
                        total: function () {
                            return localData.total;
                        },
                        records: function () {
                            return localData.records;
                        }, repeatitems: false
                    };
                    $("#column").jqGrid('clearGridData');  //清空表格
                    $("#column").setGridParam({data: localData.rows, reader: reader}).trigger('reloadGrid');
                },
                error: function () {
                    alert('Error');
                },
            });
            const column = JSON.parse(tableData);
            const names = [];
            const model = [];
            //此处因为数据源数组中的结构相同且不为空，直接遍历索引为0的数据即可
            $.each(column[0], function (key, value) {
                names.push(key);
                model.push({
                    name: key,
                    index: key,
                    autowidth: true,
                });
            });
            if ($("#gbox_newtableData") !== null) {
                $("#gbox_newtableData").remove();
            }
            $(function () {
                newtableData();
            });

            $(function () {
                $(window).ready(function () {
                    $("#newtableData").setGridWidth($(window).width() * 0.498);
                });
            });

            function newtableData() {
                jQuery("#newtableData").jqGrid(
                    {
                        datatype: 'local',
                        loadonce: true,
                        height: 300,
                        autowidth: true,
                        caption: "数据",
                        colNames: names,
                        colModel: model,
                        hidegrid:false,
                        viewrecords: true,//是否在浏览导航栏显示记录总数
                        rowNum: 10000,//每页显示记录数
                        pgbuttons: false,
                        pgtext: false,
                    });

            }
            const dataList = JSON.parse(tableData);
            const localData = {page: 1, total: 0, records: "0", rows: dataList};
            localData.rows = dataList;
            localData.records = dataList.length;
            localData.total = (dataList.length % 5 === 0) ? (dataList.length / 5) : (Math.floor(dataList.length / 5) + 1);
            const reader = {
                root: function () {
                    return localData.rows;
                },
                page: function () {
                    return localData.page;
                },
                total: function () {
                    return localData.total;
                },
                records: function () {
                    return localData.records;
                }, repeatitems: false
            };
            $("#newtableData").jqGrid('clearGridData');  //清空表格
            $("#newtableData").setGridParam({data: localData.rows, reader: reader}).trigger('reloadGrid');
            //  const table = $("#gbox_column").clone();
            // // // alert(JSON.stringify(table));
            // alert(table);
            // $("#cBottomDiv").empty().append(table);
            $("#gbox_tableData").remove();
            $("#cBottomDiv").append('<table id="newtableData"></table>');
        }
    });
}