window.onload=function(){
	var list=document.getElementById('list');
	var boxs=list.children;//获取到每条分享的box
	var timer;//定时器

	//获取当前时间并格式化
	function formateDate(date){//相当于var date=new Date();当不传入参数时,就创建了代表当前日期的对象
		var y=date.getFullYear();
		var m=date.getMonth()+1;//使用getMonth()获取月份，获取的值从0开始,所以获取值后 + 1。
		var d=date.getDate();//获取天
		var h=date.getHours();
		var mi=date.getMinutes();
		m = m>9 ? '0'+ m : m;//当月份小于9时,加上0，如09,08
		return y+'-'+m+'-'+d+' '+h+':'+mi;

	}
	//删除节点
	function removeNode(node){
		node.parentNode.removeChild(node);
	}

	/**
     * 发评论
     * @param box 每个分享的div容器
     * @param el 点击的元素
     */
     function reply(box,el){//输入框的内容，就是使用innerHTML添加到回复列表里
     	var commentList=box.getElementsByClassName('commentList')[0];//getElementsByClassName()获取的是一个数组,要加一个下标
     	var textarea=box.getElementsByClassName('comment')[0];
     	var commentBox=document.createElement('div');
     	commentBox.className='comment-box clearfix';
     	commentBox.setAttribute('user','self');//自定义的属性用setAttribute(),里面包含两个参数，一个是参数名，一个是值
     	commentBox.innerHTML=
     					'<img class="myhead" th:src="@{/images/dynamic/my.jpg}" >'+
						'<div class="comment-content">'+
							'<p class="comment-text"><span class="user">我：</span>'+textarea.value+'</p>'+
							'<p class="comment-time">'+formateDate(new Date())+
							'</p>'+
						'</div>'
		commentList.appendChild(commentBox);
		//当评论结束后，输入框应该恢复原来的样子
		textarea.value='';
		textarea.onblur();

     }

    /**
     * 赞分享
     * @param box 每个分享的div容器
     * @param el 点击的元素
     */
	function praiseBox(box,el){
		 var praiseElement=box.getElementsByClassName('praises-total')[0];
		/*它是用getElementsByClassName('praises-total')
		 获取到的是含有该样式名称的所有元素的集合 是个数组，
		 因为当前使用的是第一个，所以要加个下标[0]*/
		 var oldTotal= $("#total").val();
		 var txt=el.innerHTML;//el是a标签,el.innerHTML可能是'赞'，也可能是'取消赞''
		 var newTotal;//定义一个变量，如果txt是'赞'，就总数+1，如果txt是'取消赞'，就总数-1

		 if (txt=='赞') {
		    if($("#likeStatus").val() == 1){
		    	newTotal=oldTotal+1;
            	$("#total").val(newTotal);//使用setAttribute()来设置元素的属性,要更新total的值，不然，会在原来的tota上减1
                praiseElement.innerHTML = (newTotal==1)?'我觉得很赞':'我和'+oldTotal+'个人觉得很赞';
		        el.innerHTML='取消赞';//el.innerHTML可以取得文字.当点击赞的时候，这个位置就是取消赞
		    }
		 }
		 else{//我 取消赞

		 	if($("#likeStatus").val() == 1){
		 	    newTotal=oldTotal-1;
                $("#total").val(newTotal);  //使用setAttribute()来设置元素的属性,要更新total的值，不然，会在原来的tota上减1
                praiseElement.innerHTML=(newTotal==0)?'':newTotal+'个人觉得很赞';
                el.innerHTML='取消赞';//el.innerHTML可以取得文字.当点击赞的时候，这个位置就是取消赞
            }
		 }
		 praiseElement.style.display=(newTotal==0)?'none':'block';//设置赞内容的显示和隐藏,就是那个我和4个人觉得很赞那个地方

	}
       /**
     * 操作留言
     * @param el 点击的元素
     */
     	function operate(el){
     		var commentBox=el.parentNode.parentNode.parentNode;//评论容器
     		var box=commentBox.parentNode.parentNode.parentNode;//分享容器box
     		var txt=el.innerHTML;
     		var user=commentBox.getElementsByClassName('user')[0].innerHTML;
     		var textarea=box.getElementsByClassName('comment')[0];
     		if (txt=='回复') {//如果等于回复，就做回复操作
     			textarea.focus();//调用下面的函数，让输入框变大
     			textarea.value='回复'+user;
     			textarea.onkeyup();//统计字数
     		}else{//如果是删除，则进行删除操作
     			removeNode(commentBox);
     		}
     	}
	
	/*当我们需要对很多元素添加事件的时候，
	可以通过将事件添加到它们的父节点而将事件委托给父节点来触发处理函数。
	这主要得益于浏览器的事件冒泡机制
	事件代理的时候，使用事件对象中的srcElement属性,获取触发元素。
	IE浏览器支持window.event.srcElement ， 而firefox支持window.event.target。
	*/
	//把事件代理到每条分享的div容器里
	for(var i=0;i<boxs.length;i++){
		//点击
		boxs[i].onclick=function(e){
			e=e||window.event;
			var el=e.target||e.srcElement;//通过 target或者srcElement可以获取到点击那里的标签
			switch(el.className){
				case 'close':
				removeNode(el.parentNode);//el是a标签，el.parentNode是box,el.parentNode.parentNode就是list
				break;
				//赞分享
				case 'praise':
				praiseBox(el.parentNode.parentNode.parentNode,el);//el.parentNode.parentNode.parentNode就是box
				break;
				//回复按钮灰色
				case 'btn btn-off':
				clearTimeout(timer);//清除定时器，这样点击灰色回复按钮的时候，不会变小
				break;
				//回复按钮蓝色
				case 'btn':
				reply(el.parentNode.parentNode.parentNode,el);
				break;
				//操作留言
				case 'comment-operate':
				operate(el);
				break;

			}
		}

	//评论
	var textarea=boxs[i].getElementsByClassName('comment')[0];
	//评论获得焦点
	textarea.onfocus=function(){//输入框获得焦点时
			this.parentNode.className='text-box text-box-on';//只要将textarea的父节点的类名改变一下就行了
			this.value=this.value=='评论...'?'':this.value;//如果不等于'评论...'的时候就为空，否则就为原来的值
			this.onkeyup();//鼠标输入弹起来的时候，输入框的变化
		}
		//评论失去焦点
	textarea.onblur=function(){
			var me=this;
			var val=me.value;
			if (val=='') {
				timer=setTimeout(function(){//设置定时器的目的就是不让输入框失去焦点的时候，立马变小
					me.parentNode.className='text-box';//只要将textarea的父节点的类名改变一下就行了
					me.value='评论...';//如果不等于'评论...'的时候就为空，否则就为原来的值	
				},400);
				
			}
		}
		//评论按键事件，可以统计输入框的字数
	textarea.onkeyup=function(e){
			var len=this.value.length;//获取输入框字数长度
			var p=this.parentNode;//输入框的父元素是text-box
			var btn=p.children[1];//btn是text-box的第二个子元素
			var word=p.children[2];//word是text-box的第三个子元素
			if (len==0||len>140) {
				btn.className='btn btn-off';//如果没有输入，或者字数大于140,回复框变灰
			}else{
				btn.className='btn';
			}
			word.innerHTML=len+'/140';//这样字数就可以统计出来了。
		}
	}

	 $("#selectLikeInfo").click(function(){
          var url = "/mianbao/travel/dynamic/likeInfo";
          var data = {"id":$("#dynamicId").val()};
          get(url,data,function(data){
          if(data.success){
              debugger;
              var lists = "<ul style='display:block,width:440px,height:30px' >";
              var array = data.data.likeUser;
              array.forEach(function(val,index){
                  lists += "<li><a style='color:blue' data-id = "+val.id+">"+val.userName+"</a></li>";

              })
              lists+="</ul>";
              $("#likeList").html(lists);
          }else{
              window.alert("查看点赞详细失败");
          }
        });
    });

    $("#comment").click(function(){
        debugger;
        var url = "/mianbao/travel/dynamic/comment";
        var content = $("#content").val();
        var data = {"content" : content,"id":$("#dynamicId").val()};
        get(url,data,function(data){
            if(!data.success){
                window.alert("评论失败");
            }
        });
    });
}