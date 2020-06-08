function tabredirect(index){
    var baseId = $("#baseId").val();
    if(index === '0'){
        window.location.href = "/baseInfoB/baseInfo_view/" + baseId;
    }else if(index === '1'){
        window.location.href = "/menuinfo/menuinfo_restore";
    }else if(index === '2'){
        window.location.href = "/baseInfoB/change_view/" + baseId;

    }else if(index === '3'){
        window.location.href = "/dataInfo/dataInfo_view/" + baseId;
    }else if(index === '4'){
        window.location.href = "/orgInfo/view/" + baseId;

    }else{
        window.location.href = "/auditing/to_shenhe/" + baseId;

    }
}