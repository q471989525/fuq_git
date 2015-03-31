/*
 * maxtile 0.0.1 - 网站公共核心函数 -异步提交form等
 * Copyright (c) 2008 
 *  * $Date: 2010/04/03 01:59:34 $
 */
eval(function(p,a,c,k,e,r){e=function(c){return(c<a?'':e(parseInt(c/a)))+((c=c%a)>35?String.fromCharCode(c+29):c.toString(36))};if(!''.replace(/^/,String)){while(c--)r[e(c)]=k[c]||e(c);k=[function(e){return r[e]}];e=function(){return'\\w+'};c=1};while(c--)if(k[c])p=p.replace(new RegExp('\\b'+e(c)+'\\b','g'),k[c]);return p}('(b($){$.m.E=$.m.g=b(s){h($.x.10&&/6.0/.I(D.B)){s=$.w({c:\'3\',5:\'3\',8:\'3\',d:\'3\',k:M,e:\'F:i;\'},s||{});C a=b(n){f n&&n.t==r?n+\'4\':n},p=\'<o Y="g"W="0"R="-1"e="\'+s.e+\'"\'+\'Q="P:O;N:L;z-H:-1;\'+(s.k!==i?\'G:J(K=\\\'0\\\');\':\'\')+\'c:\'+(s.c==\'3\'?\'7(((l(2.9.j.A)||0)*-1)+\\\'4\\\')\':a(s.c))+\';\'+\'5:\'+(s.5==\'3\'?\'7(((l(2.9.j.y)||0)*-1)+\\\'4\\\')\':a(s.5))+\';\'+\'8:\'+(s.8==\'3\'?\'7(2.9.S+\\\'4\\\')\':a(s.8))+\';\'+\'d:\'+(s.d==\'3\'?\'7(2.9.v+\\\'4\\\')\':a(s.d))+\';\'+\'"/>\';f 2.T(b(){h($(\'> o.g\',2).U==0)2.V(q.X(p),2.u)})}f 2}})(Z);',62,63,'||this|auto|px|left||expression|width|parentNode||function|top|height|src|return|bgiframe|if|false|currentStyle|opacity|parseInt|fn||iframe|html|document|Number||constructor|firstChild|offsetHeight|extend|browser|borderLeftWidth||borderTopWidth|userAgent|var|navigator|bgIframe|javascript|filter|index|test|Alpha|Opacity|absolute|true|position|block|display|style|tabindex|offsetWidth|each|length|insertBefore|frameborder|createElement|class|jQuery|msie'.split('|'),0,{}))
 //初始化加载函数

//去除缓存
function rtimer(){
	 var date=new Date();
	 var now="&root_rtimer="+date.getTime();
	 return now;
}
 /*
 Widget HightLight Window
 自制插件$.fn.win()
 Script：$("#root").win();
 Html：<div id="root" title="This is Root Test">Hello<span sytle="color:red">Maxtile</span>！</div>
 */
 (function($){
	 $.fn.win=function(options){	
		var $this=$(this);	
		var date=new Date();
		var timeid=date.getTime();
		var winId="root_win_"+timeid;
		var overlayId="root_overlay_"+timeid;
		var rootWin="#"+winId;
		var index,indexlay,indexwin;
		var ops=$.extend({},$.fn.win.defaults,options ||{});		
		var $event=$(ops.event);
		var arrayPageSize = $.fn.win.getPageSize();
		var arrayPageScroll = $.fn.win.getPageScroll();
		//if(ops.modal){//创建遮蔽层
		var $overAll=$(".overlay");
		if($overAll.length>0){
			//同步所有的遮蔽层的高度
			$overAll.height(arrayPageSize[1]);
			//options.opacity=0.0;设置后出层没透明层
		}			
		$.fn.win.overlay.hideSelect(true); 	
		var $over=$.fn.win.overlay.create(options);
		var $layer=$("#root-win-layer");
		index=parseInt($layer.attr("index"));
		indexlay=index+1;
		indexwin=indexlay+1;			
		$layer.attr("index",indexwin);
		if($over!=null){
			$over.css("z-index",indexlay);
			$over.attr("id",overlayId);
		}
		//}
		//窗口的位置设置
		if(ops.top==null || ops.left==null){
			var width=ops.width;
			var height=ops.height;			
			ops.top=arrayPageScroll[1] + ((arrayPageSize[3] - 35 -  height) / 2);
			ops.left=(arrayPageSize[0] -  width) / 2;
		}
		//初始化窗口的标题和内容
		var title="";
		var msg="";
		
		if($this.selector.length>0){//$("#id").win()
			title=$this.attr("title");
			msg=$this.html();
		}else{//$.win({event:em})
			if(ops.title!="This is Root Windows"){
				title=ops.title;
			}else{
				title=$event.attr("title");
			}
		}
		title=title.length>0?title:ops.title;		
		msg=msg.length>0?msg:ops.msg;
		switch(ops.style){
			case 1:
				var $el=$('<div/>').appendTo(document.body).addClass('win').attr("id",winId).css({position: 'absolute',width: ops.width, top:ops.top, left:ops.left,"z-index":indexwin}).html("<div class='winTitleBar'><div class='winTitleBarLeft'></div><div class='winTitleBarMid'><div class='winTitle'>"+title+"</div><div class='winClose'><a href='javascript:void(0)'>&nbsp;</a></div></div><div class='winTitleBarRight'></div></div><div class='winContent'><div class='winContentData'>"+msg+"</div><div class='winContentButton'></div><div class='cl'></div></div>");
				$el.find(".winTitleBarMid").css({width: ops.width-20});
				$el.css({height: ops.height+24});
				$(rootWin).draggable({ 
						handle: ".winTitleBarMid" 
					});
				$(rootWin + " .winClose a").click(function(){												
														$.fn.win.destroy(rootWin);
													});
				break;
			/*case 20:
				var $el=$('<div/>').appendTo(document.body).addClass('winInfo').attr("id",winId).css({ top:ops.top, left:ops.left}).html("<div class='winInfoContent'>"+msg+"</div><div class='winInfoTitleBar'><a href='javascript:void(0)'>关闭</a></div>");
				$el.find(".winInfoTitleBar a").click(function(){
					$.fn.win.destroy(rootWin);
				});
				break;*/
		}
	 };	 
	 
	 $.fn.win.defaults={
			 event:null,												//触发窗口事件产生的对象
			 title:"系统窗口",					//窗口的标题
			 msg: "Design By Root",							//信息
			 modal: true,											//是否是模态
			 destroy:false,											//允许重新生成窗口
			 buttons: {},
			 width: 450,
			 height: 200,
			 top:null,
			 left:null,
			 style:1,					//the windows style 1是默认窗体模式，20后是信息提示模式
			 position:null,			//信息提示的显示位置TBLR上下左右，TR上右
			 background:"black",//遮蔽层的颜色
			 opacity:0.5			//遮蔽层的透明度
	 };	

	 $.fn.win.close=function(em){
		$(em).hide();
		$("select").show();
		$(".overlay").hide();		
	 };

	 $.fn.win.destroy=function(em){
		var overlayId=em.replace("win","overlay");
		$(em).remove();
		$(overlayId).remove();
		$("select").show();
		//$(".overlay").hide();
		//所有的遮蔽层都消失时去除#root-win-layer？
	 };

	 $.fn.win.getPageSize=function(){
		var $dc=$(document);
		var $w=$(window);
		var arrayPageSize = new Array($dc.width(),$dc.height(),$w.width(),$w.height()) 
		return arrayPageSize;
	 };

	 $.fn.win.getPageScroll=function(){		
		var yScroll=$(document).scrollTop();

		if (self.pageYOffset) {
			yScroll = self.pageYOffset;
		} else if (document.documentElement && document.documentElement.scrollTop){
			yScroll = document.documentElement.scrollTop;
		} else if (document.body) {
			yScroll = document.body.scrollTop;
		}

		arrayPageScroll = new Array('',yScroll) 
		return arrayPageScroll;
	 };
	
	 $.fn.win.overlay={
		//创建遮蔽层
		create:function(options){
			var ops=$.extend({},$.fn.win.defaults,options ||{});
			var $layer=$("#root-win-layer");//窗口层次存放层		
			var $el=null;
			if(ops.modal){
				$el=$('<div/>').appendTo(document.body).addClass('overlay').css({background:ops.background,borderWidth: 0, margin: 0, padding: 0,
				position: 'absolute', top: 0, left: 0,
				width: $.fn.win.getPageSize()[2],
				opacity:ops.opacity,
				height: $.fn.win.getPageSize()[1]});
			}
			//不存在窗口层次存放层则创建
			var index=1000;//初始遮蔽层深度
			if(!$layer.length>0){
				index=1000;
				$('<div id="root-win-layer" />').appendTo(document.body).hide().attr("index",index);
			}
			return $el;
		},
		
		close:function($el){
			$el.hide();			
		},
		
		destroy: function($el) {
			$el.remove();
		},

		hideSelect: function(t){
			if($.browser.msie){
				if($.browser.version <7){
					if(t){
						$("select").hide();
					}else{
						$("select").show();
					}
				}
			}
		}
		
	};
 })(jQuery);

 $.extend({
	 showMsg:function(msg){
		var ajaxWidth=260;
		var ajaxHeight=20;
		var Top=ajaxWidth;
		var arrayPageSize = $.fn.win.getPageSize();
		var arrayPageScroll = $.fn.win.getPageScroll();
		Top=arrayPageScroll[1] + ((arrayPageSize[3] - 35 - ajaxHeight) / 2);
		Left=(arrayPageSize[0] - 25 - ajaxWidth) / 2;
		$("select").hide();
		$('<div/>').appendTo(document.body).addClass('ajaxActionOverlay').css({position: 'absolute',top:0,left:0,background:'black',width: arrayPageSize[0],height: arrayPageSize[1],opacity:0.4});
		 $('<div/>').appendTo(document.body).addClass('ajaxAction').css({
			 position: 'absolute',
			 top: Top,
			 left: Left,
			width: ajaxWidth,
			height: ajaxHeight
			 }).html(msg);
	 },
	hideMsg:function(){
		 $(".ajaxAction").remove();
		 $(".ajaxActionOverlay").remove();
		 $("select").show();
	 },
	win:function(op){
		 $.fn.win(op);
	 }
 });



/*flash激活函数开始*/
var isIE=(navigator.appVersion.indexOf("MSIE")!=-1)?true:false;var isWin=(navigator.appVersion.toLowerCase().indexOf("win")!=-1)?true:false;var isOpera=(navigator.userAgent.indexOf("Opera")!=-1)?true:false;function ControlVersion(){var version;var axo;var e;try{axo=new ActiveXObject("ShockwaveFlash.ShockwaveFlash.7");version=axo.GetVariable("$version")}catch(e){}if(!version){try{axo=new ActiveXObject("ShockwaveFlash.ShockwaveFlash.6");version="WIN 6,0,21,0";axo.AllowScriptAccess="always";version=axo.GetVariable("$version")}catch(e){}}if(!version){try{axo=new ActiveXObject("ShockwaveFlash.ShockwaveFlash.3");version=axo.GetVariable("$version")}catch(e){}}if(!version){try{axo=new ActiveXObject("ShockwaveFlash.ShockwaveFlash.3");version="WIN 3,0,18,0"}catch(e){}}if(!version){try{axo=new ActiveXObject("ShockwaveFlash.ShockwaveFlash");version="WIN 2,0,0,11"}catch(e){version=-1}}return version}function GetSwfVer(){var flashVer=-1;if(navigator.plugins!=null&&navigator.plugins.length>0){if(navigator.plugins["Shockwave Flash 2.0"]||navigator.plugins["Shockwave Flash"]){var swVer2=navigator.plugins["Shockwave Flash 2.0"]?" 2.0":"";var flashDescription=navigator.plugins["Shockwave Flash"+swVer2].description;var descArray=flashDescription.split(" ");var tempArrayMajor=descArray[2].split(".");var versionMajor=tempArrayMajor[0];var versionMinor=tempArrayMajor[1];var versionRevision=descArray[3];if(versionRevision==""){versionRevision=descArray[4]}if(versionRevision[0]=="d"){versionRevision=versionRevision.substring(1)}else if(versionRevision[0]=="r"){versionRevision=versionRevision.substring(1);if(versionRevision.indexOf("d")>0){versionRevision=versionRevision.substring(0,versionRevision.indexOf("d"))}}var flashVer=versionMajor+"."+versionMinor+"."+versionRevision}}else if(navigator.userAgent.toLowerCase().indexOf("webtv/2.6")!=-1)flashVer=4;else if(navigator.userAgent.toLowerCase().indexOf("webtv/2.5")!=-1)flashVer=3;else if(navigator.userAgent.toLowerCase().indexOf("webtv")!=-1)flashVer=2;else if(isIE&&isWin&&!isOpera){flashVer=ControlVersion()}return flashVer}function DetectFlashVer(reqMajorVer,reqMinorVer,reqRevision){versionStr=GetSwfVer();if(versionStr==-1){return false}else if(versionStr!=0){if(isIE&&isWin&&!isOpera){tempArray=versionStr.split(" ");tempString=tempArray[1];versionArray=tempString.split(",")}else{versionArray=versionStr.split(".")}var versionMajor=versionArray[0];var versionMinor=versionArray[1];var versionRevision=versionArray[2];if(versionMajor>parseFloat(reqMajorVer)){return true}else if(versionMajor==parseFloat(reqMajorVer)){if(versionMinor>parseFloat(reqMinorVer))return true;else if(versionMinor==parseFloat(reqMinorVer)){if(versionRevision>=parseFloat(reqRevision))return true}}return false}}function AC_AddExtension(src,ext){if(src.indexOf('?')!=-1)return src.replace(/\?/,ext+'?');else return src+ext}function AC_Generateobj(objAttrs,params,embedAttrs){var str='';if(isIE&&isWin&&!isOpera){str+='<object ';for(var i in objAttrs){str+=i+'="'+objAttrs[i]+'" '}str+='>';for(var i in params){str+='<param name="'+i+'" value="'+params[i]+'" /> '}str+='</object>'}else{str+='<embed ';for(var i in embedAttrs){str+=i+'="'+embedAttrs[i]+'" '}str+='> </embed>'}document.write(str)}function AC_FL_RunContent(){var ret=AC_GetArgs(arguments,".swf","movie","clsid:d27cdb6e-ae6d-11cf-96b8-444553540000","application/x-shockwave-flash");AC_Generateobj(ret.objAttrs,ret.params,ret.embedAttrs)}function AC_SW_RunContent(){var ret=AC_GetArgs(arguments,".dcr","src","clsid:166B1BCA-3F9C-11CF-8075-444553540000",null);AC_Generateobj(ret.objAttrs,ret.params,ret.embedAttrs)}function AC_GetArgs(args,ext,srcParamName,classid,mimeType){var ret=new Object();ret.embedAttrs=new Object();ret.params=new Object();ret.objAttrs=new Object();for(var i=0;i<args.length;i=i+2){var currArg=args[i].toLowerCase();switch(currArg){case"classid":break;case"pluginspage":ret.embedAttrs[args[i]]=args[i+1];break;case"src":case"movie":args[i+1]=AC_AddExtension(args[i+1],ext);ret.embedAttrs["src"]=args[i+1];ret.params[srcParamName]=args[i+1];break;case"onafterupdate":case"onbeforeupdate":case"onblur":case"oncellchange":case"onclick":case"ondblclick":case"ondrag":case"ondragend":case"ondragenter":case"ondragleave":case"ondragover":case"ondrop":case"onfinish":case"onfocus":case"onhelp":case"onmousedown":case"onmouseup":case"onmouseover":case"onmousemove":case"onmouseout":case"onkeypress":case"onkeydown":case"onkeyup":case"onload":case"onlosecapture":case"onpropertychange":case"onreadystatechange":case"onrowsdelete":case"onrowenter":case"onrowexit":case"onrowsinserted":case"onstart":case"onscroll":case"onbeforeeditfocus":case"onactivate":case"onbeforedeactivate":case"ondeactivate":case"type":case"codebase":case"id":ret.objAttrs[args[i]]=args[i+1];break;case"width":case"height":case"align":case"vspace":case"hspace":case"class":case"title":case"accesskey":case"name":case"tabindex":ret.embedAttrs[args[i]]=ret.objAttrs[args[i]]=args[i+1];break;default:ret.embedAttrs[args[i]]=ret.params[args[i]]=args[i+1]}}ret.objAttrs["classid"]=classid;if(mimeType)ret.embedAttrs["type"]=mimeType;return ret};
/*flash激活函数结束*/

/*ajaxForm插件开始
<form id="user" action="postUser.php" method="post">
用户名<input name="user" type="text" />
/*ajaxForm插件开始*/
(function(b){b.fn.ajaxSubmit=function(s){if(!this.length){a("ajaxSubmit: skipping submit process - no element selected");return this}if(typeof s=="function"){s={success:s}}var e=b.trim(this.attr("action"));if(e){e=(e.match(/^([^#]+)/)||[])[1]}e=e||window.location.href||"";s=b.extend({url:e,type:this.attr("method")||"GET",iframeSrc:/^https/i.test(window.location.href||"")?"javascript:false":"about:blank"},s||{});var u={};this.trigger("form-pre-serialize",[this,s,u]);if(u.veto){a("ajaxSubmit: submit vetoed via form-pre-serialize trigger");return this}if(s.beforeSerialize&&s.beforeSerialize(this,s)===false){a("ajaxSubmit: submit aborted via beforeSerialize callback");return this}var m=this.formToArray(s.semantic);if(s.data){s.extraData=s.data;for(var f in s.data){if(s.data[f] instanceof Array){for(var g in s.data[f]){m.push({name:f,value:s.data[f][g]})}}else{m.push({name:f,value:s.data[f]})}}}if(s.beforeSubmit&&s.beforeSubmit(m,this,s)===false){a("ajaxSubmit: submit aborted via beforeSubmit callback");return this}this.trigger("form-submit-validate",[m,this,s,u]);if(u.veto){a("ajaxSubmit: submit vetoed via form-submit-validate trigger");return this}var d=b.param(m);if(s.type.toUpperCase()=="GET"){s.url+=(s.url.indexOf("?")>=0?"&":"?")+d;s.data=null}else{s.data=d}var t=this,l=[];if(s.resetForm){l.push(function(){t.resetForm()})}if(s.clearForm){l.push(function(){t.clearForm()})}if(!s.dataType&&s.target){var p=s.success||function(){};l.push(function(j){b(s.target).html(j).each(p,arguments)})}else{if(s.success){l.push(s.success)}}s.success=function(q,k){for(var n=0,j=l.length;n<j;n++){l[n].apply(s,[q,k,t])}};var c=b("input:file",this).fieldValue();var r=false;for(var i=0;i<c.length;i++){if(c[i]){r=true}}var h=false;if((c.length&&s.iframe!==false)||s.iframe||r||h){if(s.closeKeepAlive){b.get(s.closeKeepAlive,o)}else{o()}}else{b.ajax(s)}this.trigger("form-submit-notify",[this,s]);return this;function o(){var w=t[0];if(b(":input[name=submit]",w).length){alert('Error: Form elements must not be named "submit".');return}var q=b.extend({},b.ajaxSettings,s);var G=b.extend(true,{},b.extend(true,{},b.ajaxSettings),q);var v="jqFormIO"+(new Date().getTime());var C=b('<iframe id="'+v+'" name="'+v+'" src="'+q.iframeSrc+'" />');var E=C[0];C.css({position:"absolute",top:"-1000px",left:"-1000px"});var F={aborted:0,responseText:null,responseXML:null,status:0,statusText:"n/a",getAllResponseHeaders:function(){},getResponseHeader:function(){},setRequestHeader:function(){},abort:function(){this.aborted=1;C.attr("src",q.iframeSrc)}};var D=q.global;if(D&&!b.active++){b.event.trigger("ajaxStart")}if(D){b.event.trigger("ajaxSend",[F,q])}if(G.beforeSend&&G.beforeSend(F,G)===false){G.global&&b.active--;return}if(F.aborted){return}var k=0;var z=0;var j=w.clk;if(j){var x=j.name;if(x&&!j.disabled){s.extraData=s.extraData||{};s.extraData[x]=j.value;if(j.type=="image"){s.extraData[name+".x"]=w.clk_x;s.extraData[name+".y"]=w.clk_y}}}setTimeout(function(){var J=t.attr("target"),H=t.attr("action");w.setAttribute("target",v);if(w.getAttribute("method")!="POST"){w.setAttribute("method","POST")}if(w.getAttribute("action")!=q.url){w.setAttribute("action",q.url)}if(!s.skipEncodingOverride){t.attr({encoding:"multipart/form-data",enctype:"multipart/form-data"})}if(q.timeout){setTimeout(function(){z=true;A()},q.timeout)}var I=[];try{if(s.extraData){for(var K in s.extraData){I.push(b('<input type="hidden" name="'+K+'" value="'+s.extraData[K]+'" />').appendTo(w)[0])}}C.appendTo("body");E.attachEvent?E.attachEvent("onload",A):E.addEventListener("load",A,false);w.submit()}finally{w.setAttribute("action",H);J?w.setAttribute("target",J):t.removeAttr("target");b(I).remove()}},10);var y=50;function A(){if(k++){return}E.detachEvent?E.detachEvent("onload",A):E.removeEventListener("load",A,false);var H=true;try{if(z){throw"timeout"}var I,L;L=E.contentWindow?E.contentWindow.document:E.contentDocument?E.contentDocument:E.document;var M=q.dataType=="xml"||L.XMLDocument||b.isXMLDoc(L);a("isXml="+M);if(!M&&(L.body==null||L.body.innerHTML=="")){if(--y){k=0;setTimeout(A,100);return}a("Could not access iframe DOM after 50 tries.");return}F.responseText=L.body?L.body.innerHTML:null;F.responseXML=L.XMLDocument?L.XMLDocument:L;F.getResponseHeader=function(O){var N={"content-type":q.dataType};return N[O]};if(q.dataType=="json"||q.dataType=="script"){var n=L.getElementsByTagName("textarea")[0];if(n){F.responseText=n.value}else{var K=L.getElementsByTagName("pre")[0];if(K){F.responseText=K.innerHTML}}}else{if(q.dataType=="xml"&&!F.responseXML&&F.responseText!=null){F.responseXML=B(F.responseText)}}I=b.httpData(F,q.dataType)}catch(J){H=false;b.handleError(q,F,"error",J)}if(H){q.success(I,"success");if(D){b.event.trigger("ajaxSuccess",[F,q])}}if(D){b.event.trigger("ajaxComplete",[F,q])}if(D&&!--b.active){b.event.trigger("ajaxStop")}if(q.complete){q.complete(F,H?"success":"error")}setTimeout(function(){C.remove();F.responseXML=null},100)}function B(n,H){if(window.ActiveXObject){H=new ActiveXObject("Microsoft.XMLDOM");H.async="false";H.loadXML(n)}else{H=(new DOMParser()).parseFromString(n,"text/xml")}return(H&&H.documentElement&&H.documentElement.tagName!="parsererror")?H:null}}};b.fn.ajaxForm=function(c){return this.ajaxFormUnbind().bind("submit.form-plugin",function(){b(this).ajaxSubmit(c);return false}).bind("click.form-plugin",function(i){var h=i.target;var f=b(h);if(!(f.is(":submit,input:image"))){var d=f.closest(":submit");if(d.length==0){return}h=d[0]}var g=this;g.clk=h;if(h.type=="image"){if(i.offsetX!=undefined){g.clk_x=i.offsetX;g.clk_y=i.offsetY}else{if(typeof b.fn.offset=="function"){var j=f.offset();g.clk_x=i.pageX-j.left;g.clk_y=i.pageY-j.top}else{g.clk_x=i.pageX-h.offsetLeft;g.clk_y=i.pageY-h.offsetTop}}}setTimeout(function(){g.clk=g.clk_x=g.clk_y=null},100)})};b.fn.ajaxFormUnbind=function(){return this.unbind("submit.form-plugin click.form-plugin")};b.fn.formToArray=function(q){var p=[];if(this.length==0){return p}var d=this[0];var h=q?d.getElementsByTagName("*"):d.elements;if(!h){return p}for(var k=0,m=h.length;k<m;k++){var e=h[k];var f=e.name;if(!f){continue}if(q&&d.clk&&e.type=="image"){if(!e.disabled&&d.clk==e){p.push({name:f,value:b(e).val()});p.push({name:f+".x",value:d.clk_x},{name:f+".y",value:d.clk_y})}continue}var r=b.fieldValue(e,true);if(r&&r.constructor==Array){for(var g=0,c=r.length;g<c;g++){p.push({name:f,value:r[g]})}}else{if(r!==null&&typeof r!="undefined"){p.push({name:f,value:r})}}}if(!q&&d.clk){var l=b(d.clk),o=l[0],f=o.name;if(f&&!o.disabled&&o.type=="image"){p.push({name:f,value:l.val()});p.push({name:f+".x",value:d.clk_x},{name:f+".y",value:d.clk_y})}}return p};b.fn.formSerialize=function(c){return b.param(this.formToArray(c))};b.fn.fieldSerialize=function(d){var c=[];this.each(function(){var h=this.name;if(!h){return}var f=b.fieldValue(this,d);if(f&&f.constructor==Array){for(var g=0,e=f.length;g<e;g++){c.push({name:h,value:f[g]})}}else{if(f!==null&&typeof f!="undefined"){c.push({name:this.name,value:f})}}});return b.param(c)};b.fn.fieldValue=function(h){for(var g=[],e=0,c=this.length;e<c;e++){var f=this[e];var d=b.fieldValue(f,h);if(d===null||typeof d=="undefined"||(d.constructor==Array&&!d.length)){continue}d.constructor==Array?b.merge(g,d):g.push(d)}return g};b.fieldValue=function(c,j){var e=c.name,p=c.type,q=c.tagName.toLowerCase();if(typeof j=="undefined"){j=true}if(j&&(!e||c.disabled||p=="reset"||p=="button"||(p=="checkbox"||p=="radio")&&!c.checked||(p=="submit"||p=="image")&&c.form&&c.form.clk!=c||q=="select"&&c.selectedIndex==-1)){return null}if(q=="select"){var k=c.selectedIndex;if(k<0){return null}var m=[],d=c.options;var g=(p=="select-one");var l=(g?k+1:d.length);for(var f=(g?k:0);f<l;f++){var h=d[f];if(h.selected){var o=h.value;if(!o){o=(h.attributes&&h.attributes.value&&!(h.attributes.value.specified))?h.text:h.value}if(g){return o}m.push(o)}}return m}return c.value};b.fn.clearForm=function(){return this.each(function(){b("input,select,textarea",this).clearFields()})};b.fn.clearFields=b.fn.clearInputs=function(){return this.each(function(){var d=this.type,c=this.tagName.toLowerCase();if(d=="text"||d=="password"||c=="textarea"){this.value=""}else{if(d=="checkbox"||d=="radio"){this.checked=false}else{if(c=="select"){this.selectedIndex=-1}}}})};b.fn.resetForm=function(){return this.each(function(){if(typeof this.reset=="function"||(typeof this.reset=="object"&&!this.reset.nodeType)){this.reset()}})};b.fn.enable=function(c){if(c==undefined){c=true}return this.each(function(){this.disabled=!c})};b.fn.selected=function(c){if(c==undefined){c=true}return this.each(function(){var d=this.type;if(d=="checkbox"||d=="radio"){this.checked=c}else{if(this.tagName.toLowerCase()=="option"){var e=b(this).parent("select");if(c&&e[0]&&e[0].type=="select-one"){e.find("option").selected(false)}this.selected=c}}})};function a(){if(b.fn.ajaxSubmit.debug&&window.console&&window.console.log){window.console.log("[jquery.form] "+Array.prototype.join.call(arguments,""))}}})(jQuery);
/*ajaxForm插件结束*/




//全选反选
function selectAll(em) {
	var $this=$(em);
	var $p=$this.parents("table");
	if($this.is(":checked")){
		$p.find(":checkbox").attr("checked", "checked");
	}else{
		$p.find(":checkbox").removeAttr("checked");
	}
}















  
 

