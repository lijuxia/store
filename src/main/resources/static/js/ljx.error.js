/**
 * Restful接口返回结果异常处理
 * @param data
 */
var errorHandle = function errorHandle(data){
	if(data.code==9000){
		window.location.href = "/login.html";
	}else if(data.code==404){
		mui.toast("数据获取失败！",{ duration:'long', type:'div' }) 
	}else if(data.code==500){
		mui.toast(data.msg,{ duration:'long', type:'div' }) 
	}
}

var getLoginUser = function (){
	var store ;
	mui.ajax("/loginUser",{
	type:'get',
    cache:true, 
    async:false, 
	success:function (data){
		if(data.succeed){
			store =  data.value;
			}else{
				errorHandle(data);
			}
		}
	})
	return store;
}

//获取当前时间，格式YYYY-MM-DD
var getNowFormatDate = function () {
    var date = new Date();
    var seperator1 = "-";
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = year + seperator1 + month + seperator1 + strDate;
    return currentdate;
}

var getFirstDayOfMonthFormat = function(){
	var date = new Date(), y = date.getFullYear(), m = date.getMonth();
	var firstDay = new Date(y, m, 1);
    var seperator1 = "-";
    var year = firstDay.getFullYear();
    var month = firstDay.getMonth() + 1;
    var strDate = firstDay.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = year + seperator1 + month + seperator1 + strDate;
    return currentdate;
}

var getLastDayOfMonthFormat = function(){
	var date = new Date(), y = date.getFullYear(), m = date.getMonth();
	var lastDay = new Date(y, m + 1, 0);
    var seperator1 = "-";
    var year = lastDay.getFullYear();
    var month = lastDay.getMonth() + 1;
    var strDate = lastDay.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = year + seperator1 + month + seperator1 + strDate;
    return currentdate;
}


