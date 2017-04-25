/**
 * 公共的js
 */
//文件预览
function preview(){
	var input = document.getElementById("file_input");
    var result,div;

    if(typeof FileReader==='undefined'){
        result.innerHTML = "抱歉，你的浏览器不支持 FileReader";
        input.setAttribute('disabled','disabled');
    }else{
       	input.addEventListener('change',readFile,false);
   	}　　　　　//handler
    function readFile(){
        for(var i=0;i<this.files.length;i++){
            if (!input['value'].match(/.jpg|.gif|.png|.bmp/i)){　　//判断上传文件格式
                return alert("上传的图片格式不正确，请重新选择")
           	}
            var reader = new FileReader();
            reader.readAsDataURL(this.files[i]);
            reader.onload = function(e){
                result = '<div id="result"><img src="'+this.result+'" alt=""/></div>';
                div = document.createElement('div');
                div.innerHTML = result;
                $("body").append(div);  　　//插入dom树
            }
        }
   	}
}

//文件上传
function upload(fun,url){

	var input = document.getElementById("file_input");
    var result;

    if(typeof FileReader==='undefined'){
        result.innerHTML = "抱歉，你的浏览器不支持 FileReader";
        input.setAttribute('disabled','disabled');
    }
    var formData = new FormData();
    for(var i=0;i<input.files.length;i++){
        var reader = new FileReader();
        reader.readAsDataURL(input.files[i]);
        formData.append(input.files[i].name,input.files[i]);
    }
    $.ajax({
        url : url,
        type : "post",
        cache: false,
        data : formData,
        //不设置contentType
        contentType :false,
        //不需要对数据处理
        processData: false,
        dataType: 'json',
        success : fun
    })
}

//获取文件名
function getFileName(obj){
    var fileName="";

    if(typeof(fileName) != "undefined"){
    	 fileName = $(obj).val().split("\\").pop();
    	 fileName=fileName.substring(0, fileName.lastIndexOf("."));
    }
    return fileName;
}



//日期格式化  格式为 yyyy-mm-dd hh:mm:ss
	function dateFormatter(date){
		 var date=new Date(date);
		 var seconds=date.getSeconds();
		 if( seconds <10){
			  seconds="0"+seconds;
		  }
  	   	return date.getUTCFullYear()+"-"+(date.getUTCMonth()+1)+"-"+(date.getUTCDate())+" "
  	   		  +date.getHours()+":"+date.getMinutes()+":"+seconds;
	}

//日期格式化  格式为 yyyy-mm-dd
	function dateFormatterDate(date){
		 var date=new Date(date);
  	   	 return date.getUTCFullYear()+"-"+(date.getUTCMonth()+1)+"-"+(date.getUTCDate()+1);
	}

//日期格式化 格式为yyyy/mm/dd
	function dateFormat(date){
		 var date=new Date(date);
 	   	 return date.getUTCFullYear()+"/"+(date.getUTCMonth()+1)+"/"+(date.getUTCDate()+1);
	}


	/**
	 * form表单序列化为一个json
	 */
	$.fn.serializeObject = function(){
	    var o = {};
	    var a = this.serializeArray();
	    $.each(a, function() {
	        if (o[this.name]) {
	            if (!o[this.name].push) {
	                o[this.name] = [o[this.name]];
	            }
	            o[this.name].push(this.value || '');
	        } else {
	            o[this.name] = this.value || '';
	        }
	    });
	    return o;
	};



	//操作方法
	function operation(url,data,type,fun){
		$.ajax({
			url:url,
			type:type,
			dataType:"json",
			contentType:"application/json",
			data:data,
			success:fun
		})
	}

	//post
	function post(url,data,fun){
	    operation(url,data,"post",fun);
	}

    //get
    function get(url,data,fun){
    	operation(url,data,"get",fun);
    }

    var baseUrl = "/mianbao";


