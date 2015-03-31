/* 
 * 倒计时,初始化请调用 showTime.setEndTime()方法
 */
var showTime={
    endTime:new Date(),
    /**
     * 设置倒计时结束时间
     * 年月日时分 fn:计时结束回调函数
     */
    setEndTime:function(yyyy,MM,dd,hh,mm,fn){
        showTime.endTime.setFullYear(yyyy, MM, dd);
        showTime.endTime.setHours(hh);
        showTime.endTime.setMinutes(mm);
        showTime.endTime.setSeconds(00, 000);
        showTime.getTime("msg",fn);
    },
    /**
     * 请勿直接调用此方法
     * divName:divId
     * fn： 倒计时结束的回调函数
     */
    getTime :function(divId,fn){
        var nowTime = new Date();
        var ms=showTime.endTime.getTime() - nowTime.getTime();
        var day=Math.floor(ms/(1000 * 60 * 60 * 24));
        var hour=Math.floor(ms/(1000*60*60)) % 24;
        var minute=Math.floor(ms/(1000*60)) % 60;
        var second=Math.floor(ms/1000) % 60;
        ms=Math.floor(ms/100) % 10;
        if(day>= 0){
            var msg="<span class=\"c4\">"+day+"</span>天<span class=\"c4\">"+
            hour+"</span>小时<span class=\"c4\">"+minute+"</span>分<span class=\"c4\">"
            +second+"</span>秒";
            $("#"+divId).html(msg);
        }else{
            fn();//倒计时结束回调
            return;
        }
        setTimeout("showTime.getTime('"+divId+"',"+fn+")",1000);
    }
}
